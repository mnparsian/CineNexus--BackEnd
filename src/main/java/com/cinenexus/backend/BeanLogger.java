package com.cinenexus.backend;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import java.util.Arrays;

@Component
public class BeanLogger implements ApplicationRunner {
    private final ApplicationContext applicationContext;

    public BeanLogger(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public void run(ApplicationArguments args) {
        System.out.println("🔍 لیست تمام Bean های ثبت‌شده در Spring:");
        String[] beanNames = applicationContext.getBeanDefinitionNames();
        Arrays.sort(beanNames);
        for (String beanName : beanNames) {
            System.out.println("👉 " + beanName);
        }
    }
}

