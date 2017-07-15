package com.example.dish.adapters;

import java.util.ArrayList;
import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class MainFragmentAdapter extends FragmentPagerAdapter {

	private List<Fragment> list;

	public MainFragmentAdapter(FragmentManager fm, List<Fragment> list) {
		super(fm);
		this.list = new ArrayList<Fragment>();
		this.list.addAll(list);
	}

	@Override
	public Fragment getItem(int position) {
		return list.get(position);
	}

	@Override
	public int getCount() {
		return list.size();
	}

}
