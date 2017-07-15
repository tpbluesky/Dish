package com.example.dish.adapters;

import java.util.List;

import org.xutils.x;
import org.xutils.ex.DbException;
import org.xutils.view.annotation.ViewInject;

import com.example.dish.Constants;
import com.example.dish.MyApplication;
import com.example.dish.R;
import com.example.dish.atys.DetailActivity;
import com.example.dish.beans.Dish;
import com.example.dish.beans.User;

import android.content.Context;
import android.content.Intent;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class IndexListAdapter extends MyBaseAdapter<Dish> {

	public IndexListAdapter(Context context, List<Dish> adapterDatas) {
		super(context, adapterDatas);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final Dish dish = adapterDatas.get(position);
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.layout_index_list_item, parent, false);
			holder = new ViewHolder(convertView);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		x.image().bind(holder.getIvDishImage(), dish.getImagePath());
		holder.getTvDishName().setText(dish.getName());
		holder.getTvPublishDate().setText(DateFormat.format("yyyy年mm月dd日   hh时mm分", dish.getPublishDate()));
		holder.getTvCategory().setText(dish.getCategory());
		User u = dish.getParent(MyApplication.getDb());
		if (u != null)
			holder.getTvAuthor().setText(u.getName());
		else {
			holder.getTvAuthor().setText("admin");
		}
		convertView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context, DetailActivity.class);
				intent.putExtra("dish", dish);
				context.startActivity(intent);
			}
		});
		return convertView;
	}

	class ViewHolder {

		@ViewInject(R.id.iv_dish_image)
		private ImageView ivDishImage;
		@ViewInject(R.id.tv_dish_name)
		private TextView tvDishName;
		@ViewInject(R.id.tv_publish_date)
		private TextView tvPublishDate;
		@ViewInject(R.id.tv_category)
		private TextView tvCategory;
		@ViewInject(R.id.tv_author)
		private TextView tvAuthor;

		public ViewHolder(View convertView) {
			tvDishName = (TextView) convertView.findViewById(R.id.tv_dish_name);
			tvPublishDate = (TextView) convertView.findViewById(R.id.tv_publish_date);
			tvCategory = (TextView) convertView.findViewById(R.id.tv_category);
			tvAuthor = (TextView) convertView.findViewById(R.id.tv_author);
			ivDishImage = (ImageView) convertView.findViewById(R.id.iv_dish_image);
		}

		public ImageView getIvDishImage() {
			return ivDishImage;
		}

		public void setIvDishImage(ImageView ivDishImage) {
			this.ivDishImage = ivDishImage;
		}

		public TextView getTvDishName() {
			return tvDishName;
		}

		public void setTvDishName(TextView tvDishName) {
			this.tvDishName = tvDishName;
		}

		public TextView getTvPublishDate() {
			return tvPublishDate;
		}

		public void setTvPublishDate(TextView tvPublishDate) {
			this.tvPublishDate = tvPublishDate;
		}

		public TextView getTvCategory() {
			return tvCategory;
		}

		public void setTvCategory(TextView tvCategory) {
			this.tvCategory = tvCategory;
		}

		public TextView getTvAuthor() {
			return tvAuthor;
		}

		public void setTvAuthor(TextView tvAuthor) {
			this.tvAuthor = tvAuthor;
		}

	}

}
