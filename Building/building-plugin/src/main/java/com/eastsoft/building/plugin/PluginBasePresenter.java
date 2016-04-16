package com.eastsoft.building.plugin;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.eastsoft.building.model.RxBus;
import com.eastsoft.building.sdk.DataManeger;
import com.eastsoft.building.sdk.KeyUtil;
import com.ehomeclouds.eastsoft.channel.http.CloudService.Iview;
import com.ehomeclouds.eastsoft.channel.mqtt.MqttManeger;
import com.ehomeclouds.eastsoft.channel.mqtt.util.MqttTopicManeger;
import com.ehomeclouds.eastsoft.channel.mqtt.util.MyTag;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by ll on 2016/4/12.
 */
public class PluginBasePresenter {
    private String deviceDk;
    private String gatewayDk;
    private Context context;
    public PluginBasePresenter(Context context,final Iview iview, final String deviceKey,final String gatewayDk) {
        super();
        this.context=context;
        this.deviceDk=deviceKey;
        this.gatewayDk=gatewayDk;
        RxBus.getDefault().toObserverable(UpdateView.class).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<UpdateView>() {
            @Override
            public void call(UpdateView updateView) {
                Log.d(MyTag.TAG, "plugin rec mqttdata");
                String dk = updateView.deviceKey;
                if (deviceKey.equals(dk)) {
                    iview.onSuccess("");
                }
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {

                Log.d("subscription",throwable.toString());

            }
        });

    }
    public Map<String ,Object> pull(String dk){
        return DataManeger.getInstance().deviceInfoMap.get(dk).paramMap;
    }
    public boolean isOpen() {
        boolean open = false;
        Object switchCh1 = pull(deviceDk).get(KeyUtil.KEY_SWITCH_CH1);
        if (switchCh1 != null) {
            open = Boolean.parseBoolean(switchCh1.toString());
        }
        return open;
    }
    public boolean isOverProtected() {
        boolean ifProtected = false;
        Object overProtected = pull(deviceDk).get(KeyUtil.OVER_PROTECTED);
        if (overProtected != null) {
            ifProtected = Boolean.parseBoolean(overProtected.toString());
        }
        return ifProtected;
    }

    public double getElectricity() {
        double electricityValue = 0;
        Object electricity = pull(deviceDk).get(KeyUtil.ELECTRICITY);
        if (electricity != null) {
            electricityValue = Double.parseDouble(electricity.toString());
        }
        return electricityValue;
    }

    public double getVoltage() {
        double voltageValue = 0;
        Object voltage = pull(deviceDk).get(KeyUtil.VOLTAGE);
        if (voltage != null) {
            voltageValue = Double.parseDouble(voltage.toString());
        }
        return voltageValue;
    }

    public double getCurrent() {
        double currentValue = 0;
        Object current = pull(deviceDk).get(KeyUtil.CURRENT);
        if (current != null) {
            currentValue = Double.parseDouble(current.toString());
        }
        return currentValue;
    }

    public double getPower() {
        double powerValue = 0;
        Object power = pull(deviceDk).get(KeyUtil.POWER);
        if (power != null) {
            powerValue = Double.parseDouble(power.toString());
        }
        return powerValue;
    }

    public float getOverVoltage() {
        float overVoltageValue = 0;
        Object overVoltage = pull(deviceDk).get(KeyUtil.OVER_VOLTAGE);
        if (overVoltage != null) {
            overVoltageValue = Float.parseFloat(overVoltage.toString());
        }
        return overVoltageValue;
    }

    public float getUnderVoltage() {
        float underVoltageValue = 0;
        Object underVoltage = pull(deviceDk).get(KeyUtil.UNDER_VOLTAGE);
        if (underVoltage != null) {
            underVoltageValue = Float.parseFloat(underVoltage.toString());
        }
        return underVoltageValue;
    }

    public float getOverCurrent() {
        float underVoltageValue = 0;
        Object underVoltage = pull(deviceDk).get(KeyUtil.UNDER_VOLTAGE);
        if (underVoltage != null) {
            underVoltageValue = Float.parseFloat(underVoltage.toString());
        }
        return underVoltageValue;
    }

