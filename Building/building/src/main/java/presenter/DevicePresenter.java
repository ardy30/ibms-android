package presenter;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.eastsoft.building.model.DeviceType;
import com.eastsoft.building.model.RxBus;
import com.eastsoft.building.plugin.PluginLoad;
import com.eastsoft.building.plugin.UpdateView;
import com.eastsoft.building.sdk.DataManeger;
import com.eastsoft.building.sdk.IntentUtil;
import com.eastsoft.building.sdk.KeyUtil;
import com.ehomeclouds.eastsoft.channel.http.CloudService.Iview;
import com.ehomeclouds.eastsoft.channel.http.api.HttpCloudService;
import com.ehomeclouds.eastsoft.channel.http.base.Util.StringStaticUtils;
import com.ehomeclouds.eastsoft.channel.http.response.AreaInfo;
import com.ehomeclouds.eastsoft.channel.http.response.DeviceInfo;
import com.ehomeclouds.eastsoft.channel.http.response.DeviceTypeInfo;
import com.ehomeclouds.eastsoft.channel.mqtt.MqttManeger;
import com.ehomeclouds.eastsoft.channel.mqtt.model.MqttConnectStatus;
import com.ehomeclouds.eastsoft.channel.mqtt.model.MqttData;
import com.ehomeclouds.eastsoft.channel.mqtt.util.MqttTopicManeger;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by ll on 2016/4/6.
 */
public class DevicePresenter {

    HashMap<String, Object> typeMap = new HashMap<String, Object>() {
        {
            put(DeviceType.EASTSOFT_DEVICE_FOUR_WAY_SWITCH_CATEGORY, "");
            put(DeviceType.EASTSOFT_DEVICE_BODY_INDUCTOR_CATEGORY, "");
            put(DeviceType.EASTSOFT_DEVICE_ELECTRICAL_ENERGY_MONITOR_CATEGORY, "");
            put(DeviceType.EASTSOFT_DEVICE_BRIGHT_LIGHT_CATEGROY, "");
            put(DeviceType.EASTSOFT_DEVICE_LCD_CATEGORY,"");
        }
    };
    HashMap<String, Object> detailTypeMap = new HashMap<String, Object>() {
        {
            put(DeviceType.EASTSOFT_DEVICE_BODY_INDUCTOR_CATEGORY, "");
            put(DeviceType.EASTSOFT_DEVICE_LCD_CATEGORY,"");

        }
    };
    public static HashMap<String, Object> switchTypeMap = new HashMap<String, Object>() {
        {
            put(DeviceType.EASTSOFT_DEVICE_FOUR_WAY_SWITCH_CATEGORY, "");
            put(DeviceType.EASTSOFT_DEVICE_ELECTRICAL_ENERGY_MONITOR_CATEGORY, "");


        }
    };
    HashMap<String, Object> vdeviceMap = new HashMap<String, Object>() {
        {
            put(DeviceType.EASTSOFT_DEVICE_FOUR_WAY_SWITCH_CATEGORY, "");
        }
    };
    public static  HashMap<Long, String> channelMap = new HashMap<Long, String>() {
        {
            put(1L,KeyUtil.KEY_SWITCH_CH1);
            put(2L,KeyUtil.KEY_SWITCH_CH2);
            put(3L,KeyUtil.KEY_SWITCH_CH3);
            put(4L,KeyUtil.KEY_SWITCH_CH4);
        }
    };
    protected HttpCloudService httpCloudService;

