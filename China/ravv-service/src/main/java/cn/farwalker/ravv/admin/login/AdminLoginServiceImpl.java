package cn.farwalker.ravv.admin.login;


import cn.farwalker.ravv.admin.login.dto.AuthRequest;
import cn.farwalker.ravv.admin.login.dto.AuthResponse;
import cn.farwalker.ravv.service.sys.menu.biz.ISysMenuBiz;
import cn.farwalker.ravv.service.sys.user.biz.ISysUserBiz;
import cn.farwalker.ravv.service.sys.user.model.SysUserBo;
import cn.farwalker.ravv.service.sys.user.model.SysUserVo;
import cn.farwalker.waka.auth.util.JwtTokenUtil;
import cn.farwalker.waka.auth.validator.IReqValidator;
import cn.farwalker.waka.core.WakaException;
import cn.farwalker.waka.core.RavvExceptionEnum;
import cn.farwalker.waka.util.MD5Util;
import cn.farwalker.waka.util.Tools;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class AdminLoginServiceImpl implements AdminLoginService {
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Resource(name = "dbValidator")
    private IReqValidator reqValidator;

    @Resource
    private ISysMenuBiz sysMenuBiz;

    @Autowired
    private ISysUserBiz userBiz;

    @Autowired
    private ISysUserBiz userMapper;

    @Override
    public ResponseEntity<?> createAuthenticationToken(String account, String password) {
        AuthRequest authRequest = new AuthRequest();
        Wrapper<SysUserBo> query = new EntityWrapper<>();
        query.eq(SysUserBo.Key.account.toString(),account);
        SysUserBo user = userMapper.selectOne(query);

        if(user == null) {
            throw new WakaException(RavvExceptionEnum.USER_NO_REGISTER);
        }
        authRequest.setAccount(account);
        authRequest.setPassword(password);
        boolean validate = (boolean) reqValidator.validate(authRequest);

        if (validate) {
            return ResponseEntity.ok(getAuthResponse(user.getAccount(), user.getId()));
        } else {
            throw new WakaException(RavvExceptionEnum.USER_ACCOUNT_OR_PASSWORD_INCORRECT);
        }
    }

    @Override
    public AuthResponse doLogin(String username, String password) {
        AuthResponse rs = this.createLoginAuthenticationToken(username, password);

        return rs;
    }

    @Override
    public SysUserVo getUser(String token) {
        //解析token获取memberId
        Map<String, Object> claims = jwtTokenUtil.getClaimFromToken(token);
        if(null == claims.get("memberId")) {
            throw new WakaException("没有用户id");
        }
        //获取用户信息
        SysUserBo user = userBiz.selectById(claims.get("memberId").toString());
        SysUserVo sysUserVo = new SysUserVo();
        if(null != user) {
            BeanUtils.copyProperties(user, sysUserVo);
            //补全头像路径
            if(null != user.getAvatar()) {
                sysUserVo.setAvatar("https://wpimg.wallstcn.com" + user.getAvatar());
            }
            //返回管理员用户权限
            List<String> roleCodeList = sysMenuBiz.getRoleCodeList(user.getId());
//        	roleCodeList.add("admin");//TODO权限路由还未配置好，先开放所有权限
            sysUserVo.setRoles(roleCodeList);

            //不返回密码和盐
            sysUserVo.setPassword(null);
            sysUserVo.setSalt(null);
            return sysUserVo;
        }else {
            throw new WakaException(RavvExceptionEnum.USER_NO_REGISTER);
        }
    }


    private AuthResponse createLoginAuthenticationToken(String account, String password) {
        AuthRequest authRequest = new AuthRequest();
        if(Tools.string.isEmpty(account)){
            throw new WakaException(RavvExceptionEnum.USER_NO_REGISTER);
        }

        Wrapper<SysUserBo> query = new EntityWrapper<>();
        query.eq(SysUserBo.Key.account.toString(),account);
        SysUserBo user = userBiz.selectOne(query);
        if(user == null) {
            throw new WakaException(RavvExceptionEnum.USER_NO_REGISTER);
        }
        authRequest.setAccount(account);

        //密码加密md5
        String passwordMd5 = MD5Util.md5(password,user.getSalt());

        authRequest.setPassword(passwordMd5);
        boolean validate = (boolean) reqValidator.validate(authRequest);

        if (validate) {
            AuthResponse rs = getAuthResponse(user.getAccount(), user.getId());
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
}
