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
 * Description: ʹ��MediaRecorder¼����Ƶ
 * 
 * @author jph Date:2014.08.14 <br/>
 */
public class VideoActivity extends Activity {
	// �����е�������ť

	@ViewInject(R.id.begin_record)
	private ImageView ivBegin;
	@ViewInject(R.id.stop_record)
	private ImageView ivStop;
	// ϵͳ����Ƶ�ļ�
	File videoFile;
	MediaRecorder mRecorder;
	// ��ʾ��ƵԤ����SurfaceView
	SurfaceView sView;
	// ��¼�Ƿ����ڽ���¼��
	private boolean isRecording = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// ȥ�������� ,�������setContentView֮ǰ
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_video);
		x.view().inject(this);
		// ���ú�����ʾ
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER_LANDSCAPE);
		// ����ȫ��
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		// ѡ��֧�ְ�͸��ģʽ,����surfaceview��activity��ʹ�á�
		getWindow().setFormat(PixelFormat.TRANSLUCENT);
		// ��ȡ��������е�SurfaceView
		sView = (SurfaceView) this.findViewById(R.id.sView);
		// ���÷ֱ���
		sView.getHolder().setFixedSize(1280, 720);
		// ���ø��������Ļ�����Զ��ر�
		sView.getHolder().setKeepScreenOn(true);
	}

	@Event(value = { R.id.begin_record, R.id.stop_record }, type = View.OnClickListener.class)
	private void onClick(View v) {
		switch (v.getId()) {
		// ����¼�ư�ť
		case R.id.begin_record:
			try {
				// ��������¼����Ƶ����Ƶ�ļ�
				videoFile = new File(Constants.DISH_VIDEO_PATH, PhotoUtils.getCurrentFileName() + ".3gp");
				// ����MediaPlayer����
				mRecorder = new MediaRecorder();
				mRecorder.reset();
				// ���ô���˷�ɼ�����(������¼���������AudioSource.CAMCORDER)
				mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
				// ���ô�����ͷ�ɼ�ͼ��
				mRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
				// ������Ƶ�ļ��������ʽ
				// �������������������ʽ��ͼ������ʽ֮ǰ����
				mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
				// ������������ĸ�ʽ
				mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
				// ����ͼ�����ĸ�ʽ
				mRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
				mRecorder.setVideoSize(1280, 720);
				// ÿ�� 4֡
				mRecorder.setVideoFrameRate(20);
				mRecorder.setOutputFile(videoFile.getAbsolutePath());
				// ָ��ʹ��SurfaceView��Ԥ����Ƶ
				mRecorder.setPreviewDisplay(sView.getHolder().getSurface()); // ��
				mRecorder.prepare();
				// ��ʼ¼��
				mRecorder.start();
				// ��record��ť�����á�
				ivBegin.setVisibility(View.GONE);
				// ��stop��ť���á�
				ivStop.setVisibility(View.VISIBLE);
				isRecording = true;
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		// ����ֹͣ��ť
		case R.id.stop_record:
			// ������ڽ���¼��
			if (isRecording) {
				// ֹͣ¼��
				mRecorder.stop();
				// �ͷ���Դ
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
