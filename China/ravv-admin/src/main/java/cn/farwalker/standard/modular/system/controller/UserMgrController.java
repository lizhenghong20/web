//package cn.farwalker.standard.modular.system.controller;
//
//import java.io.File;
//import java.util.List;
//import java.util.Map;
//import java.util.UUID;
//
//import javax.annotation.Resource;
//import javax.naming.NoPermissionException;
//import javax.validation.Valid;
//
//import cn.farwalker.waka.core.BizExceptionEnum;
//import cn.farwalker.waka.core.SuccessTip;
//import cn.farwalker.waka.core.WakaException;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.validation.BindingResult;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RequestPart;
//import org.springframework.web.bind.annotation.ResponseBody;
//import org.springframework.web.multipart.MultipartFile;
//
//import cn.farwalker.ravv.service.sys.roleuser.biz.ISysRoleUserBiz;
//import cn.farwalker.ravv.service.sys.user.dao.ISysUserDao;
//import cn.farwalker.ravv.service.sys.user.dao.ISysUserMgrDao;
//import cn.farwalker.ravv.service.sys.user.model.SysUserBo;
//import cn.farwalker.standard.common.constant.dictmap.UserDict;
//import cn.farwalker.standard.common.constant.factory.ConstantFactory;
//import cn.farwalker.standard.core.shiro.ShiroKit;
//import cn.farwalker.standard.core.shiro.ShiroUser;
//import cn.farwalker.standard.modular.system.factory.UserFactory;
//import cn.farwalker.standard.modular.system.transfer.UserDto;
//import cn.farwalker.standard.modular.system.warpper.UserWarpper;
//import cn.farwalker.waka.common.annotion.BussinessLog;
//import cn.farwalker.waka.common.annotion.Permission;
//import cn.farwalker.waka.common.constant.Const;
//import cn.farwalker.waka.common.constant.state.ManagerStatus;
//import cn.farwalker.waka.config.properties.WakaProperties;
//
//import cn.farwalker.waka.core.db.Db;
//
//import cn.farwalker.waka.core.log.LogObjectHolder;
//
//
///**
// * 系统管理员控制器
// *
// * @author Jason Chen
// * @Date 2017年1月11日 下午1:08:17
// */
//@Controller
//@RequestMapping("/mgr")
//public class UserMgrController {
//
//    private static String PREFIX = "/system/user/";
//
//    @Resource
//    private WakaProperties wakaProperties;
//
//    @Resource
//    private ISysUserMgrDao managerDao;
//
//    @Resource
//    private ISysUserDao userMapper;
//
//    @Resource
//    private ISysRoleUserBiz sysRoleUserBiz;
//
//    /**
//     * 跳转到查看管理员列表的页面
//     */
//    @RequestMapping("")
//    public String index() {
//        return PREFIX + "user.html";
//    }
//
//    /**
//     * 跳转到查看管理员列表的页面
//     */
//    @RequestMapping("/user_add")
//    public String addView() {
//        return PREFIX + "user_add.html";
//    }
//
//    /**
//     * 跳转到角色分配页面
//     */
//    //@RequiresPermissions("/mgr/role_assign")  //利用shiro自带的权限检查
//    @Permission
//    @RequestMapping("/role_assign/{userId}")
//    public String roleAssign(@PathVariable Long userId, Model model) {
//        if (userId == null) {
//            throw new WakaException(BizExceptionEnum.REQUEST_NULL);
//        }
//        SysUserBo user = (SysUserBo) Db.create(ISysUserDao.class).selectOneByCon("id", userId);
//        model.addAttribute("userId", userId);
//        model.addAttribute("userAccount", user.getAccount());
//        return PREFIX + "user_roleassign.html";
//    }
//
//    /**
//     * 跳转到编辑管理员页面
//     */
//    @Permission
//    @RequestMapping("/user_edit/{userId}")
//    public String userEdit(@PathVariable Long userId, Model model) {
//        if (userId == null) {
//            throw new WakaException(BizExceptionEnum.REQUEST_NULL);
//        }
//        assertAuth(userId);
//        SysUserBo user = this.userMapper.selectById(userId);
//        model.addAttribute("user",user);
//        model.addAttribute("roleName", ConstantFactory.me().getRoleName(user.getRoleid()));
//        model.addAttribute("deptName", ConstantFactory.me().getDeptName(user.getDeptid()));
//        LogObjectHolder.me().set(user);
//        return PREFIX + "user_edit.html";
//    }
//
//    /**
//     * 跳转到查看用户详情页面
//     */
//    @RequestMapping("/user_info")
//    public String userInfo(Model model) {
//        Long userId = ShiroKit.getUser().getId();
//        if (userId == null) {
//            throw new WakaException(BizExceptionEnum.REQUEST_NULL);
//        }
//        SysUserBo user = this.userMapper.selectById(userId);
//        model.addAttribute(user);
//        model.addAttribute("roleName", ConstantFactory.me().getRoleName(user.getRoleid()));
//        model.addAttribute("deptName", ConstantFactory.me().getDeptName(user.getDeptid()));
//        LogObjectHolder.me().set(user);
//        return PREFIX + "user_view.html";
//    }
//
//    /**
//     * 跳转到修改密码界面
//     */
//    @RequestMapping("/user_chpwd")
//    public String chPwd() {
//        return PREFIX + "user_chpwd.html";
//    }
//
//    /**
//     * 修改当前用户的密码
//     */
//    @RequestMapping("/changePwd")
//    @ResponseBody
//    public Object changePwd(@RequestParam String oldPwd, @RequestParam String newPwd, @RequestParam String rePwd) {
//        if (!newPwd.equals(rePwd)) {
//            throw new WakaException(BizExceptionEnum.TWO_PWD_NOT_MATCH);
//        }
//        Long userId = ShiroKit.getUser().getId();
//        SysUserBo user = userMapper.selectById(userId);
//        String oldMd5 = ShiroKit.md5(oldPwd, user.getSalt());
//        if (user.getPassword().equals(oldMd5)) {
//            String newMd5 = ShiroKit.md5(newPwd, user.getSalt());
//            user.setPassword(newMd5);
//            user.updateById();
//            return new SuccessTip();
//        } else {
//            throw new WakaException(BizExceptionEnum.OLD_PWD_NOT_RIGHT);
//        }
//    }
//
//    /**
//     * 查询管理员列表
//     */
//    @RequestMapping("/list")
//    @Permission
//    @ResponseBody
//    public Object list(String name, String beginTime,  String endTime, Integer deptid) {
//        if (ShiroKit.isAdmin()) {
//            List<Map<String, Object>> users = managerDao.selectUsers(null, name, beginTime, endTime, deptid);
//            return new UserWarpper(users).warp();
//        } else {
//            DataScope dataScope = new DataScope(ShiroKit.getDeptDataScope());
//            List<Map<String, Object>> users = managerDao.selectUsers(dataScope, name, beginTime, endTime, deptid);
//            return new UserWarpper(users).warp();
//        }
//    }
//
//    /**
//     * 添加管理员
//     */
//    @RequestMapping("/add")
//    @BussinessLog(value = "添加管理员", key = "account", dict = UserDict.class)
//    @Permission(Const.ADMIN_NAME)
//    @ResponseBody
//    public Tip add(@Valid UserDto user, BindingResult result) {
//        if (result.hasErrors()) {
//            throw new WakaException(BizExceptionEnum.REQUEST_NULL);
//        }
//
//        // 判断账号是否重复
//        SysUserBo theUser = managerDao.getByAccount(user.getAccount());
//        if (theUser != null) {
//            throw new WakaException(BizExceptionEnum.USER_ALREADY_REG);
//        }
//
//        // 完善账号信息
//        user.setSalt(ShiroKit.getRandomSalt(5));
//        user.setPassword(ShiroKit.md5(user.getPassword(), user.getSalt()));
//        user.setStatus(ManagerStatus.OK.getCode());
//
//        this.userMapper.insert(UserFactory.createUser(user));
//        return SUCCESS_TIP;
//    }
//
//    /**
//     * 修改管理员
//     *
//     * @throws NoPermissionException
//     */
//    @RequestMapping("/edit")
//    @BussinessLog(value = "修改管理员", key = "account", dict = UserDict.class)
//    @ResponseBody
//    public Tip edit(@Valid UserDto user, BindingResult result) throws NoPermissionException {
//        if (result.hasErrors()) {
//            throw new WakaException(BizExceptionEnum.REQUEST_NULL);
//        }
//        if (ShiroKit.hasRole(Const.ADMIN_NAME)) {
//            this.userMapper.updateById(UserFactory.createUser(user));
//            return SUCCESS_TIP;
//        } else {
//            assertAuth(user.getId());
//            ShiroUser shiroUser = ShiroKit.getUser();
//            if (shiroUser.getId().equals(user.getId())) {
//                this.userMapper.updateById(UserFactory.createUser(user));
//                return SUCCESS_TIP;
//            } else {
//                throw new WakaException(BizExceptionEnum.NO_PERMITION);
//            }
//        }
//    }
//
//    /**
//     * 删除管理员（逻辑删除）
//     */
//    @RequestMapping("/delete")
//    @BussinessLog(value = "删除管理员", key = "userId", dict = UserDict.class)
//    @Permission
//    @ResponseBody
//    public Tip delete(@RequestParam Long userId) {
//        if (ToolUtil.isEmpty(userId)) {
//            throw new WakaException(BizExceptionEnum.REQUEST_NULL);
//        }
//        //不能删除超级管理员
//        if (userId.equals(Const.ADMIN_ID)) {
//            throw new WakaException(BizExceptionEnum.CANT_DELETE_ADMIN);
//        }
//        assertAuth(userId);
//        this.managerDao.setStatus(userId, ManagerStatus.DELETED.getCode());
//        return SUCCESS_TIP;
//    }
//
//    /**
//     * 查看管理员详情
//     */
//    @RequestMapping("/view/{userId}")
//    @ResponseBody
//    public SysUserBo view(@PathVariable Long userId) {
//        if (ToolUtil.isEmpty(userId)) {
//            throw new WakaException(BizExceptionEnum.REQUEST_NULL);
//        }
//        assertAuth(userId);
//        return this.userMapper.selectById(userId);
//    }
//
//    /**
//     * 重置管理员的密码
//     */
//    @RequestMapping("/reset")
//    @BussinessLog(value = "重置管理员密码", key = "userId", dict = UserDict.class)
//    @Permission(Const.ADMIN_NAME)
//    @ResponseBody
//    public Tip reset(@RequestParam Long userId) {
//        if (ToolUtil.isEmpty(userId)) {
//            throw new WakaException(BizExceptionEnum.REQUEST_NULL);
//        }
//        assertAuth(userId);
//        SysUserBo user = this.userMapper.selectById(userId);
//        user.setSalt(ShiroKit.getRandomSalt(5));
//        user.setPassword(ShiroKit.md5(Const.DEFAULT_PWD, user.getSalt()));
//        this.userMapper.updateById(user);
//        return SUCCESS_TIP;
//    }
//
//    /**
//     * 冻结用户
//     */
//    @RequestMapping("/freeze")
//    @BussinessLog(value = "冻结用户", key = "userId", dict = UserDict.class)
//    @Permission(Const.ADMIN_NAME)
//    @ResponseBody
//    public Tip freeze(@RequestParam Long userId) {
//        if (ToolUtil.isEmpty(userId)) {
//            throw new WakaException(BizExceptionEnum.REQUEST_NULL);
//        }
//        //不能冻结超级管理员
//        if (userId.equals(Const.ADMIN_ID)) {
//            throw new WakaException(BizExceptionEnum.CANT_FREEZE_ADMIN);
//        }
//        assertAuth(userId);
//        this.managerDao.setStatus(userId, ManagerStatus.FREEZED.getCode());
//        return SUCCESS_TIP;
//    }
//
//    /**
//     * 解除冻结用户
//     */
//    @RequestMapping("/unfreeze")
//    @BussinessLog(value = "解除冻结用户", key = "userId", dict = UserDict.class)
//    @Permission(Const.ADMIN_NAME)
//    @ResponseBody
//    public Tip unfreeze(@RequestParam Long userId) {
//        if (ToolUtil.isEmpty(userId)) {
//            throw new WakaException(BizExceptionEnum.REQUEST_NULL);
//        }
//        assertAuth(userId);
//        this.managerDao.setStatus(userId, ManagerStatus.OK.getCode());
//        return SUCCESS_TIP;
//    }
//
//    /**
//     * 分配角色
//     */
//    @RequestMapping("/setRole")
//    @BussinessLog(value = "分配角色", key = "userId,roleIds", dict = UserDict.class)
//    @Permission(Const.ADMIN_NAME)
//    @ResponseBody
//    public Tip setRole(@RequestParam("userId") Long userId, @RequestParam("roleIds") String roleIds) {
//        if (ToolUtil.isOneEmpty(userId, roleIds)) {
//            throw new WakaException(BizExceptionEnum.REQUEST_NULL);
//        }
//        //不能修改超级管理员
//        if (userId.equals(Const.ADMIN_ID)) {
//            throw new WakaException(BizExceptionEnum.CANT_CHANGE_ADMIN);
//        }
//        assertAuth(userId);
//        this.managerDao.setRoles(userId, roleIds);
//
//        //将角色id存入角色和用户关联表
//        sysRoleUserBiz.saveRoleIds(userId, roleIds);
//
//        return SUCCESS_TIP;
//    }
//
//    /**
//     * 上传图片(上传到项目的webapp/static/img)
//     */
//    @RequestMapping(method = RequestMethod.POST, path = "/upload")
//    @ResponseBody
//    public String upload(@RequestPart("file") MultipartFile picture) {
//        String pictureName = UUID.randomUUID().toString() + ".jpg";
//        try {
//            String fileSavePath = wakaProperties.getFileUploadPath();
//            picture.transferTo(new File(fileSavePath + pictureName));
//        } catch (Exception e) {
//            throw new WakaException(BizExceptionEnum.UPLOAD_ERROR);
//        }
//        return pictureName;
//    }
//
//    /**
//     * 判断当前登录的用户是否有操作这个用户的权限
//     */
//    private void assertAuth(Long userId) {
//        if (ShiroKit.isAdmin()) {
//            return;
//        }
//        List<Long> deptDataScope = ShiroKit.getDeptDataScope();
//        SysUserBo user = this.userMapper.selectById(userId);
//        Long deptid = user.getDeptid();
//        if (deptDataScope.contains(deptid)) {
//            return;
//        } else {
//            throw new WakaException(BizExceptionEnum.NO_PERMITION);
//        }
//
//    }
//}
