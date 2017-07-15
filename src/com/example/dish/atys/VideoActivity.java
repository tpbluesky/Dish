package com.example.dish.atys;

import java.io.File;

import org.xutils.x;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import com.example.dish.Constants;
import com.example.dish.R;
import com.example.dish.utils.PhotoUtils;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.PixelFormat;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * Description: 使用MediaRecorder录制视频
 * 
 * @author jph Date:2014.08.14 <br/>
 */
public class VideoActivity extends Activity {
	// 程序中的两个按钮

	@ViewInject(R.id.begin_record)
	private ImageView ivBegin;
	@ViewInject(R.id.stop_record)
	private ImageView ivStop;
	// 系统的视频文件
	File videoFile;
	MediaRecorder mRecorder;
	// 显示视频预览的SurfaceView
	SurfaceView sView;
	// 记录是否正在进行录制
	private boolean isRecording = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 去掉标题栏 ,必须放在setContentView之前
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_video);
		x.view().inject(this);
		// 设置横屏显示
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER_LANDSCAPE);
		// 设置全屏
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		// 选择支持半透明模式,在有surfaceview的activity中使用。
		getWindow().setFormat(PixelFormat.TRANSLUCENT);
		// 获取程序界面中的SurfaceView
		sView = (SurfaceView) this.findViewById(R.id.sView);
		// 设置分辨率
		sView.getHolder().setFixedSize(1280, 720);
		// 设置该组件让屏幕不会自动关闭
		sView.getHolder().setKeepScreenOn(true);
	}

	@Event(value = { R.id.begin_record, R.id.stop_record }, type = View.OnClickListener.class)
	private void onClick(View v) {
		switch (v.getId()) {
		// 单击录制按钮
		case R.id.begin_record:
			try {
				// 创建保存录制视频的视频文件
				videoFile = new File(Constants.DISH_VIDEO_PATH, PhotoUtils.getCurrentFileName() + ".3gp");
				// 创建MediaPlayer对象
				mRecorder = new MediaRecorder();
				mRecorder.reset();
				// 设置从麦克风采集声音(或来自录像机的声音AudioSource.CAMCORDER)
				mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
				// 设置从摄像头采集图像
				mRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
				// 设置视频文件的输出格式
				// 必须在设置声音编码格式、图像编码格式之前设置
				mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
				// 设置声音编码的格式
				mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
				// 设置图像编码的格式
				mRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
				mRecorder.setVideoSize(1280, 720);
				// 每秒 4帧
				mRecorder.setVideoFrameRate(20);
				mRecorder.setOutputFile(videoFile.getAbsolutePath());
				// 指定使用SurfaceView来预览视频
				mRecorder.setPreviewDisplay(sView.getHolder().getSurface()); // ①
				mRecorder.prepare();
				// 开始录制
				mRecorder.start();
				// 让record按钮不可用。
				ivBegin.setVisibility(View.GONE);
				// 让stop按钮可用。
				ivStop.setVisibility(View.VISIBLE);
				isRecording = true;
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		// 单击停止按钮
		case R.id.stop_record:
			// 如果正在进行录制
			if (isRecording) {
				// 停止录制
				mRecorder.stop();
				// 释放资源
				mRecorder.release();
				mRecorder = null;
				ivBegin.setVisibility(View.VISIBLE);
				ivStop.setVisibility(View.GONE);
				setResult(RESULT_OK,getIntent().putExtra("path", videoFile.getAbsolutePath()));
				finish();
			}
			break;
		}
	}
}
