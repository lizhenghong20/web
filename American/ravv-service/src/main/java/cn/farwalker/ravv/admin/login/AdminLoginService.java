package cn.farwalker.ravv.admin.login;

import cn.farwalker.ravv.admin.login.dto.AuthResponse;
import cn.farwalker.ravv.service.sys.user.model.SysUserVo;
import org.springframework.http.ResponseEntity;

public interface AdminLoginService {

    ResponseEntity<?> createAuthenticationToken(String account, String password);

    AuthResponse doLogin(String username, String password);

    SysUserVo getUser(String token);
}
