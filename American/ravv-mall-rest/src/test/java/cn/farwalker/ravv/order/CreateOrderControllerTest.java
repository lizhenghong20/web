package cn.farwalker.ravv.order;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import cn.farwalker.ravv.admin.order.AdminOrderService;
import cn.farwalker.ravv.service.order.orderinfo.model.ConfirmOrderVo;
import cn.farwalker.ravv.service.base.area.constant.CountryCodeEnum;
import cn.farwalker.ravv.service.goodssku.skudef.biz.IGoodsSkuDefBiz;
import cn.farwalker.ravv.service.goodssku.skudef.model.GoodsSkuDefBo;
import cn.farwalker.ravv.service.order.ordergoods.biz.IOrderGoodsBiz;
import cn.farwalker.ravv.service.order.ordergoods.model.OrderGoodsBo;
import cn.farwalker.ravv.service.order.orderinfo.biz.IOrderInfoBiz;
import cn.farwalker.ravv.service.order.orderinfo.constants.OrderTypeEnum;
import cn.farwalker.ravv.service.order.orderinfo.model.OrderInfoBo;
import cn.farwalker.ravv.service.order.orderinfo.model.OrderReturnSkuVo;
import cn.farwalker.ravv.service.order.orderlogistics.contants.LogisticsPaymentEnum;
import cn.farwalker.ravv.service.order.orderlogistics.contants.LogisticsStatusEnum;
import cn.farwalker.ravv.service.order.orderlogistics.contants.LogisticsTypeEnum;
import cn.farwalker.ravv.service.order.orderlogistics.model.OrderLogisticsBo;
import cn.farwalker.ravv.service.order.paymemt.model.OrderPaymemtBo;
import cn.farwalker.ravv.service.order.returns.biz.IOrderReturnsBiz;
import cn.farwalker.ravv.service.order.returns.biz.IOrderReturnsDetailBiz;
import cn.farwalker.ravv.service.order.returns.biz.IOrderReturnsService;
import cn.farwalker.ravv.service.order.returns.constants.DamagedConditionEnum;
import cn.farwalker.ravv.service.order.returns.constants.OperatorTypeEnum;
import cn.farwalker.ravv.service.order.returns.constants.ReturnsGoodsStatusEnum;
import cn.farwalker.ravv.service.order.returns.constants.ReturnsTypeEnum;
import cn.farwalker.ravv.service.order.returns.model.OrderReturnsBo;
import cn.farwalker.ravv.service.order.returns.model.OrderReturnsDetailBo;
import cn.farwalker.ravv.service.order.returns.model.OrderReturnsVo;
import cn.farwalker.waka.core.RavvExceptionEnum;
import cn.farwalker.waka.core.WakaException;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.Condition;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;



import cn.farwalker.ravv.BaseRestJUnitController;
import cn.farwalker.ravv.service.order.orderinfo.model.OrderGoodsSkuVo;
import cn.farwalker.standard.util.JwtUtil;
import cn.farwalker.waka.auth.converter.BaseTransferEntity;
import cn.farwalker.waka.core.JsonResult;
import cn.farwalker.waka.util.Tools;

import javax.annotation.Resource;

/**
 * 测试时需要 取消 {@link }
 * @author Administrator
 *
 */
public class CreateOrderControllerTest extends BaseRestJUnitController{
	private static final Logger log = LoggerFactory.getLogger(CreateOrderControllerTest.class);

	@Autowired
	private IGoodsSkuDefBiz goodsSkuDefBiz;

	@Autowired
    private AdminOrderService adminOrderService;

	@Autowired
    private IOrderReturnsBiz orderReturnsBiz;

    @Resource
    private IOrderReturnsService orderReturnsService;

    @Autowired
    private IOrderReturnsDetailBiz returnsDetailBiz;

    @Autowired
    private IOrderInfoBiz orderInfoBiz;

