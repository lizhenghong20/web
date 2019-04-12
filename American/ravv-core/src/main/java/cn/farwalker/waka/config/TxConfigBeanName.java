//package cn.farwalker.waka.config;
//
//import java.util.Properties;
//
//import org.springframework.aop.framework.autoproxy.BeanNameAutoProxyCreator;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.jdbc.datasource.DataSourceTransactionManager;
//import org.springframework.transaction.interceptor.TransactionInterceptor;
//
//@Configuration
//public class TxConfigBeanName {
//  private static final String REQUIRED = "PROPAGATION_REQUIRED,-Throwable",READONLY=REQUIRED + ",readOnly";
//  private static final String BEAN_NAME="txAdvice";
//  @Autowired
//  private DataSourceTransactionManager transactionManager;
//  // 创建事务通知
//  @Bean(name = BEAN_NAME)
//  public TransactionInterceptor getAdvisor() throws Exception {
//
//    Properties attr = new Properties();
//
//    attr.setProperty("add*",REQUIRED);
//    attr.setProperty("create*",REQUIRED);
//    attr.setProperty("delete*",REQUIRED);
//    attr.setProperty("save*",REQUIRED);
//    attr.setProperty("insert*",REQUIRED);
//    attr.setProperty("update*",REQUIRED);
//    attr.setProperty("find*",READONLY);
//
//    TransactionInterceptor tsi = new TransactionInterceptor(transactionManager,attr);
//    return tsi;
//  }
//  @Bean
//  public BeanNameAutoProxyCreator txProxy() {
//    BeanNameAutoProxyCreator creator = new BeanNameAutoProxyCreator();
//    creator.setInterceptorNames(BEAN_NAME);
//    creator.setBeanNames("*BizImpl", "*ServiceImpl");
//    creator.setProxyTargetClass(true);
//    return creator;
//  }
//}