package cn.farwalker.ravv.login;

import javax.servlet.http.HttpServletRequest;

import cn.farwalker.ravv.admin.login.AdminLoginService;
import cn.farwalker.waka.core.WakaException;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.farwalker.ravv.admin.login.dto.AuthResponse;
import cn.farwalker.ravv.service.sys.user.model.SysUserVo;
import cn.farwalker.waka.core.JsonResult;
import cn.farwalker.waka.util.Tools;

/**
 * 登陆接口<br/>
 * 登陆演示<br/>
 * //手写的注释:以"//"开始 <br/>
 * @author generateModel.java
 */
@Slf4j
@RestController
@RequestMapping("/auth")
public class LoginController{

    @Autowired
    private AdminLoginService adminLoginService;

    /**登陆
     * @param username 用户账号<br/>
     * @param password 密码<br/>
     */
    @RequestMapping("/login")
    public JsonResult<AuthResponse> doLogin(HttpServletRequest request, String username, String password){
        try{
            //createMethodSinge创建方法
            if(Tools.string.isEmpty(username)){
                throw new WakaException("用户账号不能为空");
            }
            if(Tools.string.isEmpty(password)){
                throw new WakaException("密码不能为空");
            }
	        return JsonResult.newSuccess(adminLoginService.doLogin(username, password));
        } catch(WakaException e){
        	log.error("",e);
        	return JsonResult.newFail(e.getMessage());
        } catch (Exception e) {
            log.error("", e);
            return JsonResult.newFail(e.getMessage());
        }
    }

    /**退出登陆*/
    @RequestMapping("/logout")
    public JsonResult<Boolean> doLogout(){
        //createMethodSinge创建方法
        Boolean rs =null;
        return JsonResult.newSuccess(rs);
    }

    /**取用户信息
     * @param token 令牌<br/>
     */
    @RequestMapping("/user")
    public JsonResult<SysUserVo> getUser(@RequestParam String token){
        try{
            //createMethodSinge创建方法
            if(Tools.string.isEmpty(token)){
                throw new WakaException("用户账号不能为空");
            }
            return JsonResult.newSuccess(adminLoginService.getUser(token));

        } catch(WakaException e){
            log.error("",e);
            return JsonResult.newFail(e.getMessage());
        } catch (Exception e) {
            log.error("", e);
            return JsonResult.newFail(e.getMessage());
        }

    }
}