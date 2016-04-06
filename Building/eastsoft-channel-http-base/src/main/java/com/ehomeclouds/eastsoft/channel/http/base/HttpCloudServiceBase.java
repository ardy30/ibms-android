package com.ehomeclouds.eastsoft.channel.http.base;


import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.eastsoft.android.eastsoft.channel.http.base.R;
import com.ehomeclouds.eastsoft.channel.http.base.Util.StringStaticUtils;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.logging.HttpLoggingInterceptor;


import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManagerFactory;

import retrofit.Call;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.http.Body;
import retrofit.http.POST;

//import retrofit.GsonConverterFactory;


/**
 * Created by Zcl on 2015-06-03.
 */
public abstract class HttpCloudServiceBase {

    private static final String TAG = "HttpCloudService";
    private String psd = "zcl5219";

    protected Retrofit getRetrofit(Context ctx) throws CertificateException, UnrecoverableKeyException, NoSuchAlgorithmException, IOException, KeyManagementException, KeyStoreException {
        String ip;
        String port;
        SharedPreferences mySharedPreferences = PreferenceManager.getDefaultSharedPreferences(ctx);
        ip = mySharedPreferences.getString("ip_edittext_preference", "");
        if (ip.equals("")) {
            ip = mySharedPreferences.getString("ip_list_preference", "");
        }
        if (ip.equals("")) {
            ip = "api.ehomeclouds.com.cn";
        }
        port = mySharedPreferences.getString("port_edittext_preference", "10443");
        String url;
        if (!port.equals("10443")) {
            url = "http://" + ip + ":" + port + "/";
        } else {
            url = "https://" + ip + ":" + port + "/";
        }
        url = "http://129.1.18.98:8080";
        Retrofit restAdapter = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .client(BuildHttpsClient(ctx))
                .build();
        return restAdapter;
    }

    private OkHttpClient BuildHttpsClient(final Context context) throws CertificateException, NoSuchAlgorithmException, IOException, UnrecoverableKeyException, KeyStoreException, KeyManagementException {

        KeyStore keyStore = readKeyStore(context);

        SSLContext sslContext = SSLContext.getInstance("SSL");
        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        trustManagerFactory.init(keyStore);
        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        keyManagerFactory.init(keyStore, psd.toCharArray());
        sslContext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), new SecureRandom());
//        sslContext.getSocketFactory().createSocket("https://api.ehomeclouds.com.cn",10443);

        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(15000, TimeUnit.MILLISECONDS);
        client.setReadTimeout(15000, TimeUnit.MILLISECONDS);
        client.setSslSocketFactory(sslContext.getSocketFactory());
        client.setHostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {

                if ("api.ehomeclouds.com.cn".equals(hostname)) {
                    return true;
                }
                return false;
            }
        });
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        client.interceptors().add(httpLoggingInterceptor);
        client.interceptors().add(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                Response response = chain.proceed(request);
                Headers headers=response.headers();

                if (headers.values(StringStaticUtils.RESULT_CODE).size()>0&&
                        headers.values(StringStaticUtils.RESULT_CODE).get(0).equals(StringStaticUtils.TOKEN_INVALID)) {
                    try {
                        SharedPreferences sharedPreferences=context.getSharedPreferences(StringStaticUtils.SHAREP_NAME,Context.MODE_PRIVATE);
                        String email=sharedPreferences.getString(StringStaticUtils.SHAREP_EMAIL, "");
                        String psd=sharedPreferences.getString(StringStaticUtils.SHAREP_PSD,"");
                        LoginRequest loginRequest = new LoginRequest(email,psd);
                        Call<LoginResponse> call = getRetrofit(context).create(api.class).loginIn(loginRequest);
                        retrofit.Response<LoginResponse> loginResponseResponse = call.execute();
                        String newToken = loginResponseResponse.headers().get(StringStaticUtils.TOKEN);
                        Token.token=newToken;
                        // create a new request and modify it accordingly using the new token
                        Request newRequest = request.newBuilder().header("token", newToken)
                                .build();
                        // retry the request
                        response.body().close();
                        return chain.proceed(newRequest);
                    } catch (CertificateException e) {
                        e.printStackTrace();
                    } catch (UnrecoverableKeyException e) {
                        e.printStackTrace();
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    } catch (KeyManagementException e) {
                        e.printStackTrace();
                    } catch (KeyStoreException e) {
                        e.printStackTrace();
                    }
                }
                return response;
            }
        });

        return client;
    }

    private KeyStore readKeyStore(Context ctx) throws CertificateException, NoSuchAlgorithmException, IOException {
        KeyStore ks = null;
        try {
            ks = KeyStore.getInstance(KeyStore.getDefaultType());//"BKS"
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }
        char[] password = psd.toCharArray();
        InputStream fis = null;
        try {
            fis = ctx.getResources().openRawResource(R.raw.cloudkeystore);
            if (ks != null) {
                ks.load(fis, password);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                fis.close();
            }
        }
        return ks;

    }

    interface api {
        @POST("noauth/user/credential")
        public Call<LoginResponse> loginIn(@Body LoginRequest loginRequest);

    }


}