    @Autowired
    private IOrderGoodsBiz orderGoodsBiz;

 
//	@Test
    @Rollback(false)
	public void doConfirmOrder() throws Exception {
//		List<GoodsSkuDefBo> goodsSkuDefBoList = goodsSkuDefBiz.selectList(Condition.create()
//				.isNotNull(GoodsSkuDefBo.Key.imageUrl.toString()));
//		//下单多少次
//		int count = 1;
//		log.info("下单 :{} 次", count);
//		for(int i = 0 ; i < count;i++){
//			List<GoodsSkuDefBo> randomList = new ArrayList<>();
//			//取多少个商品
//			int size = 2/* + (int)(Math.random() * 10)*/;
//			log.info("取 :{} 个商品", size);
//			for(int j = 0; j < size;j++){
//				//商品下标
//				int index = 0 + (int)(Math.random() * 165);
//				GoodsSkuDefBo randomBo = new GoodsSkuDefBo();
//				randomBo = goodsSkuDefBoList.get(index);
//				randomList.add(randomBo);
//			}
//			int quan = 1/*,salePrice = 8,price = 30*/;
//			List<OrderGoodsSkuVo> cars = new ArrayList<>();{
//				randomList.forEach(item->{
//					OrderGoodsSkuVo ids = new OrderGoodsSkuVo();
//					ids.setQuan(Integer.valueOf(quan));
//					//ids.setValueids(Arrays.asList(20l,30l,60l));
//					ids.setSkuId(item.getId());
//					ids.setGoodsId(item.getGoodsId());
//					cars.add(ids);
//				});
//			}
//			Long memberId =1067707280516718594L;
//			BaseTransferEntity baseTransferEntity = JwtUtil.wrapAsTransferEntity(cars, this.randomKey);
//			String contentData = Tools.json.toJson(baseTransferEntity);
//
//			this.getMokcSession().setAttribute("memberId", memberId);
//			MockHttpServletRequestBuilder request = post("/order/createorder/confirm")
//					.contentType(MediaType.APPLICATION_JSON_UTF8)
//					.header("Authorization", "Bearer " + this.token)
//					.header("Content-Type",MediaType.APPLICATION_JSON_VALUE)
//					.content(contentData)
//					.accept(MediaType.APPLICATION_JSON_UTF8)
//					.session(getMokcSession());
//
//			MvcResult result = mockMvc.perform(request)
//					.andExpect(status().isOk()) //模拟发出post请求
//					.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))//预期返回值类型
//					.andReturn();//返回执行结果
//
//			String json = result.getResponse().getContentAsString();
//
//			JsonResult rs = Tools.json.toObject(json, JsonResult.class);
//			if(rs.isSuccess()){
//				String jsonString = Tools.json.toJson(rs.get("total"));
//				String dataJson = JSON.toJSONString(rs.getData());
//				List<ConfirmOrderVo> confirmOrderVo = JSONObject.parseArray(dataJson, ConfirmOrderVo.class);
//                ConfirmOrderVo c1 = confirmOrderVo.get(0);
//				log.info("total:{}", jsonString/*jsonObject.getBigDecimal("subtotal")*/);
//                log.info("shipping:{}", c1.getShipping()/*jsonObject.getBigDecimal("subtotal")*/);
//			}
//			else{
//				throw new RuntimeException(rs.getMessage());
//			}
//		}

	}
	
//	@Test
	public void doCreateSingle() throws Exception {
		int quan = 4,salePrice = 8,price = 30;
		OrderGoodsSkuVo ids = new OrderGoodsSkuVo();
		ids.setQuan(Integer.valueOf(quan));
		//ids.setValueids(Arrays.asList(20l,30l,60l));
		ids.setSkuId(1070133339115433985l);
		ids.setGoodsId(1070133194256756737l);
		
		BigDecimal amt = new BigDecimal(79.92);
		Long memberId =1063965054397698050l;
		
		BaseTransferEntity baseTransferEntity = JwtUtil.wrapAsTransferEntity(ids, this.randomKey);
		String contentData = Tools.json.toJson(baseTransferEntity);
		String idsJson = Tools.json.toJson(ids);
		this.getMokcSession().setAttribute("memberId", memberId);
		
		MockHttpServletRequestBuilder request = post("/order/createorder/single")
			.contentType(MediaType.APPLICATION_JSON_UTF8)
			.header("Authorization", "Bearer " + this.token)
			.header("Content-Type",MediaType.APPLICATION_JSON_VALUE)
			.param("amt",amt.toString())
			//.param("memberid", memberId.toString())
			.param("shipmentId","-1")
			.param("addressId","1073481935026393089")
			.param("buyerMessage", "我要留言。")
			.content(contentData)
			.accept(MediaType.APPLICATION_JSON_UTF8)
			.session(getMokcSession());
		
		MvcResult result = mockMvc.perform(request)
			.andExpect(status().isOk()) //模拟发出post请求
			.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))//预期返回值类型
			.andReturn();//返回执行结果
		
		String json = result.getResponse().getContentAsString();
		JsonResult rs = Tools.json.toObject(json, JsonResult.class);
		if(rs.isSuccess()){
			;//
		}
		else{
			throw new RuntimeException(rs.getMessage());
		}
	}

	/**
	 * @description:
	 * @param:
	 * @return
	 * @author Mr.Simple
	 * @date 2019/3/27 9:51
     *  单个添加
     * //		OrderGoodsSkuVo ids1 = new OrderGoodsSkuVo();
     * //		ids1.setQuan(Integer.valueOf(quan));
     * //		//ids.setValueids(Arrays.asList(20l,30l,60l));
     * //		ids1.setSkuId(1102521775672791044L);
     * //		ids1.setGoodsId(1102521624308748289L);
     * //		cars.add(ids1);
     * //		OrderGoodsSkuVo ids2 = new OrderGoodsSkuVo();
     * //		ids2.setQuan(Integer.valueOf(quan));
     * //		//ids.setValueids(Arrays.asList(20l,30l,60l));
     * //		ids2.setSkuId(1101661397908819971L);
     * //		ids2.setGoodsId(1101655736655384577L);
     * //		cars.add(ids2);
	 */
