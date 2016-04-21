package com.eastsoft.building.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.eastsoft.building.R;

import java.util.List;

public class SpinnerListAdapter extends BaseAdapter {
	private List<String> list;
	LayoutInflater inflater;
	private int pos=0;

	public SpinnerListAdapter(Context cxt, List<String> list) {
		this.list = list;
		inflater = (LayoutInflater) cxt
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	public void setCheckedPos(int pos){
		this.pos=pos;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = null;
		if (convertView == null) {
			view = inflater.inflate(R.layout.item_pop, null);
			RoomSpinAdapterHolder holder = new RoomSpinAdapterHolder();
			holder.name = (TextView) view
					.findViewById(R.id.item_text_pop);
			holder.checked=view.findViewById(R.id.item_image_pop);
			view.setTag(holder);
		} else {
			view = convertView;
		}
		RoomSpinAdapterHolder holder = (RoomSpinAdapterHolder) view.getTag();
		if (list.size() > 0) {
			holder.name.setText(list.get(position));
		}
		if (position==pos){
			holder.checked.setVisibility(View.VISIBLE);
			holder.name.setSelected(true);
		}else{
			holder.checked.setVisibility(View.GONE);
			holder.name.setSelected(false);
		}
		return view;
	}

	public class RoomSpinAdapterHolder {
		public TextView name;
		public View checked;
	}

}
