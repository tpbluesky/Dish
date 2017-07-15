package com.example.dish.atys;

import com.example.dish.R;
import com.example.dish.utils.AudioUtils;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class RecordActivity extends Activity {
	AudioUtils audioTool;
	ImageView img_animation;
	AnimationDrawable animationDrawable;
	boolean flag = false;
	String path;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_record);
		audioTool = new AudioUtils();
		Button start = (Button) findViewById(R.id.but_save);
		Button stop = (Button) findViewById(R.id.but_cancle);
		img_animation = (ImageView) findViewById(R.id.img_animation);
		Listener listener = new Listener();
		start.setOnClickListener(listener);
		stop.setOnClickListener(listener);
		img_animation.setOnClickListener(listener);
	}

	class Listener implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.img_animation:
				flag = !flag;
				if (flag) {
					path = audioTool.startRecord();
					Start_frame();
				} else {

				}
				break;
			case R.id.but_cancle:
				finish();
				break;
			case R.id.but_save:
				audioTool.stopRecord();
				Stop_frame();
				Intent intent = getIntent();
				intent.putExtra("path", path);
				setResult(RESULT_OK, intent);
				finish();
				break;
			}
		}
	}

	public void Start_frame() {
		img_animation.setImageResource(R.drawable.animist);
		animationDrawable = (AnimationDrawable) img_animation.getDrawable();
		animationDrawable.start();
	}

	public void Stop_frame() {
		animationDrawable = (AnimationDrawable) img_animation.getDrawable();
		animationDrawable.stop();
	}
}
