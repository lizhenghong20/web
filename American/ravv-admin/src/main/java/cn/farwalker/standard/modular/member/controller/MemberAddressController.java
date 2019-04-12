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

import cn.farwalker.ravv.service.member.address.biz.IMemberAddressBiz;
import cn.farwalker.ravv.service.member.address.model.MemberAddressBo;

/**
 * 会员地址控制器
 *
 * @author Jason Chen
 * @Date 2018-04-09 08:51:13
 */
@Controller
@RequestMapping("/memberAddress")
public class MemberAddressController{

    private String PREFIX = "/member/memberAddress/";

    @Autowired
    private IMemberAddressBiz memberAddressService;

    /**
     * 跳转到会员地址首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "memberAddress.html";
    }

    /**
     * 跳转到添加会员地址
     */
    @RequestMapping("/memberAddress_add")
    public String memberAddressAdd() {
        return PREFIX + "memberAddress_add.html";
    }

    /**
     * 跳转到修改会员地址
     */
    @RequestMapping("/memberAddress_update/{memberAddressId}")
    public String memberAddressUpdate(@PathVariable Long memberAddressId, Model model) {
        MemberAddressBo memberAddress = memberAddressService.selectById(memberAddressId);
        model.addAttribute("item",memberAddress);
        LogObjectHolder.me().set(memberAddress);
        return PREFIX + "memberAddress_edit.html";
    }

    /**
     * 获取会员地址列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String condition) {
        return memberAddressService.selectList(null);
    }

    /**
     * 新增会员地址
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add(MemberAddressBo memberAddress) {
        memberAddressService.insert(memberAddress);
        return new SuccessTip();
    }

    /**
     * 删除会员地址
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(@RequestParam Integer memberAddressId) {
        memberAddressService.deleteById(memberAddressId);
        return new SuccessTip();
    }

    /**
     * 修改会员地址
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update(MemberAddressBo memberAddress) {
        memberAddressService.updateById(memberAddress);
        return new SuccessTip();
    }

    /**
     * 会员地址详情
     */
    @RequestMapping(value = "/detail/{memberAddressId}")
    @ResponseBody
    public Object detail(@PathVariable("memberAddressId") Integer memberAddressId) {
        return memberAddressService.selectById(memberAddressId);
    }
}
