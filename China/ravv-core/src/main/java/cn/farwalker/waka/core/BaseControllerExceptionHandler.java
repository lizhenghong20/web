//package cn.farwalker.waka.core;
//
///**
// * Created by asus on 2019/3/22.
// */
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.http.HttpStatus;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.ResponseBody;
//import org.springframework.web.bind.annotation.ResponseStatus;
//
//public class BaseControllerExceptionHandler {
//    private Logger log = LoggerFactory.getLogger(this.getClass());
//
//    public BaseControllerExceptionHandler() {
//    }
//
//    @ExceptionHandler({WakaException.class})
//    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
//    @ResponseBody
//    public ErrorTip notFount(WakaException e) {
//        this.log.error("业务异常:", e);
//        return new ErrorTip(e.getCode().intValue(), e.getMessage());
//    }
//
//    @ExceptionHandler({RuntimeException.class})
//    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
//    @ResponseBody
//    public ErrorTip notFount(RuntimeException e) {
//        this.log.error("运行时异常:", e);
//        return new ErrorTip(WakaExceptionEnum.SERVER_ERROR.getCode().intValue(), WakaExceptionEnum.SERVER_ERROR.getMessage());
//    }
//}