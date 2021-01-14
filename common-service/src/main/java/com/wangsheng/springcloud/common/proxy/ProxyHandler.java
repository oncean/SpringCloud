package com.wangsheng.springcloud.common.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class ProxyHandler implements InvocationHandler {

    private Object car;

    public Object bind(Object car){
        this.car = car;
        return Proxy.newProxyInstance(car.getClass().getClassLoader(),car.getClass().getInterfaces(),this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("代理car"+method.getName());
        method.invoke(car,args);
        return null;
    }

    public static void main(String[] args) {
        ProxyHandler carHandler = new ProxyHandler();
        Car car = (Car) carHandler.bind(new Bus());
        car.openDoor();
    }
}
