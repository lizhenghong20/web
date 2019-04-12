package cn.farwalker.ravv.goods;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.farwalker.ravv.service.goods.base.biz.IGoodsBiz;
import cn.farwalker.ravv.service.goods.base.model.GoodsBo;
import cn.farwalker.ravv.service.goods.base.model.GoodsVo;
import cn.farwalker.ravv.service.goods.base.model.SimpleGoodsVo;
import cn.farwalker.waka.core.JsonResult;
import cn.farwalker.waka.core.base.controller.ControllerUtils;
import cn.farwalker.waka.util.Tools;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.cangwu.frame.web.crud.QueryFilter;

/**
 * 商品运维信息管理<br/>
 * //手写的注释:以"//"开始 <br/>
 * @author generateModel.java
 */
@RestController
@RequestMapping("/goods/goodsrunstate")
public class GoodsRunstateController {
    private final static  Logger log =LoggerFactory.getLogger(GoodsRunstateController.class);
    @Resource
    private IGoodsBiz goodsBiz;
    protected IGoodsBiz getBiz(){
        return goodsBiz;
    }

    /**商品基础信息详情
     * @param goodsId null\r\n<br/>
     */
    @RequestMapping("/get")
    public JsonResult<GoodsVo> get(Long goodsId){
        //createMethodSinge创建方法
    	GoodsBo bo =getBiz().selectById(goodsId);
    	GoodsVo vo = Tools.bean.cloneBean(bo, new GoodsVo());
        return JsonResult.newSuccess(vo);
    }
    /**商品基础信息详情
     * @param goodsId 商品id<br/>
     * @param skuId skuId<br/>
     */
    @RequestMapping("/simple")
    public JsonResult<SimpleGoodsVo> getSimpleGoods(Long goodsId,Long skuId){
        //createMethodSinge创建方法
        if(goodsId==null){
            return JsonResult.newFail("商品id不能为空");
        }
        if(skuId==null){
            return JsonResult.newFail("skuId不能为空");
        }
        SimpleGoodsVo rs =null;
        return JsonResult.newSuccess(rs);
    }
    /**获取商品基础信息列表
     * @param query 查询条件\r\n<br/>
     * @param start 开始行号\r\n<br/>
     * @param size 记录数\r\n<br/>
     * @param sortfield 排序+/-字段\r\n<br/>
     */
    @RequestMapping("/list")
    public JsonResult<Page<GoodsBo>> list(Long userId, @RequestBody List<QueryFilter> query,Integer start,Integer size,String sortfield){
		if (userId == null) {
			return JsonResult.newFail("当前用户id不能为空");
		}
    	
        Page<GoodsBo> page =ControllerUtils.getPage(start,size,sortfield);
        Wrapper<GoodsBo> wrap =ControllerUtils.getWrapper(query);
        wrap.eq(GoodsBo.Key.merchantId.toString(), userId);
        
        Page<GoodsBo> rs =getBiz().selectPage(page,wrap);
        return JsonResult.newSuccess(rs);
    }
    /**添加访问记录
     * @param memberId 会员id<br/>
     * @param goodsId 商品id<br/>
     */
    @RequestMapping("/viewlog")
    public JsonResult<Integer> viewLog(Long memberId,Long goodsId){
        //createMethodSinge创建方法
        if(memberId==null){
            return JsonResult.newFail("会员id不能为空");
        }
        if(goodsId==null){
            return JsonResult.newFail("商品id不能为空");
        }
        Integer rs =null;
        return JsonResult.newSuccess(rs);
    }
}