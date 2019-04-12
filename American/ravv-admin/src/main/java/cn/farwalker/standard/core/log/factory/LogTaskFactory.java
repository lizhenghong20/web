//package cn.farwalker.standard.core.log.factory;
//
//import java.util.TimerTask;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import cn.farwalker.ravv.service.sys.loginlog.dao.ISysLoginLogDao;
//import cn.farwalker.ravv.service.sys.loginlog.model.SysLoginLogBo;
//import cn.farwalker.ravv.service.sys.operationlog.dao.ISysOperationLogDao;
//import cn.farwalker.ravv.service.sys.operationlog.model.SysOperationLogBo;
//import cn.farwalker.waka.common.constant.state.LogSucceed;
//import cn.farwalker.waka.common.constant.state.LogType;
//import cn.farwalker.waka.core.db.Db;
//import cn.farwalker.waka.core.log.LogManager;
//import cn.farwalker.waka.core.util.ToolUtil;
//
///**
// * 日志操作任务创建工厂
// *
// * @author Jason Chen
// * @date 2016年12月6日 下午9:18:27
// */
//public class LogTaskFactory {
//
//    private static Logger logger = LoggerFactory.getLogger(LogManager.class);
//    private static ISysLoginLogDao loginLogMapper = Db.getMapper(ISysLoginLogDao.class);
//    private static ISysOperationLogDao operationLogMapper = Db.getMapper(ISysOperationLogDao.class);
//
//    public static TimerTask loginLog(final Long userId, final String ip) {
//        return new TimerTask() {
//            @Override
//            public void run() {
//                try {
//                    SysLoginLogBo loginLog = LogFactory.createLoginLog(LogType.LOGIN, userId, null, ip);
//                    loginLogMapper.insert(loginLog);
//                } catch (Exception e) {
//                    logger.error("创建登录日志异常!", e);
//                }
//            }
//        };
//    }
//
//    public static TimerTask loginLog(final String username, final String msg, final String ip) {
//        return new TimerTask() {
//            @Override
//            public void run() {
//            	SysLoginLogBo loginLog = LogFactory.createLoginLog(
//                        LogType.LOGIN_FAIL, null, "账号:" + username + "," + msg, ip);
//                try {
//                    loginLogMapper.insert(loginLog);
//                } catch (Exception e) {
//                    logger.error("创建登录失败异常!", e);
//                }
//            }
//        };
//    }
//
//    public static TimerTask exitLog(final Long userId, final String ip) {
//        return new TimerTask() {
//            @Override
//            public void run() {
//            	SysLoginLogBo loginLog = LogFactory.createLoginLog(LogType.EXIT, userId, null,ip);
//                try {
//                    loginLogMapper.insert(loginLog);
//                } catch (Exception e) {
//                    logger.error("创建退出日志异常!", e);
//                }
//            }
//        };
//    }
//
//    public static TimerTask bussinessLog(final Long userId, final String bussinessName, final String clazzName, final String methodName, final String msg) {
//        return new TimerTask() {
//            @Override
//            public void run() {
//                SysOperationLogBo operationLog = LogFactory.createOperationLog(
//                        LogType.BUSSINESS, userId, bussinessName, clazzName, methodName, msg, LogSucceed.SUCCESS);
//                try {
//                    operationLogMapper.insert(operationLog);
//                } catch (Exception e) {
//                    logger.error("创建业务日志异常!", e);
//                }
//            }
//        };
//    }
//
//    public static TimerTask exceptionLog(final Long userId, final Exception exception) {
//        return new TimerTask() {
//            @Override
//            public void run() {
//                String msg = ToolUtil.getExceptionMsg(exception);
//                SysOperationLogBo operationLog = LogFactory.createOperationLog(
//                        LogType.EXCEPTION, userId, "", null, null, msg, LogSucceed.FAIL);
//                try {
//                    operationLogMapper.insert(operationLog);
//                } catch (Exception e) {
//                    logger.error("创建异常日志异常!", e);
//                }
//            }
//        };
//    }
//}
