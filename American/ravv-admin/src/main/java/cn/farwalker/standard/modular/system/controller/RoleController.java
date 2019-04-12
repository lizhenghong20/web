package cn.farwalker.standard.modular.system.controller;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.validation.Valid;

import cn.farwalker.standard.core.temp.BussinessLog;
import cn.farwalker.standard.core.temp.Const;
import cn.farwalker.standard.core.temp.LogObjectHolder;
import cn.farwalker.standard.core.temp.Permission;
import cn.farwalker.waka.core.*;
import cn.farwalker.waka.util.Tools;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.farwalker.ravv.service.sys.role.biz.ISysRoleBiz;
import cn.farwalker.ravv.service.sys.role.dao.ISysRoleDao;
import cn.farwalker.ravv.service.sys.role.dao.ISysRoleMgrDao;
import cn.farwalker.ravv.service.sys.role.model.SysRoleBo;
import cn.farwalker.ravv.service.sys.user.dao.ISysUserDao;
import cn.farwalker.ravv.service.sys.user.model.SysUserBo;
import cn.farwalker.standard.common.constant.dictmap.RoleDict;
import cn.farwalker.standard.common.constant.factory.ConstantFactory;
import cn.farwalker.standard.modular.system.warpper.RoleWarpper;

/**
 * 角色控制器
 *
 * @author Jason Chen
 * @Date 2017年2月12日21:59:14
 */
@Controller
@RequestMapping("/role")
public class RoleController {

    private static String PREFIX = "/system/role";

    @Resource
    private ISysUserDao userMapper;

    @Resource
    private ISysRoleDao roleMapper;

    @Resource
    ISysRoleMgrDao roleDao;

    @Resource
    ISysRoleBiz roleService;

    /**
     * 跳转到角色列表页面
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "/role.html";
    }

    /**
     * 跳转到添加角色
     */
    @RequestMapping(value = "/role_add")
    public String roleAdd() {
        return PREFIX + "/role_add.html";
    }

    /**
     * 跳转到修改角色
     */
    @Permission
    @RequestMapping(value = "/role_edit/{roleId}")
    public String roleEdit(@PathVariable Long roleId, Model model) {
        if (roleId == null) {
            throw new WakaException(BizExceptionEnum.REQUEST_NULL);
        }
        SysRoleBo role = this.roleMapper.selectById(roleId);
        model.addAttribute("role",role);
        model.addAttribute("pName", ConstantFactory.me().getSingleRoleName(role.getPid()));
        model.addAttribute("deptName", ConstantFactory.me().getDeptName(role.getDeptid()));
        LogObjectHolder.me().set(role);
        return PREFIX + "/role_edit.html";
    }

    /**
     * 跳转到角色分配
     */
    @Permission
    @RequestMapping(value = "/role_assign/{roleId}")
    public String roleAssign(@PathVariable("roleId") Long roleId, Model model) {
        if (roleId == null) {
            throw new WakaException(BizExceptionEnum.REQUEST_NULL);
        }
        model.addAttribute("roleId", roleId);
        model.addAttribute("roleName", ConstantFactory.me().getSingleRoleName(roleId));
        return PREFIX + "/role_assign.html";
    }

    /**
     * 获取角色列表
     */
    @Permission
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(@RequestParam(required = false) String roleName) {
        List<Map<String, Object>> roles = this.roleDao.selectRoles(HttpKit.getRequest().getParameter("roleName"));
        return new RoleWarpper(roles).warp();
    }

    /**
     * 角色新增
     */
    @RequestMapping(value = "/add")
    @BussinessLog(value = "添加角色", key = "name", dict = RoleDict.class)
    @Permission(Const.ADMIN_NAME)
    @ResponseBody
    public Tip add(@Valid SysRoleBo role, BindingResult result) {
        if (result.hasErrors()) {
            throw new WakaException(BizExceptionEnum.REQUEST_NULL);
        }
        role.setId(null);
        this.roleMapper.insert(role);
        return new SuccessTip();
    }

    /**
     * 角色修改
     */
    @RequestMapping(value = "/edit")
    @BussinessLog(value = "修改角色", key = "name", dict = RoleDict.class)
    @Permission(Const.ADMIN_NAME)
    @ResponseBody
    public Tip edit(@Valid SysRoleBo role, BindingResult result) {
        if (result.hasErrors()) {
            throw new WakaException(BizExceptionEnum.REQUEST_NULL);
        }
        this.roleMapper.updateById(role);

        //删除缓存
        return new SuccessTip();
    }

    /**
     * 删除角色
     */
    @RequestMapping(value = "/remove")
    @BussinessLog(value = "删除角色", key = "roleId", dict = RoleDict.class)
    @Permission(Const.ADMIN_NAME)
    @ResponseBody
    public Tip remove(@RequestParam Long roleId) {
        if (roleId == null) {
            throw new WakaException(BizExceptionEnum.REQUEST_NULL);
        }

        //不能删除超级管理员角色
        if(roleId.equals(Const.ADMIN_ROLE_ID)){
            throw new WakaException(BizExceptionEnum.CANT_DELETE_ADMIN);
        }

        //缓存被删除的角色名称
        LogObjectHolder.me().set(ConstantFactory.me().getSingleRoleName(roleId));

        this.roleService.deleteRoleById(roleId);

        //删除缓存
        return new SuccessTip();
    }

    /**
     * 查看角色
     */
    @RequestMapping(value = "/view/{roleId}")
    @ResponseBody
    public Tip view(@PathVariable Integer roleId) {
        if (roleId == null) {
            throw new WakaException(BizExceptionEnum.REQUEST_NULL);
        }
        this.roleMapper.selectById(roleId);
        return new SuccessTip();
    }

    /**
     * 配置权限
     */
    @RequestMapping("/setAuthority")
    @BussinessLog(value = "配置权限", key = "roleId,ids", dict = RoleDict.class)
    @Permission(Const.ADMIN_NAME)
    @ResponseBody
    public Tip setAuthority(@RequestParam("roleId") Long roleId, @RequestParam("ids") String ids) {
        if (roleId == null) {
            throw new WakaException(BizExceptionEnum.REQUEST_NULL);
        }
        this.roleService.saveAuthority(roleId, ids);
        return new SuccessTip();
    }

    /**
     * 获取角色列表
     */
    @RequestMapping(value = "/roleTreeList")
    @ResponseBody
    public List<ZTreeNode> roleTreeList() {
        List<ZTreeNode> roleTreeList = this.roleDao.roleTreeList();
        roleTreeList.add(ZTreeNode.createParent());
        return roleTreeList;
    }

    /**
     * 获取角色列表
     */
    @RequestMapping(value = "/roleTreeListByUserId/{userId}")
    @ResponseBody
    public List<ZTreeNode> roleTreeListByUserId(@PathVariable Long userId) {
        SysUserBo theUser = this.userMapper.selectById(userId);
        String roleid = theUser.getRoleid();
        if (roleid == null) {
            List<ZTreeNode> roleTreeList = this.roleDao.roleTreeList();
            return roleTreeList;
        } else {
            String[] strArray = (String[])Tools.string.convertStringList(roleid).toArray();
            List<ZTreeNode> roleTreeListByUserId = this.roleDao.roleTreeListByRoleId(strArray);
            return roleTreeListByUserId;
        }
    }

}
