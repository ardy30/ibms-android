package com.eastsoft.building.sdk;

import com.eastsoft.building.model.DeviceType;
import com.ehomeclouds.eastsoft.channel.http.response.AreaInfo;
import com.ehomeclouds.eastsoft.channel.http.response.DeviceTypeInfo;
import com.ehomeclouds.eastsoft.channel.http.response.ScenarioInfo;

import java.util.ArrayList;

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
    public static ArrayList<ScenarioInfo> scenarioInfoArrayList=new ArrayList<>();
    public static ArrayList<AreaInfo> areaInfoArrayList=new ArrayList<>();
    public static ArrayList<DeviceTypeInfo> deviceTypeArrayList=new ArrayList<>();


    public static  DataManeger getInstance(){
        if (dataManeger==null){
            dataManeger= new DataManeger();
        }
        return dataManeger;
    }
}
