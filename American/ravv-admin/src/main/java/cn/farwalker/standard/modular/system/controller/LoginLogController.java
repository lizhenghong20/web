package cn.farwalker.standard.modular.system.controller;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import cn.farwalker.standard.core.temp.BussinessLog;
import cn.farwalker.standard.core.temp.Const;
import cn.farwalker.standard.core.temp.Permission;
import cn.farwalker.waka.core.SuccessTip;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.farwalker.ravv.service.sys.loginlog.biz.ISysLoginLogBiz;
import cn.farwalker.ravv.service.sys.loginlog.model.SysLoginLogBo;
import cn.farwalker.ravv.service.sys.operationlog.dao.ISysOperationLogMgrDao;
import cn.farwalker.waka.components.wechatpay.common.util.StringUtils;
import cn.farwalker.waka.core.JsonResult;
import cn.farwalker.waka.core.base.controller.ControllerUtils;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.SqlRunner;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;

/**
 * 日志管理的控制器
 *
 * @author Jason Chen
 * @Date 2017年4月5日 19:45:36
 */
@Controller
@RequestMapping("/loginLog")
public class LoginLogController{

    private static String PREFIX = "/system/log/";

    @Resource
    private ISysOperationLogMgrDao logDao;
    
    @Resource 
    private ISysLoginLogBiz sysLoginLogBiz;

    /**
     * 跳转到日志管理的首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "login_log.html";
    }

    /**
     * 查询登录日志列表
     */
    @RequestMapping("/list")
    @Permission(Const.ADMIN_NAME)
    @ResponseBody
    public JsonResult<Page<SysLoginLogBo>> list(Integer offset, Integer limit, String logName) {
    	Page<SysLoginLogBo> page = ControllerUtils.getPage(offset + 1, limit, null);
		Wrapper<SysLoginLogBo> wrapper = new EntityWrapper<>();
		if(StringUtils.isNotEmpty(logName)) {
			wrapper.like(SysLoginLogBo.Key.logname.toString(), logName);
		}
		Page<SysLoginLogBo> rs = sysLoginLogBiz.selectPage(page, wrapper);
		return JsonResult.newSuccess(rs);
    }

    /**
     * 清空日志
     */
    @BussinessLog("清空登录日志")
    @RequestMapping("/delLoginLog")
    @Permission(Const.ADMIN_NAME)
    @ResponseBody
    public Object delLog() {
        SqlRunner.db().delete("delete from sys_login_log");
        return new SuccessTip();
    }
    
}
