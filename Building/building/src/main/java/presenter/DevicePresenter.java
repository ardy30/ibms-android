package presenter;

import android.content.Context;

import com.eastsoft.building.sdk.DataManeger;
import com.eastsoft.building.sdk.KeyUtil;
import com.ehomeclouds.eastsoft.channel.http.CloudService.Iview;
import com.ehomeclouds.eastsoft.channel.http.api.HttpCloudService;
import com.ehomeclouds.eastsoft.channel.http.response.AreaInfo;
import com.ehomeclouds.eastsoft.channel.http.response.DeviceInfo;
import com.ehomeclouds.eastsoft.channel.http.response.DeviceTypeInfo;
import com.ehomeclouds.eastsoft.channel.mqtt.MqttManeger;
import com.ehomeclouds.eastsoft.channel.mqtt.util.MqttTopicManeger;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ll on 2016/4/6.
 */
public class DevicePresenter {

    protected HttpCloudService httpCloudService;

    public DevicePresenter(HttpCloudService httpCloudService) {
        this.httpCloudService = httpCloudService;
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
    public void getDeviceList(long areaId, String typeId,int pageNumber,int pageSize, final Iview iview) {
        httpCloudService.getDeviceList(DataManeger.getInstance().userId, areaId, typeId, pageNumber,pageSize,new Iview() {
            @Override
            public void onSuccess(Object object) {
               DataManeger.getInstance().deviceInfoArrayList=(ArrayList<DeviceInfo>) object;
                for (DeviceInfo deviceInfo:DataManeger.getInstance().deviceInfoArrayList){

                    DataManeger.getInstance().deviceInfoMap.put(deviceInfo.device_key,deviceInfo);
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
}
