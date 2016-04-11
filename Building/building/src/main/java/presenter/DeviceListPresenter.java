package presenter;

import com.eastsoft.building.sdk.DataManeger;
import com.ehomeclouds.eastsoft.channel.http.CloudService.Iview;
import com.ehomeclouds.eastsoft.channel.http.api.HttpCloudService;

/**
 * Created by ll on 2016/4/11.
 */
public class DeviceListPresenter {
    private HttpCloudService httpCloudService;

    public DeviceListPresenter(HttpCloudService httpCloudService) {
        this.httpCloudService = httpCloudService;
    }

    public void getGroupDeviceList(long id,int pageNumber,int pageSize,Iview iview){
        httpCloudService.getGroupDeviceList(DataManeger.getInstance().userId,pageNumber,pageSize,id,iview);

    }
}
