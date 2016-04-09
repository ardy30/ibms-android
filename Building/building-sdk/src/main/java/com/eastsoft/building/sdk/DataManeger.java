package com.eastsoft.building.sdk;

import com.eastsoft.building.model.DeviceType;
import com.ehomeclouds.eastsoft.channel.http.response.AreaInfo;
import com.ehomeclouds.eastsoft.channel.http.response.DeviceInfo;
import com.ehomeclouds.eastsoft.channel.http.response.DeviceTypeInfo;
import com.ehomeclouds.eastsoft.channel.http.response.ScenarioInfo;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by ll on 2016/3/31.
 */
public class DataManeger {
    public String brokerUrl;
    public String brokerUsername;
    public String  brokerPassword;
    public String brokerDomain;
    public long userId;
    private static DataManeger dataManeger=null;
    public   ArrayList<ScenarioInfo> scenarioInfoArrayList=new ArrayList<>();
    public   ArrayList<AreaInfo> areaInfoArrayList=new ArrayList<>();
    public   ArrayList<DeviceTypeInfo> deviceTypeArrayList=new ArrayList<>();
    public   ArrayList<DeviceInfo> deviceInfoArrayList=new ArrayList<>();
    public HashMap<String,DeviceInfo> deviceInfoMap=new HashMap();


    public static  DataManeger getInstance(){
        if (dataManeger==null){
            dataManeger= new DataManeger();
        }
        return dataManeger;
    }
}
