package com.ehomeclouds.eastsoft.channel.http.request;

/**
 * Created by ll on 2016/3/31.
 */
public class GetScenarioDeviceRequest extends BaseRequest {
    public long profileId;

    public GetScenarioDeviceRequest(long userId, long profileId) {
        this.userId = userId;
        this.profileId = profileId;
    }

}
