package com.example.dish.atys;

import java.util.ArrayList;
import java.util.List;

import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import com.example.dish.MyApplication;
import com.example.dish.R;
import com.example.dish.R.layout;
import com.example.dish.R.menu;
import com.example.dish.adapters.IndexListAdapter;
import com.example.dish.beans.Collect;
import com.example.dish.beans.Dish;
import com.example.dish.beans.User;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.app.Activity;
import android.view.Menu;
import android.widget.EditText;
import android.widget.ListView;

@ContentView(R.layout.activity_collect)
public class CollectActivity extends BaseActivity {

	@ViewInject(R.id.actv)
	private EditText actv;

	@ViewInject(R.id.lv_my_collect)
	private ListView lvDish;

	private IndexListAdapter adapter;
	private List<Dish> dishData = null;

	private DbManager db;

	private User user = null;

	protected void initViews() {
		try {

			db = MyApplication.getDb();
			user = MyApplication.getLoginUser();
			// db.save(new Collect(1, 1));
			// db.save(new Collect(1, 2));

			dishData = new ArrayList<Dish>();
			List<Collect> datas = db.selector(Collect.class).where("userId", "=", user.getId()).findAll();
			if (datas != null) {
				for (Collect collect : datas) {
					dishData.add(collect.getDish(db));
				}
			}
			if (dishData != null) {
				adapter = new IndexListAdapter(this, dishData);
				lvDish.setAdapter(adapter);
			}
		} catch (DbException e) {
			e.printStackTrace();
		}

		actv.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				String text = s.toString();
				try {
					if (s.equals("")) {
						List<Collect> datas = db.selector(Collect.class).where("userId", "=", user.getId()).findAll();
						dishData.clear();
						for (Collect collect : datas) {
							dishData.add(collect.getDish(db));
						}
						adapter.setDatas(dishData);
					} else {
						List<Collect> datas = db.selector(Collect.class).where("userId", "=", user.getId()).findAll();
						dishData.clear();
						for (Collect collect : datas) {
							Dish dish = collect.getDish(db);
							if (dish.getName().contains(text) || dish.getCategory().contains(text)) {
								dishData.add(dish);
							}
						}
						adapter.setDatas(dishData);
					}
				} catch (DbException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});

	}

	@Override
	protected void onResume() {
		actv.setText("");
		super.onResume();
	}

}
