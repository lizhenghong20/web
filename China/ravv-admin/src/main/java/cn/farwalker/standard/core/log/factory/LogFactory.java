//package cn.farwalker.standard.core.log.factory;
//
//import cn.farwalker.ravv.service.sys.loginlog.model.SysLoginLogBo;
//import cn.farwalker.ravv.service.sys.operationlog.model.SysOperationLogBo;
//import cn.farwalker.waka.common.constant.state.LogSucceed;
//import cn.farwalker.waka.common.constant.state.LogType;
//
///**
// * 日志对象创建工厂
// *
// * @author Jason Chen
// * @date 2016年12月6日 下午9:18:27
// */
//public class LogFactory {
//
//    /**
//     * 创建操作日志
//     *
//     * @author Jason Chen
//     * @Date 2017/3/30 18:45
//     */
//    public static SysOperationLogBo createOperationLog(LogType logType, Long userId, String bussinessName, String clazzName, String methodName, String msg, LogSucceed succeed) {
//    	SysOperationLogBo operationLog = new SysOperationLogBo();
//        operationLog.setLogtype(logType.getMessage());
//        operationLog.setLogname(bussinessName);
//        operationLog.setUserid(userId);
//        operationLog.setClassname(clazzName);
//        operationLog.setMethod(methodName);
//        operationLog.setSucceed(succeed.getMessage());
//        operationLog.setMessage(msg);
//        return operationLog;
//    }
//
//    /**
//     * 创建登录日志
//     *
//     * @author Jason Chen
//     * @Date 2017/3/30 18:46
//     */
//    public static SysLoginLogBo createLoginLog(LogType logType, Long userId, String msg,String ip) {
//    	SysLoginLogBo loginLog = new SysLoginLogBo();
//        loginLog.setLogname(logType.getMessage());
//        loginLog.setUserid(userId);
//        loginLog.setSucceed(LogSucceed.SUCCESS.getMessage());
//        loginLog.setIp(ip);
//        loginLog.setMessage(msg);
//        return loginLog;
//    }
//}
