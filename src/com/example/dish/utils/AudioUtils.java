package com.example.dish.utils;

import android.media.MediaRecorder;
import android.os.Environment;
import android.util.Log;

import java.io.File;

import com.example.dish.Constants;

/**
 * Created by lxcy on 2017/7/13.
 */
public class AudioUtils {
	MediaRecorder recorder = null;

	public String startRecord() {
		if (recorder == null)
			recorder = new MediaRecorder();
		recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
		recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
		recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
		String path = Constants.DISH_AUDIO_PATH + "/" + PhotoUtils.getCurrentFileName() + ".3gp";
		recorder.setOutputFile(path);
		try {
			recorder.prepare();
		} catch (Exception e) {
			e.printStackTrace();
		}
		recorder.start();
		return path;
	}

	public void stopRecord() {
		if (recorder != null) {
			recorder.stop();
			recorder.release();
			recorder = null;
		}
	}
}
