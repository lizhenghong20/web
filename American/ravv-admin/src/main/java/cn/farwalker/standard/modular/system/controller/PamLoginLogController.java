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

import cn.farwalker.ravv.service.member.pam.loginlog.biz.IPamLoginLogBiz;
import cn.farwalker.ravv.service.member.pam.loginlog.model.PamLoginLogBo;

/**
 * PAM_会员登录日志控制器
 *
 * @author Jason Chen
 * @Date 2018-04-09 08:57:47
 */
@Controller
@RequestMapping("/pamLoginLog")
public class PamLoginLogController{

    private String PREFIX = "/system/pamLoginLog/";

    @Autowired
    private IPamLoginLogBiz pamLoginLogService;

    /**
     * 跳转到PAM_会员登录日志首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "pamLoginLog.html";
    }

    /**
     * 跳转到添加PAM_会员登录日志
     */
    @RequestMapping("/pamLoginLog_add")
    public String pamLoginLogAdd() {
        return PREFIX + "pamLoginLog_add.html";
    }

    /**
     * 跳转到修改PAM_会员登录日志
     */
    @RequestMapping("/pamLoginLog_update/{pamLoginLogId}")
    public String pamLoginLogUpdate(@PathVariable Long pamLoginLogId, Model model) {
        PamLoginLogBo pamLoginLog = pamLoginLogService.selectById(pamLoginLogId);
        model.addAttribute("item",pamLoginLog);
        LogObjectHolder.me().set(pamLoginLog);
        return PREFIX + "pamLoginLog_edit.html";
    }

    /**
     * 获取PAM_会员登录日志列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String condition) {
        return pamLoginLogService.selectList(null);
    }

    /**
     * 新增PAM_会员登录日志
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add(PamLoginLogBo pamLoginLog) {
        pamLoginLogService.insert(pamLoginLog);
        return new SuccessTip();
    }

    /**
     * 删除PAM_会员登录日志
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(@RequestParam Integer pamLoginLogId) {
        pamLoginLogService.deleteById(pamLoginLogId);
        return new SuccessTip();
    }

    /**
     * 修改PAM_会员登录日志
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update(PamLoginLogBo pamLoginLog) {
        pamLoginLogService.updateById(pamLoginLog);
        return new SuccessTip();
    }

    /**
     * PAM_会员登录日志详情
     */
    @RequestMapping(value = "/detail/{pamLoginLogId}")
    @ResponseBody
    public PamLoginLogBo detail(@PathVariable("pamLoginLogId") Integer pamLoginLogId) {
        return pamLoginLogService.selectById(pamLoginLogId);
    }
}
