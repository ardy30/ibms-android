package com.eastsoft.building.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.eastsoft.building.R;

import java.util.List;

/**
 * Created by ll on 2016/4/11.
 */
public class DeviceListAdapter  extends BaseAdapter {
    private final List<DeviceListData> adapterDatas;

    public DeviceListAdapter(List<DeviceListData> adapterDatas) {
        this.adapterDatas = adapterDatas;
    }


    @Override
    public int getCount() {
        return adapterDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return adapterDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {

            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_device_list, parent, false);
            final ItemHolder holder = new ItemHolder();
            holder.name = (TextView) convertView.findViewById(R.id.item_name);
            holder.areaName= (TextView) convertView.findViewById(R.id.item_area);

            convertView.setTag(holder);

        }
        ItemHolder itemHolder = (ItemHolder) convertView.getTag();

        final DeviceListData adapterData = adapterDatas.get(position);
        itemHolder.name.setText(adapterData.name);
        itemHolder.areaName.setText(adapterData.areaName);

        return convertView;
    }

    class ItemHolder {
        public TextView name,areaName;


    }
}
