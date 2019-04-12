package cn.farwalker.ravv.order.ordergoods;
import cn.farwalker.waka.core.JsonResult;
import javax.annotation.Resource;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.baomidou.mybatisplus.mapper.Wrapper;

import cn.farwalker.waka.core.base.controller.ControllerUtils;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import cn.farwalker.ravv.service.order.ordergoods.biz.IOrderGoodsBiz;
import cn.farwalker.ravv.service.order.ordergoods.model.OrderGoodsBo;
import com.baomidou.mybatisplus.plugins.Page;
import java.util.List;
import com.cangwu.frame.web.crud.QueryFilter;

/**
 * 订单商品快照<br/>
 * 订单商品快照 controller<br/>
 * //手写的注释:以"//"开始 <br/>
 * @author generateModel.java
 */
@RestController
@RequestMapping("/order/ordergoods")
public class OrderGoodsController{
    private final static  Logger log =LoggerFactory.getLogger(OrderGoodsController.class);
    @Resource
    private IOrderGoodsBiz orderGoodsBiz;
    protected IOrderGoodsBiz getBiz(){
        return orderGoodsBiz;
    }
    /**创建记录
     * @param vo null<br/>
     */
    @RequestMapping("/create")
    public JsonResult<?> doCreate(OrderGoodsBo vo){
        //createMethodSinge创建方法
        if(vo == null){
            return JsonResult.newFail("vo不能为空");
        }
        Object rs =getBiz().insert(vo);
        return JsonResult.newSuccess(rs);
    }
    /**删除记录
     * @param id null<br/>
     */
    @RequestMapping("/delete")
    public JsonResult<Boolean> doDelete(@RequestParam Long id){
        //createMethodSinge创建方法
        if(id==null){
            return JsonResult.newFail("id不能为空");
        }
        Boolean rs =getBiz().deleteById(id);
        return JsonResult.newSuccess(rs);
    }
    /**取得单条记录
     * @param id null<br/>
     */
    @RequestMapping("/get")
    public JsonResult<OrderGoodsBo> doGet(@RequestParam Long id){
        //createMethodSinge创建方法
        if(id==null){
            return JsonResult.newFail("id不能为空");
        }
        OrderGoodsBo rs =getBiz().selectById(id);
        return JsonResult.newSuccess(rs);
    }
    /**列表记录
     * @param query 查询条件<br/>
     * @param start 开始行号<br/>
     * @param size 记录数<br/>
     * @param sortfield 排序(+字段1,-字段名2)<br/>
     */
    @RequestMapping("/list")
    public JsonResult<Page<OrderGoodsBo>> doList(List<QueryFilter> query,Integer start,Integer size,String sortfield){
        //createMethodSinge创建方法
        Page<OrderGoodsBo> page =ControllerUtils.getPage(start,size,sortfield);
        Wrapper<OrderGoodsBo> wrap =ControllerUtils.getWrapper(query);
        Page<OrderGoodsBo> rs =getBiz().selectPage(page,wrap);
        return JsonResult.newSuccess(rs);
    }
    /**修改记录
     * @param vo null<br/>
     */
    @RequestMapping("/update")
    public JsonResult<?> doUpdate(OrderGoodsBo vo){
        //createMethodSinge创建方法
        if(vo == null){
            return JsonResult.newFail("vo不能为空");
        }
        Object rs =getBiz().updateById(vo);
        return JsonResult.newSuccess(rs);
    }
}