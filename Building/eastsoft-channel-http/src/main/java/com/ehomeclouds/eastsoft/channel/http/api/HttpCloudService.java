package com.ehomeclouds.eastsoft.channel.http.api;

import android.content.Context;

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
import java.util.ArrayList;

import com.ehomeclouds.eastsoft.channel.http.R;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;

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

    public void loginIn(String userId,String psd, final Iview iview){
        LoginRequest loginRequest=new LoginRequest(userId,psd);
        Call<LoginResponse> call=iHttpCloudService.loginIn(loginRequest);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Response<LoginResponse> response) {
                if (response.errorBody()!=null){
                    iview.onFailed(getErrorString());
                }else{

                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    public String getErrorString(){
        return  context.getResources().getString(R.string.http_error_body_failed);
    }

}