//	@Test
	@Rollback(false)
	public void doCreateMulti() throws Exception {
//		int quan = 1/*,salePrice = 8,price = 30*/;
//		List<OrderGoodsSkuVo> cars = new ArrayList<>();{
//			OrderGoodsSkuVo ids1 = new OrderGoodsSkuVo();
//      		ids1.setQuan(Integer.valueOf(quan));
//      		//ids.setValueids(Arrays.asList(20l,30l,60l));
//      		ids1.setSkuId(1102521775672791044L);
//      		ids1.setGoodsId(1102521624308748289L);
//      		cars.add(ids1);
//      		OrderGoodsSkuVo ids2 = new OrderGoodsSkuVo();
//      		ids2.setQuan(Integer.valueOf(quan));
//      		//ids.setValueids(Arrays.asList(20l,30l,60l));
//      		ids2.setSkuId(1101661397908819971L);
//      		ids2.setGoodsId(1101655736655384577L);
//      		cars.add(ids2);
//		}
//
//		Long memberId =1067707280516718594L;
//		BaseTransferEntity baseTransferEntity = JwtUtil.wrapAsTransferEntity(cars, this.randomKey);
//		String contentData = Tools.json.toJson(baseTransferEntity);
//
//
//		//String idsJson = Tools.json.toJson(ids);
//		BigDecimal amt = new BigDecimal(1/*total*/);
//		BigDecimal allAmt = new BigDecimal(5)/*Tools.bigDecimal.add(amt, new BigDecimal(5))*/;
//
//		MockHttpServletRequestBuilder request = post("/order/createorder/multi")
//			.contentType(MediaType.APPLICATION_JSON_UTF8)
//			.header("Authorization", "Bearer " + this.token)
//			.header("Content-Type",MediaType.APPLICATION_JSON_VALUE)
//			.param("amt",allAmt.toString())
//			//.param("memberid", memberId.toString())
//			.param("shipmentId", "-1")
//			.param("addressId", "1077028699297873921")
//			.param("buyerMessage", "我要留言。")
//			.content(contentData)
//			.accept(MediaType.APPLICATION_JSON_UTF8)
//			.session(getMokcSession());
//
//		MvcResult result = mockMvc.perform(request)
//			.andExpect(status().isOk()) //模拟发出post请求
//			.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))//预期返回值类型
//			.andReturn();//返回执行结果
//
//		String json = result.getResponse().getContentAsString();
//		JsonResult rs = Tools.json.toObject(json, JsonResult.class);
//		if(rs.isSuccess()){
//			;//
//		}
//		else{
//			throw new RuntimeException(rs.getMessage());
//		}
	}
	
//	@Test
	public void doProfit() throws Exception {
		
		MockHttpServletRequestBuilder request = post("/order/createorder/profit")
			.contentType(MediaType.APPLICATION_JSON_UTF8)
			.header("Authorization", "Bearer " + this.token)
			.header("Content-Type",MediaType.APPLICATION_JSON_VALUE)
			.param("shipmentId", "-1")
			.param("addressId", "1073481935026393089")
			.param("buyerMessage", "我要留言。")
			.accept(MediaType.APPLICATION_JSON_UTF8)
			.session(getMokcSession());
		
		MvcResult result = mockMvc.perform(request)
			.andExpect(status().isOk()) //模拟发出post请求
			.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))//预期返回值类型
			.andReturn();//返回执行结果
		
		String json = result.getResponse().getContentAsString();
		JsonResult rs = Tools.json.toObject(json, JsonResult.class);
		if(rs.isSuccess()){
			;//
		}
		else{
			throw new RuntimeException(rs.getMessage());
		}
	}

	@Test
    @Rollback(false)
    public void testOrder() throws Exception{

	    for(int i = 0 ; i < 10; i++){
            Random random = new Random();
	        int key = random.nextInt(4);
	        if(key == 0){
	            log.info("==================换货:{}", i);
                doCreateOrderAndChange();
            } else if(key == 1){
                log.info("==================仅退款:{}", i);
                doCreateOrderAndRefund();
            } else if(key == 2){
                log.info("==================退货退款:{}", i);
                doCreateOrderAndReGoods();
            }  else if(key == 3){
                log.info("==================仅下单:{}", i);
                doCreateOrderOnly();
            }
            Thread.sleep(10000);
        }
    }

    public void doCreateOrderOnly() throws Exception{
        List<OrderGoodsSkuVo> cars = getGoods();
        BaseTransferEntity baseTransferEntity = JwtUtil.wrapAsTransferEntity(cars, this.randomKey);
        String contentData = Tools.json.toJson(baseTransferEntity);

        log.info("=========================下单前确认");
        BigDecimal allAmt = confirm(contentData);

        log.info("=========================下单");
        Long orderId = createOrder(allAmt, contentData);
    }

