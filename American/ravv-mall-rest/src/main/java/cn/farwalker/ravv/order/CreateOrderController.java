package cn.farwalker.ravv.order;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import cn.farwalker.ravv.service.shipment.biz.IShipmentBiz;
import cn.farwalker.waka.core.HttpKit;
import cn.farwalker.waka.core.WakaException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.farwalker.ravv.order.dto.ConfirmOrderVo;
import cn.farwalker.ravv.service.base.storehouse.biz.IStorehouseBiz;
import cn.farwalker.ravv.service.base.storehouse.model.StorehouseBo;
import cn.farwalker.ravv.service.goods.utils.GoodsUtil;
import cn.farwalker.ravv.service.order.constants.PaymentPlatformEnum;
import cn.farwalker.ravv.service.order.ordergoods.model.OrderGoodsBo;
import cn.farwalker.ravv.service.order.ordergoods.model.OrderGoodsVo;
import cn.farwalker.ravv.service.order.orderinfo.biz.IOrderCreateService;
import cn.farwalker.ravv.service.order.orderinfo.biz.IOrderInfoBiz;
import cn.farwalker.ravv.service.order.orderinfo.model.OrderGoodsSkuVo;
import cn.farwalker.ravv.service.order.paymemt.model.OrderPaymemtBo;
import cn.farwalker.ravv.service.sale.profitallot.biz.ISaleProfitAllotBiz;
import cn.farwalker.ravv.service.shipment.biz.IShipmentService;
import cn.farwalker.ravv.service.shipment.model.ShipmentBo;
import cn.farwalker.waka.core.JsonResult;
import cn.farwalker.waka.util.Tools;

/**
 * 创建订单处理(支付前)<br/>
 * @author generateModel.java
 */
@RestController
@RequestMapping("/order/createorder")
public class CreateOrderController{
	public static final String K_MemberId="memberId";
    private final static  Logger log =LoggerFactory.getLogger(CreateOrderController.class);
    @Resource
    private IOrderCreateService orderCreateBiz;
    @Resource
    private IOrderInfoBiz orderInfoBiz;
    @Resource
    private IStorehouseBiz storeHouseBiz;
    @Resource
    private IShipmentService shipmentServer;

    @Autowired
    private IShipmentBiz shipmentBiz;
    
 
    /**
     * 创建订单前的信息确认
     * @param valueids 选择的sku及数量<br/>
     * @return
     */
    @RequestMapping("/confirm")
    public JsonResult<List<ConfirmOrderVo>> doConfirmOrder(@RequestBody List<OrderGoodsSkuVo> valueids,  Long addressId,
                                @RequestParam(value = "shipmentId", required = false, defaultValue = "-1") Long shipmentId){
        if(Tools.collection.isEmpty(valueids)){
            return JsonResult.newFail("选择的属性值不能为空");
        }
        if(Tools.number.isEmpty(addressId)){
            throw new WakaException("地址id为空");
        }
        BigDecimal total = BigDecimal.ZERO ;
        BigDecimal tax = BigDecimal.ZERO;
        Map<Long,List<OrderGoodsVo>> storeBos = orderCreateBiz.getConfirmOrder(valueids);
        List<ConfirmOrderVo> rds = new ArrayList<>();
        for(Map.Entry<Long,List<OrderGoodsVo>> e : storeBos.entrySet()){
        	ConfirmOrderVo vo = new ConfirmOrderVo();
        	List goods = e.getValue();
        	OrderGoodsVo v1 = (OrderGoodsVo) goods.get(0);
        	vo.setGoodsBos(goods);
        	BigDecimal fee = null;
            if(shipmentId.longValue()==-1){//默认运费
                ShipmentBo freight = orderCreateBiz.calcFreightGoods(goods);
                if(freight != null){
                    fee = freight.getFee();
                }
            }
            else{
                ShipmentBo sbo = shipmentBiz.selectById(shipmentId);
                if(sbo==null){
                    throw new WakaException("运费id不存在:" + shipmentId);
                }
                fee = sbo.getFee();
            }
        	vo.setShipping(fee == null ? BigDecimal.ZERO : fee);
            log.info("vo.getShipping:{}",vo.getShipping());
        	StorehouseBo storeBo = storeHouseBiz.selectById(e.getKey());
        	vo.setStoreHouseBo(storeBo);
        	log.info("vo.getSubtotal:{}",vo.getSubtotal());
            total = Tools.bigDecimal.add(total,vo.getSubtotal()); //total + (int)(vo.getSubtotal().doubleValue() *100);
            //根据addressId查找地址，计算税费
            BigDecimal subTax = orderCreateBiz.calTaxByStore(addressId, e.getKey(), vo.getSubtotal());
            log.info("subTax:{}", subTax);
            tax = Tools.bigDecimal.add(tax, subTax);
        	rds.add(vo);
        	//设置图片
        	for(Object g :goods){
        		OrderGoodsBo go = (OrderGoodsBo)g;
        		String imgdesc = GoodsUtil.getCdnFullPaths(go.getImgDesc());
        		go.setImgDesc(imgdesc);
        		String imgtitle = GoodsUtil.getCdnFullPaths(go.getImgTitle());
        		go.setImgTitle(imgtitle);
        		String major = GoodsUtil.getCdnFullPaths( go.getImgMajor());
        		go.setImgMajor(major);
        		String imgsku = GoodsUtil.getCdnFullPaths( go.getImgSku());
        		go.setImgSku(imgsku);
        	}
        }
        JsonResult<List<ConfirmOrderVo>> result = JsonResult.newSuccess(rds);
        result.put("total", total);//"不含物流总金额/BigDecimal"
        result.put("tax", tax);
        return result;
    }
    
