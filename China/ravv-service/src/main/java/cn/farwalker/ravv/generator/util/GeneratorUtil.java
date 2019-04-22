package cn.farwalker.ravv.generator.util;

import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.rules.DbType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;

public class GeneratorUtil {

    private static String tableName1 = "base_test";

    public static void main(String[] args) {
        String packageName = "cn.farwalker.ravv.service.test";
        generatorByTables(packageName, tableName1);
    }

    private static void generatorByTables(String packageName, String... tableNames){
        // 全局配置
        GlobalConfig config = new GlobalConfig();
        config.setActiveRecord(true)
                .setAuthor("Mr.Simple")
                .setOutputDir("E:\\ravv_amazon\\ravv-service\\src\\main\\java")//这里填写项目的真实路径
                .setMapperName("%sDao")//这里重写Dao命名
                .setIdType(IdType.ID_WORKER)
                .setEnableCache(false)
                .setFileOverride(true);

        //数据源配置
        String dbUrl = "jdbc:mysql://47.107.236.246:3306/ravv?autoReconnect=true&useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=false";
        String username = "root";
        String password = "Ravv_123";
        String drivername = "com.mysql.jdbc.Driver";
        DataSourceConfig dataSourceConfig = new DataSourceConfig();
        dataSourceConfig.setDbType(DbType.MYSQL)
                .setUrl(dbUrl)
                .setUsername(username)
                .setPassword(password)
                .setDriverName(drivername);

        //策略配置
        StrategyConfig strategyConfig = new StrategyConfig();
        strategyConfig
                .setCapitalMode(true)
                .setEntityLombokModel(false)
                .setDbColumnUnderline(true)
                .setNaming(NamingStrategy.underline_to_camel)
                .setInclude(tableNames);//修改替换成你需要的表名，多个表名传数组


        new AutoGenerator().setGlobalConfig(config)
                .setDataSource(dataSourceConfig)
                .setStrategy(strategyConfig)
                .setPackageInfo(
                        new PackageConfig()
                                .setParent(packageName)
                                .setEntity("model")
                                .setMapper("dao")
                                .setXml("dao")
                                .setService("biz")
                                .setServiceImpl("biz.impl")

                ).execute();
    }

}
