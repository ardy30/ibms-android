package com.ehomeclouds.eastsoft.channel.http.base.Util;

import java.util.UUID;

/**
 * Created by Zcl on 2015-06-03.
 */
public class GenerateUtils {

    /**
     * 获得一个UUID
     * @return String UUID
     */
    public static String getUUID(){
        return UUID.randomUUID().toString();
        //去掉“-”符号
       // return s.substring(0,8)+s.substring(9,13)+s.substring(14,18)+s.substring(19,23)+s.substring(24);
    }
    public static final int PAGE_SIZE=100;

}
