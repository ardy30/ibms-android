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


    public ElectricMonitorPresenter(Context context, Iview iview, String dk, String gatewayDk) {
        // 从activity获取参数
        super(context,iview, dk,gatewayDk);
        this.context = context;
        this.gatewayDk = gatewayDk;
        this.deviceDk = dk;
    }






}
