package com.eastsoft.building.plugin;

import android.content.Context;
import android.content.Intent;

import com.eastsoft.building.model.DeviceType;
import com.eastsoft.building.plugin.bodyinfrared.BodyInfraredActivity;
import com.eastsoft.building.plugin.bodyinfrared.BodyInfraredPresenter;
import com.eastsoft.building.plugin.energymonitor.ElectricMonitorActivity;
import com.eastsoft.building.sdk.KeyUtil;
import com.ehomeclouds.eastsoft.channel.http.response.DeviceInfo;

/**
 * Created by ll on 2016/4/11.
 */
public class PluginLoad {
    public static void load(Context context,DeviceInfo deviceInfo){
        if (deviceInfo.device_type_code.substring(0,7).equals(DeviceType.EASTSOFT_DEVICE_ELECTRICAL_ENERGY_MONITOR_CATEGORY)){
            Intent intent=new Intent(context, ElectricMonitorActivity.class);
            intent.putExtra(KeyUtil.DEVICE_KEY,deviceInfo.device_key);
            context.startActivity(intent);
        }else if (deviceInfo.device_type_code.substring(0,7).equals(DeviceType.EASTSOFT_DEVICE_BODY_INDUCTOR_CATEGORY)){
            Intent intent=new Intent(context, BodyInfraredActivity.class);
            intent.putExtra(KeyUtil.DEVICE_KEY,deviceInfo.device_key);
            context.startActivity(intent);
        }

    }
}
