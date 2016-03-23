package com.ehomeclouds.eastsoft.channel.mqtt.model;

/**
 * Created by Admin on 2015/12/26.
 */
public class MqttData {
    private String topic;
    private String payload;
    private String groupStr;
    private String deviceDk;

    public String getGroupStr() {
        return groupStr;
    }

    public void setGroupStr(String groupStr) {
        this.groupStr = groupStr;
    }

    public String getDeviceDk() {
        return deviceDk;
    }

    public void setDeviceDk(String deviceDk) {
        this.deviceDk = deviceDk;
    }

    public MqttData() {
    }

    public MqttData(String topic, String payload) {

        this.topic = topic;
        this.payload = payload;
    }

    public String getTopic() {

        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

}
