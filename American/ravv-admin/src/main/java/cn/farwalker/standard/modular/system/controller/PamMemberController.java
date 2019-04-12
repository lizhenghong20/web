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

import cn.farwalker.ravv.service.member.pam.member.biz.IPamMemberBiz;
import cn.farwalker.ravv.service.member.pam.member.model.PamMemberBo;

/**
 * PAM_会员登录账号控制器
 *
 * @author Jason Chen
 * @Date 2018-04-09 08:57:58
 */
@Controller
@RequestMapping("/pamMember")
public class PamMemberController {

    private String PREFIX = "/system/pamMember/";

    @Autowired
    private IPamMemberBiz pamMemberService;

    /**
     * 跳转到PAM_会员登录账号首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "pamMember.html";
    }

    /**
     * 跳转到添加PAM_会员登录账号
     */
    @RequestMapping("/pamMember_add")
    public String pamMemberAdd() {
        return PREFIX + "pamMember_add.html";
    }

    /**
     * 跳转到修改PAM_会员登录账号
     */
    @RequestMapping("/pamMember_update/{pamMemberId}")
    public String pamMemberUpdate(@PathVariable Long pamMemberId, Model model) {
        PamMemberBo pamMember = pamMemberService.selectById(pamMemberId);
        model.addAttribute("item",pamMember);
        LogObjectHolder.me().set(pamMember);
        return PREFIX + "pamMember_edit.html";
    }

    /**
     * 获取PAM_会员登录账号列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String condition) {
        return pamMemberService.selectList(null);
    }

    /**
     * 新增PAM_会员登录账号
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add(PamMemberBo pamMember) {
        pamMemberService.insert(pamMember);
        return new SuccessTip();
    }

    /**
     * 删除PAM_会员登录账号
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(@RequestParam Integer pamMemberId) {
        pamMemberService.deleteById(pamMemberId);
        return new SuccessTip();
    }

    /**
     * 修改PAM_会员登录账号
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update(PamMemberBo pamMember) {
        pamMemberService.updateById(pamMember);
        return new SuccessTip();
    }

    /**
     * PAM_会员登录账号详情
     */
    @RequestMapping(value = "/detail/{pamMemberId}")
    @ResponseBody
    public Object detail(@PathVariable("pamMemberId") Integer pamMemberId) {
        return pamMemberService.selectById(pamMemberId);
    }
}
