package cn.farwalker.standard.modular.system.controller;

import cn.farwalker.standard.core.temp.LogObjectHolder;
import cn.farwalker.waka.core.SuccessTip;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.farwalker.ravv.service.member.pam.operationlog.biz.IPamOperationLogBiz;
import cn.farwalker.ravv.service.member.pam.operationlog.model.PamOperationLogBo;


/**
 * PAM_操作日志控制器
 *
 * @author Jason Chen
 * @Date 2018-04-09 08:58:03
 */
@Controller
@RequestMapping("/pamOperationLog")
public class PamOperationLogController{

    private String PREFIX = "/system/pamOperationLog/";

    @Autowired
    private IPamOperationLogBiz pamOperationLogService;

    /**
     * 跳转到PAM_操作日志首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "pamOperationLog.html";
    }

    /**
     * 跳转到添加PAM_操作日志
     */
    @RequestMapping("/pamOperationLog_add")
    public String pamOperationLogAdd() {
        return PREFIX + "pamOperationLog_add.html";
    }

    /**
     * 跳转到修改PAM_操作日志
     */
    @RequestMapping("/pamOperationLog_update/{pamOperationLogId}")
    public String pamOperationLogUpdate(@PathVariable Long pamOperationLogId, Model model) {
        PamOperationLogBo pamOperationLog = pamOperationLogService.selectById(pamOperationLogId);
        model.addAttribute("item",pamOperationLog);
        LogObjectHolder.me().set(pamOperationLog);
        return PREFIX + "pamOperationLog_edit.html";
    }

    /**
     * 获取PAM_操作日志列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String condition) {
        return pamOperationLogService.selectList(null);
    }

    /**
     * 新增PAM_操作日志
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add(PamOperationLogBo pamOperationLog) {
        pamOperationLogService.insert(pamOperationLog);
        return new SuccessTip();
    }

    /**
     * 删除PAM_操作日志
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(@RequestParam Integer pamOperationLogId) {
        pamOperationLogService.deleteById(pamOperationLogId);
        return new SuccessTip();
    }

    /**
     * 修改PAM_操作日志
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update(PamOperationLogBo pamOperationLog) {
        pamOperationLogService.updateById(pamOperationLog);
        return new SuccessTip();
    }

    /**
     * PAM_操作日志详情
     */
    @RequestMapping(value = "/detail/{pamOperationLogId}")
    @ResponseBody
    public Object detail(@PathVariable("pamOperationLogId") Integer pamOperationLogId) {
        return pamOperationLogService.selectById(pamOperationLogId);
    }
}
