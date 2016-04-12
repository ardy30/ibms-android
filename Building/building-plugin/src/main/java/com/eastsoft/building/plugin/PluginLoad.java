package com.eastsoft.building.plugin;

import android.content.Context;
import android.content.Intent;

import com.eastsoft.building.model.DeviceType;
import com.ehomeclouds.eastsoft.channel.http.response.DeviceInfo;

/**
 * Created by ll on 2016/4/11.
 */
public class PluginLoad {
    public static void load(Context context,DeviceInfo deviceInfo){
        if (deviceInfo.device_type_code.substring(0,7).equals(DeviceType.EASTSOFT_DEVICE_ELECTRICAL_ENERGY_MONITOR_CATEGORY)){
//            context.startActivity(new Intent(context,Mon));
        }

    }
}
