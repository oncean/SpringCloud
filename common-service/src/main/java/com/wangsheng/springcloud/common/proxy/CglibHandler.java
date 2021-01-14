package com.wangsheng.springcloud.common.proxy;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class CglibHandler implements MethodInterceptor {


    public static Object bind(Class superclass){
        Enhancer enhancer =new Enhancer();
        enhancer.setSuperclass(superclass);
        enhancer.setCallback(new CglibHandler());
        return enhancer.create();
    }


    public static void main(String[] args) {
        Bus bus = (Bus) CglibHandler.bind(Bus.class);
        bus.openDoor();
        while (true){

        }
    }

    @Override
    public Object intercept(Object o, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        System.out.println("cglib动态大礼");
        Object object = methodProxy.invokeSuper(o,args);
        return object;
    }

}
