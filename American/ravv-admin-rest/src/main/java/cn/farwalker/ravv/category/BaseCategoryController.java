//package cn.farwalker.ravv.category;
//
//import java.lang.reflect.InvocationTargetException;
//import java.util.ArrayList;
//import java.util.List;
//
//import org.apache.commons.beanutils.BeanUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//import cn.farwalker.ravv.service.category.basecategory.biz.IBaseCategoryBiz;
//import cn.farwalker.ravv.service.category.basecategory.model.BaseCategoryBo;
//import cn.farwalker.waka.core.base.controller.BaseController;
//
//import com.baomidou.mybatisplus.mapper.EntityWrapper;
///**
// *
// * @author Administrator
// * @deprecated 由CategoryController代替
// */
//@RestController
//@RequestMapping("/category2/")
//public class BaseCategoryController extends BaseController{
//
//	 @Autowired
//	 private IBaseCategoryBiz baseCategoryService;
//
//	/**
//     * 获取类目基础信息列表
//	 * @throws InvocationTargetException
//	 * @throws IllegalAccessException
//     */
//    @RequestMapping(value = "/leaf/list")
//    public ResponseEntity<List<BaseCategoryBo>> list(Long memberId) throws IllegalAccessException, InvocationTargetException {
//    	this.checkId(memberId);
//    	EntityWrapper<BaseCategoryBo> ew = new EntityWrapper<>();
//    	ew.eq(BaseCategoryBo.Key.leaf.toString(), 1);
//        List<BaseCategoryBo> cateList = baseCategoryService.selectList(ew);
//
//    	return ResponseEntity.ok(cateList);
//    }
//
//    /**
//     * 新增类目基础信息
//     */
//    @RequestMapping(value = "/add")
//    public Object add(BaseCategoryBo baseCategory) {
//        baseCategoryService.insert(baseCategory);
//        return super.SUCCESS_TIP;
//    }
//
//    /**
//     * 删除类目基础信息
//     */
//    @RequestMapping(value = "/delete")
//    public Object delete(@RequestParam Integer baseCategoryId) {
//        baseCategoryService.deleteById(baseCategoryId);
//        return SUCCESS_TIP;
//    }
//
//    /**
//     * 修改类目基础信息
//     */
//    @RequestMapping(value = "/update")
//    public Object update(BaseCategoryBo baseCategory) {
//        baseCategoryService.updateById(baseCategory);
//        return super.SUCCESS_TIP;
//    }
//
//    /**
//     * 类目基础信息详情
//     */
//    @RequestMapping(value = "/detail/{baseCategoryId}")
//    public Object detail(@PathVariable("baseCategoryId") Integer baseCategoryId) {
//        return baseCategoryService.selectById(baseCategoryId);
//    }
//}
