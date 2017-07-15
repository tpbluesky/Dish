package com.example.dish.atys;

import org.xutils.x;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

public abstract class BaseActivity extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		x.view().inject(this);
		initViews();
	}

	protected abstract void initViews();

	protected void showShortToast(String msg) {
		Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
	}

	protected void showLongToast(String msg) {
		Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
	}

	protected void showAlertDialog(String title, String msg, String btnMesg) {
		new AlertDialog.Builder(this).setTitle(title).setMessage(msg).setPositiveButton(btnMesg, null).create().show();
	}

	protected void showDefaultAlertDialog(String title, String msg) {
		new AlertDialog.Builder(this).setTitle(title).setMessage(msg).setPositiveButton("È·¶¨", null).create().show();
	}

}
