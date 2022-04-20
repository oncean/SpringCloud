package com.wangsheng.springcloud.common.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class ProxyHandler implements InvocationHandler {

    private Object object;

    public Object bind(Object object){
        this.object = object;
        return Proxy.newProxyInstance(object.getClass().getClassLoader(),object.getClass().getInterfaces(),this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("代理car"+method.getName());
        method.invoke(object,args);
        return null;
    }

    public static void main(String[] args) {
        ProxyHandler carHandler = new ProxyHandler();
        Car car = (Car) carHandler.bind(new Bus());
        car.openDoor();
    }
}
