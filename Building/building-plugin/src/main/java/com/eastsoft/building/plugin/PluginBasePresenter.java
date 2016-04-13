package com.eastsoft.building.plugin;

import android.util.Log;

import com.eastsoft.building.model.RxBus;
import com.eastsoft.building.sdk.DataManeger;
import com.ehomeclouds.eastsoft.channel.http.CloudService.Iview;
import com.ehomeclouds.eastsoft.channel.mqtt.util.MyTag;

import java.util.Map;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by ll on 2016/4/12.
 */
public class PluginBasePresenter {
    public PluginBasePresenter(final Iview iview, final String deviceKey) {
        super();
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

    //获取配置设备的配置Json数据
//    public Map<String,Object> pullConfigJson(String dk){
//        return DataManeger.getInstance().getDeviceInfoMap().get(dk).getConfig();
//    }
}
