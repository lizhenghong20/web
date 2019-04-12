//package cn.farwalker.standard.core.intercept;
//
//import org.aspectj.lang.ProceedingJoinPoint;
//import org.aspectj.lang.annotation.Around;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Pointcut;
//import org.springframework.stereotype.Component;
//
//import cn.farwalker.waka.core.base.controller.BaseController;
//import cn.farwalker.waka.core.util.HttpSessionHolder;
//
///**
// * 静态调用session的拦截器
// *
// * @author Jason Chen
// * @date 2016年11月13日 下午10:15:42
// */
//@Aspect
//@Component
//public class SessionHolderInterceptor{
//
//    @Pointcut("execution(* cn.farwalker.standard.*..controller.*.*(..))")
//    public void cutService() {
//    }
//
//    @Around("cutService()")
//    public Object sessionKit(ProceedingJoinPoint point) throws Throwable {
//
//        HttpSessionHolder.put(super.getHttpServletRequest().getSession());
//        try {
//            return point.proceed();
//        } finally {
//            HttpSessionHolder.remove();
//        }
//    }
//}
