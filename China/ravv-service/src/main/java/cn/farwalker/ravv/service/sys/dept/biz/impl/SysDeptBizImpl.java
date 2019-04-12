package cn.farwalker.ravv.service.sys.dept.biz.impl;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.farwalker.ravv.service.sys.dept.biz.ISysDeptBiz;
import cn.farwalker.ravv.service.sys.dept.dao.ISysDeptDao;
import cn.farwalker.ravv.service.sys.dept.model.SysDeptBo;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Transactional;

/**
 * 部门表<br/>
 * <br/>
 * //手写的注释:以"//"开始 <br/>
 * @author generateModel.java
 */
@Service
public class SysDeptBizImpl extends ServiceImpl<ISysDeptDao,SysDeptBo> implements ISysDeptBiz{

    @Resource
    private ISysDeptDao deptMapper;

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public void deleteDept(Long deptId) {	

        SysDeptBo dept = deptMapper.selectById(deptId);

        Wrapper<SysDeptBo> wrapper = new EntityWrapper<>();
        wrapper = wrapper.like("pids", "%[" + dept.getId() + "]%");
        List<SysDeptBo> subDepts = deptMapper.selectList(wrapper);
        for (SysDeptBo temp : subDepts) {
            temp.deleteById();
        }

        dept.deleteById();
    }
}