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
 * Created by ll on 2016/4/6.
 */
public class GroupAdapter  extends BaseAdapter {
    private final List<CommontAdapterData> adapterDatas;
    IOnGroupClick iOnGroupClick;

    public GroupAdapter(List<CommontAdapterData> adapterDatas, IOnGroupClick iOnStartScenario) {
        this.adapterDatas = adapterDatas;
        this.iOnGroupClick =iOnStartScenario;
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

            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_group, parent, false);
            final ItemHolder holder = new ItemHolder();
            holder.name = (TextView) convertView.findViewById(R.id.item_name);
            holder.button= (Button) convertView.findViewById(R.id.item_btn_1);
            holder.button2= (Button) convertView.findViewById(R.id.item_btn_2);

            holder.button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    iOnGroupClick.onClickGroup(holder.id,true);
                }
            });
            holder.button2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    iOnGroupClick.onClickGroup(holder.id,false);
                }
            });
            convertView.setTag(holder);

        }
        ItemHolder itemHolder = (ItemHolder) convertView.getTag();

        CommontAdapterData adapterData = adapterDatas.get(position);
        itemHolder.name.setText(adapterData.name);
        itemHolder.id=adapterData.id;
        return convertView;
    }

    class ItemHolder {
        public TextView name;
        public Button button;
        public Button button2;
        public long id;

    }
    public interface  IOnGroupClick{
        public void onClickGroup(long id,boolean on);
    }


}
