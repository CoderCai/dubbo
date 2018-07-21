package com.neo.test;

public class DubboTestServiceImpl implements DubboTestService {
    @Override
    public String sayHello(String name) {
        System.out.println("interface invoke start.");
        return "Hello, " + name;
    }
}
