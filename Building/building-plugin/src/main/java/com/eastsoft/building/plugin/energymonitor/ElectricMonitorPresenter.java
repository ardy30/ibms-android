package com.eastsoft.building.plugin.energymonitor;

import android.content.Context;

import com.eastsoft.building.plugin.PluginBasePresenter;
import com.eastsoft.building.sdk.DataManeger;
import com.eastsoft.building.sdk.KeyUtil;
import com.ehomeclouds.eastsoft.channel.http.CloudService.Iview;
import com.ehomeclouds.eastsoft.channel.mqtt.MqttManeger;
import com.ehomeclouds.eastsoft.channel.mqtt.util.MqttTopicManeger;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by wangyp on 2016/3/29.
 * Created by wangyp on 9:34.
 * This is a built-in template.
 * It contains a code fragment .
 */
public class ElectricMonitorPresenter extends PluginBasePresenter {

    private Context context;
    private String gatewayDk;
    private String deviceDk;


    public ElectricMonitorPresenter(Context context,Iview iview,String dk,String gatewayDk) {
        // 从activity获取参数
        super(iview,dk);
        this.context=context;
        this.gatewayDk=gatewayDk;
        this.deviceDk=dk;
    }

    public boolean isOpen(){
        boolean open = false;
        Object switchCh1 = pull(deviceDk).get(KeyUtil.KEY_SWITCH_CH1);
        if (switchCh1 != null){
            open = Boolean.parseBoolean(switchCh1.toString());
        }
        return open;
    }

    public boolean isOverProtected(){
        boolean ifProtected = false;
        Object overProtected = pull(deviceDk).get(KeyUtil.OVER_PROTECTED);
        if (overProtected != null){
            ifProtected = Boolean.parseBoolean(overProtected.toString());
        }
        return ifProtected;
    }

    public double getElectricity(){
        double electricityValue = 0;
        Object electricity = pull(deviceDk).get(KeyUtil.ELECTRICITY);
        if (electricity != null){
            electricityValue = Double.parseDouble(electricity.toString());
        }
        return electricityValue;
    }

    public double getVoltage(){
        double voltageValue = 0;
        Object voltage = pull(deviceDk).get(KeyUtil.VOLTAGE);
        if (voltage != null){
            voltageValue = Double.parseDouble(voltage.toString());
        }
        return voltageValue;
    }

    public double getCurrent(){
        double currentValue = 0;
        Object current = pull(deviceDk).get(KeyUtil.CURRENT);
        if (current != null){
            currentValue = Double.parseDouble(current.toString());
        }
        return currentValue;
    }

    public double getPower(){
        double powerValue = 0;
        Object power = pull(deviceDk).get(KeyUtil.POWER);
        if (power != null){
            powerValue = Double.parseDouble(power.toString());
        }
        return powerValue;
    }

    public float getOverVoltage(){
        float overVoltageValue = 0;
        Object overVoltage = pull(deviceDk).get(KeyUtil.OVER_VOLTAGE);
        if (overVoltage != null){
            overVoltageValue = Float.parseFloat(overVoltage.toString());
        }
        return overVoltageValue;
    }

    public float getUnderVoltage(){
        float underVoltageValue = 0;
        Object underVoltage = pull(deviceDk).get(KeyUtil.UNDER_VOLTAGE);
        if (underVoltage != null){
            underVoltageValue = Float.parseFloat(underVoltage.toString());
        }
        return underVoltageValue;
    }

    public float getOverCurrent(){
        float underVoltageValue = 0;
        Object underVoltage = pull(deviceDk).get(KeyUtil.UNDER_VOLTAGE);
        if (underVoltage != null){
            underVoltageValue = Float.parseFloat(underVoltage.toString());
        }
        return underVoltageValue;
    }

    public float getOverPower(){
        float overPowerValue = 0;
        Object overPower = pull(deviceDk).get(KeyUtil.OVER_POWER);
        if (overPower != null){
            overPowerValue = Float.parseFloat(overPower.toString());
        }
        return overPowerValue;
    }



    public void on(boolean order){
        JSONObject openMonitorRequest = new JSONObject();
        JSONObject function = new JSONObject();
        try{
            function.put(KeyUtil.KEY_SWITCH_CH1,order);
            openMonitorRequest.put(KeyUtil.FUNCTION,function);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        MqttManeger.getInstance(context).publish(openMonitorRequest.toString(), MqttTopicManeger.getPubTopic(DataManeger.getInstance().brokerDomain, gatewayDk, deviceDk));
    }


    public void enableOverProtect(boolean order){
        JSONObject protectRequest = new JSONObject();
        JSONObject function = new JSONObject();
        try{
            function.put(KeyUtil.OVER_PROTECTED,order);
            protectRequest.put(KeyUtil.FUNCTION, function);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        MqttManeger.getInstance(context).publish(protectRequest.toString(), MqttTopicManeger.getPubTopic(DataManeger.getInstance().brokerDomain,gatewayDk,deviceDk));
    }

    public void setOverV(float maxVoltage){
        JSONObject limitRequest = new JSONObject();
        JSONObject function = new JSONObject();
        try{
            function.put(KeyUtil.OVER_VOLTAGE,maxVoltage);


            limitRequest.put(KeyUtil.FUNCTION,function);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        MqttManeger.getInstance(context).publish(limitRequest.toString(),MqttTopicManeger.getPubTopic(DataManeger.getInstance().brokerDomain,gatewayDk,deviceDk));
    }
    public void setUnderV(float minVoltage){
        JSONObject limitRequest = new JSONObject();
        JSONObject function = new JSONObject();
        try{
            function.put(KeyUtil.UNDER_VOLTAGE,minVoltage);

            limitRequest.put(KeyUtil.FUNCTION,function);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        MqttManeger.getInstance(context).publish(limitRequest.toString(),MqttTopicManeger.getPubTopic(DataManeger.getInstance().brokerDomain,gatewayDk,deviceDk));
    }
    public void setOverA(float current ){
        JSONObject limitRequest = new JSONObject();
        JSONObject function = new JSONObject();
        try{
            function.put(KeyUtil.OVER_CURRENT,current);

            limitRequest.put(KeyUtil.FUNCTION,function);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        MqttManeger.getInstance(context).publish(limitRequest.toString(),MqttTopicManeger.getPubTopic(DataManeger.getInstance().brokerDomain,gatewayDk,deviceDk));
    }
    public void setOverp(float power){
        JSONObject limitRequest = new JSONObject();
        JSONObject function = new JSONObject();
        try{
            function.put(KeyUtil.OVER_POWER,power);

            limitRequest.put(KeyUtil.FUNCTION,function);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        MqttManeger.getInstance(context).publish(limitRequest.toString(),MqttTopicManeger.getPubTopic(DataManeger.getInstance().brokerDomain,gatewayDk,deviceDk));
    }




}