//	@Test
    @Rollback(false)
    public void doCreateOrderAndChange() throws Exception{

        List<OrderGoodsSkuVo> cars = getGoods();

        Long orderId = createAndPayOrder(cars);
        log.info("==========================orderId:{}", orderId);

        //发货和收货
        sendAndReceive(orderId);

        //查看是否分单，若分单根据goodsId,skuId查找子单id
        orderId = isSplit(orderId, cars.get(0));

        //买家申请换货
        Long returnsId = applyChangeGoods(cars.get(0), orderId, ReturnsTypeEnum.ChangeGoods);
        log.info("returnsId:{}", returnsId);

        //防止状态错误
        Thread.sleep(2000);

        //商家同意
        acceptApply(returnsId, ReturnsGoodsStatusEnum.exchangeAuditSucess);

        //防止状态错误
        Thread.sleep(2000);

        //买家发货
        returnShipment(returnsId);

        //防止状态错误
        Thread.sleep(2000);

        //商家收货（验收换货商品）
        acceptanceReturnsGoods(returnsId);

        //防止状态错误
        Thread.sleep(2000);

        //商家验货（修改状态为已验收换货退品）
        warehouseOperateRefund(returnsId, ReturnsGoodsStatusEnum.exchangeAcceptanceReceived);

        //防止状态错误
        Thread.sleep(2000);

        //修改状态为允许换货
        serviceOperateRefund(returnsId, ReturnsGoodsStatusEnum.allowExchange);

        //防止状态错误
        Thread.sleep(2000);

        //商家发货
        sendReturnGoods(returnsId);

        //防止状态错误
        Thread.sleep(2000);

        //买家收货
        confirmReceipt(returnsId);
    }

//    @Test
    @Rollback(false)
    public void doCreateOrderAndRefund() throws Exception{
        List<OrderGoodsSkuVo> cars = getGoods();

        Long orderId = createAndPayOrder(cars);

        //查看是否分单，若分单根据goodsId,skuId查找子单id
        orderId = isSplit(orderId, cars.get(0));

        //买家申请退款
        Long returnsId = applyChangeGoods(cars.get(0), orderId, ReturnsTypeEnum.Refund);
        log.info("returnsId:{}", returnsId);

        Thread.sleep(2000);

        //修改状态为允许换货
        serviceOperateRefund(returnsId, ReturnsGoodsStatusEnum.refundAuditSucess);

        //执行退款操作
        doRefundPay(returnsId);

        //等待退款线程执行完毕
        Thread.sleep(10000);

    }

//    @Test
    @Rollback(false)
    public void doCreateOrderAndReGoods() throws Exception{
        List<OrderGoodsSkuVo> cars = getGoods();

        Long orderId = createAndPayOrder(cars);

        //发货和收货
        sendAndReceive(orderId);

        //查看是否分单，若分单根据goodsId,skuId查找子单id
        orderId = isSplit(orderId, cars.get(0));
        //买家申请退货退款
        Long returnsId = applyChangeGoods(cars.get(0), orderId, ReturnsTypeEnum.ReGoods);
        log.info("returnsId:{}", returnsId);

        //商家同意
        acceptApply(returnsId, ReturnsGoodsStatusEnum.refundAuditSucess);

        //买家发货
        returnShipment(returnsId);

        //商家收货（验收换货商品）
        acceptanceReturnsGoods(returnsId);

        //商家验货（修改状态为已验收换货退品）
        warehouseOperateRefund(returnsId, ReturnsGoodsStatusEnum.exchangeAcceptanceReceived);

        //修改状态为允许退货退款
        serviceOperateRefund(returnsId, ReturnsGoodsStatusEnum.allowRefund);

        //执行退款操作
        doRefundPay(returnsId);

        //等待退款线程执行完毕
        Thread.sleep(10000);

    }

