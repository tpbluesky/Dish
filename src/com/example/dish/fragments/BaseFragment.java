package com.example.dish.fragments;

import org.xutils.x;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public abstract class BaseFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = x.view().inject(this, inflater, container);
		initViews();
		return view;
	}

	protected abstract void initViews();

	protected void showShortToast(String msg) {
		Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
	}

	protected void showLongToast(String msg) {
		Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
	}

	protected void showAlertDialog(String title, String msg, String btnMesg) {
		new AlertDialog.Builder(getActivity()).setTitle(title).setMessage(msg).setPositiveButton(btnMesg, null).create()
				.show();
	}

	protected void showDefaultAlertDialog(String title, String msg) {
		new AlertDialog.Builder(getActivity()).setTitle(title).setMessage(msg).setPositiveButton("È·¶¨", null).create()
				.show();
	}
}
