package cn.farwalker.standard.system;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.junit.Test;

import cn.farwalker.ravv.service.sys.dept.dao.ISysDeptDao;
import cn.farwalker.ravv.service.sys.dept.dao.ISysDeptMgrDao;
import cn.farwalker.ravv.service.sys.dept.model.SysDeptBo;
import cn.farwalker.standard.base.BaseJunit;

/**
 * 字典服务测试
 *
 * @author Jason Chen
 * @date 2017-04-27 17:05
 */
public class DeptTest extends BaseJunit {

    @Resource
    ISysDeptMgrDao deptDao;

    @Resource
    ISysDeptDao deptMapper;

    @Test
    public void addDeptTest() {
        SysDeptBo dept = new SysDeptBo();
        dept.setFullname("测试fullname");
        dept.setNum(5);
        dept.setPid(1L);
        dept.setSimplename("测试");
        dept.setTips("测试tips");
        Integer insert = deptMapper.insert(dept);
        assertEquals(insert, new Integer(1));
    }

    @Test
    public void updateTest() {
    	SysDeptBo dept = this.deptMapper.selectById(24);
        dept.setTips("哈哈");
        boolean flag = dept.updateById();
        assertTrue(flag);
    }

    @Test
    public void deleteTest() {
    	SysDeptBo dept = this.deptMapper.selectById(24);
        Integer integer = deptMapper.deleteById(dept);
        assertTrue(integer > 0);
    }

    @Test
    public void listTest() {
        List<Map<String, Object>> list = this.deptDao.list("总公司");
        assertTrue(list.size() > 0);
    }
}
