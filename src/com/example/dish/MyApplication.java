package com.example.dish;

import org.xutils.DbManager;
import org.xutils.DbManager.DaoConfig;
import org.xutils.DbManager.DbOpenListener;
import org.xutils.x;

import com.example.dish.beans.User;

import android.app.Application;

public class MyApplication extends Application {

	private DaoConfig daoConfig;
	private static DbManager db;

	public static String DISH_IMAGE_PATH;

	private static User loginUser = null;

	@Override
	public void onCreate() {
		super.onCreate();
		x.Ext.init(this);
		daoConfig = new DbManager.DaoConfig().setDbName("dish.db").setDbVersion(1).setAllowTransaction(true)
				.setDbOpenListener(new DbOpenListener() {

					@Override
					public void onDbOpened(DbManager db) {
						db.getDatabase().enableWriteAheadLogging();

					}
				});
		db = x.getDb(daoConfig);
		new Constants(this);
	}

	public static DbManager getDb() {
		return db;
	}

	public static void setLoginUser(User user) {
		loginUser = user;
	}

	public static User getLoginUser() {
		return loginUser;
	}
}
