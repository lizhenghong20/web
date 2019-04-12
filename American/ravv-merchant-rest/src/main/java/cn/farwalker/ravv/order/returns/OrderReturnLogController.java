package cn.farwalker.ravv.order.returns;
import cn.farwalker.waka.core.JsonResult;
import javax.annotation.Resource;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.baomidou.mybatisplus.mapper.Wrapper;
import cn.farwalker.waka.core.base.controller.ControllerUtils;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import cn.farwalker.ravv.service.order.returns.biz.IOrderReturnLogBiz;
import cn.farwalker.ravv.service.order.returns.model.OrderReturnLogBo;
import org.springframework.web.bind.annotation.RequestBody;
import com.baomidou.mybatisplus.plugins.Page;
import java.util.List;
import com.cangwu.frame.web.crud.QueryFilter;

/**
 * 退单操作日志<br/>
 * 退单操作日志 controller<br/>
 * //手写的注释:以"//"开始 <br/>
 * @author generateModel.java
 */
@RestController
@RequestMapping("/order/returns/returnsLog")
public class OrderReturnLogController{
    private final static  Logger log =LoggerFactory.getLogger(OrderReturnLogController.class);
    @Resource
    private IOrderReturnLogBiz orderReturnLogBiz;
    protected IOrderReturnLogBiz getBiz(){
        return orderReturnLogBiz;
    }
    /**创建记录
     * @param vo null<br/>
     */
    @RequestMapping("/create")
    public JsonResult<?> doCreate(@RequestBody OrderReturnLogBo vo){
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
    public JsonResult<OrderReturnLogBo> doGet(@RequestParam Long id){
        //createMethodSinge创建方法
        if(id==null){
            return JsonResult.newFail("id不能为空");
        }
        OrderReturnLogBo rs =getBiz().selectById(id);
        return JsonResult.newSuccess(rs);
    }
    /**列表记录
     * @param query 查询条件<br/>
     * @param start 开始行号<br/>
     * @param size 记录数<br/>
     * @param sortfield 排序(+字段1,-字段名2)<br/>
     */
    @RequestMapping("/list")
    public JsonResult<Page<OrderReturnLogBo>> doList(@RequestBody List<QueryFilter> query,Integer start,Integer size,String sortfield, Long returnsId){
        //createMethodSinge创建方法
        Page<OrderReturnLogBo> page =ControllerUtils.getPage(start,size,sortfield);
        Wrapper<OrderReturnLogBo> wrap =ControllerUtils.getWrapper(query);
        wrap.eq(OrderReturnLogBo.Key.returnId.toString(), returnsId);
        wrap.orderBy(OrderReturnLogBo.Key.operationTime.toString(), false);
        wrap.orderBy(OrderReturnLogBo.Key.id.toString(), false);
        Page<OrderReturnLogBo> rs =getBiz().selectPage(page,wrap);
        return JsonResult.newSuccess(rs);
    }
    
    /**修改记录
     * @param vo null<br/>
     */
    @RequestMapping("/update")
    public JsonResult<?> doUpdate(@RequestBody OrderReturnLogBo vo){
        //createMethodSinge创建方法
        if(vo == null){
            return JsonResult.newFail("vo不能为空");
        }
        Object rs =getBiz().updateById(vo);
        return JsonResult.newSuccess(rs);
    }
}