    public DevicePresenter(HttpCloudService httpCloudService, final Iview iview) {
        this.httpCloudService = httpCloudService;
        RxBus.getDefault().toObserverable(MqttData.class).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<MqttData>() {
            @Override
            public void call(MqttData mqttData) {
                handleControl(mqttData.getDeviceDk(), mqttData.getPayload(),iview);
                UpdateView updateView=new UpdateView(mqttData.getDeviceDk());
                RxBus.getDefault().post(updateView);

            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                Log.e(KeyUtil.KEY_TAG, "mqtt throwable");
            }
        });
    }

    public ArrayList<DeviceInfo> getMyDevice() {
        ArrayList<DeviceInfo> mtList = new ArrayList<>();
        for (DeviceInfo deviceInfo : DataManeger.getInstance().deviceInfoArrayList) {
            if (typeMap.containsKey(deviceInfo.device_type_code.substring(0, 7))) {
                mtList.add(deviceInfo);
            }
        }
        return mtList;
    }


    public void handleControl(String dk, String paload,Iview iview) {
        try {
            JSONObject jsonObject = new JSONObject(paload);
            for (DeviceInfo device :DataManeger.getInstance().deviceInfoArrayList) {
                if (device.device_key.equals(dk)) {
//                    if (device.device_type_code.substring(0,7).equals(DeviceType.EASTSOFT_DEVICE_FOUR_WAY_SWITCH_CATEGORY)){
//
//                    }
                    if (!jsonObject.isNull(KeyUtil.FUNCTION)) {
                        JSONObject functions = jsonObject.getJSONObject(KeyUtil.FUNCTION);
                        Iterator<String> keys = functions.keys();
                        while (keys.hasNext()) {
                            String key = keys.next();
                            device.paramMap.put(key, functions.get(key));
                        }
                    }
                    DataManeger.getInstance().deviceInfoMap.put(device.device_key, device);

                }
            }

            iview.onSuccess("");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void publishSwitch(Context context, String dk,long cha, boolean on) {
        long channel = 1;
        DeviceInfo deviceInfo = DataManeger.getInstance().deviceInfoMap.get(dk);
        if (vdeviceMap.containsKey(deviceInfo.device_type_code.substring(0, 7))) {
            channel = cha;
        }
        JSONObject deviceJson = new JSONObject();

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(channelMap.get(channel), on);
            deviceJson.put(KeyUtil.FUNCTION,jsonObject);
            deviceJson.put(KeyUtil.DEVICE_KEY,dk);
            deviceJson.put(KeyUtil.CMD,KeyUtil.CMD_WRITE);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        String topic = MqttTopicManeger.getPubTopic(DataManeger.getInstance().brokerDomain, deviceInfo.gateway_device_key, deviceInfo.device_key);

        MqttManeger.getInstance(context).publish(deviceJson.toString(), topic);
        Toast.makeText(context,"下发成功",Toast.LENGTH_LONG).show();
    }


    public void getArea(final Iview iview) {
        httpCloudService.getAreaList(DataManeger.getInstance().userId, new Iview() {
            @Override
            public void onSuccess(Object object) {
//                iview.onSuccess("");
                DataManeger.getInstance().areaInfoArrayList = (AreaInfo[]) object;
//                getType(iview);
            }

            @Override
            public void onFailed(String errorStr) {
                iview.onFailed(errorStr);

            }

            @Override
            public void showProgress(boolean show) {

            }
        });

    }

    public void getType(final Iview iview) {
        httpCloudService.getDeviceTypeList(DataManeger.getInstance().userId, new Iview() {
            @Override
            public void onSuccess(Object object) {
                DataManeger.getInstance().deviceTypeArrayList = (DeviceTypeInfo[]) object;
                iview.onSuccess("");
            }

            @Override
            public void onFailed(String errorStr) {
                iview.onFailed(errorStr);
            }

            @Override
            public void showProgress(boolean show) {

            }
        });


    }

    public void getDeviceList(final Context context, long areaId, String typeId, int pageNumber, int pageSize, final Iview iview) {

        httpCloudService.getDeviceList(DataManeger.getInstance().userId, areaId, typeId, pageNumber, pageSize, new Iview() {
            @Override
            public void onSuccess(Object object) {
                DataManeger.getInstance().deviceInfoArrayList.clear();
                DataManeger.getInstance().deviceInfoArrayList.addAll((ArrayList<DeviceInfo>) object);

                for (DeviceInfo deviceInfo : DataManeger.getInstance().deviceInfoArrayList) {

                    String param = deviceInfo.param;
                    if (param!=null&&!param.equals("")){
                        try {
                            JSONObject jsonObject = new JSONObject(param);
                            Iterator<String> keys = jsonObject.keys();
                            while (keys.hasNext()) {
                                String key = keys.next();
                                deviceInfo.paramMap.put(key, jsonObject.get(key));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }



                    DataManeger.getInstance().deviceInfoMap.put(deviceInfo.device_key, deviceInfo);
//                    if(switchTypeMap.containsKey(deviceInfo.device_type_code.substring(0,7))){

                        MqttManeger.getInstance(context).subscribe(MqttTopicManeger.getSubTopic(DataManeger.getInstance().brokerDomain,
                                deviceInfo.gateway_device_key,
                                deviceInfo.device_key));
//                    }
                }


                iview.onSuccess("");
            }

            @Override
            public void onFailed(String errorStr) {

                iview.onFailed(errorStr);
            }

            @Override
            public void showProgress(boolean show) {

            }
        });

    }

    public void connectBroker(Context context) {

        MqttManeger.getInstance(context).connect(DataManeger.getInstance().brokerUrl, DataManeger.getInstance().brokerUsername, DataManeger.getInstance().brokerPassword);

    }

    public boolean showDetail(DeviceInfo deviceInfo) {
        if (detailTypeMap.containsKey(deviceInfo.device_type_code.substring(0, 7))) {
            return true;
        }
        return false;
    }

    public void startPluginActivity(String dk) {
        DeviceInfo deviceInfo=DataManeger.getInstance().deviceInfoMap.get(dk);
//        PluginLoad.load(deviceInfo);

    }
}
