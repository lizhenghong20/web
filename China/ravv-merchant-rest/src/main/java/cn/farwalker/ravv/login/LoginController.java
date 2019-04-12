package cn.farwalker.ravv.login;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import cn.farwalker.waka.core.WakaException;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.farwalker.ravv.login.dto.AuthRequest;
import cn.farwalker.ravv.login.dto.AuthResponse;
import cn.farwalker.ravv.login.dto.MerchantRoleVo;
import cn.farwalker.ravv.service.merchant.biz.IMerchantBiz;
import cn.farwalker.ravv.service.merchant.model.MerchantBo;
import cn.farwalker.waka.auth.util.JwtTokenUtil;
import cn.farwalker.waka.auth.validator.IReqValidator;
import cn.farwalker.waka.core.JsonResult;
import cn.farwalker.waka.core.RavvExceptionEnum;
import cn.farwalker.waka.util.Tools;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;

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
    private JwtTokenUtil jwtTokenUtil;

    @Resource
    private IMerchantBiz merchantBiz;

    @Resource(name = "dbValidator")
    private IReqValidator reqValidator;
    

    /**登陆
     * @param username 用户账号<br/>
     * @param password 密码<br/>
     */
    @RequestMapping("/login")
    public JsonResult<AuthResponse> doLogin(HttpServletRequest request, String username, String password){

    	String realPath = request.getServletPath();
        //createMethodSinge创建方法
        if(Tools.string.isEmpty(username)){
            return JsonResult.newFail("用户账号不能为空");
        }
        if(Tools.string.isEmpty(password)){
            return JsonResult.newFail("密码不能为空");
        }
        try{
        	AuthResponse rs = this.createAuthenticationToken(username, password);
 	        return JsonResult.newSuccess(rs);
        }
        catch(WakaException e){
        	log.error("",e);
        	return JsonResult.newFail(e.getMessage());
        }
    }

//    @RequestMapping(value = "${jwt.auth-path}")
    private AuthResponse createAuthenticationToken(String account, String password) {
    	AuthRequest authRequest = new AuthRequest();
    	if(Tools.string.isEmpty(account)){
    		 throw new WakaException(RavvExceptionEnum.USER_NO_REGISTER);
    	}
    	
    	Wrapper<MerchantBo> query = new EntityWrapper<>();
    	query.eq(MerchantBo.Key.account.toString(),account);
    	MerchantBo merchantBo = merchantBiz.selectOne(query);
        if(merchantBo == null) {
        	throw new WakaException(RavvExceptionEnum.USER_NO_REGISTER);
        }
        authRequest.setAccount(account);
        
        /*
        //密码加密md5
        String passwordMd5 = MD5Util.md5(password,user.getSalt());
        authRequest.setPassword(passwordMd5);
        authRequest.setType(AccountTypeEnum.USERNAME.getCode());
        boolean validate = (boolean) reqValidator.validate(authRequest);
        */
        boolean validate = (password!=null && password.equalsIgnoreCase(merchantBo.getLoginPassword()));
        if (validate) {
        	AuthResponse rs = getAuthResponse(merchantBo.getAccount(), merchantBo.getId());
            return rs;
        } else {
            throw new WakaException(RavvExceptionEnum.USER_ACCOUNT_OR_PASSWORD_INCORRECT);
        }
    }

    private AuthResponse getAuthResponse(String account, Long memberId) {
    	final String randomKey = jwtTokenUtil.getRandomKey();
        final String token = jwtTokenUtil.generateToken(account, memberId, Long.toString(System.currentTimeMillis()), randomKey);
    	AuthResponse authResponse = new AuthResponse(token, randomKey);
    	authResponse.setMemberId(memberId);
    	authResponse.setAccount(account);
    	return authResponse;
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
    public JsonResult<MerchantRoleVo> getUser(@RequestParam String token){
        //createMethodSinge创建方法
        if(Tools.string.isEmpty(token)){
            return JsonResult.newFail("令牌不能为空");
        }

        //解析token获取memberId
        Map<String, Object> claims = jwtTokenUtil.getClaimFromToken(token);
        if(null == claims.get("memberId")) {
        	return JsonResult.newFail("没有用户id");
        }
        //获取用户信息
        MerchantBo merchantBo = merchantBiz.selectById(claims.get("memberId").toString());
        if(null != merchantBo) {
        	MerchantRoleVo vo = Tools.bean.cloneBean(merchantBo, new MerchantRoleVo());
        	vo.setLoginPassword("");//不返回密码
        	//返回管理员用户权限 
        	List<String> roleCodeList = Collections.singletonList("admin");
        	vo.setRoles(roleCodeList);

            return JsonResult.newSuccess(vo);
        }else {
        	return JsonResult.newFail(RavvExceptionEnum.USER_NO_REGISTER.getCode(),RavvExceptionEnum.USER_NO_REGISTER.getMessage());
        }
    }
}