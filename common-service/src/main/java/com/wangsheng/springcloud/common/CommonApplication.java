package com.wangsheng.springcloud.common;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CommonApplication  implements InitializingBean, BeanPostProcessor {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context  = new AnnotationConfigApplicationContext();
        context.register(CommonApplication.class);
        context.refresh();
        context.getBean("A");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("bean init");
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName)  {
        System.out.println("bean post before");
        return bean;
    }
}
