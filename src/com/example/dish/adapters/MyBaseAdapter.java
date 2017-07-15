package com.example.dish.adapters;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;

public abstract class MyBaseAdapter<T> extends BaseAdapter {

	protected List<T> adapterDatas;
	protected LayoutInflater inflater;
	protected Context context;

	public MyBaseAdapter(Context context, List<T> datas) {
		this.context = context;
		this.adapterDatas = new ArrayList<T>();
		setDatas(datas);
		this.inflater = LayoutInflater.from(context);
	}

	public void setDatas(List<T> datas) {
		this.adapterDatas.clear();
		if (datas != null) {
			for (int i = datas.size()-1; i >= 0; i--) {
				this.adapterDatas.add(datas.get(i));
			}
		}
		notifyDataSetChanged();
	}

	public void addDatas(List<T> addDatas) {
		if (addDatas != null) {
			this.adapterDatas.addAll(addDatas);
		}
		notifyDataSetChanged();
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
}