    public float getOverPower() {
        float overPowerValue = 0;
        Object overPower = pull(deviceDk).get(KeyUtil.OVER_POWER);
        if (overPower != null) {
            overPowerValue = Float.parseFloat(overPower.toString());
        }
        return overPowerValue;
    }


    public void on(boolean order) {
        JSONObject openMonitorRequest = new JSONObject();
        JSONObject function = new JSONObject();
        try {
            function.put(KeyUtil.KEY_SWITCH_CH1, order);
            openMonitorRequest.put(KeyUtil.FUNCTION, function);
            openMonitorRequest.put(KeyUtil.DEVICE_KEY, deviceDk);
            openMonitorRequest.put(KeyUtil.CMD,KeyUtil.CMD_WRITE);

            publishMessage(openMonitorRequest.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    public void publishMessage(String paload){
        MqttManeger.getInstance(context).publish(paload, MqttTopicManeger.getPubTopic(DataManeger.getInstance().brokerDomain, gatewayDk, deviceDk));
        showSuccessToast();
    }


    public void showSuccessToast(){
        Toast.makeText(context, "下发成功", Toast.LENGTH_LONG).show();

    }
    public void enableOverProtect(boolean order) {
        JSONObject protectRequest = new JSONObject();
        JSONObject function = new JSONObject();
        try {
            function.put(KeyUtil.OVER_PROTECTED, order);
            protectRequest.put(KeyUtil.FUNCTION, function);
            protectRequest.put(KeyUtil.DEVICE_KEY, deviceDk);
            protectRequest.put(KeyUtil.CMD, KeyUtil.CMD_WRITE);
            publishMessage(protectRequest.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void setOverV(float maxVoltage) {
        JSONObject limitRequest = new JSONObject();
        JSONObject function = new JSONObject();
        try {
            function.put(KeyUtil.OVER_VOLTAGE, maxVoltage);


            limitRequest.put(KeyUtil.FUNCTION, function);
            limitRequest.put(KeyUtil.DEVICE_KEY, deviceDk);
            limitRequest.put(KeyUtil.CMD,KeyUtil.CMD_WRITE);
            publishMessage(limitRequest.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void setUnderV(float minVoltage) {
        JSONObject limitRequest = new JSONObject();
        JSONObject function = new JSONObject();
        try {
            function.put(KeyUtil.UNDER_VOLTAGE, minVoltage);

            limitRequest.put(KeyUtil.FUNCTION, function);
            limitRequest.put(KeyUtil.DEVICE_KEY, deviceDk);
            limitRequest.put(KeyUtil.CMD,KeyUtil.CMD_WRITE);

            publishMessage(limitRequest.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void setOverA(float current) {
        JSONObject limitRequest = new JSONObject();
        JSONObject function = new JSONObject();
        try {
            function.put(KeyUtil.OVER_CURRENT, current);

            limitRequest.put(KeyUtil.FUNCTION, function);
            limitRequest.put(KeyUtil.DEVICE_KEY, deviceDk);
            limitRequest.put(KeyUtil.CMD,KeyUtil.CMD_WRITE);

            publishMessage(limitRequest.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void setOverp(float power) {
        JSONObject limitRequest = new JSONObject();
        JSONObject function = new JSONObject();
        try {
            function.put(KeyUtil.OVER_POWER, power);

            limitRequest.put(KeyUtil.FUNCTION, function);
            limitRequest.put(KeyUtil.DEVICE_KEY, deviceDk);
            limitRequest.put(KeyUtil.CMD,KeyUtil.CMD_WRITE);

            publishMessage(limitRequest.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void refresh() {
        JSONObject limitRequest = new JSONObject();
        JSONObject function = new JSONObject();
        try {
            function.put(KeyUtil.ELECTRICITY, 0.1);
            function.put(KeyUtil.VOLTAGE, 0.1);
            function.put(KeyUtil.CURRENT, 0.1);
            function.put(KeyUtil.POWER, 0.1);

            limitRequest.put(KeyUtil.FUNCTION, function);
            limitRequest.put(KeyUtil.DEVICE_KEY, deviceDk);
            limitRequest.put(KeyUtil.CMD,KeyUtil.CMD_READ);
            publishMessage(limitRequest.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
