package com.wangsheng.springcloud.common.Zookeeper.impl;

import com.wangsheng.springcloud.common.Zookeeper.Lock;

public class LockImpl  implements Lock {


    public getZooKeeper(){

    }

    @Override
    public boolean lock(String guidNodeName, String clientGuid) {

        boolean result = false;

        if (getZooKeeper().exists(guidNodeName, false) == null) {

            getZooKeeper().create(guidNodeName, clientGuid.getBytes(),

                    ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);

            byte[] data = getZooKeeper().getData(guidNodeName, false, null);

            if (data != null && clientGuid.equals(new String(data))) {

                result = true;

            }

        }

        return result;

    }

    @Override
    public boolean unlock() {
        return false;
    }

    @Override
    public boolean lock(String guidNodeName, String clientGuid) {
        return false;
    }

    @Override
    public boolean release(String guidNodeName, String clientGuid) {
        return false;
    }

    @Override
    public boolean exists(String guidNodeName) {
        return false;
    }
}
