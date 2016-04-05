package com.ehomeclouds.eastsoft.channel.http.request;

/**
 * Created by ll on 2016/3/31.
 */
public class StartScenarioRequest  {
    public long profileId;
    public long userId;

    public StartScenarioRequest(long userId,long profileId) {
        this.profileId = profileId;
        this.userId = userId;
    }
}
