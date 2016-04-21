package com.ehomeclouds.eastsoft.channel.mqtt.model;

/**
 * Created by ll on 2016/4/18.
 */
public class PublishState {
    public static final int FAILED=0;
    public static final int SUCCESS=1;

    public int publishState=SUCCESS;
    public PublishState(int publishState) {
        this.publishState=publishState;
    }
}
