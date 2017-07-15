package com.example.dish.fragments;

import java.util.List;

import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import com.example.dish.MyApplication;
import com.example.dish.R;
import com.example.dish.adapters.IndexListAdapter;
import com.example.dish.beans.Dish;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ListView;

@ContentView(R.layout.fregment_index)
public class IndexFragment extends BaseFragment {

	@ViewInject(R.id.actv)
	private EditText actv;

	@ViewInject(R.id.lv_dishes)
	private ListView lvDish;

	private IndexListAdapter adapter;
	private List<Dish> dishData = null;

	private DbManager db;

	protected void initViews() {
		try {
			db = MyApplication.getDb();
			dishData = db.findAll(Dish.class);
			adapter = new IndexListAdapter(getActivity(), dishData);
			lvDish.setAdapter(adapter);
		} catch (DbException e) {
			e.printStackTrace();
		}

		actv.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				String text = s.toString();
				try {
					if (s.equals("")) {
						adapter.setDatas(db.findAll(Dish.class));
					} else {
						adapter.setDatas(db.selector(Dish.class).where("name", "like", "%" + text + "%")
								.or("category", "like", "%" + text + "%").findAll());
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

	public void setDataChange() {
		if (actv.getText().equals("")) {
			try {
				Log.e("IndexFragment", "setDataChange111");
				adapter.setDatas(db.findAll(Dish.class));
			} catch (DbException e) {
				e.printStackTrace();
			}
		} else {
			actv.setText("");
			Log.e("IndexFragment", "setDataChange222");
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		setDataChange();
	}

}
