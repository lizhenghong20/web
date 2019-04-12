package cn.farwalker.standard.modular.member.controller;

import cn.farwalker.standard.core.temp.LogObjectHolder;
import cn.farwalker.waka.core.SuccessTip;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.farwalker.ravv.service.member.basememeber.biz.IMemberBiz;
import cn.farwalker.ravv.service.member.basememeber.model.MemberBo;

/**
 * 会员控制器
 *
 * @author Jason Chen
 * @Date 2018-04-09 08:51:03
 */
@Controller
@RequestMapping("/member")
public class MemberController{

    private String PREFIX = "/member/member/";

    @Autowired
    private IMemberBiz memberService;

    /**
     * 跳转到会员首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "member.html";
    }

    /**
     * 跳转到添加会员
     */
    @RequestMapping("/member_add")
    public String memberAdd() {
        return PREFIX + "member_add.html";
    }

    /**
     * 跳转到修改会员
     */
    @RequestMapping("/member_update/{memberId}")
    public String memberUpdate(@PathVariable Long memberId, Model model) {
        MemberBo member = memberService.selectById(memberId);
        model.addAttribute("item",member);
        LogObjectHolder.me().set(member);
        return PREFIX + "member_edit.html";
    }

    /**
     * 获取会员列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String condition) {
        return memberService.selectList(null);
    }

    /**
     * 新增会员
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add(MemberBo member) {
        memberService.insert(member);
        return new SuccessTip();
    }

    /**
     * 删除会员
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(@RequestParam Integer memberId) {
        memberService.deleteById(memberId);
        return new SuccessTip();
    }

    /**
     * 修改会员
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update(MemberBo member) {
        memberService.updateById(member);
        return new SuccessTip();
    }

    /**
     * 会员详情
     */
    @RequestMapping(value = "/detail/{memberId}")
    @ResponseBody
    public Object detail(@PathVariable("memberId") Integer memberId) {
        return memberService.selectById(memberId);
    }
}
