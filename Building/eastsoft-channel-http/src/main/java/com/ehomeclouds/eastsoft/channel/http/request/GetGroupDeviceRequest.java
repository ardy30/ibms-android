package com.ehomeclouds.eastsoft.channel.http.request;

import com.ehomeclouds.eastsoft.channel.http.response.BaseResponse;

/**
 * Created by ll on 2016/3/31.
 */
public class GetGroupDeviceRequest extends BaseRequest {
    public long groupId;

    public GetGroupDeviceRequest(long userId, long groupId) {
        this.userId = userId;
        this.groupId = groupId;
    }
}
