package cn.farwalker.ravv.basebrand;
import cn.farwalker.ravv.admin.basebrand.AdminBaseBrandService;
import cn.farwalker.waka.core.JsonResult;

import javax.annotation.Resource;

import cn.farwalker.waka.core.WakaException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import cn.farwalker.ravv.service.base.brand.biz.IBaseBrandBiz;

import cn.farwalker.ravv.service.base.brand.model.BaseBrandBo;
import cn.farwalker.ravv.service.goods.base.biz.IGoodsService;

import org.springframework.web.bind.annotation.RequestBody;

import com.baomidou.mybatisplus.plugins.Page;

import java.util.List;

import com.cangwu.frame.web.crud.QueryFilter;

/**
 * 品牌<br/>
 * <br/>
 * //手写的注释:以"//"开始 <br/>
 * @author generateModel.java
 */
@RestController
@RequestMapping("/basebrand")
public class BaseBrandController{
    private final static  Logger log =LoggerFactory.getLogger(BaseBrandController.class);
    @Resource
    private IBaseBrandBiz baseBrandBiz;
    protected IBaseBrandBiz getBiz(){
        return baseBrandBiz;
    }
    
    @Resource 
    private IGoodsService goodsService;

    @Autowired
    private AdminBaseBrandService adminBaseBrandService;
    
    /**创建记录
     * @param vo vo<br/>
     */
    @RequestMapping("/create")
    public JsonResult<Boolean> doCreate(@RequestBody BaseBrandBo vo){
        try{
            //createMethodSinge创建方法
            if(vo == null){
                throw new WakaException("vo不能为空");
            }

            return JsonResult.newSuccess(adminBaseBrandService.create(vo));
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
     * @param id 品牌id<br/>
     */
    @RequestMapping("/delete")
    public JsonResult<Boolean> doDelete(@RequestParam Long id){
        try{
            //createMethodSinge创建方法
            if(id == null){
                return JsonResult.newFail("品牌id不能为空");
            }
            return JsonResult.newSuccess(adminBaseBrandService.delete(id));
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
     * @param id 品牌id<br/>
     */
    @RequestMapping("/get")
    public JsonResult<BaseBrandBo> doGet(@RequestParam Long id){
        try{
            if(id == null){
                return JsonResult.newFail("品牌id不能为空");
            }

            return JsonResult.newSuccess(adminBaseBrandService.getOne(id));
        }
        catch(WakaException e){
            log.error("",e);
            return JsonResult.newFail(e.getCode(),e.getMessage());
        }catch(Exception e){
            log.error("",e);
            return JsonResult.newFail(e.getMessage());

        }
        //createMethodSinge创建方法

    }
    /**列表记录
     * @param query 查询条件<br/>
     * @param start 开始行号<br/>
     * @param size 记录数<br/>
     * @param sortfield 排序(+字段1,-字段名2)<br/>
     */
    @RequestMapping("/list")
    public JsonResult<Page<BaseBrandBo>> doList(@RequestBody List<QueryFilter> query,Integer start,Integer size,String sortfield){
        try{
//            if(Tools.collection.isEmpty(query) || start < 0 || size < 0 || Tools.string.isEmpty(sortfield)){
//                throw new WakaException("品牌id不能为空");
//            }
            return JsonResult.newSuccess(adminBaseBrandService.getList(query, start, size, sortfield));
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
    public JsonResult<Boolean> doUpdate(@RequestBody BaseBrandBo vo){
        try{
            //createMethodSinge创建方法
            if(vo == null){
                return JsonResult.newFail("vo不能为空");
            }

            return JsonResult.newSuccess(adminBaseBrandService.update(vo));
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