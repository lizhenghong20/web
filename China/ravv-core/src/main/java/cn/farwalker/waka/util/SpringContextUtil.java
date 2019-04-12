package cn.farwalker.waka.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class SpringContextUtil implements ApplicationContextAware {



    public SpringContextUtil(){

    }

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    /**
     * @description: 获取applicationContext
     * @param:
     * @return
     * @author Mr.Simple
     * @date 2018/11/14 14:16
     */
    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    /**
     * @description: 通过name返回指定的Bean
     * @param: name
     * @return
     * @author Mr.Simple
     * @date 2018/11/14 14:16
     */
    public Object getBean(String name) {
        return getApplicationContext().getBean(name);
    }

    /**
     * @description: 通过clazz返回指定的Bean
     * @param: clazz,<T>
     * @return
     * @author Mr.Simple
     * @date 2018/11/14 14:16
     */
    public <T> T getBean(Class<T> clazz) {
        return getApplicationContext().getBean(clazz);
    }

    /**
     * @description: 通过name,以及Clazz返回指定的Bean
     * @param: name,clazz,<T>
     * @return
     * @author Mr.Simple
     * @date 2018/11/14 14:16
     */
    public <T> T getBean(String name, Class<T> clazz) {
        return getApplicationContext().getBean(name, clazz);
    }

}
