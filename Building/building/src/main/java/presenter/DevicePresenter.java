package presenter;

import android.content.Context;
import android.util.Log;

import com.eastsoft.building.model.RxBus;
import com.eastsoft.building.sdk.DataManeger;
import com.eastsoft.building.sdk.KeyUtil;
import com.ehomeclouds.eastsoft.channel.http.CloudService.Iview;
import com.ehomeclouds.eastsoft.channel.http.api.HttpCloudService;
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
import java.util.Iterator;
import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by ll on 2016/4/6.
 */
public class DevicePresenter {

    protected HttpCloudService httpCloudService;

    public DevicePresenter(HttpCloudService httpCloudService) {
        this.httpCloudService = httpCloudService;
        RxBus.getDefault().toObserverable(MqttData.class).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<MqttData>() {
            @Override
            public void call(MqttData mqttData) {
                handleControl(mqttData.getPayload());
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                Log.e(KeyUtil.KEY_TAG,"mqtt throwable");
            }
        });
    }
    public void handleControl(String paload) {
        try {
            JSONObject jsonObject=new JSONObject(paload);
            for (DeviceInfo device : DataManeger.getInstance().deviceInfoMap.values()) {
                if (device.device_key.equals(jsonObject.getString(KeyUtil.DEVICE_KEY))) {
                    if (!jsonObject.isNull(KeyUtil.FUNCTION)) {
                        JSONObject functions = jsonObject.getJSONObject(KeyUtil.FUNCTION);
                        Iterator<String> keys = functions.keys();
                        while (keys.hasNext()) {
                            String key = keys.next();
                            device.paramMap.put(key, functions.get(key));
                        }
                    }
                    DataManeger.getInstance().deviceInfoMap.put(device.device_key, device);
                    break;
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void publishSwitch(Context context,String dk, boolean on) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(KeyUtil.KEY_SWITCH_CH1, on);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        DeviceInfo deviceInfo = DataManeger.getInstance().deviceInfoMap.get(dk);
        String topic = MqttTopicManeger.getPubTopic(DataManeger.getInstance().brokerDomain, deviceInfo.gateway_device_key, deviceInfo.device_key);

        MqttManeger.getInstance(context).publish(jsonObject.toString(), topic);
    }


    public void getArea(final Iview iview) {
        httpCloudService.getAreaList(DataManeger.getInstance().userId, new Iview() {
            @Override
            public void onSuccess(Object object) {
//                iview.onSuccess("");
                DataManeger.getInstance().areaInfoArrayList = (ArrayList<AreaInfo>) object;
                getType(iview);
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
    public void getType(final  Iview iview){
        httpCloudService.getDeviceTypeList(DataManeger.getInstance().userId, new Iview() {
            @Override
            public void onSuccess(Object object) {
                DataManeger.getInstance().deviceTypeArrayList = (ArrayList<DeviceTypeInfo>) object;
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
    public void getDeviceList(final Context context,long areaId, String typeId,int pageNumber,int pageSize, final Iview iview) {
        httpCloudService.getDeviceList(DataManeger.getInstance().userId, areaId, typeId, pageNumber, pageSize, new Iview() {
            @Override
            public void onSuccess(Object object) {
                DataManeger.getInstance().deviceInfoArrayList = (ArrayList<DeviceInfo>) object;
                for (DeviceInfo deviceInfo : DataManeger.getInstance().deviceInfoArrayList) {

                    String param=deviceInfo.param;
                    try {
                        JSONObject jsonObject=new JSONObject(param);
                        Iterator<String> keys=jsonObject.keys();
                        while (keys.hasNext()){
                            String key=keys.next();
                            deviceInfo.paramMap.put(key,jsonObject.get(key));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    DataManeger.getInstance().deviceInfoMap.put(deviceInfo.device_key, deviceInfo);
                    MqttManeger.getInstance(context).subscribe(MqttTopicManeger.getSubTopic(DataManeger.getInstance().brokerDomain,
                            deviceInfo.gateway_device_key,
                            deviceInfo.device_key));
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

    public void connectBroker(Context context){
        MqttManeger.getInstance(context).connect(DataManeger.getInstance().brokerUrl, DataManeger.getInstance().brokerUsername, DataManeger.getInstance().brokerPassword);

    }
}