    /**取得通用的物流方式*/
    @RequestMapping("/shipments")
    public JsonResult<List<ShipmentBo>> getShipments(){
    	List<ShipmentBo> rd = shipmentServer.getGeneralList();
    	return JsonResult.newSuccess(rd);
    }
    
    /**支付平台*/
    @RequestMapping("/platforms")
    public JsonResult<List<PaymentPlatformEnum>> getPaymentPlatforms(){
    	List<PaymentPlatformEnum> rds = Arrays.asList(PaymentPlatformEnum.values());
    	return JsonResult.newSuccess(rds);
    }
    
    /**用户使用购物车创建订单(多个商品)
     * @param amt 订单总金额<br/>
     * @param valueids 选择的属性值<br/>
     * @param buyerMessage 买家留言<br/>
     * @param shipmentId 物流id,如果是默认运费就传-1<br/>
     * @param addressId 配送地址<br/>
     */
    @RequestMapping("/multi")
    public JsonResult<OrderPaymemtBo> doCreateMulti(BigDecimal amt,@RequestBody List<OrderGoodsSkuVo> valueids
    		,  String buyerMessage,Long shipmentId,Long addressId){
    	HttpSession sin =HttpKit.getRequest().getSession();
    	Long memberId = (Long)sin.getAttribute(CreateOrderController.K_MemberId);
         //createMethodSinge创建方法
        if(amt==null){
            return JsonResult.newFail("订单总金额不能为空");
        }
        if(Tools.collection.isEmpty(valueids)){
            return JsonResult.newFail("选择的属性值不能为空");
        }
        if(memberId == null || memberId.longValue()==0){
        	return JsonResult.newFail("买家id不能为空");
        }
        OrderPaymemtBo bo = orderCreateBiz.createOrder(valueids, shipmentId,amt, memberId,buyerMessage,addressId);
        bo.setId(null);//清楚id，避免与orderid混淆
        
        return JsonResult.newSuccess(bo);
    }
    
    /**用户立即下单时创建订单(单个商品)
     * @param amt 订单总金额<br/>
     * @param valueids 选择的属性值<br/>
     * @param buyerMessage 买家留言<br/>
     * @param shipmentId 物流id,如果是默认运费就传-1 {@link ShipmentBo}
     * @param addressId 配送地址<br/>
     */
    @RequestMapping("/single")
    public JsonResult<OrderPaymemtBo> doCreateSingle( BigDecimal amt,@RequestBody OrderGoodsSkuVo valueids
    		, String buyerMessage,Long shipmentId,Long addressId){
        log.info("==============================前端传入金额:{}",amt);
        //createMethodSinge创建方法
    	HttpSession sin = HttpKit.getRequest().getSession();
    	Long memberId = (Long)sin.getAttribute(CreateOrderController.K_MemberId);
        if(amt==null){
            return JsonResult.newFail("订单总金额不能为空");
        }
        if(valueids == null){
            return JsonResult.newFail("选择的属性值不能为空");
        }
        if(memberId == null || memberId.longValue()==0){
        	return JsonResult.newFail("买家id不能为空");
        }
        
        List<OrderGoodsSkuVo> skus = Arrays.asList(valueids);
        OrderPaymemtBo bo = orderCreateBiz.createOrder(skus, shipmentId,amt ,memberId,buyerMessage,addressId);
        bo.setId(null);//清楚id，避免与orderid混淆
        return JsonResult.newSuccess(bo);
    }
    protected IOrderInfoBiz getBiz(){
        return orderInfoBiz;
    }
    
	@Resource
	private ISaleProfitAllotBiz saleProfitAllotBiz;
    
    /**
     * 测试最终分润
     */
    @RequestMapping("/profit")
    public JsonResult<Boolean> doProfit(BigDecimal amt){
    	List<Long> orderIdList = new ArrayList<>();
    	orderIdList.add(1082119565347778561L);
    	orderIdList.add(1082119567214243841L);
    	
//    	saleProfitAllotBiz.updateFinalProfit(orderIdList);
    	
        return JsonResult.newSuccess(true);
    }
}