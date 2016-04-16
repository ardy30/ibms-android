package com.eastsoft.building.plugin.bodyinfrared;

import android.content.Context;
import android.content.Intent;

import com.eastsoft.building.plugin.PluginBasePresenter;
import com.eastsoft.building.sdk.DataManeger;
import com.eastsoft.building.sdk.KeyUtil;
import com.ehomeclouds.eastsoft.channel.http.CloudService.Iview;
import com.ehomeclouds.eastsoft.channel.mqtt.MqttManeger;
import com.ehomeclouds.eastsoft.channel.mqtt.util.MqttTopicManeger;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.PublicKey;

/**
 * Created by 王熙斌 on 2016/4/13.
 */
public class BodyInfraredPresenter extends PluginBasePresenter {

    private Context context;
    private String gatewayDk;
    private String deviceDk;


    public BodyInfraredPresenter(Context context,Iview iview,String dk,String gatewayDk) {
        // 从activity获取参数
        super(context,iview,dk,gatewayDk);
        this.context=context;
        this.gatewayDk=gatewayDk;
        this.deviceDk=dk;
    }

    public int getLight(){
        double light = 0;
        Object lightObj = pull(deviceDk).get(KeyUtil.LIGHT);
        if (lightObj != null){
            light = Double.parseDouble(lightObj.toString());
        }
        return (int) light;
    }
    public int getCloseDelay(){
        double light = 0;
        Object lightObj = pull(deviceDk).get(KeyUtil.DELAY_TIME);
        if (lightObj != null){
            light = Double.parseDouble(lightObj.toString());
        }
        return (int) light;
    }
    public int getOpen(){
        double light = 0;
        Object lightObj = pull(deviceDk).get(KeyUtil.LIGHT_OPEN_THRESHOLD);
        if (lightObj != null){
            light =  Double.parseDouble(lightObj.toString());
        }
        return (int) light;
    }
    public int getClose(){
        double light = 0;
        Object lightObj = pull(deviceDk).get(KeyUtil.LIGHT_CLOSE_THRESHOLD);
        if (lightObj != null){
            light =  Double.parseDouble(lightObj.toString());
        }
        return (int) light;
    }

    public boolean hasPeople(){
        boolean ifProtected = false;
        Object overProtected = pull(deviceDk).get(KeyUtil.PEOPLE);
        if (overProtected != null){
            ifProtected = Boolean.parseBoolean(overProtected.toString());
        }
        return ifProtected;
    }

    public int getSensity(){
        Object lightObj = pull(deviceDk).get(KeyUtil.LIGHT_INDUCTION_SENSITY);
        if (lightObj != null){
            String sensor ;
            sensor = lightObj.toString();
            if (sensor.equals(KeyUtil.LIGHT_INDUCTION_SENSITY_LOW)){
                return LOW;
            }else if (sensor.equals(KeyUtil.LIGHT_INDUCTION_SENSITY_MIDDLE)){
                return MIDDLE;
            }else if (sensor.equals(KeyUtil.LIGHT_INDUCTION_SENSITY_HIGH)){
                return HIGH;
            }
        }
        return 0;
    }
    public static final int LOW=0;
    public static final int MIDDLE=1;
    public static final int HIGH=2;

    public void send(int delay,int open,int close){
        JSONObject limitRequest = new JSONObject();
        JSONObject function = new JSONObject();
        try{
            function.put(KeyUtil.DELAY_TIME,delay);
            function.put(KeyUtil.LIGHT_OPEN_THRESHOLD,open);
            function.put(KeyUtil.LIGHT_CLOSE_THRESHOLD,close);

            limitRequest.put(KeyUtil.FUNCTION,function);
            limitRequest.put(KeyUtil.DEVICE_KEY,deviceDk);
            limitRequest.put(KeyUtil.CMD,KeyUtil.CMD_WRITE);

            publishMessage(limitRequest.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void setSensor(String sensor) {
        JSONObject limitRequest = new JSONObject();
        JSONObject function = new JSONObject();
        try{

            function.put(KeyUtil.LIGHT_INDUCTION_SENSITY,sensor);

            limitRequest.put(KeyUtil.FUNCTION, function);
            limitRequest.put(KeyUtil.DEVICE_KEY,deviceDk);
            limitRequest.put(KeyUtil.CMD,KeyUtil.CMD_WRITE);

            publishMessage(limitRequest.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
