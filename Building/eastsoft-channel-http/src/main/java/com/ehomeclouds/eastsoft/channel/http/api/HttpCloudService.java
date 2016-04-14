package com.ehomeclouds.eastsoft.channel.http.api;

import android.content.Context;
import android.util.Log;

import com.eastsoft.building.model.RxBus;
import com.ehomeclouds.eastsoft.channel.http.CloudService.IHttpCloudService;
import com.ehomeclouds.eastsoft.channel.http.CloudService.Iview;
import com.ehomeclouds.eastsoft.channel.http.base.HttpCloudServiceBase;
import com.ehomeclouds.eastsoft.channel.http.base.LoginRequest;
import com.ehomeclouds.eastsoft.channel.http.base.LoginResponse;


import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.sql.DatabaseMetaData;
import java.util.ArrayList;

import com.ehomeclouds.eastsoft.channel.http.R;
import com.ehomeclouds.eastsoft.channel.http.base.Util.GenerateUtils;
import com.ehomeclouds.eastsoft.channel.http.base.Util.StringStaticUtils;
import com.ehomeclouds.eastsoft.channel.http.base.protocol.DecodeCloudHeader;
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

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by ll on 2015-06-03.
 */
public class HttpCloudService extends HttpCloudServiceBase {

    private Context context;
    private IHttpCloudService iHttpCloudService;

