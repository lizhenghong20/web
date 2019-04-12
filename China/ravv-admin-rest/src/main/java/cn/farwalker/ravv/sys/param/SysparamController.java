package cn.farwalker.ravv.sys.param;
import cn.farwalker.ravv.admin.sys.AdminSysService;
import cn.farwalker.waka.core.JsonResult;
import javax.annotation.Resource;

import cn.farwalker.waka.core.WakaException;
import cn.farwalker.waka.util.Tools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import cn.farwalker.ravv.service.sys.param.biz.ISysParamBiz;
import cn.farwalker.ravv.service.sys.param.model.SysParamBo;
import org.springframework.web.bind.annotation.RequestBody;
import com.baomidou.mybatisplus.plugins.Page;
import java.util.List;


import com.cangwu.frame.web.crud.QueryFilter;

/**
 * 系统参数<br/>
 * <br/>
 * //手写的注释:以"//"开始 <br/>
 * @author generateModel.java
 */
@RestController
@RequestMapping("/sys/param")
public class SysparamController{
    private final static  Logger log =LoggerFactory.getLogger(SysparamController.class);
    @Resource
    private ISysParamBiz sysParamBiz;
    protected ISysParamBiz getSysParamBiz(){
        return sysParamBiz;
    }

    @Autowired
    private AdminSysService adminSysService;
    /**创建记录
     * @param vo null<br/>
     */
    @RequestMapping("/create")
    public JsonResult<Boolean> doCreate(@RequestBody SysParamBo vo){
        try{
            if (vo == null) {
                throw new WakaException("vo不能为空");
            }
            if(Tools.string.isEmpty(vo.getCode())){
                throw new WakaException("参数编码不能为空");
            }
            return JsonResult.newSuccess(adminSysService.createSysparam(vo));
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
            return JsonResult.newSuccess(adminSysService.deleteSysparam(id));
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
    public JsonResult<SysParamBo> doGet(@RequestParam Long id){
        try{
            if (id == null) {
                throw new WakaException("vo不能为空");
            }
            return JsonResult.newSuccess(adminSysService.getOneSysparam(id));
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
    public JsonResult<Page<SysParamBo>> doList(@RequestBody List<QueryFilter> query,Integer start,Integer size,String sortfield){
        try{
//            if (Tools.collection.isEmpty(query) || start < 0 || size < 0 || Tools.string.isEmpty(sortfield)) {
//                throw new WakaException("id不能为空");
//            }
            return JsonResult.newSuccess(adminSysService.getListSysparam(query, start, size, sortfield));
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
    public JsonResult<Boolean> doUpdate(@RequestBody SysParamBo vo){
        try{
            if (vo == null) {
                throw new WakaException("vo不能为空");
            }
            if(Tools.string.isEmpty(vo.getCode())){
                throw new WakaException("参数编码不能为空");
            }
            if(Tools.string.isEmpty(vo.getPvalue())){
                throw new WakaException("参数值不能为空");
            }
            return JsonResult.newSuccess(adminSysService.updateSysparam(vo));
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
     * 清除缓存
     */
    @RequestMapping("/cache_refresh")
    public JsonResult<Boolean> cacheRefresh(){
        try{
            return JsonResult.newSuccess(adminSysService.cacheRefresh());
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