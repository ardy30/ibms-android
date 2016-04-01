package com.eastsoft.building.sdk;

/**
 * Created by ll on 2016/3/31.
 */
public class DataManeger {
    public String brokerUrl;
    public String brokerUsername;
    public String  brokerPassword;
    public String brokerDomain;
    private static DataManeger dataManeger=null;

    public static  DataManeger getInstance(){
        if (dataManeger==null){
            dataManeger= new DataManeger();
        }
        return dataManeger;
    }
}
