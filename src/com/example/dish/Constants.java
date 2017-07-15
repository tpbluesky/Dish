package com.example.dish;

import java.io.File;

import android.content.Context;
import android.os.Environment;

public class Constants {

	public static String USER_ICON_PATH = null;
	public static String DISH_IMAGE_PATH = null;
	public static String DISH_AUDIO_PATH = null;
	public static String DISH_VIDEO_PATH = null;

	public Constants(Context context) {
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			USER_ICON_PATH = context.getExternalFilesDir("user_icons").getAbsolutePath();
			DISH_IMAGE_PATH = context.getExternalFilesDir("dish_images").getAbsolutePath();
			DISH_AUDIO_PATH = context.getExternalFilesDir("dish_audios").getAbsolutePath();
			DISH_VIDEO_PATH = context.getExternalFilesDir("dish_videos").getAbsolutePath();
		} else {
			USER_ICON_PATH = context.getFilesDir().getPath() + "/user_icons";
			DISH_IMAGE_PATH = context.getFilesDir().getPath() + "/dish_images";
			DISH_AUDIO_PATH = context.getFilesDir().getPath() + "/dish_audios";
			DISH_VIDEO_PATH = context.getFilesDir().getPath() + "dish_videos";
			File f1 = new File(USER_ICON_PATH);
			if (!f1.exists())
				f1.mkdir();
			File f2 = new File(DISH_IMAGE_PATH);
			if (!f2.exists())
				f2.mkdir();
			File f3 = new File(DISH_AUDIO_PATH);
			if (!f3.exists())
				f3.mkdir();
			File f4 = new File(DISH_VIDEO_PATH);
			if (!f4.exists())
				f4.mkdir();
		}
	}

}
