package com.ehomeclouds.eastsoft.channel.mqtt.model;

/**
 * Created by ll on 2016/1/28.
 */
public class MqttConnectStatus {
    public static final int CONNECT_SUCCESS=0;
    public static final int CONNECT_FAILED=1;
    public static final int CONNECT_LOST=2;

    private int connectStatus=CONNECT_FAILED;

    public MqttConnectStatus(int connectStatus) {
        this.connectStatus = connectStatus;
    }

    public int getConnectStatus() {

        return connectStatus;
    }

    public void setConnectStatus(int connectStatus) {
        this.connectStatus = connectStatus;
    }
}
