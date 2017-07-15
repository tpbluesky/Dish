package com.example.dish.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SPUtils {
	private static final String SHARE_PRES_NAME = "account";
	private SharedPreferences sp;

	public SPUtils(Context context) {
		sp = context.getSharedPreferences(SHARE_PRES_NAME, Context.MODE_PRIVATE);
	}

	public String getAccount() {
		return sp.getString("account", null);
	}
	
	public String getPassword() {
		return sp.getString("pwd", null);
	}

	public void saveAccount(String account, String password) {
		Editor edit = sp.edit();
		edit.putString("account", account);
		edit.putString("pwd", password);
		edit.commit();
	}

	public void removeAccount() {
		Editor edit = sp.edit();
		edit.clear();
		edit.commit();
	}

}
