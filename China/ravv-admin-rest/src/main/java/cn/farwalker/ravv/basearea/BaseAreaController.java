package cn.farwalker.ravv.basearea;
import java.util.List;

import javax.annotation.Resource;

import cn.farwalker.ravv.admin.basearea.AdminBaseareaService;
import cn.farwalker.waka.core.WakaException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.plugins.Page;
import com.cangwu.frame.web.crud.QueryFilter;

import cn.farwalker.ravv.service.base.area.biz.IBaseAreaBiz;
import cn.farwalker.ravv.service.base.area.constant.CountryCodeEnum;
import cn.farwalker.ravv.service.base.area.model.BaseAreaBo;
import cn.farwalker.waka.core.JsonResult;
import cn.farwalker.waka.util.Tools;

/**
 * 地区<br/>
 * <br/>
 * //手写的注释:以"//"开始 <br/>
 * @author generateModel.java
 */	
@RestController
@RequestMapping("/basearea")
public class BaseAreaController{
    private final static  Logger log =LoggerFactory.getLogger(BaseAreaController.class);
    @Resource
    private IBaseAreaBiz baseAreaBiz;
    protected IBaseAreaBiz getBiz(){
        return baseAreaBiz;
    }

    @Autowired
    private AdminBaseareaService adminBaseareaService;
    /**创建记录
     * @param vo vo<br/>
     */
    @RequestMapping("/create")
    public JsonResult<Boolean> doCreate(@RequestBody BaseAreaBo vo){
        try{
            //createMethodSinge创建方法
            if(vo == null){
                throw new WakaException("vo不能为空");
            }
            if(vo.getPid() == null){
                throw new WakaException("父级id不能为空");
            }
            if(null == vo.getCountryCode()){
                throw new WakaException("国家编码不能为空");
            }
            if(Tools.string.isEmpty(vo.getName())){
                throw new WakaException("区域名称不能为空");
            }
            return JsonResult.newSuccess(adminBaseareaService.create(vo));
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
     * @param id 地域id<br/>
     */
    @RequestMapping("/delete")
    public JsonResult<Boolean> doDelete(@RequestParam Long id){

        try{
            //createMethodSinge创建方法
            if(id==null){
                return JsonResult.newFail("地域id不能为空");
            }
            return JsonResult.newSuccess(adminBaseareaService.delete(id));
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
     * @param id 地域id<br/>
     */
    @RequestMapping("/get")
    public JsonResult<BaseAreaBo> doGet(@RequestParam Long id){
        try{
            //createMethodSinge创建方法
            if(id == null){
                return JsonResult.newFail("地域id不能为空");
            }
            return JsonResult.newSuccess(adminBaseareaService.doGet(id));
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
    public JsonResult<Page<BaseAreaBo>> doList(@RequestBody List<QueryFilter> query,Integer start,Integer size,String sortfield){
        try{
//            if(query.size() == 0 || start < 0 || size < 0 || Tools.string.isEmpty(sortfield))
//                throw new WakaException(RavvExceptionEnum.DATA_PARSE_ERROR);
            return JsonResult.newSuccess(adminBaseareaService.getList(query, start, size, sortfield));
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
     * @param vo vo<br/>
     */
    @RequestMapping("/update")
    public JsonResult<?> doUpdate(@RequestBody BaseAreaBo vo){
        try{
            if(vo == null){
                throw new WakaException("vo不能为空");
            }
            return JsonResult.newSuccess(adminBaseareaService.update(vo));
        }
        catch(WakaException e){
            log.error("",e);
            return JsonResult.newFail(e.getCode(),e.getMessage());
        }catch(Exception e){
            log.error("",e);
            return JsonResult.newFail(e.getMessage());

        }

    }
    
    @RequestMapping("/areatree")
     public JsonResult<List<BaseAreaBo>> getAreaTree(CountryCodeEnum countryCode, Long parentid){
        try{

            if(null == countryCode) {
                throw new WakaException("请选择国家");
            }
            return JsonResult.newSuccess(adminBaseareaService.getAreaTree(countryCode, parentid));
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