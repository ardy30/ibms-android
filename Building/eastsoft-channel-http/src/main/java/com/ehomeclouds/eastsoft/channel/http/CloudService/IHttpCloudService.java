package com.ehomeclouds.eastsoft.channel.http.CloudService;

import com.ehomeclouds.eastsoft.channel.http.base.LoginRequest;
import com.ehomeclouds.eastsoft.channel.http.base.LoginResponse;
import com.ehomeclouds.eastsoft.channel.http.request.BaseRequest;
import com.ehomeclouds.eastsoft.channel.http.request.CtrlGroupRequest;
import com.ehomeclouds.eastsoft.channel.http.request.GetAreaRequest;
import com.ehomeclouds.eastsoft.channel.http.request.GetDeviceListRequest;
import com.ehomeclouds.eastsoft.channel.http.request.GetDeviceTypeRequest;
import com.ehomeclouds.eastsoft.channel.http.request.GetGroupDeviceRequest;
import com.ehomeclouds.eastsoft.channel.http.request.GetGroupListRequest;
import com.ehomeclouds.eastsoft.channel.http.request.GetScenarioDeviceRequest;
import com.ehomeclouds.eastsoft.channel.http.request.GetScenarioListRequest;
import com.ehomeclouds.eastsoft.channel.http.request.StartScenarioRequest;
import com.ehomeclouds.eastsoft.channel.http.response.AreaInfo;
import com.ehomeclouds.eastsoft.channel.http.response.BaseResponse;
import com.ehomeclouds.eastsoft.channel.http.response.DeviceTypeInfo;
import com.ehomeclouds.eastsoft.channel.http.response.GetAreaListResponse;
import com.ehomeclouds.eastsoft.channel.http.response.GetDeviceListResponse;
import com.ehomeclouds.eastsoft.channel.http.response.GetDeviceTypeResponse;
import com.ehomeclouds.eastsoft.channel.http.response.GetGroupDeviceResponse;
import com.ehomeclouds.eastsoft.channel.http.response.GetGroupListResponse;
import com.ehomeclouds.eastsoft.channel.http.response.GetScenarioDeviceResponse;
import com.ehomeclouds.eastsoft.channel.http.response.GetScenarioListResponse;
import com.ehomeclouds.eastsoft.channel.http.response.VoidResponse;

import java.security.PublicKey;

import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.PUT;

/**
 * Created by ll on 2016/1/14.
 */
public  interface IHttpCloudService {

    @POST("mobile/login")//登录
    public Call<LoginResponse> loginIn(@Body LoginRequest loginRequest);


    @POST("mobile /qryGroup")//查询组列表
    public Call<GetGroupListResponse> getGroupList(@Body GetGroupListRequest getGroupListRequest);

    @POST("mobile/doAllCtrl")//控制组开 组关
    public Call<VoidResponse> ctrlGroup(@Body CtrlGroupRequest ctrlGroupRequest);


    @POST("mobile/qryGroupDevice")//获取某个组下的设备列表
    public Call<GetGroupDeviceResponse> getGroupDeviceList(@Body GetGroupDeviceRequest getGroupDeviceRequest);


    @POST("mobile/qryProfile")//获取情景模式列表
    public Call<GetScenarioListResponse> getScenarioList(@Body GetScenarioListRequest getScenarioListRequest);

    @POST("mobile/qryProfileDevice")//获取某个情景模式下的设备列表
    public Call<GetScenarioDeviceResponse> getScenarioDeviceList(@Body GetScenarioDeviceRequest getScenarioDeviceRequest);

    @POST("mobile/doProfile")//启动情景模式
    public Call<VoidResponse> startScenario(@Body StartScenarioRequest startScenarioRequest);

    @POST("mobile/qryDeviceType")//获取设备类型列表
    public Call<DeviceTypeInfo[] > getDeviceTypeList(@Body GetDeviceTypeRequest getDeviceTypeRequest);

    @POST("mobile/qryAreaInfo")//获取区域列表
    public Call<AreaInfo[]> getAreaList(@Body GetAreaRequest baseRequest);

    @POST("/mobile/qryDeviceByArea")//获取设备列表
    public Call<GetDeviceListResponse> getDeviceList(@Body GetDeviceListRequest  getDeviceListRequest);




}
