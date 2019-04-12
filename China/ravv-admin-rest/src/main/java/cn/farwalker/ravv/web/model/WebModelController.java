package cn.farwalker.ravv.web.model;
import cn.farwalker.ravv.admin.web.AdminWebService;
import cn.farwalker.waka.core.JsonResult;

import cn.farwalker.waka.core.WakaException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import cn.farwalker.ravv.service.web.webmodel.model.WebModelBo;
import org.springframework.web.bind.annotation.RequestBody;
import com.baomidou.mybatisplus.plugins.Page;
import java.util.List;
import com.cangwu.frame.web.crud.QueryFilter;

/**
 * 首页模块<br/>
 * 首页模块 controller<br/>
 * //手写的注释:以"//"开始 <br/>
 * @author generateModel.java
 */
@RestController
@RequestMapping("/web/model")
public class WebModelController{
    private final static  Logger log =LoggerFactory.getLogger(WebModelController.class);

    @Autowired
    private AdminWebService adminWebService;
    /**创建记录
     * @param vo null<br/>
     */
    @RequestMapping("/create")
    public JsonResult<?> doCreate(@RequestBody WebModelBo vo){
        try{
            if (vo == null) {
                throw new WakaException("vo不能为空");
            }
            return JsonResult.newSuccess(adminWebService.createWebModel(vo));
        }
        catch(WakaException e){
            log.error("",e);
            return JsonResult.newFail(e.getCode(),e.getMessage());
        }catch(Exception e){
            log.error("",e);
            return JsonResult.newFail(e.getMessage());
        }

    }

    /**删除记录
     * @param id null<br/>
     */
    @RequestMapping("/delete")
    public JsonResult<Boolean> doDelete(@RequestParam Long id){
        try{
            if (id == null) {
                throw new WakaException("id不能为空");
            }
            return JsonResult.newSuccess(adminWebService.deleteWebModel(id));
        }
        catch(WakaException e){
            log.error("",e);
            return JsonResult.newFail(e.getCode(),e.getMessage());
        }catch(Exception e){
            log.error("",e);
            return JsonResult.newFail(e.getMessage());
        }

    }

    /**取得单条记录
     * @param id null<br/>
     */
    @RequestMapping("/get")
    public JsonResult<WebModelBo> doGet(@RequestParam Long id){
        try{
            if (id == null) {
                throw new WakaException("vo不能为空");
            }
            return JsonResult.newSuccess(adminWebService.getOneWebModel(id));
        }
        catch(WakaException e){
            log.error("",e);
            return JsonResult.newFail(e.getCode(),e.getMessage());
        }catch(Exception e){
            log.error("",e);
            return JsonResult.newFail(e.getMessage());
        }
    }

    /**列表记录
     * @param query 查询条件<br/>
     * @param start 开始行号<br/>
     * @param size 记录数<br/>
     * @param sortfield 排序(+字段1,-字段名2)<br/>
     */
    @RequestMapping("/list")
    public JsonResult<Page<WebModelBo>> doList(@RequestBody List<QueryFilter> query,Integer start,Integer size,
                                               String sortfield){
        try{
//            if (Tools.collection.isEmpty(query) || start < 0 || size < 0 || Tools.string.isEmpty(sortfield)) {
//                throw new WakaException(RavvExceptionEnum.INVALID_PARAMETER_ERROR);
//            }
            return JsonResult.newSuccess(adminWebService.getListWebModel(query, start, size, sortfield));
        }
        catch(WakaException e){
            log.error("",e);
            return JsonResult.newFail(e.getCode(),e.getMessage());
        }catch(Exception e){
            log.error("",e);
            return JsonResult.newFail(e.getMessage());
        }

    }

    /**修改记录
     * @param vo null<br/>
     */
    @RequestMapping("/update")
    public JsonResult<?> doUpdate(@RequestBody WebModelBo vo){
        try{
            if (vo == null) {
                throw new WakaException("vo不能为空");
            }
            return JsonResult.newSuccess(adminWebService.updateWebModel(vo));
        }
        catch(WakaException e){
            log.error("",e);
            return JsonResult.newFail(e.getCode(),e.getMessage());
        }catch(Exception e){
            log.error("",e);
            return JsonResult.newFail(e.getMessage());
        }

    }
    
    /**
     * 获取所有模块列表
     * @return
     */
    @RequestMapping("/alllist")
	public JsonResult<List<WebModelBo>> getAllList() {
        try{
            return JsonResult.newSuccess(adminWebService.getAllListWebModel());
        }
        catch(WakaException e){
            log.error("",e);
            return JsonResult.newFail(e.getCode(),e.getMessage());
        }catch(Exception e){
            log.error("",e);
            return JsonResult.newFail(e.getMessage());
        }

	}
}