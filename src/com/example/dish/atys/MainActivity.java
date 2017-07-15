package com.example.dish.atys;

import java.util.ArrayList;
import java.util.List;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import com.example.dish.R;
import com.example.dish.adapters.MainFragmentAdapter;
import com.example.dish.fragments.AddFragment;
import com.example.dish.fragments.IndexFragment;
import com.example.dish.fragments.MineFragment;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

@ContentView(R.layout.activity_main)
public class MainActivity extends BaseActivity {

	@ViewInject(R.id.tv_title)
	private TextView tvTitle;
	@ViewInject(R.id.main_content)
	private ViewPager mainContent;

	@ViewInject(R.id.ll_add)
	private LinearLayout llIndex;
	@ViewInject(R.id.ll_add)
	private LinearLayout llAdd;
	@ViewInject(R.id.ll_mine)
	private LinearLayout llMine;

	@ViewInject(R.id.iv_index)
	private ImageView ivIndex;
	@ViewInject(R.id.iv_add)
	private ImageView ivAdd;
	@ViewInject(R.id.iv_mine)
	private ImageView ivMine;

	@ViewInject(R.id.tv_index)
	private TextView tvIndex;
	@ViewInject(R.id.tv_add)
	private TextView tvAdd;
	@ViewInject(R.id.tv_mine)
	private TextView tvMine;

	private MainFragmentAdapter adapter;

	private List<Fragment> list;
	private IndexFragment indexFragment;

	@Override
	protected void initViews() {
		list = new ArrayList<Fragment>();
		indexFragment = new IndexFragment();
		list.add(indexFragment);
		list.add(new AddFragment());
		list.add(new MineFragment());
		adapter = new MainFragmentAdapter(getSupportFragmentManager(), list);
		mainContent.setAdapter(adapter);
		setSelect(0);
	}

	public void setPage(int pos) {
		mainContent.setCurrentItem(pos);
	}

	@Event(value = R.id.main_content, type = ViewPager.OnPageChangeListener.class, method = "onPageSelected")
	private void onPageSelected(int position) {
		setSelect(position);
	}

	@Event(value = { R.id.ll_index, R.id.ll_add, R.id.ll_mine }, type = View.OnClickListener.class)
	private void onClick(View v) {
		switch (v.getId()) {
		case R.id.ll_index:
			mainContent.setCurrentItem(0, true);
			setSelect(0);
			break;
		case R.id.ll_add:
			mainContent.setCurrentItem(1, true);
			setSelect(1);
			break;
		case R.id.ll_mine:
			mainContent.setCurrentItem(2, true);
			setSelect(2);
			break;

		default:
			break;
		}
	}

	// 上一次点击返回键的时间
	private long mFirstClick = 0;

	/**
	 * 当用户在一秒内点击两次返回键才退出程序，否则不退出程序
	 */
	@Override
	public void onBackPressed() {
		long secondClick = System.currentTimeMillis();
		if (secondClick - mFirstClick > 1000) {
			showShortToast("再按一次返回键退出菜谱大全");
			mFirstClick = secondClick;
		} else {
			super.onBackPressed();
		}
	}

	private void setSelect(int position) {
		tvIndex.setTextColor(Color.BLACK);
		tvAdd.setTextColor(Color.BLACK);
		tvMine.setTextColor(Color.BLACK);
		ivIndex.setSelected(false);
		ivAdd.setSelected(false);
		ivMine.setSelected(false);
		switch (position) {
		case 0:
			tvTitle.setText(getResources().getString(R.string.text_index));
			tvIndex.setTextColor(getResources().getColor(R.color.app_theme_color));
			ivIndex.setSelected(true);
			break;
		case 1:
			tvTitle.setText(getResources().getString(R.string.text_add));
			tvAdd.setTextColor(getResources().getColor(R.color.app_theme_color));
			ivAdd.setSelected(true);
			break;
		case 2:
			tvTitle.setText(getResources().getString(R.string.text_mine));
			tvMine.setTextColor(getResources().getColor(R.color.app_theme_color));
			ivMine.setSelected(true);
			break;

		default:
			break;
		}
	}

	public void notifyDataChange() {
		Log.e("MainActivity", "notifyDataChange");
		indexFragment.setDataChange();
	}
}
