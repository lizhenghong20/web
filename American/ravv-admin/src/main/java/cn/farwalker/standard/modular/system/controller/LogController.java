//package cn.farwalker.standard.modular.system.controller;
//
//import java.util.List;
//import java.util.Map;
//
//import javax.annotation.Resource;
//
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.ResponseBody;
//
//import cn.farwalker.ravv.service.sys.operationlog.dao.ISysOperationLogDao;
//import cn.farwalker.ravv.service.sys.operationlog.dao.ISysOperationLogMgrDao;
//import cn.farwalker.ravv.service.sys.operationlog.model.SysOperationLogBo;
//import cn.farwalker.standard.modular.system.warpper.LogWarpper;
//import cn.farwalker.waka.common.annotion.BussinessLog;
//import cn.farwalker.waka.common.annotion.Permission;
//import cn.farwalker.waka.common.constant.Const;
//import cn.farwalker.waka.common.constant.state.BizLogType;
//import cn.farwalker.waka.core.base.controller.BaseController;
//import cn.farwalker.waka.core.support.BeanKit;
//import cn.farwalker.waka.factory.PageFactory;
//
//import com.baomidou.mybatisplus.mapper.SqlRunner;
//import com.baomidou.mybatisplus.plugins.Page;
//
///**
// * 日志管理的控制器
// *
// * @author Jason Chen
// * @Date 2017年4月5日 19:45:36
// */
//@Controller
//@RequestMapping("/log")
//public class LogController extends BaseController {
//
//    private static String PREFIX = "/system/log/";
//
//    @Resource
//    private ISysOperationLogDao operationLogMapper;
//
//    @Resource
//    private ISysOperationLogMgrDao logDao;
//
//    /**
//     * 跳转到日志管理的首页
//     */
//    @RequestMapping("")
//    public String index() {
//        return PREFIX + "log.html";
//    }
//
//    /**
//     * 查询操作日志列表
//     */
//    @RequestMapping("/list")
//    @Permission(Const.ADMIN_NAME)
//    @ResponseBody
//    public Object list(@RequestParam(required = false) String beginTime, @RequestParam(required = false) String endTime, @RequestParam(required = false) String logName, @RequestParam(required = false) Integer logType) {
//        Page<SysOperationLogBo> page = new PageFactory<SysOperationLogBo>().defaultPage();
//        List<Map<String, Object>> result = logDao.getOperationLogs(page, beginTime, endTime, logName, BizLogType.valueOf(logType), page.getOrderByField(), page.isAsc());
//        page.setRecords((List<SysOperationLogBo>) new LogWarpper(result).warp());
//        return super.packForBT(page);
//    }
//
//    /**
//     * 查询操作日志详情
//     */
//    @RequestMapping("/detail/{id}")
//    @Permission(Const.ADMIN_NAME)
//    @ResponseBody
//    public Object detail(@PathVariable Integer id) {
//    	SysOperationLogBo operationLog = operationLogMapper.selectById(id);
//        Map<String, Object> stringObjectMap = BeanKit.beanToMap(operationLog);
//        return super.warpObject(new LogWarpper(stringObjectMap));
//    }
//
//    /**
//     * 清空日志
//     */
//    @BussinessLog(value = "清空业务日志")
//    @RequestMapping("/delLog")
//    @Permission(Const.ADMIN_NAME)
//    @ResponseBody
//    public Object delLog() {
//        SqlRunner.db().delete("delete from sys_operation_log");
//        return super.SUCCESS_TIP;
//    }
//}
