package com.example.dish.atys;

import com.example.dish.R;
import com.example.dish.utils.CameraUtils;
import com.example.dish.utils.PhotoUtils;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

public class TakePhotoActivity extends Activity implements View.OnClickListener {

	private SurfaceView sv;
	private ImageView image_takepic;
	private CameraUtils camera;
	private PhotoUtils model;
	private Bitmap bitmap;
	private ImageView image_cancle;
	private ImageView image_add;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_take_photo);
		sv = (SurfaceView) findViewById(R.id.sv_takephone);
		image_takepic = (ImageView) findViewById(R.id.image_takepic);
		image_add = (ImageView) findViewById(R.id.image_add);
		image_cancle = (ImageView) findViewById(R.id.image_cancle);

		camera = new CameraUtils(TakePhotoActivity.this, sv);
		camera.initCamera();

		image_takepic.setOnClickListener(this);
		image_add.setOnClickListener(this);
		image_cancle.setOnClickListener(this);

	}

	public void showPhoto(Bitmap bitmap) {
		this.bitmap = bitmap;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		switch (v.getId()) {
		case R.id.image_takepic:
			camera.takePhoto();
			image_takepic.setVisibility(View.INVISIBLE);
			image_add.setVisibility(View.VISIBLE);
			image_cancle.setVisibility(View.VISIBLE);
			break;
		case R.id.image_add:
			String path = PhotoUtils.saveBitmap(getFilesDir().getPath() + "/images/", bitmap);
			Intent intent = getIntent();
			intent.putExtra("path", path);
			setResult(RESULT_OK, intent);
			finish();
			break;
		case R.id.image_cancle:
			/*
			 * image_takepic.setVisibility(View.VISIBLE);
			 * image_add.setVisibility(View.INVISIBLE);
			 * image_cancle.setVisibility(View.INVISIBLE);
			 */
			setResult(RESULT_CANCELED);
			finish();
			break;
		default:
			break;
		}
	}
}
