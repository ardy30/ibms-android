package com.ehomeclouds.eastsoft.channel.http.CloudService;

/**
 * Created by Admin on 2016/1/16.
 */
public interface Iview {
    public void onSuccess(Object object);
    public void onFailed(String errorStr);
    public  void showProgress(boolean show);



}
