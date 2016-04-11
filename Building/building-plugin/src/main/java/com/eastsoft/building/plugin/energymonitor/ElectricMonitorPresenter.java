package com.eastsoft.building.plugin.energymonitor;

import android.content.Context;

import com.ehomeclouds.eastsoft.channel.http.CloudService.Iview;
import com.ehomeclouds.eastsoft.channel.mqtt.MqttManeger;
import com.ehomeclouds.eastsoft.plugin.presenter.BasePresenter;
import com.ehomeclouds.eastsoft.presenter.util.ConstantUtil;
import com.ehomeclouds.eastsoft.presenter.util.DataManeger;
import com.ehomeclouds.eastsoft.presenter.util.MqttTopicManeger;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by wangyp on 2016/3/29.
 * Created by wangyp on 9:34.
 * This is a built-in template.
 * It contains a code fragment .
 */
public class ElectricMonitorPresenter extends BasePresenter {

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
        Object switchCh1 = pull(deviceDk).get(ConstantUtil.SWITCH_CH1);
        if (switchCh1 != null){
            open = Boolean.parseBoolean(switchCh1.toString());
        }
        return open;
    }

    public boolean ifOverProtected(){
        boolean ifProtected = false;
        Object overProtected = pull(deviceDk).get(ConstantUtil.OVER_PROTECTED);
        if (overProtected != null){
            ifProtected = Boolean.parseBoolean(overProtected.toString());
        }
        return ifProtected;
    }

    public double getElectricity(){
        double electricityValue = 0;
        Object electricity = pull(deviceDk).get(ConstantUtil.ELECTRICITY);
        if (electricity != null){
            electricityValue = Double.parseDouble(electricity.toString());
        }
        return electricityValue;
    }

    public double getVoltage(){
        double voltageValue = 0;
        Object voltage = pull(deviceDk).get(ConstantUtil.VOLTAGE);
        if (voltage != null){
            voltageValue = Double.parseDouble(voltage.toString());
        }
        return voltageValue;
    }

    public double getCurrent(){
        double currentValue = 0;
        Object current = pull(deviceDk).get(ConstantUtil.CURRENT);
        if (current != null){
            currentValue = Double.parseDouble(current.toString());
        }
        return currentValue;
    }

    public double getPower(){
        double powerValue = 0;
        Object power = pull(deviceDk).get(ConstantUtil.POWER);
        if (power != null){
            powerValue = Double.parseDouble(power.toString());
        }
        return powerValue;
    }

    public int getOverVoltage(){
        int overVoltageValue = 0;
        Object overVoltage = pull(deviceDk).get(ConstantUtil.OVER_VOLTAGE);
        if (overVoltage != null){
            overVoltageValue = Integer.parseInt(overVoltage.toString());
        }
        return overVoltageValue;
    }

    public int getUnderVoltage(){
        int underVoltageValue = 0;
        Object underVoltage = pull(deviceDk).get(ConstantUtil.UNDER_VOLTAGE);
        if (underVoltage != null){
            underVoltageValue = Integer.parseInt(underVoltage.toString());
        }
        return underVoltageValue;
    }

    public int getOverCurrent(){
        int underVoltageValue = 0;
        Object underVoltage = pull(deviceDk).get(ConstantUtil.UNDER_VOLTAGE);
        if (underVoltage != null){
            underVoltageValue = Integer.parseInt(underVoltage.toString());
        }
        return underVoltageValue;
    }

    public int getOverPower(){
        int overPowerValue = 0;
        Object overPower = pull(deviceDk).get(ConstantUtil.OVER_POWER);
        if (overPower != null){
            overPowerValue = Integer.parseInt(overPower.toString());
        }
        return overPowerValue;
    }



    public void openMonitor(boolean order){
        JSONObject openMonitorRequest = new JSONObject();
        JSONObject function = new JSONObject();
        try{
            function.put(ConstantUtil.SWITCH_CH1,order);
            openMonitorRequest.put(ConstantUtil.FUNCTION,function);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        MqttManeger.getInstance(context).publish(openMonitorRequest.toString(), MqttTopicManeger.getPubTopic(DataManeger.getInstance().getDomain(),gatewayDk,deviceDk));
    }


    public void enableOverProtect(boolean order){
        JSONObject protectRequest = new JSONObject();
        JSONObject function = new JSONObject();
        try{
            function.put(ConstantUtil.OVER_PROTECTED,order);
            protectRequest.put(ConstantUtil.FUNCTION, function);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        MqttManeger.getInstance(context).publish(protectRequest.toString(), MqttTopicManeger.getPubTopic(DataManeger.getInstance().getDomain(),gatewayDk,deviceDk));
    }

    public void setLimitThreshold(float maxVoltage,float minVoltage,float current,float power){
        JSONObject limitRequest = new JSONObject();
        JSONObject function = new JSONObject();
        try{
            function.put(ConstantUtil.OVER_VOLTAGE,maxVoltage);
            function.put(ConstantUtil.UNDER_VOLTAGE,minVoltage);
            function.put(ConstantUtil.OVER_CURRENT,current);
            function.put(ConstantUtil.OVER_POWER,power);

            limitRequest.put(ConstantUtil.FUNCTION,function);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        MqttManeger.getInstance(context).publish(limitRequest.toString(),MqttTopicManeger.getPubTopic(DataManeger.getInstance().getDomain(),gatewayDk,deviceDk));
    }

    public void clearCurrentElec(){
        JSONObject clearRequest = new JSONObject();
        JSONObject function = new JSONObject();
        try{
            function.put(ConstantUtil.ELECTRICITY,ConstantUtil.ELEC_ZERO);
            clearRequest.put(ConstantUtil.FUNCTION,function);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        MqttManeger.getInstance(context).publish(clearRequest.toString(),MqttTopicManeger.getPubTopic(DataManeger.getInstance().getDomain(),gatewayDk,deviceDk));
    }

}
