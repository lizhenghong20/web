package cn.farwalker.standard.modular.system.controller;


import java.util.List;

import cn.farwalker.waka.core.HttpKit;
import cn.farwalker.waka.core.MenuNode;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.farwalker.ravv.service.sys.menu.dao.ISysMenuMgrDao;
import cn.farwalker.ravv.service.sys.user.dao.ISysUserDao;
import cn.farwalker.ravv.service.sys.user.model.SysUserBo;
import cn.farwalker.standard.core.shiro.ShiroKit;
import cn.farwalker.standard.core.shiro.ShiroUser;
import cn.farwalker.standard.core.util.ApiMenuFilter;
import cn.farwalker.standard.core.util.KaptchaUtil;
import cn.farwalker.waka.util.Tools;

import com.google.code.kaptcha.Constants;

/**
 * 登录控制器
 *
 * @author Jason Chen
 * @Date 2017年1月10日 下午8:25:24
 */
@Controller
public class LoginController{

    @Autowired
    ISysMenuMgrDao menuDao;

    @Autowired
    ISysUserDao userMapper;

    protected static final String REDIRECT = "redirect:";

    /**
     * 跳转到主页
     */
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index(Model model) {
        //获取菜单列表
        List<Long> roleList = ShiroKit.getUser().getRoleList();
        if (roleList == null || roleList.size() == 0) {
            ShiroKit.getSubject().logout();
            model.addAttribute("tips", "该用户没有角色，无法登陆");
            return "/login.html";
        }
        List<MenuNode> menus = menuDao.getMenusByRoleIds(roleList);
        List<MenuNode> titles = MenuNode.buildTitle(menus);
        titles = ApiMenuFilter.build(titles);

        model.addAttribute("titles", titles);

        //获取用户头像
        Long id = ShiroKit.getUser().getId();
        SysUserBo user = userMapper.selectById(id);
        String avatar = user.getAvatar();
        model.addAttribute("avatar", avatar);

        return "/index.html";
    }
    
    /**
     * 跳转到登录页面
     */
    @ResponseBody
    @RequestMapping(value = "/login2", method = RequestMethod.POST)
    public String login(String account, String password) {
    	SysUserBo user1 = new SysUserBo();
		user1.setAccount(account);
		user1.setPassword(password);
		SysUserBo user = userMapper.selectOne(user1);
    	
		 Subject currentUser = ShiroKit.getSubject();
		 UsernamePasswordToken token = new UsernamePasswordToken(user.getName(), password.toCharArray());
		
		 currentUser.login(token);

		 HttpKit.getRequest().getSession().setAttribute("adminUser", user);
        HttpKit.getRequest().getSession().setAttribute("username", user.getName());

		 ShiroKit.getSession().setAttribute("sessionFlag", true);
		
		 return REDIRECT + "/";
    }

    /**
     * 跳转到登录页面
     */
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login() {
        if (ShiroKit.isAuthenticated() || ShiroKit.getUser() != null) {
            return REDIRECT + "/";
        } else {
            return "/login.html";
        }
    }

    /**
     * 点击登录执行的动作
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String loginVali() {

        String username = HttpKit.getRequest().getParameter("username").trim();
        String password = HttpKit.getRequest().getParameter("password").trim();
        String remember = HttpKit.getRequest().getParameter("remember");

        //验证验证码是否正确
        if (KaptchaUtil.getKaptchaOnOff()) {
            String kaptcha = HttpKit.getRequest().getParameter("kaptcha").trim();
            String code = (String) HttpKit.getRequest().getSession().getAttribute(Constants.KAPTCHA_SESSION_KEY);
            if (Tools.string.isEmpty(kaptcha) || !kaptcha.equalsIgnoreCase(code)) {
                throw new RuntimeException();
            }
        }

        Subject currentUser = ShiroKit.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(username, password.toCharArray());

        if ("on".equals(remember)) {
            token.setRememberMe(true);
        } else {
            token.setRememberMe(false);
        }

        currentUser.login(token);

        ShiroUser shiroUser = ShiroKit.getUser();
        HttpKit.getRequest().getSession().setAttribute("shiroUser", shiroUser);
        HttpKit.getRequest().getSession().setAttribute("username", shiroUser.getAccount());


        ShiroKit.getSession().setAttribute("sessionFlag", true);

        return REDIRECT + "/";
    }
    
    /**
     * 用于测试代码
     */
    @RequestMapping(value = "/auth", method = RequestMethod.POST)
    @ResponseBody
    public ShiroUser auth() {

        String username = HttpKit.getRequest().getParameter("username").trim();
        String password = HttpKit.getRequest().getParameter("password").trim();
        String remember = HttpKit.getRequest().getParameter("remember");

        //验证验证码是否正确
        if (KaptchaUtil.getKaptchaOnOff()) {
            String kaptcha = HttpKit.getRequest().getParameter("kaptcha").trim();
            String code = (String) HttpKit.getRequest().getSession().getAttribute(Constants.KAPTCHA_SESSION_KEY);
            if (Tools.string.isEmpty(kaptcha) || !kaptcha.equalsIgnoreCase(code)) {
                throw new RuntimeException();
            }
        }

        Subject currentUser = ShiroKit.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(username, password.toCharArray());

        if ("on".equals(remember)) {
            token.setRememberMe(true);
        } else {
            token.setRememberMe(false);
        }

        currentUser.login(token);

        ShiroUser shiroUser = ShiroKit.getUser();
        HttpKit.getRequest().getSession().setAttribute("shiroUser", shiroUser);
        HttpKit.getRequest().getSession().setAttribute("username", shiroUser.getAccount());
        

        ShiroKit.getSession().setAttribute("sessionFlag", true);

        return shiroUser;
    }

    /**
     * 退出登录
     */
    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logOut() {
        ShiroKit.getSubject().logout();
        return REDIRECT + "/login";
    }
}
