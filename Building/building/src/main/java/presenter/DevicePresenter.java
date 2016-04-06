package presenter;

import com.eastsoft.building.sdk.DataManeger;
import com.eastsoft.building.sdk.KeyUtil;
import com.ehomeclouds.eastsoft.channel.http.CloudService.Iview;
import com.ehomeclouds.eastsoft.channel.http.api.HttpCloudService;
import com.ehomeclouds.eastsoft.channel.http.response.AreaInfo;
import com.ehomeclouds.eastsoft.channel.http.response.DeviceInfo;
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

    public void publishSwitch(int pos, boolean on) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(KeyUtil.KEY_SWITCH_CH1, on);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        DeviceInfo deviceInfo = deviceInfoArrayList.get(pos);
        String topic = MqttTopicManeger.getPubTopic(DataManeger.getInstance().brokerDomain, deviceInfo.gateway_device_key, deviceInfo.device_key);

        MqttManeger.getInstance(getActivity()).publish(jsonObject.toString(), topic);
    }

    public void getData() {

    }

    public void getDeviceList(long areaId, String typeId) {
        httpCloudService.getDeviceList(DataManeger.getInstance().userId, areaId, typeId, new Iview() {
            @Override
            public void onSuccess(Object object) {
                ArrayList<DeviceInfo> deviceInfoArrayList = (ArrayList<DeviceInfo>) object;
                update(deviceInfoArrayList);
            }

            @Override
            public void onFailed(String errorStr) {

            }

            @Override
            public void showProgress(boolean show) {

            }
        });

    }

    public void getArea() {
        final List<String> areaNames = new ArrayList<>();
        for (AreaInfo areaInfo : DataManeger.getInstance().areaInfoArrayList) {
            areaNames.add(areaInfo.area_name);
        }
        return areaNames;
    }
}
