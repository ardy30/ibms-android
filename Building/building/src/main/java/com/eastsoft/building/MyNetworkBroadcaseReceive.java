package com.eastsoft.building;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.eastsoft.building.sdk.DataManeger;
import com.eastsoft.building.sdk.UtilityInfo;
import com.ehomeclouds.eastsoft.channel.mqtt.MqttManeger;

/**
 * Created by Admin on 2016/2/19.
 */
public class MyNetworkBroadcaseReceive extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
      if (UtilityInfo.networkIsConnected(context)){
          MqttManeger.getInstance(context).connect(DataManeger.getInstance().brokerUrl, DataManeger.getInstance().brokerUsername,DataManeger.getInstance().brokerPassword);
      }

    }


}
