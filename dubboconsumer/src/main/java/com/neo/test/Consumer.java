package com.neo.test;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Consumer {
    public static void main(String[] args) {
        System.setProperty("java.net.preferIPv4Stack", "true");
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[] {"META-INF/dubbo-consumer.xml"});
        context.start();
        DubboTestService demoService = (DubboTestService)context.getBean("dubboTestService");
        String result = demoService.sayHello("Neo");
        System.out.println(result);
    }
}
