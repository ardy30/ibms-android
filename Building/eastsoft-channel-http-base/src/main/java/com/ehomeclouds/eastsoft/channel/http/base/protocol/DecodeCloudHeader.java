package com.ehomeclouds.eastsoft.channel.http.base.protocol;

import android.content.Context;

import com.eastsoft.android.eastsoft.channel.http.base.R;
import com.ehomeclouds.eastsoft.channel.http.base.Util.StringStaticUtils;
import com.squareup.okhttp.Headers;


/**
 * Created by Zcl on 2015-06-03.
 */
public class DecodeCloudHeader {


    public static String decodeHeader(Headers headers, String name,Context ctx) {
        if (name == null || "".equals(name)){
            return "";
        }
        if (headers.values(name) == null || headers.values(name).size() == 0) {
            return "";
        }
        if (name.equals(StringStaticUtils.RESULT_CODE)){
            return getResultCodeMessage(Integer.parseInt(headers.values(name).get(0)), ctx);
        }
        return headers.values(name).get(0);
    }

//    public static String decodeHeader(List<Header> lstHeader, String header) {
//        if (header == null || "".equals(header))
//            return "header get must not null!";
//        for (Header h : lstHeader) {
//            if (h.getName().equals(header)) {
//                return h.getValue();
//            }
//        }
//        return null;
//    }
//
//    public static String decodeResultHeader(List<Header> lstHeader, Context ctx) {
//        for (Header h : lstHeader) {
//            if (h.getName().equals(StringStaticUtils.RESULT_CODE)) {
//                return getResultCodeMessage(Integer.parseInt(h.getValue()), ctx);
//            }
//        }
//        return "Error";
//    }
//
//
//    public static String decodeResultRtLanHeader(List<Header> lstHeader, Context ctx) {
//        for (Header h : lstHeader) {
//            if (h.getName().equals(StringStaticUtils.RESULT_CODE)) {
//                return getResultCodeRtMessage(Integer.parseInt(h.getValue()), ctx);
//            }
//        }
//        return "Error";
//    }
//
//    private static String getResultCodeRtMessage(int code, Context ctx) {
//
//
//        String msg = "Error";
//        switch (code) {
//            case 0:
//                msg = ctx.getString(R.string.failed);
//                break;
//            case 1:
//                msg = "SUCCESS";
//                break;
//            default:
//                break;
//        }
//        return msg;
//    }


    public static String getResultCodeMessage(int code, Context ctx) {
        String msg = "Error";
        switch (code) {
            case 0:
                msg = 0 + "";
                break;
            case 1001:
                msg = ctx.getString(R.string.M1001);
                break;
            case 1002:
                msg = ctx.getString(R.string.M1002);
                break;
            case 1003:
                msg = ctx.getString(R.string.M1003);
                break;
            case 1004:
                msg = ctx.getString(R.string.M1004);
                break;
            case 1005:
                msg = ctx.getString(R.string.M1005);
                break;
            case 1006:
                msg = ctx.getString(R.string.M1006);
                break;
            case 1007:
                msg = ctx.getString(R.string.M1007);
                break;
            case 1008:
                msg = ctx.getString(R.string.M1008);
                break;
            case 2001:
                msg = ctx.getString(R.string.M2001);
                break;
            case 2002:
                msg = ctx.getString(R.string.M2002);
                break;
            case 2003:
                msg = ctx.getString(R.string.M2003);
                break;
            case 2004:
                msg = ctx.getString(R.string.M2004);
                break;
            case 2005:
                msg = ctx.getString(R.string.M2005);
                break;
            case 2501:
                msg = ctx.getString(R.string.M2501);
                break;
            case 2502:
                msg = ctx.getString(R.string.M2502);
                break;
            case 2503:
                msg = ctx.getString(R.string.M2503);
                break;
            case 2504:
                msg = ctx.getString(R.string.M2504);
                break;
            case 2505:
                msg = ctx.getString(R.string.M2505);
                break;
            case 2506:
                msg = ctx.getString(R.string.M2506);
                break;
            case 2507:
                msg = ctx.getString(R.string.M2507);
                break;
            case 2508:
                msg = ctx.getString(R.string.M2508);
                break;
            case 2509:
                msg = ctx.getString(R.string.M2509);
                break;
            case 2510:
                msg = ctx.getString(R.string.M2510);
                break;
            case 2511:
                msg = ctx.getString(R.string.M2511);
                break;
            case 2512:
                msg = ctx.getString(R.string.M2512);
                break;
            case 2513:
                msg = ctx.getString(R.string.M2513);
                break;
            case 3001:
                msg = ctx.getString(R.string.M3001);
                break;
            case 3002:
                msg = ctx.getString(R.string.M3002);
                break;
            default:
                msg = code+"";
                break;
        }
        return msg;
    }

}
