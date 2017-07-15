package com.example.dish.fragments;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.xutils.x;
import org.xutils.ex.DbException;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import com.example.dish.Constants;
import com.example.dish.MyApplication;
import com.example.dish.R;
import com.example.dish.atys.CollectActivity;
import com.example.dish.atys.LoginActivity;
import com.example.dish.atys.UploadActivity;
import com.example.dish.beans.User;
import com.example.dish.utils.PhotoUtils;
import com.example.dish.utils.SPUtils;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

@ContentView(R.layout.fragment_mine)
public class MineFragment extends BaseFragment {

	@ViewInject(R.id.tv_mine_account)
	private TextView tvMineAccount;

	@ViewInject(R.id.tv_mine_username)
	private TextView tvMineUsername;

	@ViewInject(R.id.tv_mine_name_update)
	private TextView tvMineNameUpdate;

	@ViewInject(R.id.iv_user_icon)
	private ImageView ivUserIcon;

	private User user;

	public MineFragment() {
	}

	@Override
	protected void initViews() {
		user = MyApplication.getLoginUser();
		if (!TextUtils.isEmpty(user.getUserIcon())) {
			Bitmap bitmap = BitmapFactory.decodeFile(user.getUserIcon());
			if (bitmap != null) {
				ivUserIcon.setImageBitmap(toRoundBitmap(bitmap));
			}
		}
		tvMineAccount.setText(user.getPhoneNumber());
		tvMineUsername.setText(user.getName());
	}

