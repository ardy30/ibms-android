package com.ehomeclouds.eastsoft.channel.mqtt;

import android.content.Context;

import android.content.SharedPreferences;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.eastsoft.building.model.RxBus;
import com.ehomeclouds.eastsoft.channel.mqtt.model.MqttConnectStatus;
import com.ehomeclouds.eastsoft.channel.mqtt.model.MqttData;
import com.ehomeclouds.eastsoft.channel.mqtt.model.PublishState;
import com.ehomeclouds.eastsoft.channel.mqtt.util.KeyUtil;
import com.ehomeclouds.eastsoft.channel.mqtt.util.MyTag;
import com.ehomeclouds.eastsoft.channel.mqtt.util.Numbers;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.eclipse.paho.client.mqttv3.MqttTopic;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.util.logging.LoggingMXBean;

/**
 * Created by ll on 2015/12/26.
 */
public class MqttManeger {
    private  String userName = "eastsofe";
    private  String passWord = "es";
    private String host = "tcp://iot.eastsoft.com.cn:1883";

    private static MqttManeger mqttManeger;
    public MqttClient client = null;
    private static final int QOS = 1;
    private MqttConnectOptions options;
    private Context context;


    private MqttManeger(Context context) {
        this.context=context;
    }

    public static MqttManeger getInstance(Context context) {
        if (mqttManeger == null) {
            mqttManeger = new MqttManeger(context);
        }
        return mqttManeger;
    }

    public void connect(String host,String userName, String passWord) {
        this.host=host;
        this.userName = userName;
        this.passWord = passWord;
        initMqttClient();
        if (!client.isConnected()) {
            con();
        }
    }


    private void con() {
        new Thread(new Runnable() {

            public void run() {
                try {
                    if (client != null&&!client.isConnected()) {
                        client.connect(options);
                        RxBus.getDefault().post(new MqttConnectStatus(MqttConnectStatus.CONNECT_SUCCESS));
                        System.out.println("connectionSuccess----------");

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    RxBus.getDefault().post(new MqttConnectStatus(MqttConnectStatus.CONNECT_FAILED));
                    System.out.println("connectionFailed----------");


                }
            }
        }).start();
    }



    private void initMqttClient() {
        try {
//            String clientId=getDeviceId();

            client = new MqttClient(host, Numbers.getUuid(), new MemoryPersistence());
//            client = new MqttClient(host,Build.SERIAL, new MemoryPersistence());
            options = new MqttConnectOptions();
            options.setCleanSession(true);
            options.setUserName(userName);
            options.setPassword(passWord.toCharArray());
            options.setConnectionTimeout(10);
            options.setKeepAliveInterval(30);
            client.setCallback(new MqttCallback() {

                public void connectionLost(Throwable cause) {
                    // 连接丢失后，一般在这里面进行重连
                    System.out.println("connectionLost----------");
                    RxBus.getDefault().post(new MqttConnectStatus(MqttConnectStatus.CONNECT_LOST));
                    con();
                }

                public void deliveryComplete(IMqttDeliveryToken token) {
                    // publish后会执行到这里
                    System.out.println("deliveryComplete---------"
                            + token.isComplete());
                    RxBus.getDefault().post(new PublishState(PublishState.SUCCESS));

                }

                public void messageArrived(String topicName, MqttMessage message)
                    throws Exception {
                    Log.d(MyTag.TAG, "receive---------" + topicName + message.toString());
                    String[] topicBytes = topicName.split("\\/");
                    if (topicBytes.length > 2) {
                        MqttData mqttData = new MqttData(topicName, message.toString());
                        mqttData.setGroupStr(topicBytes[1]);
                        mqttData.setDeviceDk(topicBytes[2]);
                        RxBus.getDefault().post(mqttData);
                    }

                }

            });


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String  getDeviceId() {
        TelephonyManager TelephonyMgr = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        String deviceId = TelephonyMgr.getDeviceId();
        if (deviceId==null){

            deviceId=Build.SERIAL;
        }
        return  deviceId;
    }

    public boolean isConnected() {
        if (client==null){
            return false;
        }
        return client.isConnected();
    }

    public void unsubscribe(String topic[]) {
        if (client != null && client.isConnected()) {
            try {

                client.unsubscribe(topic);
                Log.d(MyTag.TAG, "-unsubscibe");

            } catch (MqttException e) {
                e.printStackTrace();
            }
        }
    }
    public void subscribe(String[] topic) {
        if (client != null && client.isConnected()) {
            try {
                client.subscribe(topic);
                Log.d(MyTag.TAG, "+subscibe");

            } catch (MqttException e) {
                e.printStackTrace();
            }
        }
    }

//    public void publish(String payload, String mac) {
//        MqttTopic mqttTopic = client.getTopic(TOPIC_HEAD + mac + CMD);
//        MqttMessage mqttMessage = new MqttMessage();
//        mqttMessage.setQos(QOS);
//        mqttMessage.setRetained(true);
//        mqttMessage.setPayload(payload.getBytes());
//        try {
//            MqttDeliveryToken token;
//            token = mqttTopic.publish(mqttMessage);
//            token.waitForCompletion();
//        } catch (MqttPersistenceException e) {
//            e.printStackTrace();
//        } catch (MqttException e) {
//            e.printStackTrace();
//        }
//
//    }

    public void publish(String payload, String topic) {
        MqttTopic mqttTopic = client.getTopic(topic);
        MqttMessage mqttMessage = new MqttMessage();
        mqttMessage.setQos(QOS);
        mqttMessage.setRetained(false);
        mqttMessage.setPayload(payload.getBytes());
        Log.d(MyTag.TAG, "publish---------topic=" + topic + "paload=" + payload);

        try {
            MqttDeliveryToken token;
            token = mqttTopic.publish(mqttMessage);
            token.waitForCompletion();

        } catch (MqttPersistenceException e) {
            RxBus.getDefault().post(new PublishState(PublishState.FAILED));

            e.printStackTrace();
        } catch (MqttException e) {
            RxBus.getDefault().post(new PublishState(PublishState.FAILED));

            e.printStackTrace();
        }

    }

    private void disConnect() {
        try {
            if (client != null && client.isConnected()) {
                client.disconnect();
            }
        } catch (MqttException e) {
            e.printStackTrace();
        }

    }
    public void destroy() {
        disConnect();
        mqttManeger = null;
    }

    private static String  getLocalUrl(Context context) {
        SharedPreferences sharedPreferences=context.getSharedPreferences(KeyUtil.SHARE_URL, Context.MODE_PRIVATE);
        String url=sharedPreferences.getString(KeyUtil.SHARE_URL_KEY, "");
        return url;
    }

}