    public HttpCloudService(Context ctx) {
        this.context = ctx;
        try {
            iHttpCloudService = getRetrofit(ctx).create(IHttpCloudService.class);
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (UnrecoverableKeyException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }
    }

    public IHttpCloudService getiHttpCloudService() {
        return iHttpCloudService;
    }

    public void loginIn(final String userId, String psd, final Iview iview) {
        LoginRequest loginRequest = new LoginRequest(userId, psd);
        final Call<LoginResponse> call = iHttpCloudService.loginIn(loginRequest);
        final Subscription subscription = RxBus.getDefault().toObserverable(Cancel.class).subscribe(new Action1<Cancel>() {
            @Override
            public void call(Cancel cancel) {

                call.cancel();
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                Log.e(HttpCloudService.class.getName(), "error");
            }
        });
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Response<LoginResponse> response) {
                subscription.unsubscribe();
                if (response.errorBody() != null) {
                    iview.onFailed(getErrorString());
                } else {

                    LoginResponse loginResponse = response.body();
                    String resultCode = DecodeCloudHeader.decodeHeader(response.headers(), StringStaticUtils.RESULT_CODE, context);

                    if (resultCode.equals(StringStaticUtils.RESULT_CODE_SUCCESS)) {
                        iview.onSuccess(loginResponse);
                    } else {
                        iview.onFailed(getResuleCodeString(resultCode));
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {
                subscription.unsubscribe();

                iview.onFailed(getFailedString());
            }
        });


    }

    public void getGroupList(long userId, int pageNumber, int pageSize, final Iview iview) {
        GetGroupListRequest getGroupListRequest = new GetGroupListRequest(userId);
        getGroupListRequest.pageNumber = pageNumber;
        getGroupListRequest.pageSize = pageSize;
        final Call<GetGroupListResponse> call = iHttpCloudService.getGroupList(getGroupListRequest);
        final Subscription subscription = RxBus.getDefault().toObserverable(Cancel.class).subscribe(new Action1<Cancel>() {
            @Override
            public void call(Cancel cancel) {

                call.cancel();
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                Log.e(HttpCloudService.class.getName(), "error");
            }
        });
        call.enqueue(new Callback<GetGroupListResponse>() {
            @Override
            public void onResponse(Response<GetGroupListResponse> response) {
                subscription.unsubscribe();

                if (response.errorBody() != null) {
                    iview.onFailed(getErrorString());
                } else {


                    GetGroupListResponse resp = response.body();
                    String resultCode = DecodeCloudHeader.decodeHeader(response.headers(), StringStaticUtils.RESULT_CODE, context);

                    if (resultCode.equals(StringStaticUtils.RESULT_CODE_SUCCESS)) {
                        iview.onSuccess(resp.list);
                    } else {
                        iview.onFailed(getResuleCodeString(resultCode));
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {
                subscription.unsubscribe();

                iview.onFailed(getFailedString());
            }
        });


    }

    public void ctrlGroup(long userId, long groupId, boolean on, final Iview iview) {


        CtrlGroupRequest ctrlGroupRequest = new CtrlGroupRequest(userId, groupId, on);
        final Call<VoidResponse> call = iHttpCloudService.ctrlGroup(ctrlGroupRequest);
        final Subscription subscription = RxBus.getDefault().toObserverable(Cancel.class).subscribe(new Action1<Cancel>() {
            @Override
            public void call(Cancel cancel) {

                call.cancel();
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                Log.e(HttpCloudService.class.getName(), "error");
            }
        });
        call.enqueue(new Callback<VoidResponse>() {
            @Override
            public void onResponse(Response<VoidResponse> response) {
                subscription.unsubscribe();

                if (response.errorBody() != null) {
                    iview.onFailed(getErrorString());
                } else {
                    String resultCode = DecodeCloudHeader.decodeHeader(response.headers(), StringStaticUtils.RESULT_CODE, context);

                    if (resultCode.equals(StringStaticUtils.RESULT_CODE_SUCCESS)) {
                        iview.onSuccess("");
                    } else {
                        iview.onFailed(getResuleCodeString(resultCode));
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {
                subscription.unsubscribe();

                iview.onFailed(getFailedString());
            }
        });

    }

    public void getGroupDeviceList(long userId, int pageNumber, int pageSize, long groupId, final Iview iview) {

        GetGroupDeviceRequest getGroupDeviceRequest = new GetGroupDeviceRequest(userId, groupId);
        getGroupDeviceRequest.pageNumber = pageNumber;
        getGroupDeviceRequest.pageSize = pageSize;
        final Call<GetGroupDeviceResponse> call = iHttpCloudService.getGroupDeviceList(getGroupDeviceRequest);
        final Subscription subscription = RxBus.getDefault().toObserverable(Cancel.class).subscribe(new Action1<Cancel>() {
            @Override
            public void call(Cancel cancel) {

                call.cancel();
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                Log.e(HttpCloudService.class.getName(), "error");
            }
        });
        call.enqueue(new Callback<GetGroupDeviceResponse>() {
            @Override
            public void onResponse(Response<GetGroupDeviceResponse> response) {
                subscription.unsubscribe();

                if (response.errorBody() != null) {
                    iview.onFailed(getErrorString());
                } else {

                    GetGroupDeviceResponse resp = response.body();
                    String resultCode = DecodeCloudHeader.decodeHeader(response.headers(), StringStaticUtils.RESULT_CODE, context);

                    if (resultCode.equals(StringStaticUtils.RESULT_CODE_SUCCESS)) {
                        iview.onSuccess(resp.list);
                    } else {
                        iview.onFailed(getResuleCodeString(resultCode));
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {
                iview.onFailed(getFailedString());
                subscription.unsubscribe();

            }
        });
    }

    public void getScenarioList(long userId, int pageNumber, int pageSize, final Iview iview) {
        GetScenarioListRequest getScenarioListRequest = new GetScenarioListRequest(userId);
        getScenarioListRequest.pageNumber = pageNumber;
        getScenarioListRequest.pageSize = pageSize;
        final Call<GetScenarioListResponse> call = iHttpCloudService.getScenarioList(getScenarioListRequest);
        final Subscription subscription = RxBus.getDefault().toObserverable(Cancel.class).subscribe(new Action1<Cancel>() {
            @Override
            public void call(Cancel cancel) {

                call.cancel();
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                Log.e(HttpCloudService.class.getName(), "error");
            }
        });
        call.enqueue(new Callback<GetScenarioListResponse>() {
            @Override
            public void onResponse(Response<GetScenarioListResponse> response) {
                subscription.unsubscribe();

                if (response.errorBody() != null) {
                    iview.onFailed(getErrorString());
                } else {

                    GetScenarioListResponse resp = response.body();
                    String resultCode = DecodeCloudHeader.decodeHeader(response.headers(), StringStaticUtils.RESULT_CODE, context);

                    if (resultCode.equals(StringStaticUtils.RESULT_CODE_SUCCESS)) {
                        iview.onSuccess(resp.list);
                    } else {
                        iview.onFailed(getResuleCodeString(resultCode));
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {
                subscription.unsubscribe();

                iview.onFailed(getFailedString());
            }
        });
    }

    public void getScenarioDeviceList(long userId, int pageNumber, int pageSize, long scenarioId, final Iview iview) {

        GetScenarioDeviceRequest getGroupDeviceRequest = new GetScenarioDeviceRequest(userId, scenarioId);
        getGroupDeviceRequest.pageNumber = pageNumber;
        getGroupDeviceRequest.pageSize = pageSize;
        final Call<GetScenarioDeviceResponse> call = iHttpCloudService.getScenarioDeviceList(getGroupDeviceRequest);
        final Subscription subscription = RxBus.getDefault().toObserverable(Cancel.class).subscribe(new Action1<Cancel>() {
            @Override
            public void call(Cancel cancel) {

                call.cancel();
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                Log.e(HttpCloudService.class.getName(), "error");
            }
        });
        call.enqueue(new Callback<GetScenarioDeviceResponse>() {
            @Override
            public void onResponse(Response<GetScenarioDeviceResponse> response) {
                subscription.unsubscribe();

                if (response.errorBody() != null) {
                    iview.onFailed(getErrorString());
                } else {

                    GetScenarioDeviceResponse resp = response.body();
                    String resultCode = DecodeCloudHeader.decodeHeader(response.headers(), StringStaticUtils.RESULT_CODE, context);

                    if (resultCode.equals(StringStaticUtils.RESULT_CODE_SUCCESS)) {
                        iview.onSuccess(resp.list);
                    } else {
                        iview.onFailed(getResuleCodeString(resultCode));
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {
                subscription.unsubscribe();

                iview.onFailed(getFailedString());

            }
        });
    }

    public void startScenario(long userId, long scenarioId, final Iview iview) {
        StartScenarioRequest startScenarioRequest = new StartScenarioRequest(userId, scenarioId);
        iHttpCloudService.startScenario(startScenarioRequest);
        final Call<VoidResponse> call = iHttpCloudService.startScenario(startScenarioRequest);
        final Subscription subscription = RxBus.getDefault().toObserverable(Cancel.class).subscribe(new Action1<Cancel>() {
            @Override
            public void call(Cancel cancel) {

                call.cancel();
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                Log.e(HttpCloudService.class.getName(), "error");
            }
        });
        call.enqueue(new Callback<VoidResponse>() {
            @Override
            public void onResponse(Response<VoidResponse> response) {
                subscription.unsubscribe();

                if (response.errorBody() != null) {
                    iview.onFailed(getErrorString());
                } else {
                    String resultCode = DecodeCloudHeader.decodeHeader(response.headers(), StringStaticUtils.RESULT_CODE, context);

                    if (resultCode.equals(StringStaticUtils.RESULT_CODE_SUCCESS)) {
                        iview.onSuccess("");
                    } else {
                        iview.onFailed(getResuleCodeString(resultCode));
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {
                subscription.unsubscribe();

                iview.onFailed(getFailedString());
            }
        });

    }

    public void getDeviceTypeList(long userId, final Iview iview) {
        GetDeviceTypeRequest getDeviceTypeRequest = new GetDeviceTypeRequest(userId);
        final Call<DeviceTypeInfo[]> call = iHttpCloudService.getDeviceTypeList(getDeviceTypeRequest);
        final Subscription subscription = RxBus.getDefault().toObserverable(Cancel.class).subscribe(new Action1<Cancel>() {
            @Override
            public void call(Cancel cancel) {

                call.cancel();
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                Log.e(HttpCloudService.class.getName(), "error");
            }
        });

        call.enqueue(new Callback<DeviceTypeInfo[]>() {
            @Override
            public void onResponse(Response<DeviceTypeInfo[]> response) {
                subscription.unsubscribe();

                if (response.errorBody() != null) {
                    iview.onFailed(getErrorString());
                } else {

                    DeviceTypeInfo[] resp = response.body();
                    String resultCode = DecodeCloudHeader.decodeHeader(response.headers(), StringStaticUtils.RESULT_CODE, context);

                    if (resultCode.equals(StringStaticUtils.RESULT_CODE_SUCCESS)) {
                        iview.onSuccess(resp);
                    } else {
                        iview.onFailed(getResuleCodeString(resultCode));
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {
                subscription.unsubscribe();

                iview.onFailed(getFailedString());

            }
        });
    }

    public void getAreaList(long userId, final Iview iview) {
        GetAreaRequest getAreaRequest = new GetAreaRequest(userId);
        final Call<AreaInfo[]> call = iHttpCloudService.getAreaList(getAreaRequest);
        final Subscription subscription = RxBus.getDefault().toObserverable(Cancel.class).subscribe(new Action1<Cancel>() {
            @Override
            public void call(Cancel cancel) {

                call.cancel();
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                Log.e(HttpCloudService.class.getName(), "error");
            }
        });

        call.enqueue(new Callback<AreaInfo[]>() {
            @Override
            public void onResponse(Response<AreaInfo[]> response) {
                subscription.unsubscribe();

                if (response.errorBody() != null) {
                    iview.onFailed(getErrorString());
                } else {

                    AreaInfo[] list = response.body();
                    String resultCode = DecodeCloudHeader.decodeHeader(response.headers(), StringStaticUtils.RESULT_CODE, context);

                    if (resultCode.equals(StringStaticUtils.RESULT_CODE_SUCCESS)) {
                        iview.onSuccess(list);
                    } else {
                        iview.onFailed(getResuleCodeString(resultCode));
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {
                subscription.unsubscribe();

                iview.onFailed(getFailedString());

            }
        });
    }

    public void getDeviceList(long userId, long areaId, String deviceTypeCode, int pageNumber, int pageSize, final Iview iview) {
        GetDeviceListRequest getDeviceListRequest = new GetDeviceListRequest(userId, areaId, deviceTypeCode);

        getDeviceListRequest.pageNumber = pageNumber;
        getDeviceListRequest.pageSize = pageSize;
        final Call<GetDeviceListResponse> call = iHttpCloudService.getDeviceList(getDeviceListRequest);
        final Subscription subscription = RxBus.getDefault().toObserverable(Cancel.class).subscribe(new Action1<Cancel>() {
            @Override
            public void call(Cancel cancel) {

                call.cancel();
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                Log.e(HttpCloudService.class.getName(), "error");
            }
        });

        call.enqueue(new Callback<GetDeviceListResponse>() {
            @Override
            public void onResponse(Response<GetDeviceListResponse> response) {
                subscription.unsubscribe();

                if (response.errorBody() != null) {
                    iview.onFailed(getErrorString());
                } else {

                    GetDeviceListResponse resp = response.body();
                    String resultCode = DecodeCloudHeader.decodeHeader(response.headers(), StringStaticUtils.RESULT_CODE, context);

                    if (resultCode.equals(StringStaticUtils.RESULT_CODE_SUCCESS)) {
                        iview.onSuccess(resp.list);
                    } else {
                        iview.onFailed(getResuleCodeString(resultCode));
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {
                subscription.unsubscribe();

                iview.onFailed(getFailedString());

            }
        });
    }


    public String getErrorString() {
        return context.getResources().getString(R.string.http_error_body_failed);
    }

    public String getResuleCodeString(String resultCode) {
        return DecodeCloudHeader.getResultCodeMessage(Integer.parseInt(resultCode), context);
    }

    public String getFailedString() {
        return context.getResources().getString(R.string.http_failed);

    }
}
