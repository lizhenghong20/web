//package cn.farwalker.waka.config;
//
//import java.sql.SQLException;
//import java.util.HashMap;
//
//import org.mybatis.spring.annotation.MapperScan;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import com.alibaba.druid.pool.DruidDataSource;
//import com.baomidou.mybatisplus.mapper.MetaObjectHandler;
//import com.baomidou.mybatisplus.plugins.OptimisticLockerInterceptor;
//import com.baomidou.mybatisplus.plugins.PaginationInterceptor;
//
//import cn.farwalker.waka.common.constant.DatasourceEnum;
//import cn.farwalker.waka.core.WakaMetaObjectHandler;
//import cn.farwalker.waka.core.datascope.DataScopeInterceptor;
//import cn.farwalker.waka.core.datasource.DruidProperties;
//import cn.farwalker.waka.core.mutidatasource.DynamicDataSource;
//import cn.farwalker.waka.core.mutidatasource.config.MutiDataSourceProperties;
//
///**
// * MybatisPlus配置
// *
// * @author Jason Chen
// * @Date 2017年8月23日12:51:41
// */
//@Configuration
//@MapperScan(basePackages = {"cn.farwalker.standard.modular.*.dao"
//		,"cn.farwalker.standard.common.persistence.dao"
//		,"cn.farwalker.standard.rest.*.dao"
//		, "cn.farwalker.standard.rest.common.persistence.dao"
//		,"cn.farwalker.ravv.service.**.dao"})
//public class MybatisPlusConfig {
//
//	@Autowired
//    DruidProperties druidProperties;
//
//    @Autowired
//    MutiDataSourceProperties mutiDataSourceProperties;
//
//
//    /**
//     * 另一个数据源
//     */
//    private DruidDataSource bizDataSource() {
//        DruidDataSource dataSource = new DruidDataSource();
//        druidProperties.config(dataSource);
//        mutiDataSourceProperties.config(dataSource);
//        return dataSource;
//    }
//
//    /**
//     * waka的数据源
//     */
//    private DruidDataSource dataSourceWaka(){
//        DruidDataSource dataSource = new DruidDataSource();
//        druidProperties.config(dataSource);
//        return dataSource;
//    }
//
//    /**
//     * 单数据源连接池配置
//     */
//    @Bean
//    @ConditionalOnProperty(prefix = "waka", name = "muti-datasource-open", havingValue = "false")
//    public DruidDataSource singleDatasource() {
//        return dataSourceWaka();
//    }
//
//    /**
//     * 多数据源连接池配置
//     */
//    @Bean
//    @ConditionalOnProperty(prefix = "waka", name = "muti-datasource-open", havingValue = "true")
//    public DynamicDataSource mutiDataSource() {
//
//        DruidDataSource dataSourceWaka = dataSourceWaka();
//        DruidDataSource bizDataSource = bizDataSource();
//
//        try {
//            dataSourceWaka.init();
//            bizDataSource.init();
//        }catch (SQLException sql){
//            sql.printStackTrace();
//        }
//
//        DynamicDataSource dynamicDataSource = new DynamicDataSource();
//        HashMap<Object, Object> hashMap = new HashMap<>();
//        hashMap.put(DatasourceEnum.DATA_SOURCE_GUNS, dataSourceWaka);
//        hashMap.put(DatasourceEnum.DATA_SOURCE_BIZ, bizDataSource);
//        dynamicDataSource.setTargetDataSources(hashMap);
//        dynamicDataSource.setDefaultTargetDataSource(dataSourceWaka);
//        return dynamicDataSource;
//    }
//
//	/**
//	 * 公共字段自动填写
//	 */
//	@Bean
//	public MetaObjectHandler metaObjectHandler() {
//		return new WakaMetaObjectHandler();
//	}
//
//    /**
//     * mybatis-plus分页插件
//     */
//    @Bean
//    public PaginationInterceptor paginationInterceptor() {
//        return new PaginationInterceptor();
//    }
//
//    /**
//     * 数据范围mybatis插件
//     */
//    @Bean
//    public DataScopeInterceptor dataScopeInterceptor() {
//        return new DataScopeInterceptor();
//    }
//
//    /**
//     * 乐观锁mybatis插件
//     */
//    @Bean
//    public OptimisticLockerInterceptor optimisticLockerInterceptor() { return new OptimisticLockerInterceptor(); }
//}
