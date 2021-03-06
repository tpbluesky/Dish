package com.example.dish.fragments;

import java.sql.Date;

import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import com.example.dish.Constants;
import com.example.dish.MyApplication;
import com.example.dish.R;
import com.example.dish.atys.MainActivity;
import com.example.dish.atys.RecordActivity;
import com.example.dish.atys.VideoActivity;
import com.example.dish.beans.Dish;
import com.example.dish.beans.User;
import com.example.dish.utils.PhotoUtils;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

@ContentView(R.layout.fragment_add)
public class AddFragment extends BaseFragment {

	DbManager db = null;
	Bitmap bitmap = null;
	public static final int AUDIO_RECORD = 100;
	public static final int PHOTO_SELECT = 200;
	public static final int TAKE_PHOTOS = 300;
	public static final int VIDEO_RECORD = 400;
	private static final int TAKE_PHOTO = 101;
	final String IMAGE_TYPE = "image/*";
	@ViewInject(R.id.tv_dish_name)
	private EditText tvDishName;
	@ViewInject(R.id.sp_dish_category)
	private Spinner spDishCategory;
	@ViewInject(R.id.iv_dish_image)
	private ImageView ivDishImage;

	@ViewInject(R.id.tv_audio_hint)
	private TextView tvAudioHint;
	@ViewInject(R.id.tv_video_hint)
	private TextView tvVideoHint;

	@ViewInject(R.id.tv_dish_material)
	private EditText tvDishMaterial;
	@ViewInject(R.id.tv_dish_method)
	private EditText tvDishMethod;

	@ViewInject(R.id.btn_upload)
	private Button Add_Upload;

	@ViewInject(R.id.btn_camera)
	private Button Add_TakePhoto;

	@ViewInject(R.id.btn_cancle)
	private Button Add_Cancle;

	@ViewInject(R.id.btn_finish)
	private Button Add_Finish;

	@ViewInject(R.id.btn_microp)
	private ImageView Add_RecordAudio;
	@ViewInject(R.id.btn_video)
	private ImageView Add_RecordVideo;

	private User user = null;

	public AddFragment() {
	}

	@Override
	protected void initViews() {
		user = MyApplication.getLoginUser();
		db = MyApplication.getDb();
		Init_Add();

	}

	public void Init_Add() {
		Listener listener = new Listener();
		Add_Upload.setOnClickListener(listener);
		Add_TakePhoto.setOnClickListener(listener);
		Add_RecordAudio.setOnClickListener(listener);
		Add_RecordVideo.setOnClickListener(listener);
		Add_Finish.setOnClickListener(listener);
		Add_Cancle.setOnClickListener(listener);
	}

	class Listener implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btn_upload:
				Intent pickPhoto = new Intent(Intent.ACTION_PICK, null);
				pickPhoto.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
				startActivityForResult(pickPhoto, PHOTO_SELECT);
				break;
			case R.id.btn_camera:
				Intent takePhoto = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				startActivityForResult(takePhoto, TAKE_PHOTO);
				break;
			case R.id.btn_microp:
				Intent intent2 = new Intent(getActivity(), RecordActivity.class);
				startActivityForResult(intent2, AUDIO_RECORD);
				break;
			case R.id.btn_video:
				Intent intent = new Intent(getActivity(), VideoActivity.class);
				startActivityForResult(intent, VIDEO_RECORD);
				break;
			case R.id.btn_finish:
				if (check()) {
					clearInfor();
					MainActivity activity = (MainActivity) getActivity();
					activity.notifyDataChange();
					activity.setPage(0);
				}
				break;
			case R.id.btn_cancle:
				clearInfor();
				break;
			// finish();
			}
		}
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode != Activity.RESULT_OK || data == null)
			return;
		switch (requestCode) {
		case PHOTO_SELECT:
			Uri selectedImage = data.getData();
			String[] filePathColumns = { MediaStore.Images.Media.DATA };
			Cursor c = getActivity().getContentResolver().query(selectedImage, filePathColumns, null, null, null);
			c.moveToFirst();
			String imagePath = c.getString(0);
			Bitmap bm = BitmapFactory.decodeFile(imagePath);
			String pickPath = PhotoUtils.saveBitmap(Constants.DISH_IMAGE_PATH, bm);
			ivDishImage.setImageBitmap(PhotoUtils.compressBitmap(bm));
			dishImagePath = pickPath;
			// 保存pickPath
			break;
		case TAKE_PHOTO:
			Bundle bundle = data.getExtras();
			Bitmap bitmap = (Bitmap) bundle.get("data");
			String takePath = PhotoUtils.saveBitmap(Constants.DISH_IMAGE_PATH, bitmap);
			ivDishImage.setImageBitmap(bitmap);
			dishImagePath = takePath;
			// 保存takePath
			break;
		case AUDIO_RECORD:
			String audioPath = data.getStringExtra("path");
			dishAudioPath = audioPath;
			// 保存audioPath
			tvAudioHint.setText("音频已录制^_^");
			break;
		case VIDEO_RECORD:
			String videoPath = data.getStringExtra("path");
			// 保存videoPath
			dishVideoPath = videoPath;
			tvVideoHint.setText("视频已录制^_^");
			break;
		default:
			break;
		}
	}

	private String dishImagePath = null, dishAudioPath = null, dishVideoPath = null;

	public boolean check() {
		Dish d = new Dish();
		String name = tvDishName.getText().toString().trim();
		if (name.equals("")) {
			showShortToast("请给你的菜取个名字哦!");
			return false;
		}
		d.setName(name);

		String[] stringArray = getResources().getStringArray(R.array.food_style);
		int pos = spDishCategory.getSelectedItemPosition();
		if (pos == Spinner.INVALID_POSITION || pos == 0) {
			showShortToast("请选择菜系");
			return false;
		}
		String category = stringArray[pos];
		d.setCategory(category);

		if (dishImagePath == null) {
			showShortToast("请给你的菜上传照片");
			return false;
		}
		d.setImagePath(dishImagePath);
		if (dishAudioPath != null)
			d.setAudiopath(dishAudioPath);
		if (dishVideoPath != null)
			d.setVideopath(dishVideoPath);
		String material = tvDishMaterial.getText().toString().trim();
		if (material.equals("")) {
			showShortToast("请输入所需材料");
			return false;
		}
		d.setMaterial(material);

		String method = tvDishMethod.getText().toString().trim();
		if (method.equals("")) {
			showShortToast("请输入菜的做法步骤");
			return false;
		}
		d.setMethod(method);
		d.setOwner(user.getId());
		d.setPublishDate(new Date(System.currentTimeMillis()));
		try {
			db.save(d);
			showShortToast("上传菜谱成功！");
		} catch (DbException e) {
			e.printStackTrace();
		}
		return true;
	}

	public void clearInfor() {
		tvDishName.setText("");
		tvDishMaterial.setText("");
		tvDishMethod.setText("");
		ivDishImage.setImageResource(R.drawable.ic_launcher);
		spDishCategory.setSelection(0);
		tvAudioHint.setText("还没有录音频哦");
		tvVideoHint.setText("还没有录视频哦");
	}
}
