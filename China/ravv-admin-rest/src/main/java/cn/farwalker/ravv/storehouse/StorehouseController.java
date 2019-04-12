package cn.farwalker.ravv.storehouse;
import java.util.List;

import cn.farwalker.ravv.admin.storehouse.AdminStorehouseService;
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

import cn.farwalker.ravv.service.base.storehouse.model.StorehouseBo;
import cn.farwalker.ravv.service.sys.user.model.SysUserBo;
import cn.farwalker.ravv.admin.storehouse.dto.StorehouseUserVO;
import cn.farwalker.waka.core.JsonResult;

/**
 * 仓库管理
<br/>
 * <br/>
 * //手写的注释:以"//"开始 <br/>
 * @author generateModel.java
 */
@RestController
@RequestMapping("/storehouse")
public class StorehouseController{
    private final static  Logger log =LoggerFactory.getLogger(StorehouseController.class);

    @Autowired
    private AdminStorehouseService adminStorehouseService;
    
    /**修改记录
     * @param vo null<br/>
     */
    @RequestMapping("/update3")
    public JsonResult<?> doUpdate3(@RequestBody StorehouseBo vo){
        try{
            if (vo == null) {
                throw new WakaException("vo不能为空");
            }
            return JsonResult.newSuccess(adminStorehouseService.updateStorehouse3(vo));
        }
        catch(WakaException e){
            log.error("",e);
            return JsonResult.newFail(e.getCode(),e.getMessage());
        }catch(Exception e){
            log.error("",e);
            return JsonResult.newFail(e.getMessage());
        }

    }
    /**创建记录
     * @param vo vo<br/>
     */
    @RequestMapping("/create")
    public JsonResult<Boolean> doCreate(@RequestBody StorehouseBo vo, List<SysUserBo> sysUserSelection){
        try{
            if (vo == null) {
                throw new WakaException("vo不能为空");
            }
            return JsonResult.newSuccess(adminStorehouseService.create(vo, sysUserSelection));
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
     * @param id 仓库id<br/>
     */
    @RequestMapping("/delete")
    public JsonResult<Boolean> doDelete(@RequestParam Long id){
        try{
            if (id == null) {
                throw new WakaException("id不能为空");
            }

            return JsonResult.newSuccess(adminStorehouseService.delete(id));
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
     * @param id 仓库id<br/>
     */
    @RequestMapping("/get")
    public JsonResult<StorehouseBo> doGet(@RequestParam Long id){
        try{
            if (id == null) {
                throw new WakaException("id不能为空");
            }

            return JsonResult.newSuccess(adminStorehouseService.getOne(id));
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
    public JsonResult<Page<StorehouseBo>> doList(@RequestBody List<QueryFilter> query,Integer start,Integer size,
                                                 String sortfield){
        try{
//            if (Tools.collection.isEmpty(query) || start < 0 || size < 0 || Tools.string.isEmpty(sortfield)) {
//                throw new WakaException(RavvExceptionEnum.INVALID_PARAMETER_ERROR);
//            }
            return JsonResult.newSuccess(adminStorehouseService.getList(query, start, size, sortfield));
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
    public JsonResult<Boolean> doUpdate(@RequestBody StorehouseBo vo, List<SysUserBo> sysUserSelection){
        try{
            if(vo == null){
                throw new WakaException("vo不能为空");
            }

            return JsonResult.newSuccess(adminStorehouseService.update(vo, sysUserSelection));
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
     * 获取仓库关联系统用户id
     * @param id 仓库id
     * @return
     */
    @RequestMapping("/storehouse_user")
    public JsonResult<StorehouseUserVO> getStorehouseUser(@RequestParam Long id){
        try{
//            if (id == null) {
//                throw new WakaException("id不能为空");
//            }
            return JsonResult.newSuccess(adminStorehouseService.getStorehouseUser(id));
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
     * 保存仓库管理员信息
     * @param sysUserSelection 选中用户
     * @param storeId 仓库id
     * @return
     */
    @RequestMapping("/save_storehouse_user")
    public JsonResult<Boolean> saveStorehouseUser(List<SysUserBo> sysUserSelection,Long storeId){
        try{
//            if (Tools.collection.isEmpty(sysUserSelection)) {
//                throw new WakaException(RavvExceptionEnum.INVALID_PARAMETER_ERROR);
//            }
            //清除原有仓库管理员
            if(null == storeId) {
                throw new WakaException("仓库id不能为空");
            }
            return JsonResult.newSuccess(adminStorehouseService.saveStorehouseUser(sysUserSelection, storeId));
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