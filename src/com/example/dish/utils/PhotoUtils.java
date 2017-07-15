package com.example.dish.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.graphics.Bitmap;
import android.graphics.Matrix;

public class PhotoUtils {

	public static String saveBitmap(String dirPath, Bitmap bitmap) {
		File file = new File(dirPath, getCurrentFileName() + ".jpg");
		String filepath = file.getPath();
		try {
			FileOutputStream bos = new FileOutputStream(file);
			Matrix matrix = new Matrix();
			matrix.postScale(0.5f, 0.5f);
			bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
			bos.flush();
			bos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
		}
		return filepath;
	}

	public static String getCurrentFileName() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		Date date = new Date(System.currentTimeMillis());
		String name = sdf.format(date);
		return name;
	}

	public static Bitmap compressBitmap(Bitmap bitmap) {
		Matrix matrix = new Matrix();
		matrix.postScale(0.7f, 0.7f);
		return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
	}
}
