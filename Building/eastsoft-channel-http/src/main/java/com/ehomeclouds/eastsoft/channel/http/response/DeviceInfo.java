package com.ehomeclouds.eastsoft.channel.http.response;

import java.util.HashMap;

/**
 * Created by ll on 2016/3/31.
 */
public class DeviceInfo {

    public String device_type_code;
    public String area_name;
    public long area_id;
    public String device_name;
    public String device_key;

    public String gateway_device_key;
    public long id;
    public long profile_id;
    public long  channel=1;

    public String type_name;
    public String state;//开关状态,对开关类设备：on表示开状态，off表示关状态。

    public String param;//设备状态json信息，broker上报的设备信息中function全部，包含设备的详细信息。

    public HashMap<String,Object> paramMap=new HashMap<>();
}