//    @Test
    @Rollback(false)
    public void testForOne() throws Exception{
	    Long returnsId = 1111876382529261569L;
        //商家同意
//        acceptApply(returnsId);

        //买家发货
//        returnShipment(returnsId);

        //商家收货（验收换货商品）
//        acceptanceReturnsGoods(returnsId);

        //商家验货（修改状态为已验收换货退品）
//        warehouseOperateRefund(returnsId);

        //修改状态为允许换货
//        serviceOperateRefund(returnsId);

        //商家发货
//        sendReturnGoods(returnsId);

        //买家收货
        confirmReceipt(returnsId);
    }

    public Long createAndPayOrder(List<OrderGoodsSkuVo> cars) throws Exception{


        BaseTransferEntity baseTransferEntity = JwtUtil.wrapAsTransferEntity(cars, this.randomKey);
        String contentData = Tools.json.toJson(baseTransferEntity);

        log.info("=========================下单前确认");
        BigDecimal allAmt = confirm(contentData);

        log.info("=========================下单");
        Long orderId = createOrder(allAmt, contentData);

        log.info("=========================进入支付");
        pay(orderId);
	    return orderId;
    }

    public List<OrderGoodsSkuVo> getGoods(){
        List<GoodsSkuDefBo> goodsSkuDefBoList = goodsSkuDefBiz.selectList(Condition.create()
                .isNotNull(GoodsSkuDefBo.Key.imageUrl.toString()));
        //下单多少次
        int count = 1;
        log.info("下单 :{} 次", count);
        List<GoodsSkuDefBo> randomList = new ArrayList<>();
        //取多少个商品
        int size = 2/* + (int)(Math.random() * 10)*/;
        log.info("取 :{} 个商品", size);
        //记录已取到的商品skuid
        List<Long> recordSkuId = new ArrayList<>();
        for(int j = 0; j < size;j++){
            //商品下标
            int index = 0 + (int)(Math.random() * 165);
            GoodsSkuDefBo randomBo = new GoodsSkuDefBo();
            randomBo = goodsSkuDefBoList.get(index);
            log.info("=================================goodsId:{}", randomBo.getGoodsId());
            if(recordSkuId.size() >= 1){
                //如果商品sku存在，则重新生成
                while(recordSkuId.contains(randomBo.getId())){
                    index = 0 + (int)(Math.random() * 165);
                    randomBo = goodsSkuDefBoList.get(index);
                }

            }
            recordSkuId.add(randomBo.getId());
            randomList.add(randomBo);
        }
        int quan = 1/*,salePrice = 8,price = 30*/;
        List<OrderGoodsSkuVo> cars = new ArrayList<>();{
            randomList.forEach(item->{
                OrderGoodsSkuVo ids = new OrderGoodsSkuVo();
                ids.setQuan(Integer.valueOf(quan));
                //ids.setValueids(Arrays.asList(20l,30l,60l));
                ids.setSkuId(item.getId());
                ids.setGoodsId(item.getGoodsId());
                cars.add(ids);
            });
        }
//        List<OrderGoodsSkuVo> cars = new ArrayList<>();{
//            OrderGoodsSkuVo ids1 = new OrderGoodsSkuVo();
//            ids1.setQuan(Integer.valueOf(1));
//            //ids.setValueids(Arrays.asList(20l,30l,60l));
//            ids1.setSkuId(1101661397908819970L);
//            ids1.setGoodsId(1101655736655384577L);
//            cars.add(ids1);
//            OrderGoodsSkuVo ids2 = new OrderGoodsSkuVo();
//            ids2.setQuan(Integer.valueOf(1));
//            //ids.setValueids(Arrays.asList(20l,30l,60l));
//            ids2.setSkuId(1069895887570599937L);
//            ids2.setGoodsId(1069895862077620226L);
//            cars.add(ids2);
//        }
        return cars;
    }

    public BigDecimal confirm(String contentData) throws Exception{
        Long memberId = 1067707280516718594L;
        //下单前确认
        this.getMokcSession().setAttribute("memberId", memberId);
        MockHttpServletRequestBuilder request1 = post("/order/createorder/confirm")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .header("Authorization", "Bearer " + this.token)
                .header("Content-Type",MediaType.APPLICATION_JSON_VALUE)
                .param("shipmentId", "-1")
                .param("addressId", "1077028699297873921")
                .content(contentData)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .session(getMokcSession());

        MvcResult result1 = mockMvc.perform(request1)
                .andExpect(status().isOk()) //模拟发出post请求
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))//预期返回值类型
                .andReturn();//返回执行结果

        String json1 = result1.getResponse().getContentAsString();

        JsonResult rs1 = Tools.json.toObject(json1, JsonResult.class);
        String total = "";
        String shipping = "";
        //解析confirm
        if(rs1.isSuccess()){
            String jsonString = Tools.json.toJson(rs1.get("total"));
            log.info("======================total:{}", jsonString/*jsonObject.getBigDecimal("subtotal")*/);
            String dataJson = JSON.toJSONString(rs1.getData());
            List<ConfirmOrderVo> confirmOrderVo = JSONObject.parseArray(dataJson, ConfirmOrderVo.class);
            double allShip = 0.0;
            for (ConfirmOrderVo item: confirmOrderVo){
                allShip += item.getShipping().doubleValue();
                log.info("==============================shipping:{}", item.getShipping()/*jsonObject.getBigDecimal("subtotal")*/);
            }
            log.info("===============================ship:{}", allShip);

            total = jsonString;

            shipping = String.valueOf(allShip);
            log.info("===============================ship:{}", shipping);
        } else {
            throw new RuntimeException(rs1.getMessage());
        }
        BigDecimal allAmt = Tools.bigDecimal.add(new BigDecimal(total), new BigDecimal(shipping));
	    return allAmt;
    }

    public Long createOrder(BigDecimal allAmt, String contentData) throws  Exception{
        MockHttpServletRequestBuilder request = post("/order/createorder/multi")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .header("Authorization", "Bearer " + this.token)
                .header("Content-Type",MediaType.APPLICATION_JSON_VALUE)
                .param("amt",allAmt.toString())
                .param("shipmentId", "-1")
                .param("addressId", "1077028699297873921")
                .param("buyerMessage", "我要留言。")
                .content(contentData)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .session(getMokcSession());

        MvcResult result = mockMvc.perform(request)
                .andExpect(status().isOk()) //模拟发出post请求
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))//预期返回值类型
                .andReturn();//返回执行结果

        String json = result.getResponse().getContentAsString();
        JsonResult rs = Tools.json.toObject(json, JsonResult.class);
        Long orderId = 0L;
        if(rs.isSuccess()){
            OrderPaymemtBo orderPaymemtBo = JSONObject.parseObject(JSON.toJSONString(rs.getData()), OrderPaymemtBo.class);
            orderId = orderPaymemtBo.getOrderId();
        } else {
            throw new RuntimeException(rs.getMessage());
        }
	    return orderId;
    }

    public void pay(Long orderId) throws Exception{
        //钱包付款
        String payType = "Advance";
        String original = "123456";
        String payPassword = Tools.md5.md5Hex(original).toLowerCase();
        MockHttpServletRequestBuilder request2 = post("/pay/pay_by_order")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .header("Authorization", "Bearer " + this.token)
                .header("Content-Type",MediaType.APPLICATION_JSON_VALUE)
                .param("orderId",orderId.toString())
                .param("payType", payType)
                .param("payPassword", payPassword)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .session(getMokcSession());
        MvcResult result2 = mockMvc.perform(request2)
                .andExpect(status().isOk()) //模拟发出post请求
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))//预期返回值类型
                .andReturn();//返回执行结果
        String json2 = result2.getResponse().getContentAsString();
        JsonResult rs2 = Tools.json.toObject(json2, JsonResult.class);
        if(rs2.isSuccess()){
            ;//
        }
        else{
            throw new RuntimeException(rs2.getMessage());
        }
    }

    private void sendAndReceive(Long orderId) throws Exception{
        //查出所有订单（包括子单）
        List<OrderInfoBo> allOrderList = getAllOrder(orderId);
        allOrderList.forEach(item->{

            //商家发货
            sendGoods(item.getId());

            //防止状态错误
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            //买家收货
            try {
                receiverBuy(item.getId());
            } catch (Exception e) {
                e.printStackTrace();
            }

            //防止状态错误
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

    public void sendGoods(Long orderId){
        log.info("=========================发货（下单发货）");
        OrderLogisticsBo logisticsBo = new OrderLogisticsBo();
        logisticsBo.setLogisticsStatus(LogisticsStatusEnum.NONE);
        logisticsBo.setReceiverFullname("sanpao");
        logisticsBo.setLogisticsType(LogisticsTypeEnum.LandCarriage);
        logisticsBo.setLogisticsCompany("申通快递");
        logisticsBo.setLogisticsPayment(LogisticsPaymentEnum.SELLER);
        logisticsBo.setLogisticsNo("147258");
        logisticsBo.setReceiverProvince("New York");
        logisticsBo.setReceiverCity("Nassau");
        logisticsBo.setReceiverDetailAddress("88,minjingni");
        logisticsBo.setReceiverMobile("0987653728");
        logisticsBo.setReceiverPostCode("11765");
        logisticsBo.setSendGoodsTime(new Date());
        logisticsBo.setOrderId(orderId);
        logisticsBo.setGmtCreate(new Date());
        logisticsBo.setGmtModified(new Date());
        logisticsBo.setReceiverAreaId(2071317231617296443L);
        logisticsBo.setCountryCode(CountryCodeEnum.USA);
        logisticsBo.setShipmentId(1087900490842017794L);
        if(!adminOrderService.doOrderInfoSendGoods(logisticsBo, 1L)){
            throw new RuntimeException("发货操作失败");
        }
    }

    public void doRefundPay(Long returnsId){
        log.info("=========================商家执行退款");
	    //执行退款操作
        OrderReturnsBo orderReturnsBo = orderReturnsBiz.selectById(returnsId);
        if(orderReturnsBo == null){
            throw new WakaException(RavvExceptionEnum.SELECT_ERROR);
        }
        orderReturnsBo.setAdjustFee(orderReturnsBo.getRefundFee());
        orderReturnsBo.setFinishTime(new Date());
        adminOrderService.doReundPay(orderReturnsBo, 1L);
    }

    public void receiverBuy(Long orderId) throws Exception{
        log.info("=========================买家收货（下单收货）");
        Long memberId = 1067707280516718594L;
        //下单前确认
        this.getMokcSession().setAttribute("memberId", memberId);
        MockHttpServletRequestBuilder request1 = post("/order/my/receiver")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .header("Authorization", "Bearer " + this.token)
                .header("Content-Type",MediaType.APPLICATION_JSON_VALUE)
                .param("orderid", orderId.toString())
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .session(getMokcSession());

        MvcResult result1 = mockMvc.perform(request1)
                .andExpect(status().isOk()) //模拟发出post请求
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))//预期返回值类型
                .andReturn();//返回执行结果

        String json1 = result1.getResponse().getContentAsString();

        JsonResult rs1 = Tools.json.toObject(json1, JsonResult.class);
        //解析confirm
        if(rs1.isSuccess()){

        } else {
            throw new RuntimeException(rs1.getMessage());
        }
    }

    public Long applyChangeGoods(OrderGoodsSkuVo goodsSkuVo, Long orderId, ReturnsTypeEnum returnsType) throws Exception{
        log.info("=========================仅退款开始");
	    List<OrderReturnSkuVo> skus = new ArrayList<>();
	    OrderReturnSkuVo orderReturnSkuVo = new OrderReturnSkuVo();
	    BeanUtils.copyProperties(goodsSkuVo, orderReturnSkuVo);
        orderReturnSkuVo.setReason("你来打我呀");
        orderReturnSkuVo.setReasonType("颜色/图案/款式与商品描述不符");
        orderReturnSkuVo.setReturnsType(returnsType);
        skus.add(orderReturnSkuVo);
        Long memberId = 1067707280516718594L;
        BaseTransferEntity baseTransferEntity = JwtUtil.wrapAsTransferEntity(skus, this.randomKey);
        String contentData = Tools.json.toJson(baseTransferEntity);

        this.getMokcSession().setAttribute("memberId", memberId);
        MockHttpServletRequestBuilder request1 = post("/order/return/save")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .header("Authorization", "Bearer " + this.token)
                .header("Content-Type",MediaType.APPLICATION_JSON_VALUE)
                .param("orderId", orderId.toString())
                .param("returnsType", returnsType.toString())
                .content(contentData)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .session(getMokcSession());

        MvcResult result1 = mockMvc.perform(request1)
                .andExpect(status().isOk()) //模拟发出post请求
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))//预期返回值类型
                .andReturn();//返回执行结果

        String json1 = result1.getResponse().getContentAsString();
        Long returnsId = 0L;
        JsonResult rs1 = Tools.json.toObject(json1, JsonResult.class);
        if(rs1.isSuccess()){
            OrderReturnsBo orderReturnsBo = JSONObject.parseObject(JSON.toJSONString(rs1.getData()), OrderReturnsBo.class);
            returnsId = orderReturnsBo.getId();
        } else {
            throw new RuntimeException(rs1.getMessage());
        }
        return returnsId;
    }

    public void acceptApply(Long returnsId, ReturnsGoodsStatusEnum status){
        log.info("=========================商家同意退换货");
        //根据退货id查询出信息
        OrderReturnsBo orderReturnsBo = orderReturnsBiz.selectById(returnsId);
        orderReturnsBo.setStatus(status);
        orderReturnsBo.setGmtModified(new Date());
        orderReturnsBo.setCheckTime(new Date());
        orderReturnsService.updateOrderReturnsAndCreateLog(1L, OperatorTypeEnum.server, orderReturnsBo);
    }

    public void returnShipment(Long returnsId) throws Exception{
        log.info("=========================买家退回商品");
        Long memberId = 1067707280516718594L;
        String logisticsNo = "125897";
        String shipmentId = "1087900490842017794";

        this.getMokcSession().setAttribute("memberId", memberId);
        MockHttpServletRequestBuilder request1 = post("/order/return/return_shipment")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .header("Authorization", "Bearer " + this.token)
                .header("Content-Type",MediaType.APPLICATION_JSON_VALUE)
                .param("returnsId", returnsId.toString())
                .param("logisticsNo", logisticsNo)
                .param("shipmentId", shipmentId)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .session(getMokcSession());

        MvcResult result1 = mockMvc.perform(request1)
                .andExpect(status().isOk()) //模拟发出post请求
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))//预期返回值类型
                .andReturn();//返回执行结果

        String json1 = result1.getResponse().getContentAsString();
        JsonResult rs1 = Tools.json.toObject(json1, JsonResult.class);
        //解析confirm
        if(rs1.isSuccess()){

        } else {
            throw new RuntimeException(rs1.getMessage());
        }
    }

    public void acceptanceReturnsGoods(Long returnsId){
        log.info("=========================商家验货（退换货商品）");
	    //根据returnsId查出returnsDetail
        OrderReturnsVo returnsVo = getByreturnsId(returnsId);

        adminOrderService.checkAndAcceptanceGoods(returnsVo, 1L);
    }

    public void warehouseOperateRefund(Long returnsId, ReturnsGoodsStatusEnum status){
        log.info("=========================商家验货（修改状态为已验收换货退品）");
        OrderReturnsVo returnsVo = getByreturnsId(returnsId);
        returnsVo.setStatus(status);

        adminOrderService.warehouseOperateRefund(returnsVo, 1L);
    }

    public void serviceOperateRefund(Long returnsId, ReturnsGoodsStatusEnum status){
        log.info("=========================修改状态为允许退款");
	    OrderReturnsVo returnsVo = getByreturnsId(returnsId);
	    returnsVo.setStatus(status);
	    returnsVo.setBuyerBearPostage(false);//如果拒绝换货，则要多设置是否退回字段
	    adminOrderService.serviceOperateRefund(returnsVo, 1L);
    }

    public void sendReturnGoods(Long returnsId){
        log.info("=========================商家发回退换货商品");
        OrderLogisticsBo logisticsBo = new OrderLogisticsBo();
        logisticsBo.setLogisticsStatus(LogisticsStatusEnum.NONE);
        logisticsBo.setReceiverFullname("sanpao");
        logisticsBo.setLogisticsType(LogisticsTypeEnum.LandCarriage);
        logisticsBo.setLogisticsCompany("申通快递");
        logisticsBo.setLogisticsPayment(LogisticsPaymentEnum.SELLER);
        logisticsBo.setLogisticsNo("258147");
        logisticsBo.setReceiverProvince("New York");
        logisticsBo.setReceiverCity("Nassau");
        logisticsBo.setReceiverDetailAddress("88,minjingni");
        logisticsBo.setReceiverMobile("0987653728");
        logisticsBo.setReceiverPostCode("11765");
        logisticsBo.setSendGoodsTime(new Date());
        logisticsBo.setReturnsId(returnsId);
        logisticsBo.setGmtCreate(new Date());
        logisticsBo.setGmtModified(new Date());
        logisticsBo.setReceiverAreaId(2071317231617296443L);
        logisticsBo.setCountryCode(CountryCodeEnum.USA);
        logisticsBo.setShipmentId(1087900490842017794L);
        adminOrderService.doReturnGoodsSendGoods(logisticsBo, returnsId, 1L);
    }

    public void confirmReceipt(Long returnsId) throws Exception{
        log.info("=========================买家收货（最终收货）");
        Long memberId = 1067707280516718594L;
        BaseTransferEntity baseTransferEntity = JwtUtil.wrapAsTransferEntity(returnsId, this.randomKey);
        String contentData = Tools.json.toJson(baseTransferEntity);

        this.getMokcSession().setAttribute("memberId", memberId);
        MockHttpServletRequestBuilder request1 = post("/order/return/confirm_receipt")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .header("Authorization", "Bearer " + this.token)
                .header("Content-Type",MediaType.APPLICATION_JSON_VALUE)
                .param("returnsId", returnsId.toString())
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .session(getMokcSession());

        MvcResult result1 = mockMvc.perform(request1)
                .andExpect(status().isOk()) //模拟发出post请求
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))//预期返回值类型
                .andReturn();//返回执行结果

        String json1 = result1.getResponse().getContentAsString();
        JsonResult rs1 = Tools.json.toObject(json1, JsonResult.class);
        //解析confirm
        if(rs1.isSuccess()){

        } else {
            throw new RuntimeException(rs1.getMessage());
        }
    }


    private OrderReturnsVo getByreturnsId(Long returnsId){
        OrderReturnsBo returnsBo = orderReturnsBiz.selectById(returnsId);
        OrderReturnsVo returnsVo = new OrderReturnsVo();
        BeanUtils.copyProperties(returnsBo, returnsVo);
        //根据returnsId查出orderreturnsdetail id
        OrderReturnsDetailBo returnsDetailBo = returnsDetailBiz.selectOne(Condition.create()
                .eq(OrderReturnsDetailBo.Key.returnsId.toString(), returnsId));
        returnsVo.setDamagedCondition(DamagedConditionEnum.undamaged);
        returnsVo.setPackingDamaged(false);
        returnsVo.setOrderReturnsDetailId(returnsDetailBo.getId());
        returnsVo.setGmtModified(new Date());
        return returnsVo;
    }

    private Long isSplit(Long orderId, OrderGoodsSkuVo orderGoodsSkuVo){
	    OrderInfoBo orderInfoBo = orderInfoBiz.selectById(orderId);
	    if(orderInfoBo.getOrderType().equals(OrderTypeEnum.SINGLE)){
	        return orderId;
        } else if(orderInfoBo.getOrderType().equals(OrderTypeEnum.MASTER)){
	        List<OrderInfoBo> allSubOrder = orderInfoBiz.selectList(Condition.create()
                                            .eq(OrderInfoBo.Key.pid.toString(), orderId));
	        List<Long> orderIdList = new ArrayList<>();
	        allSubOrder.forEach(item->{
	            orderIdList.add(item.getId());
            });
	        //查找要退货订单id
            List<OrderGoodsBo> orderGoodsBoList = orderGoodsBiz.selectList(Condition.create()
                            .in(OrderGoodsBo.Key.orderId.toString(), orderIdList));
            //记录退货orderGoods
            List<OrderGoodsBo> returnsGoods = new ArrayList<>();
            orderGoodsBoList.forEach(item->{
                if(item.getGoodsId().equals(orderGoodsSkuVo.getGoodsId())
                        && item.getSkuId().equals(orderGoodsSkuVo.getSkuId())){
                    returnsGoods.add(item);
                }
            });
            return returnsGoods.get(0).getOrderId();
        }
	    return null;
    }

    private List<OrderInfoBo> getAllOrder(Long orderId){
        OrderInfoBo orderInfoBo = orderInfoBiz.selectById(orderId);
        List<OrderInfoBo> allList = new ArrayList<>();
        if(orderInfoBo.getOrderType().equals(OrderTypeEnum.SINGLE)){
            allList.add(orderInfoBo);
            return allList;
        } else if(orderInfoBo.getOrderType().equals(OrderTypeEnum.MASTER)){
            //查找要退货订单id
            List<OrderInfoBo> orderInfoBoList = orderInfoBiz.selectList(Condition.create()
                    .eq(OrderInfoBo.Key.pid.toString(), orderId));
            return orderInfoBoList;
        }
        return null;
    }



}
