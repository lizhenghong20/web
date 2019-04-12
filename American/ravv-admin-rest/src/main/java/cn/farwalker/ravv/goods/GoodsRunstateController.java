package cn.farwalker.ravv.goods;
import java.util.List;

import javax.annotation.Resource;

import cn.farwalker.ravv.admin.goods.AdminGoodsService;
import cn.farwalker.waka.core.WakaException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.farwalker.ravv.service.goods.base.biz.IGoodsBiz;
import cn.farwalker.ravv.service.goods.base.model.GoodsBo;
import cn.farwalker.ravv.service.goods.base.model.GoodsVo;
import cn.farwalker.ravv.service.goods.base.model.SimpleGoodsVo;
import cn.farwalker.waka.core.JsonResult;

import com.baomidou.mybatisplus.plugins.Page;
import com.cangwu.frame.web.crud.QueryFilter;

/**
 * 商品运维信息管理<br/>
 * //手写的注释:以"//"开始 <br/>
 * @author generateModel.java
 */
@RestController
@RequestMapping("/goods/goodsrunstate")
public class GoodsRunstateController{
    private final static  Logger log =LoggerFactory.getLogger(GoodsRunstateController.class);
    @Resource
    private IGoodsBiz goodsBiz;
    protected IGoodsBiz getBiz(){
        return goodsBiz;
    }

    @Autowired
    private AdminGoodsService adminGoodsService;

    /**商品基础信息详情
     * @param goodsId null\r\n<br/>
     */
    @RequestMapping("/get")
    public JsonResult<GoodsVo> get(Long goodsId){
        try {
//            if(goodsId == 0 || goodsId == null){
//                throw new WakaException("商品不能为空");
//            }
            return JsonResult.newSuccess(adminGoodsService.getGoodsRunstate(goodsId));
        } catch (WakaException e) {
            log.error("删除记录", e);
            return JsonResult.newFail(e.getMessage());
        } catch (Exception e) {
            log.error("", e);
            return JsonResult.newFail(e.getMessage());
        }

    }
    /**商品基础信息详情
     * @param goodsId 商品id<br/>
     * @param skuId skuId<br/>
     */
    @RequestMapping("/simple")
    public JsonResult<SimpleGoodsVo> getSimpleGoods(Long goodsId,Long skuId){
        try {
            if(goodsId == 0 || goodsId == null){
                throw new WakaException("商品不能为空");
            }
            if(skuId==null){
                throw new WakaException("skuId不能为空");
            }
            SimpleGoodsVo rs =null;
            return JsonResult.newSuccess(rs);
        } catch (WakaException e) {
            log.error("删除记录", e);
            return JsonResult.newFail(e.getMessage());
        } catch (Exception e) {
            log.error("", e);
            return JsonResult.newFail(e.getMessage());
        }


    }
    /**获取商品基础信息列表
     * @param query 查询条件\r\n<br/>
     * @param start 开始行号\r\n<br/>
     * @param size 记录数\r\n<br/>
     * @param sortfield 排序+/-字段\r\n<br/>
     */
    @RequestMapping("/list")
    public JsonResult<Page<GoodsBo>> list(@RequestBody List<QueryFilter> query,Integer start,Integer size,String sortfield){
        try {
//            if(Tools.collection.isEmpty(query) || start < 0 || size < 0 || Tools.string.isEmpty(sortfield)){
//                throw new WakaException(RavvExceptionEnum.INVALID_PARAMETER_ERROR);
//            }
            return JsonResult.newSuccess(adminGoodsService.list(query, start, size, sortfield));
        } catch (WakaException e) {
            log.error("删除记录", e);
            return JsonResult.newFail(e.getMessage());
        } catch (Exception e) {
            log.error("", e);
            return JsonResult.newFail(e.getMessage());
        }

    }
    /**添加访问记录
     * @param memberId 会员id<br/>
     * @param goodsId 商品id<br/>
     */
    @RequestMapping("/viewlog")
    public JsonResult<Integer> viewLog(Long memberId,Long goodsId){
        try {
            //createMethodSinge创建方法
            if(memberId == null){
                return JsonResult.newFail("会员id不能为空");
            }
            if(goodsId == null){
                return JsonResult.newFail("商品id不能为空");
            }
            Integer rs = null;
            return JsonResult.newSuccess(rs);
        } catch (WakaException e) {
            log.error("删除记录", e);
            return JsonResult.newFail(e.getMessage());
        } catch (Exception e) {
            log.error("", e);
            return JsonResult.newFail(e.getMessage());
        }

    }
    
    /**获取供应商商品运维信息列表
     * @param query 查询条件\r\n<br/>
     * @param start 开始行号\r\n<br/>
     * @param size 记录数\r\n<br/>
     * @param sortfield 排序+/-字段\r\n<br/>
     */
    @RequestMapping("/merchant_goodsrunstate")
    public JsonResult<Page<GoodsBo>> getMerchantGoodsrunstate(Long merchantId, @RequestBody List<QueryFilter> query,
                                                              Integer start,Integer size,String sortfield){
        try {
            if (merchantId == null) {
                throw new WakaException("供应商id不能为空");
            }

            return JsonResult.newSuccess(adminGoodsService.getMerchantGoodsrunstate(merchantId, query, start,
                    size, sortfield)) ;
        } catch (WakaException e) {
            log.error("删除记录", e);
            return JsonResult.newFail(e.getMessage());
        } catch (Exception e) {
            log.error("", e);
            return JsonResult.newFail(e.getMessage());
        }

    }
}