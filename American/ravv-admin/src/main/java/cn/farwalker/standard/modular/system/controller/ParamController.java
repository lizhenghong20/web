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

import cn.farwalker.ravv.service.sys.param.biz.ISysParamBiz;
import cn.farwalker.ravv.service.sys.param.model.SysParamBo;


/**
 * 系统参数控制器
 *
 * @author Jason Chen
 * @Date 2018-02-10 18:44:40
 */
@Controller
@RequestMapping("/param")
public class ParamController{

    private String PREFIX = "/system/param/";

    @Autowired
    private ISysParamBiz paramService;

    /**
     * 跳转到系统参数首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "param.html";
    }

    /**
     * 跳转到添加系统参数
     */
    @RequestMapping("/param_add")
    public String paramAdd() {
        return PREFIX + "param_add.html";
    }

    /**
     * 跳转到修改系统参数
     */
    @RequestMapping("/param_update/{paramId}")
    public String paramUpdate(@PathVariable Integer paramId, Model model) {
        SysParamBo param = paramService.selectById(paramId);
        model.addAttribute("item",param);
        LogObjectHolder.me().set(param);
        return PREFIX + "param_edit.html";
    }

    /**
     * 获取系统参数列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String condition) {
        return paramService.selectList(null);
    }

    /**
     * 新增系统参数
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add(SysParamBo param) {
        paramService.insert(param);
        return new SuccessTip();
    }

    /**
     * 删除系统参数
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(@RequestParam Integer paramId) {
        paramService.deleteById(paramId);
        return new SuccessTip();
    }

    /**
     * 修改系统参数
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update(SysParamBo param) {
        paramService.updateById(param);
        return new SuccessTip();
    }

    /**
     * 系统参数详情
     */
    @RequestMapping(value = "/detail/{paramId}")
    @ResponseBody
    public Object detail(@PathVariable("paramId") Integer paramId) {
        return paramService.selectById(paramId);
    }
}
