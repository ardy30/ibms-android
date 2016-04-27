package com.ehomeclouds.eastsoft.channel.mqtt.util;

/**
 * Created by ll on 2016/1/28.
 */
public class MqttTopicManeger {
    public static String getPubTopic(String domain,String groupId,String deviceId){
        return domain+"/"+groupId+"/"+deviceId+"/"+"command";
    }

    public static String getSubTopic(String domain, String groupId){
        return domain+"/"+groupId+"/+";
    }

    private static String getSubGroupTopic(String domain, String groupId){
        return domain+"/"+groupId+"/"+"+";
    }

}
