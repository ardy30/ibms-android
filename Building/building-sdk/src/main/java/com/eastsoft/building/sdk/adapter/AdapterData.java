package com.eastsoft.building.sdk.adapter;

/**
 * Created by ll on 2016/2/15.
 */
public class AdapterData {
    public String deviceKey;
    public int drawableId;
    public String name;
    public String state="false";
    public boolean selected=false;
    public int vchannel;

    public AdapterData(String name, int drawableId) {
        this.name = name;
        this.drawableId = drawableId;
    }


}
