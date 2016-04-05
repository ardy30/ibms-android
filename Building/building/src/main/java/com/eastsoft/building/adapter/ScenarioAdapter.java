package com.eastsoft.building.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.eastsoft.building.sdk.R;

import java.util.List;

/**
 * Created by ll on 2015/5/27 0027.
 */
public class ScenarioAdapter extends BaseAdapter {
    private final List<ScenarioAdapterData> adapterDatas;
    IOnStartScenario iOnStartScenario;

    public ScenarioAdapter(List<ScenarioAdapterData> adapterDatas,IOnStartScenario iOnStartScenario) {
        this.adapterDatas = adapterDatas;
        this.iOnStartScenario=iOnStartScenario;
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

            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_scenario, parent, false);
            final ItemHolder holder = new ItemHolder();
            holder.name = (TextView) convertView.findViewById(R.id.item_name);
            holder.button= (Button) convertView.findViewById(R.id.item_btn_1);
            holder.button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    iOnStartScenario.onStartScenario(holder.id);
                }
            });
            convertView.setTag(holder);

        }
        ItemHolder itemHolder = (ItemHolder) convertView.getTag();

        ScenarioAdapterData adapterData = adapterDatas.get(position);
        itemHolder.name.setText(adapterData.name);
        itemHolder.id=adapterData.scenarioId;
        return convertView;
    }

    class ItemHolder {
        public TextView name;
        public Button button;
        public long id;

    }
    public interface  IOnStartScenario{
        public void onStartScenario(long id);
    }


}