	@Event(value = { R.id.ll_upload, R.id.ll_collect, R.id.ll_changepwd, R.id.ll_logout, R.id.tv_mine_name_update,
			R.id.iv_user_icon }, type = View.OnClickListener.class)
	private void onClick(View v) {
		switch (v.getId()) {
		case R.id.ll_upload:
			startActivity(new Intent(getActivity(), UploadActivity.class));
			break;
		case R.id.ll_collect:
			startActivity(new Intent(getActivity(), CollectActivity.class));
			break;
		case R.id.ll_logout:
			new SPUtils(getActivity()).removeAccount();
			startActivity(new Intent(getActivity(), LoginActivity.class));
			getActivity().finish();
			break;
		case R.id.iv_user_icon:
			AlertDialog userIcon = new AlertDialog.Builder(getActivity()).setTitle("上传头像")
					.setItems(new String[] { "拍照", "从相册选择" }, new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							switch (which) {
							case 0:
								picTyTakePhoto();
								break;
							case 1:
								pickPhoto();
								break;

							default:
								break;
							}
						}
					}).create();
			userIcon.show();
			break;
		case R.id.ll_changepwd:
			View root = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_change_password, null);
			final AlertDialog dialogPwd = new AlertDialog.Builder(getActivity()).setView(root).setTitle("修改密码")
					.setCancelable(false).setPositiveButton("确定", null).setNegativeButton("取消", null).create();
			final EditText etOldPwd = (EditText) root.findViewById(R.id.et_old_pwd);
			final EditText etNewPwd = (EditText) root.findViewById(R.id.et_new_pwd);
			final EditText etRenewPwd = (EditText) root.findViewById(R.id.et_renew_pwd);
			dialogPwd.setOnShowListener(new DialogInterface.OnShowListener() {

				@Override
				public void onShow(DialogInterface d) {
					Button positionButton = dialogPwd.getButton(AlertDialog.BUTTON_POSITIVE);
					Button negativeButton = dialogPwd.getButton(AlertDialog.BUTTON_NEGATIVE);
					positionButton.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							String oldPwd = etOldPwd.getText().toString().trim();
							String newPwd = etNewPwd.getText().toString().trim();
							String renewPwd = etRenewPwd.getText().toString().trim();
							// 先判断密码是否正确,不正确返回
							// 判断两次输入的密码是都一致并且不同时为空
							if (!user.getPasword().equals(oldPwd)) {
								showShortToast("原密码不正确");
								return;
							}
							if (!newPwd.equals(renewPwd)) {
								showShortToast("两次输入的密码不一致");
								return;
							} else {
								if (newPwd.equals("")) {
									showShortToast("新密码不能为空");
									return;
								}
							}
							user.setPasword(newPwd);
							try {
								MyApplication.getDb().saveOrUpdate(user);
							} catch (DbException e) {
								e.printStackTrace();
							}
							dialogPwd.dismiss();
						}
					});
				}
			});
			dialogPwd.show();
			break;
		case R.id.tv_mine_name_update:
			View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_change_username, null);
			final AlertDialog dialog = new AlertDialog.Builder(getActivity()).setView(view).setTitle("修改用户名")
					.setCancelable(false).setPositiveButton("确定", null).setNegativeButton("取消", null).create();
			final EditText editText = (EditText) view.findViewById(R.id.et_name);
			dialog.setOnShowListener(new DialogInterface.OnShowListener() {

				@Override
				public void onShow(DialogInterface d) {
					Button positionButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
					Button negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
					positionButton.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							String name = editText.getText().toString().trim();
							if (TextUtils.isEmpty(name)) {
								showLongToast("用户名不能为空");
								return;
							}
							// 做修改User信息的操作
							user.setName(name);
							try {
								MyApplication.getDb().saveOrUpdate(user);
								tvMineUsername.setText(name);
							} catch (DbException e) {
								e.printStackTrace();
							}
							dialog.dismiss();
						}
					});
				}
			});
			dialog.show();
			break;

		default:
			break;
		}
	}

	// 使用照相机拍照获取图片
	public static final int TAKE_PHOTO_CODE = 1;
	// 使用相册中的图片
	public static final int SELECT_PIC_CODE = 2;
	// 图片裁剪
	private static final int PHOTO_CROP_CODE = 3;
	// 定义图片的Uri
	private Uri photoUri;
	// 图片文件路径
	private String picPath;

	/**
	 * 拍照获取图片
	 */
	private void picTyTakePhoto() {
		// 判断SD卡是否存在
		String SDState = Environment.getExternalStorageState();
		if (SDState.equals(Environment.MEDIA_MOUNTED)) {
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);// "android.media.action.IMAGE_CAPTURE"
			/***
			 * 使用照相机拍照，拍照后的图片会存放在相册中。使用这种方式好处就是：获取的图片是拍照后的原图，
			 * 如果不实用ContentValues存放照片路径的话，拍照后获取的图片为缩略图有可能不清晰
			 */
			ContentValues values = new ContentValues();
			photoUri = getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
			intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, photoUri);
			startActivityForResult(intent, TAKE_PHOTO_CODE);
		} else {
			Toast.makeText(getActivity(), "内存卡不存在", Toast.LENGTH_LONG).show();
		}
	}

	/***
	 * 从相册中取图片
	 */
	private void pickPhoto() {
		Intent intent = new Intent(Intent.ACTION_PICK, null);
		intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
		startActivityForResult(intent, SELECT_PIC_CODE);
	}

	/**
	 * @param
	 * @description 裁剪图片
	 * @author ldm
	 * @time 2016/11/30 15:19
	 */
	private void startPhotoZoom(Uri uri, int REQUE_CODE_CROP) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		// crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
		intent.putExtra("crop", "true");
		// 去黑边
		intent.putExtra("scale", true);
		intent.putExtra("scaleUpIfNeeded", true);
		// aspectX aspectY 是宽高的比例，根据自己情况修改
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX outputY 是裁剪图片宽高像素
		intent.putExtra("outputX", 300);
		intent.putExtra("outputY", 300);
		intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
		// 取消人脸识别功能
		intent.putExtra("noFaceDetection", true);
		// 设置返回的uri
		intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
		// 设置为不返回数据
		intent.putExtra("return-data", false);
		startActivityForResult(intent, REQUE_CODE_CROP);
	}

	/**
	 * @param
	 * @description 把Uri转换为文件路径
	 * @author ldm
	 * @time 2016/11/30 15:22
	 */
	private String uriToFilePath(Uri uri) {
		// 获取图片数据
		String[] proj = { MediaStore.Images.Media.DATA };
		// 查询
		Cursor cursor = getActivity().managedQuery(uri, proj, null, null, null);
		// 获得用户选择的图片的索引值
		int image_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		// 返回图片路径
		return cursor.getString(image_index);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == getActivity().RESULT_OK) {
			// 从相册取图片，有些手机有异常情况，请注意
			if (requestCode == SELECT_PIC_CODE) {
				if (null != data && null != data.getData()) {
					photoUri = data.getData();
					picPath = uriToFilePath(photoUri);
					startPhotoZoom(photoUri, PHOTO_CROP_CODE);
				} else {
					Toast.makeText(getActivity(), "图片选择失败", Toast.LENGTH_LONG).show();
				}
			} else if (requestCode == TAKE_PHOTO_CODE) {
				String[] pojo = { MediaStore.Images.Media.DATA };
				Cursor cursor = getActivity().managedQuery(photoUri, pojo, null, null, null);
				if (cursor != null) {
					int columnIndex = cursor.getColumnIndexOrThrow(pojo[0]);
					cursor.moveToFirst();
					picPath = cursor.getString(columnIndex);
					if (Build.VERSION.SDK_INT < 14) {
						cursor.close();
					}
				}
				if (picPath != null) {
					photoUri = Uri.fromFile(new File(picPath));
					startPhotoZoom(photoUri, PHOTO_CROP_CODE);
				} else {
					Toast.makeText(getActivity(), "图片选择失败", Toast.LENGTH_LONG).show();
				}
			} else if (requestCode == PHOTO_CROP_CODE) {
				if (photoUri != null) {
					Bitmap bitmap = BitmapFactory.decodeFile(picPath);
					if (bitmap != null) {
						Bitmap roundBitmap = toRoundBitmap(bitmap);
						String path = PhotoUtils.saveBitmap(Constants.USER_ICON_PATH, roundBitmap);
						ivUserIcon.setImageBitmap(roundBitmap);
						user.setUserIcon(path);
						try {
							MyApplication.getDb().saveOrUpdate(user);
						} catch (DbException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
	}

	public Bitmap toRoundBitmap(Bitmap bitmap) {
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		float roundPx;
		float left, top, right, bottom, dst_left, dst_top, dst_right, dst_bottom;
		if (width <= height) {
			roundPx = width / 2;
			top = 0;
			bottom = width;
			left = 0;
			right = width;
			height = width;
			dst_left = 0;
			dst_top = 0;
			dst_right = width;
			dst_bottom = width;
		} else {
			roundPx = height / 2;
			float clip = (width - height) / 2;
			left = clip;
			right = width - clip;
			top = 0;
			bottom = height;
			width = height;
			dst_left = 0;
			dst_top = 0;
			dst_right = height;
			dst_bottom = height;
		}
		Bitmap output = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(output);
		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect src = new Rect((int) left, (int) top, (int) right, (int) bottom);
		final Rect dst = new Rect((int) dst_left, (int) dst_top, (int) dst_right, (int) dst_bottom);
		final RectF rectF = new RectF(dst);
		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
		canvas.drawBitmap(bitmap, src, dst, paint);
		return output;
	}
}
