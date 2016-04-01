package com.eastsoft.building.sdk.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.eastsoft.building.sdk.R;

import java.util.List;

/**
 * Created by ll on 2015/5/27 0027.
 */
public class CommonAdapter extends BaseAdapter {
    private final List<AdapterData> adapterDatas;
    private boolean isGridview;

    public CommonAdapter(List<AdapterData> adapterDatas, boolean isGridview) {
        this.adapterDatas = adapterDatas;
        this.isGridview = isGridview;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {

            if (isGridview) {
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_gridview, parent, false);
            } else {
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_listview, parent, false);
            }
            ItemHolder holder = new ItemHolder();
            holder.icon = (ImageView) convertView.findViewById(R.id.item_icon);
            holder.name = (TextView) convertView.findViewById(R.id.item_name);
            convertView.setTag(holder);

        }
        ItemHolder itemHolder = (ItemHolder) convertView.getTag();

        AdapterData adapterData = adapterDatas.get(position);
        itemHolder.name.setText(adapterData.name);
        itemHolder.icon.setImageResource(adapterData.drawableId);

        itemHolder.icon.setSelected(adapterData.selected);
        return convertView;
    }

    class ItemHolder {
        public ImageView icon;
        public TextView name;

    }


}
