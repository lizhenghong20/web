//package cn.farwalker.waka.aop;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.http.HttpStatus;
//import org.springframework.web.bind.annotation.ControllerAdvice;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.ResponseBody;
//import org.springframework.web.bind.annotation.ResponseStatus;
//
//import cn.farwalker.waka.core.aop.BaseControllerExceptionHandler;
//import cn.farwalker.waka.core.base.tips.ErrorTip;
//import cn.farwalker.waka.core.RavvExceptionEnum;
//import io.jsonwebtoken.JwtException;
//
///**
// * 全局的的异常拦截器（拦截所有的控制器）（带有@RequestMapping注解的方法上都会拦截）
// *
// * @author Jason Chen
// * @date 2016年11月12日 下午3:19:56
// */
//@ControllerAdvice
//public class GlobalExceptionHandler extends BaseControllerExceptionHandler {
//
//    private Logger log = LoggerFactory.getLogger(this.getClass());
//
//    /**
//     * 拦截jwt相关异常
//     */
//    @ExceptionHandler(JwtException.class)
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    @ResponseBody
//    public ErrorTip jwtException(JwtException e) {
//    	log.error("拦截jwt相关异常",e);
//        return new ErrorTip(RavvExceptionEnum.TOKEN_VERIFICATION_FAILED.getCode(), RavvExceptionEnum.TOKEN_VERIFICATION_FAILED.getMessage());
//    }
//}
