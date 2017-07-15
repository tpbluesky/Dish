package com.example.dish.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

import com.example.dish.atys.TakePhotoActivity;

import java.io.IOException;

public class CameraUtils {
	private TakePhotoActivity view;
	private Camera myCamera;
	private SurfaceView surfaceView;
	private SurfaceHolder holder;
	private Bitmap bitmp;

	public CameraUtils(TakePhotoActivity view, SurfaceView surfaceView) {
		this.view = view;
		this.surfaceView = surfaceView;
	}

	public SurfaceHolder getHolder() {
		return holder;
	}

	public void initCamera() {
		holder = surfaceView.getHolder();
		holder.addCallback(new Callback() {

			@Override
			public void surfaceDestroyed(SurfaceHolder holder) {
				stopCamera();
			}

			@Override
			public void surfaceCreated(SurfaceHolder holder) {
				if (myCamera == null) {
					myCamera = Camera.open();
				}
				Camera.Parameters param = myCamera.getParameters();
				param.setPictureFormat(ImageFormat.JPEG);
				myCamera.setParameters(param);
				try {
					myCamera.setPreviewDisplay(holder);
					myCamera.setDisplayOrientation(90);
					startCamera();
				} catch (IOException e) {
					e.printStackTrace();
				}

			}

			@Override
			public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
			}
		});
	}

	public void startCamera() {
		myCamera.startPreview();

	}

	public void stopCamera() {
		if (myCamera == null) {
			return;
		}
		myCamera.stopPreview();
		myCamera.release();
		myCamera = null;

	}

	public void takePhoto() {
		ShutterCallback shutter = new ShutterCallback() {
			@Override
			public void onShutter() {

			}
		};
		PictureCallback raw = new PictureCallback() {

			@Override
			public void onPictureTaken(byte[] data, Camera camera) {

			}
		};
		PictureCallback jpeg = new PictureCallback() {
			@Override
			public void onPictureTaken(byte[] data, Camera camera) {
				bitmp = BitmapFactory.decodeByteArray(data, 0, data.length);
				Log.e("TakePhoto", bitmp.toString());
				view.showPhoto(bitmp);
			}
		};
		myCamera.takePicture(shutter, raw, jpeg);
	}

}
