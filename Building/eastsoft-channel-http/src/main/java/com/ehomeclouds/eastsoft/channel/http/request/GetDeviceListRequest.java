package com.ehomeclouds.eastsoft.channel.http.request;

/**
 * Created by ll on 2016/3/31.
 */
public class GetDeviceListRequest extends BaseRequest {

    public long areaId;
    public  String deviceTypeCode;

    public GetDeviceListRequest(long userId,long areaId, String deviceTypeCode) {
        this.userId=userId;
        this.areaId = areaId;
        this.deviceTypeCode = deviceTypeCode;
    }
}
