package cn.farwalker.standard.modular.system.controller;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import cn.farwalker.standard.core.temp.BussinessLog;
import cn.farwalker.standard.core.temp.LogObjectHolder;
import cn.farwalker.standard.core.temp.Permission;
import cn.farwalker.waka.core.BizExceptionEnum;
import cn.farwalker.waka.core.SuccessTip;
import cn.farwalker.waka.core.WakaException;
import cn.farwalker.waka.core.ZTreeNode;
import cn.farwalker.waka.util.Tools;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.farwalker.ravv.service.sys.dept.biz.ISysDeptBiz;
import cn.farwalker.ravv.service.sys.dept.dao.ISysDeptDao;
import cn.farwalker.ravv.service.sys.dept.dao.ISysDeptMgrDao;
import cn.farwalker.ravv.service.sys.dept.model.SysDeptBo;
import cn.farwalker.standard.common.constant.dictmap.DeptDict;
import cn.farwalker.standard.common.constant.factory.ConstantFactory;
import cn.farwalker.standard.modular.system.warpper.DeptWarpper;
/**
 * 部门控制器
 *
 * @author Jason Chen
 * @Date 2017年2月17日20:27:22
 */
@Controller
@RequestMapping("/dept")
public class DeptController  {

    private String PREFIX = "/system/dept/";

    @Resource
    ISysDeptMgrDao deptDao;

    @Resource
    ISysDeptDao deptMapper;

    @Resource
    ISysDeptBiz deptService;

    /**
     * 跳转到部门管理首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "dept.html";
    }

    /**
     * 跳转到添加部门
     */
    @RequestMapping("/dept_add")
    public String deptAdd() {
        return PREFIX + "dept_add.html";
    }

    /**
     * 跳转到修改部门
     */
    @Permission
    @RequestMapping("/dept_update/{deptId}")
    public String deptUpdate(@PathVariable Long deptId, Model model) {
        SysDeptBo dept = deptMapper.selectById(deptId);
        model.addAttribute("dept",dept);
        model.addAttribute("pName", ConstantFactory.me().getDeptName(dept.getPid()));
        LogObjectHolder.me().set(dept);
        return PREFIX + "dept_edit.html";
    }

    /**
     * 获取部门的tree列表
     */
    @RequestMapping(value = "/tree")
    @ResponseBody
    public List<ZTreeNode> tree() {
        List<ZTreeNode> tree = this.deptDao.tree();
        tree.add(ZTreeNode.createParent());
        return tree;
    }

    /**
     * 新增部门
     */
    @BussinessLog(value = "添加部门", key = "simplename", dict = DeptDict.class)
    @RequestMapping(value = "/add")
    @Permission
    @ResponseBody
    public Object add(SysDeptBo dept) {
        if (dept == null || Tools.string.isEmpty(dept.getSimplename())) {
            throw new WakaException(BizExceptionEnum.REQUEST_NULL);
        }
        //完善pids,根据pid拿到pid的pids
        deptSetPids(dept);
        return this.deptMapper.insert(dept);
    }

    /**
     * 获取所有部门列表
     */
    @RequestMapping(value = "/list")
    @Permission
    @ResponseBody
    public Object list(String condition) {
        List<Map<String, Object>> list = this.deptDao.list(condition);
        return new DeptWarpper(list).warp();
    }

    /**
     * 部门详情
     */
    @RequestMapping(value = "/detail/{deptId}")
    @Permission
    @ResponseBody
    public Object detail(@PathVariable("deptId") Long deptId) {
        return deptMapper.selectById(deptId);
    }

    /**
     * 修改部门
     */
    @BussinessLog(value = "修改部门", key = "simplename", dict = DeptDict.class)
    @RequestMapping(value = "/update")
    @Permission
    @ResponseBody
    public Object update(SysDeptBo dept) {
        if (dept == null || dept.getId() == null) {
            throw new WakaException(BizExceptionEnum.REQUEST_NULL);
        }
        deptSetPids(dept);
        deptMapper.updateById(dept);
        return new SuccessTip();
    }

    /**
     * 删除部门
     */
    @BussinessLog(value = "删除部门", key = "deptId", dict = DeptDict.class)
    @RequestMapping(value = "/delete")
    @Permission
    @ResponseBody
    public Object delete(@RequestParam Long deptId) {

        //缓存被删除的部门名称
        LogObjectHolder.me().set(ConstantFactory.me().getDeptName(deptId));

        deptService.deleteDept(deptId);

        return new SuccessTip();
    }

    private void deptSetPids(SysDeptBo dept) {
        if (dept.getPid() == null || dept.getPid().equals(0L)) {
            dept.setPid(0L);
            dept.setPids("[0],");
        } else {
        	Long pid = dept.getPid();
        	SysDeptBo temp = deptMapper.selectById(pid);
            String pids = temp.getPids();
            dept.setPid(pid);
            dept.setPids(pids + "[" + pid + "],");
        }
    }
}
