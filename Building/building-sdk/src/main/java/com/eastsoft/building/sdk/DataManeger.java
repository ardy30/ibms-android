package com.eastsoft.building.sdk;

import android.annotation.TargetApi;
import android.os.Build;
import android.util.ArrayMap;

import com.eastsoft.building.model.DeviceType;
import com.ehomeclouds.eastsoft.channel.http.response.AreaInfo;
import com.ehomeclouds.eastsoft.channel.http.response.DeviceInfo;
import com.ehomeclouds.eastsoft.channel.http.response.DeviceTypeInfo;
import com.ehomeclouds.eastsoft.channel.http.response.ScenarioInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ll on 2016/3/31.
 */
public class DataManeger {
    public String brokerUrl;
    public String brokerUsername;
    public String  brokerPassword;
    public String brokerDomain;
    public long userId;
    public static final int PAGESIZE=13;
    private static DataManeger dataManeger=null;
    public   ArrayList<ScenarioInfo> scenarioInfoArrayList=new ArrayList<>();
    public   AreaInfo[] areaInfoArrayList;
    public   DeviceTypeInfo[] deviceTypeArrayList;
    public HashMap<String,DeviceInfo> deviceInfoMap=new HashMap();
    public ArrayMap<String,String> gatewayMap=new ArrayMap<>();


    public static  DataManeger getInstance(){
        if (dataManeger==null){
            dataManeger= new DataManeger();
        }
        return dataManeger;
    }
    public static void destroy(){
        dataManeger=null;
    }
}
