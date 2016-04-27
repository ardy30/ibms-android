package com.eastsoft.building.adapter;

/**
 * Created by ll on 2016/2/15.
 */
public class CommontAdapterData {

    public final String name;
    public long id;
    public String dk;
    public int channel=1;
    public boolean showDetail=false;
    public boolean selected=true;
    public CommontAdapterData(String name, long id) {
        this.name = name;
        this.id =id;
    }


}
