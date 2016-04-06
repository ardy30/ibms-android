package com.eastsoft.building.sdk;


import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;

/**
 * define some constant value and commonly use method
 * 
 * @author ljt
 * @date 2015-2-10 15:34:05
 *
 */
public class UtilityInfo {
	public static final String SHARE_PRERERENCE_ACCOUNT = "acount_gateway";
	public static final String SHARE_PRERERENCE_IS_RECEIEVE_IN_BACKGROUND = "is_recv_in_background";
	public static final String SHARE_PRERERENCE_IS_RECEIEVE_MSG = "is_recv_msg";


    //将后台消息服务储存到本地
	public static void setBackgroundRecEventMsg(Context ctx, boolean isRecv) {
		SharedPreferences sp = ctx.getSharedPreferences(
				UtilityInfo.SHARE_PRERERENCE_ACCOUNT, Context.MODE_PRIVATE);
		if (isRecv) {
			sp.edit()
					.putString(
							UtilityInfo.SHARE_PRERERENCE_IS_RECEIEVE_IN_BACKGROUND,
							"1").apply();
		} else {
			sp.edit()
					.putString(
							UtilityInfo.SHARE_PRERERENCE_IS_RECEIEVE_IN_BACKGROUND,
							"0").apply();
		}
		sp.edit().commit();

	}
    ////开启后台消息服务
	public static boolean isRecvInBackground(Context ctx) {
		SharedPreferences sp = ctx.getSharedPreferences(
				UtilityInfo.SHARE_PRERERENCE_ACCOUNT, Context.MODE_PRIVATE);
		String str = sp.getString(
				UtilityInfo.SHARE_PRERERENCE_IS_RECEIEVE_IN_BACKGROUND, "1");
		if ("1".equals(str)) {
			return true;
		}
		return false;
	}

	public static void setRecEventMsg(Context ctx, boolean isRecv) {
		SharedPreferences sp = ctx.getSharedPreferences(
				UtilityInfo.SHARE_PRERERENCE_ACCOUNT, Context.MODE_PRIVATE);
		if (isRecv) {
			sp.edit()
					.putString(UtilityInfo.SHARE_PRERERENCE_IS_RECEIEVE_MSG,
							"1").apply();
		} else {
			sp.edit()
					.putString(UtilityInfo.SHARE_PRERERENCE_IS_RECEIEVE_MSG,
							"0").apply();
		}
		sp.edit().commit();

	}

	public static boolean isRecvMsg(Context ctx) {
		SharedPreferences sp = ctx.getSharedPreferences(
				UtilityInfo.SHARE_PRERERENCE_ACCOUNT, Context.MODE_PRIVATE);
		String str = sp.getString(UtilityInfo.SHARE_PRERERENCE_IS_RECEIEVE_MSG,
				"1");
		if ("1".equals(str)) {
			return true;
		}
		return false;
	}

	public static boolean networkIsConnected(Context context) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivityManager != null) {
			NetworkInfo[] networkInfos = connectivityManager
					.getAllNetworkInfo();
			for (int i = 0; i < networkInfos.length; i++) {
				State state = networkInfos[i].getState();
				if (State.CONNECTED == state) {
					System.out.println("------------> Network is ok");
					return true;
				}
			}
		}
		return false;
	}
	
	
	
}
