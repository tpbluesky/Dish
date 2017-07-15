package com.example.dish.adapters;

import java.util.ArrayList;
import java.util.List;

import org.xutils.x;
import org.xutils.ex.DbException;

import com.daimajia.swipe.SimpleSwipeListener;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.BaseSwipeAdapter;
import com.example.dish.Constants;
import com.example.dish.MyApplication;
import com.example.dish.R;
import com.example.dish.atys.DetailActivity;
import com.example.dish.atys.EditActivity;
import com.example.dish.beans.Dish;

import android.content.Context;
import android.content.Intent;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class UploadListAdapter extends BaseSwipeAdapter {

	private List<Dish> datas;
	private Context mContext;

	public UploadListAdapter(Context mContext, List<Dish> d) {
		datas = new ArrayList<Dish>();
		this.mContext = mContext;
		if (d != null) {
			for (int i = d.size() - 1; i >= 0; i--) {
				this.datas.add(d.get(i));
			}
		}
	}

	@Override
	public int getCount() {
		return datas.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public void fillValues(int position, View convertView) {
		Dish d = datas.get(position);
		TextView tvDishName = (TextView) convertView.findViewById(R.id.tv_dish_name);
		TextView tvPublishDate = (TextView) convertView.findViewById(R.id.tv_publish_date);
		TextView tvCategory = (TextView) convertView.findViewById(R.id.tv_category);
		ImageView ivDishImage = (ImageView) convertView.findViewById(R.id.iv_dish_image);
		ImageView ivEdit = (ImageView) convertView.findViewById(R.id.iv_edit);
		ImageView ivDelete = (ImageView) convertView.findViewById(R.id.iv_delete);
		x.image().bind(ivDishImage, d.getImagePath());
		tvDishName.setText(d.getName());
		tvCategory.setText(d.getCategory());
		tvPublishDate.setText(DateFormat.format("yyyy年mm月dd日   hh时mm分", d.getPublishDate()));
	}

	@Override
	public View generateView(final int position, ViewGroup parent) {
		View v = LayoutInflater.from(mContext).inflate(R.layout.layout_upload_list_item, null);
		SwipeLayout swipeLayout = (SwipeLayout) v.findViewById(getSwipeLayoutResourceId(position));
		swipeLayout.setShowMode(SwipeLayout.ShowMode.PullOut);
		swipeLayout.setDragEdge(SwipeLayout.DragEdge.Right);
		swipeLayout.addSwipeListener(new SimpleSwipeListener() {
			@Override
			public void onOpen(SwipeLayout layout) {
				super.onOpen(layout);
			}

			@Override
			public void onClose(SwipeLayout layout) {
				super.onClose(layout);
			}
		});
		v.findViewById(R.id.iv_delete).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				try {
					MyApplication.getDb().delete(datas.get(position));
					datas.remove(datas.get(position));
					notifyDataSetChanged();
				} catch (DbException e) {
					e.printStackTrace();
				}
			}
		});
		v.findViewById(R.id.iv_edit).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(mContext, EditActivity.class);
				intent.putExtra("dish", datas.get(position));
				mContext.startActivity(intent);
			}
		});
		v.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mContext, DetailActivity.class);
				intent.putExtra("dish", datas.get(position));
				mContext.startActivity(intent);
			}
		});
		return v;
	}

	@Override
	public int getSwipeLayoutResourceId(int arg0) {
		return R.id.swipe;
	}

	public void setDatas(List<Dish> findAll) {
		datas.clear();
		if (findAll != null) {
			for (int i = findAll.size() - 1; i >= 0; i--) {
				datas.add(findAll.get(i));
			}
		}
		notifyDataSetChanged();
	}

}
