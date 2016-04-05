package com.ehomeclouds.eastsoft.channel.http.request;

/**
 * Created by ll on 2016/3/31.
 */
public class CtrlGroupRequest {
    public long groupId;
    public long userId;
    public boolean ctrlLabel;

    public CtrlGroupRequest( long userId,long groupId, boolean ctrlLabel) {
        this.groupId = groupId;
        this.userId = userId;
        this.ctrlLabel = ctrlLabel;
    }
}
