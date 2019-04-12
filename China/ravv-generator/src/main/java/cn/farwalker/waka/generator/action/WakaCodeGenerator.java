package cn.farwalker.waka.generator.action;


import cn.farwalker.waka.generator.action.config.WakaGeneratorConfig;

/**
 * 代码生成器,可以生成实体,dao,service,controller,html,js
 *
 * @author Jason Chen
 * @Date 2017/5/21 12:38
 */
public class WakaCodeGenerator {

    public static void main(String[] args) {

        /**
         * Mybatis-Plus的代码生成器:
         *      mp的代码生成器可以生成实体,mapper,mapper对应的xml,service
         */
        WakaGeneratorConfig wakaGeneratorConfig = new WakaGeneratorConfig();
        wakaGeneratorConfig.doMpGeneration();

        /**
         * waka的生成器:
         *      waka的代码生成器可以生成controller,html页面,页面对应的js
         */
        wakaGeneratorConfig.doWakaGeneration();
    }

}