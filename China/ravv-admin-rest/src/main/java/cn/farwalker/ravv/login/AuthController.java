
//package cn.farwalker.ravv.login;
//
//
//import cn.farwalker.ravv.admin.login.AdminLoginService;
//import cn.farwalker.waka.core.WakaException;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//import cn.farwalker.waka.util.Tools;
//
//
//
///**
// * 请求验证的API
// *
// * @author Jason Chen
// * @Date 2018/2/12 14:22
// */
//@Slf4j
//@RestController
//public class AuthController {
//
//    @Autowired
//    private AdminLoginService adminLoginService;
//
//    @RequestMapping(value = "${jwt.auth-path}")
//    public ResponseEntity<?> createAuthenticationToken(String account, String password) {
//    	if(Tools.string.isEmpty(account)){
//    		throw new WakaException("账号不能为空!");
//    	}
//        return adminLoginService.createAuthenticationToken(account, password);
//    }
//
//
//}

