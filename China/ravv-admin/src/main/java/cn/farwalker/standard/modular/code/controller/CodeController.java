//package cn.farwalker.standard.modular.code.controller;
//
//import cn.farwalker.standard.modular.code.factory.DefaultTemplateFactory;
//import cn.farwalker.standard.modular.code.service.TableService;
//import cn.farwalker.waka.core.base.controller.BaseController;
//import cn.farwalker.waka.core.datasource.DruidProperties;
//import cn.farwalker.waka.generator.action.config.WebGeneratorConfig;
//import cn.farwalker.waka.generator.action.model.GenQo;
//import io.swagger.annotations.ApiOperation;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.ResponseBody;
//
///**
// * 代码生成控制器
// *
// * @author Jason Chen
// * @Date 2017年11月30日16:39:19
// */
//@Controller
//@RequestMapping("/code")
//public class CodeController{
//
//    private static String PREFIX = "/code";
//
//    @Autowired
//    private TableService tableService;
//
//    @Autowired
//    private DruidProperties druidProperties;
//
//
//    /**
//     * 跳转到代码生成主页
//     */
//    @RequestMapping("")
//    public String blackboard(Model model) {
//        model.addAttribute("tables", tableService.getAllTables());
//        model.addAttribute("params", DefaultTemplateFactory.getDefaultParams());
//        model.addAttribute("templates", DefaultTemplateFactory.getDefaultTemplates());
//        return PREFIX + "/code.html";
//    }
//
//    /**
//     * 生成代码
//     */
//    @ApiOperation("生成代码")
//    @RequestMapping(value = "/generate", method = RequestMethod.POST)
//    @ResponseBody
//    public Object generate(GenQo genQo) {
//        genQo.setUrl(druidProperties.getUrl());
//        genQo.setUserName(druidProperties.getUsername());
//        genQo.setPassword(druidProperties.getPassword());
//        WebGeneratorConfig webGeneratorConfig = new WebGeneratorConfig(genQo);
//        webGeneratorConfig.doMpGeneration();
//        webGeneratorConfig.doWakaGeneration();
//        return SUCCESS_TIP;
//    }
//}
