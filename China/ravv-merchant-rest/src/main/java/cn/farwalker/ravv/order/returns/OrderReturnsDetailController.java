package cn.farwalker.ravv.order.returns;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.cangwu.frame.web.crud.QueryFilter;

import cn.farwalker.ravv.service.order.returns.biz.IOrderReturnsDetailBiz;
import cn.farwalker.ravv.service.order.returns.model.OrderReturnsDetailBo;
import cn.farwalker.waka.core.JsonResult;
import cn.farwalker.waka.core.base.controller.ControllerUtils;

/**
 * 订单退货详情<br/>
 * 订单退货详情 controller<br/>
 * //手写的注释:以"//"开始 <br/>
 * @author generateModel.java
 */
@RestController
@RequestMapping("/order/returns/detail")
public class OrderReturnsDetailController{
    private final static  Logger log =LoggerFactory.getLogger(OrderReturnsDetailController.class);
    @Resource
    private IOrderReturnsDetailBiz orderReturnsDetailBiz;
    protected IOrderReturnsDetailBiz getBiz(){
        return orderReturnsDetailBiz;
    }
    /**创建记录
     * @param vo null<br/>
     */
    @RequestMapping("/create")
    public JsonResult<?> doCreate(@RequestBody OrderReturnsDetailBo vo){
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
    public JsonResult<OrderReturnsDetailBo> doGet(@RequestParam Long id){
        //createMethodSinge创建方法
        if(id==null){
            return JsonResult.newFail("id不能为空");
        }
        OrderReturnsDetailBo rs =getBiz().selectById(id);
        //需返回对应sku商品信息
        return JsonResult.newSuccess(rs);
    }
    /**列表记录
     * @param query 查询条件<br/>
     * @param start 开始行号<br/>
     * @param size 记录数<br/>
     * @param sortfield 排序(+字段1,-字段名2)<br/>
     */
    @RequestMapping("/list")
    public JsonResult<Page<OrderReturnsDetailBo>> doList(@RequestBody List<QueryFilter> query,Integer start,Integer size,String sortfield){
        //createMethodSinge创建方法
        Page<OrderReturnsDetailBo> page =ControllerUtils.getPage(start,size,sortfield);
        Wrapper<OrderReturnsDetailBo> wrap =ControllerUtils.getWrapper(query);
        Page<OrderReturnsDetailBo> rs =getBiz().selectPage(page,wrap);
        return JsonResult.newSuccess(rs);
    }
    /**修改记录
     * @param vo null<br/>
     */
    @RequestMapping("/update")
    public JsonResult<?> doUpdate(@RequestBody OrderReturnsDetailBo vo){
        //createMethodSinge创建方法
        if(vo == null){
            return JsonResult.newFail("vo不能为空");
        }
        Object rs =getBiz().updateById(vo);
        return JsonResult.newSuccess(rs);
    }
}