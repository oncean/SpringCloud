package com.wangsheng.springcloud.common.proxy;

public class Bus implements Car{

    private String name = "Bus";

    @Override
    public void openDoor() {
        System.out.println("打开小汽车的车门");
    }

    @Override
    public String getName(){
        return  this.name;
    }
}
