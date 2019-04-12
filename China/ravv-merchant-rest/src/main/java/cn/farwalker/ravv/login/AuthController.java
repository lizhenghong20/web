//package cn.farwalker.ravv.login;
//
//import javax.annotation.Resource;
//
//import cn.farwalker.waka.core.WakaException;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//
//import cn.farwalker.ravv.login.dto.AuthRequest;
//import cn.farwalker.ravv.login.dto.AuthResponse;
//import cn.farwalker.ravv.service.merchant.biz.IMerchantBiz;
//import cn.farwalker.ravv.service.merchant.model.MerchantBo;
//import cn.farwalker.waka.auth.util.JwtTokenUtil;
//import cn.farwalker.waka.auth.validator.IReqValidator;
//
//import cn.farwalker.waka.core.RavvExceptionEnum;
//import cn.farwalker.waka.util.Tools;
//
//import com.baomidou.mybatisplus.mapper.EntityWrapper;
//import com.baomidou.mybatisplus.mapper.Wrapper;
//
///**
// * 请求验证的API
// *
// * @author Jason Chen
// * @Date 2018/2/12 14:22
// */
//@RestController
//public class AuthController {
//
//    private final static Logger logger = LoggerFactory.getLogger(AuthController.class);
//
//    @Autowired
//    private JwtTokenUtil jwtTokenUtil;
//
//    @Resource(name = "dbValidator")
//    private IReqValidator reqValidator;
//
//
//    //@Autowired
//    //private IMerchantDao merchantMapper;
//
//    @Resource
//    private IMerchantBiz merchantBiz;
//
//    @RequestMapping(value = "${jwt.auth-path}")
//    public ResponseEntity<?> createAuthenticationToken(String account, String password) {
//    	if(Tools.string.isEmpty(account)){
//    		throw new WakaException("账号不能为空!");
//    	}
//    	AuthRequest authRequest = new AuthRequest();
//    	Wrapper<MerchantBo> query = new EntityWrapper<>();
//    	query.eq(MerchantBo.Key.account.toString(),account);
//    	query.last("limit 1");
//    	MerchantBo merchantBo = merchantBiz.selectOne(query);
//
//        if(merchantBo == null) {
//        	throw new WakaException(RavvExceptionEnum.USER_NO_REGISTER);
//        }
//        authRequest.setAccount(account);
//        authRequest.setPassword(password);
//        authRequest.setType(AccountTypeEnum.USERNAME.getCode());
//        boolean validate = (boolean) reqValidator.validate(authRequest);
//
//        if (validate) {
//            return ResponseEntity.ok(getAuthResponse(merchantBo.getAccount(), merchantBo.getId()));
//        } else {
//            throw new WakaException(RavvExceptionEnum.USER_ACCOUNT_OR_PASSWORD_INCORRECT);
//        }
//    }
//
//  //用于测试
//    @RequestMapping(value = "/auth-test")
//    public ResponseEntity<?> createAuthenticationTokenByAccount(String account, Long memberId,String password) {
//    	AuthRequest authRequest = new AuthRequest();
//    	authRequest.setAccount(account);
//        authRequest.setPassword(password);
//        authRequest.setType(AccountTypeEnum.USERNAME.getCode());
//        boolean validate = (boolean) reqValidator.validate(authRequest);
//
//        if (validate) {
//            return ResponseEntity.ok(getAuthResponse(account, memberId));
//        } else {
//            throw new WakaException(RavvExceptionEnum.USER_ACCOUNT_OR_PASSWORD_INCORRECT);
//        }
//    }
//
////    @RequestMapping("/register")
////    public ResponseEntity<?> register(String code, String encryptedData, String iv) {
////    	// 用户信息校验
////        /*if (!this.wxService.getUserService().checkUserInfo(sessionKey, rawData, signature)) {
////            throw new WakaException(BizExceptionEnum.USER_CHECK_FAILED);
////        }*/
////    	WxMaUserInfo userInfo;
////    	Member member;
////    	try {
////	    	WxMaJscode2SessionResult session = this.wxService.getUserService().getSessionInfo(code);
////	        // 解密用户信息
////	        userInfo = this.wxService.getUserService().getUserInfo(session.getSessionKey(), encryptedData, iv);
////	        PamMember pamMember = getPamMemberByOpenId(session.getOpenid());
////            if(pamMember != null) { //重复注册判断
////            	throw new WakaException(BizExceptionEnum.REGISTER_DUPLICATE);
////            }
////	        member = addMember(userInfo);
////	        addPamMember(userInfo, member.getId());
////
////    	}catch (WxErrorException e) {
////            this.logger.error(e.getMessage(), e);
////            return ResponseEntity.ok(BizExceptionEnum.GET_WECHAT_SESSION_ERROR);
////        }
////    	if(userInfo != null && member != null) {
////    		return ResponseEntity.ok(member.getId());
////    	}else {
////    		return ResponseEntity.ok(BizExceptionEnum.USER_CHECK_FAILED);
////    	}
////
////    }
//
//    @RequestMapping("/check-token")
//    public ResponseEntity<?> register() {
//
//    	//TODO 可能不需要这个接口，只需要校验JWT token就可以了
//    	// 用户信息校验
//        /*if (!this.wxService.getUserService().checkUserInfo(sessionKey, rawData, signature)) {
//            return ResponseEntity.ok("");
//        }*/
//    	//如果token不通过，在AuthFilter就直接返回错误了,这里重新刷新token
////        return ResponseEntity.ok(new AuthResponse());
//    	return null;
//    }
//    /*
//    private MemberBo addMember(WxMaUserInfo userInfo) {
//    	MemberBo member = new MemberBo();
//    	member.setAddress(userInfo.getProvince() + userInfo.getCity() + userInfo.getCountry());
//    	member.setAvator(userInfo.getAvatarUrl());
//    	member.setLoginCount(0);
//    	member.setName(userInfo.getNickName());
////    	member.setSex(GenderEnum.getCodeByMessage(userInfo.getGender()));
//    	member.setRegIp(HttpKit.getIp());
//    	member.setRegisterTime(new Date());
////    	member.setStatus(EnabledStatusEnum.ENABLED.getCode());
//    	memberServie.insert(member);
//    	return member;
//    }
//    */
////    private PamMember addPamMember(WxMaUserInfo userInfo, Long memberId) {
////    	PamMember pamMember = new PamMember();
////    	pamMember.setMemberId(memberId);
////    	pamMember.setAccount(userInfo.getOpenId());
////    	if(StringUtils.isNotEmpty(userInfo.getUnionId())) {
////    		pamMember.setAppendAccount(userInfo.getUnionId());
////    	}
////    	pamMember.setType(AccountTypeEnum.WECHAT.getCode());
//////    	pamMember.setLoginstyle(LoginStyleEnum.AUTHORIZE.getCode());
////    	pamMember.setEnabled(EnabledStatusEnum.ENABLED.getCode());
////    	pamMemberServie.insert(pamMember);
////    	return pamMember;
////    }
////
//    private AuthResponse getAuthResponse(String account, Long memberId) {
//    	final String randomKey = jwtTokenUtil.getRandomKey();
//        final String token = jwtTokenUtil.generateToken(account, memberId, Long.toString(System.currentTimeMillis()), randomKey);
//    	AuthResponse authResponse = new AuthResponse(token, randomKey);
//    	authResponse.setMemberId(memberId);
//    	authResponse.setAccount(account);
//    	return authResponse;
//    }
//
////    private PamMember getPamMemberByOpenId(String openid) {
////    	Map<String, Object> pamMemberExist = new HashMap<>();
////    	pamMemberExist.put("account", openid);
////    	pamMemberExist.put("type", 2);
////    	List<PamMember> accounts = pamMemberServie.selectByMap(pamMemberExist);
////    	if(CollectionUtils.isNotEmpty(accounts)) {
////    		return accounts.get(0);
////    	}else {
////    		return  null;
////    	}
////    }
//}
