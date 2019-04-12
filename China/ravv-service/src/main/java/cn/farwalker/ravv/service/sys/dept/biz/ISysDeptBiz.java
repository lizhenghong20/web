package cn.farwalker.ravv.service.sys.dept.biz;
import com.baomidou.mybatisplus.service.IService;
import cn.farwalker.ravv.service.sys.dept.model.SysDeptBo;

/**
 * 部门表<br/>
 * <br/>
 * //手写的注释:以"//"开始 <br/>
 * @author generateModel.java
 */
public interface ISysDeptBiz extends IService<SysDeptBo>{
    /**
     * 删除部门
     *
     * @author Jason Chen
     * @Date 2017/7/11 22:30
     */
   void deleteDept(Long deptId);
}