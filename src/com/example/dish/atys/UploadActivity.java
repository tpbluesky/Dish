package com.example.dish.atys;

import java.util.List;

import org.xutils.DbManager;
import org.xutils.db.sqlite.WhereBuilder;
import org.xutils.ex.DbException;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import com.example.dish.MyApplication;
import com.example.dish.R;
import com.example.dish.R.layout;
import com.example.dish.R.menu;
import com.example.dish.adapters.IndexListAdapter;
import com.example.dish.adapters.UploadListAdapter;
import com.example.dish.beans.Dish;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.app.Activity;
import android.view.Menu;
import android.widget.EditText;
import android.widget.ListView;

@ContentView(R.layout.activity_my_upload)
public class UploadActivity extends BaseActivity {

	@ViewInject(R.id.lv_my_uupload)
	private ListView lvMyUpload;

	@ViewInject(R.id.actv)
	private EditText actv;

	private UploadListAdapter adapter;
	private List<Dish> dishData = null;

	private DbManager db;
	private int userId;

	private boolean firstIn = true;

	protected void initViews() {
		try {
			db = MyApplication.getDb();
			dishData = db.selector(Dish.class).where("owner", "=", MyApplication.getLoginUser().getId()).findAll();
			if (dishData != null) {
				adapter = new UploadListAdapter(this, dishData);
				lvMyUpload.setAdapter(adapter);
			}
		} catch (DbException e) {
			e.printStackTrace();
		}

		actv.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				String text = s.toString();
				Log.e("UploadActivity", text);
				try {
					userId = MyApplication.getLoginUser().getId();
					if (s.equals("")) {
						adapter.setDatas(db.selector(Dish.class).where("owner", "=", userId).findAll());
					} else {
						WhereBuilder builder = WhereBuilder.b("name", "like", "%" + text + "%");
						builder.or("category", "like", "%" + text + "%");
						adapter.setDatas(db.selector(Dish.class).where("owner", "=", userId).and(builder).findAll());
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
