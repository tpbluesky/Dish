package com.example.dish.atys;

import java.io.IOException;

import org.xutils.DbManager;
import org.xutils.x;
import org.xutils.ex.DbException;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import com.example.dish.MyApplication;
import com.example.dish.R;
import com.example.dish.R.id;
import com.example.dish.beans.Collect;
import com.example.dish.beans.Dish;

import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

@ContentView(R.layout.activity_detail)
public class DetailActivity extends BaseActivity {

	@ViewInject(R.id.iv_back)
	private ImageView ivBack;
	@ViewInject(R.id.iv_love)
	private ImageView ivLove;

	MediaController mediaController = null;
	ImageView detail_image, detail_video;
	TextView detail_name, detail_style;
	ImageButton detail_audio;
	SeekBar progressBar;
	VideoView videoview;
	TextView detail_food, detail_method;
	MediaPlayer mediaPlayer;

	private Dish dish;
	private boolean hasLoved = false;
	private int userId;
	private DbManager db;
	private Collect love;

	@Override
	protected void initViews() {
		dish = (Dish) getIntent().getSerializableExtra("dish");
		userId = MyApplication.getLoginUser().getId();
		db = MyApplication.getDb();
		try {
			love = db.selector(Collect.class).where("userId", "=", userId).and("dishId", "=", dish.getId()).findFirst();
			if (love != null) {
				ivLove.setImageResource(R.drawable.icon_detail_collect);
				hasLoved = true;
			}
		} catch (DbException e) {
			e.printStackTrace();
		}
		InitView();

	}

	@Event(value = { R.id.iv_back, R.id.iv_love }, type = View.OnClickListener.class)
	private void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_back:
			finish();
			break;
		case R.id.iv_love:
			try {
				if (hasLoved) {
					db.delete(love);
					hasLoved = false;
					ivLove.setImageResource(R.drawable.icon_detail_uncollect);
				} else {
					Collect c = new Collect(userId, dish.getId());
					db.save(c);
					love = db.selector(Collect.class).where("userId", "=", userId).and("dishId", "=", dish.getId())
							.findFirst();
					hasLoved = true;
					ivLove.setImageResource(R.drawable.icon_detail_collect);
				}
			} catch (DbException e) {
				e.printStackTrace();
			}
			break;

		default:
			break;
		}
	}

	private String name;
	private String style;
	private String audio_path;
	private String video_path;
	private String image;
	private String food;
	private String method;
	boolean flag = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mediaPlayer = new MediaPlayer();
		mediaController = new MediaController(getApplicationContext());
		mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
			@Override
			public void onCompletion(MediaPlayer mp) {
				flag = true;
				mediaPlayer.reset();
			}
		});
		videoview.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
			@Override
			public void onCompletion(MediaPlayer mp) {

			}
		});
	}

	final android.os.Handler handler = new android.os.Handler();
	Runnable updateThread = new Runnable() {
		@Override
		public void run() {
			progressBar.setProgress(mediaPlayer.getCurrentPosition());
			// 每次延20毫秒再启动线程
			handler.postDelayed(updateThread, 20);
		}
	};

	void InitView() {
		detail_image = (ImageView) findViewById(id.detail_image);
		detail_video = (ImageView) findViewById(id.detail_video);
		detail_name = (TextView) findViewById(id.detail_name);
		detail_style = (TextView) findViewById(id.detail_style);
		detail_audio = (ImageButton) findViewById(id.detail_audio);
		progressBar = (SeekBar) findViewById(R.id.progressbar);
		videoview = (VideoView) findViewById(id.videoview);
		detail_food = (TextView) findViewById(id.detail_food);
		detail_method = (TextView) findViewById(id.detail_method);
		getData();
		if (image == "" || image == null)
			detail_image.setImageResource(R.drawable.ic_launcher);
		else {
			x.image().bind(detail_image, image);
		}
		detail_food.setText(food);
		detail_method.setText(method);
		detail_style.setText(style);
		detail_name.setText(name);

		Listener listener = new Listener();
		videoview.setOnClickListener(listener);
		detail_audio.setOnClickListener(listener);
		detail_video.setOnClickListener(listener);
		progressBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				if (fromUser) {
					mediaPlayer.seekTo(progress);// 当进度条的值改变时，音乐播放器从新的位置开始播放
				}
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {

			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {

			}
		});
	}

	class Listener implements View.OnClickListener {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case id.detail_video:
				if (video_path == "" || video_path == null)
					Toast.makeText(getApplicationContext(), "此处还没有上传视频", Toast.LENGTH_SHORT).show();
				else {
					if (mediaPlayer.isPlaying()) {
						mediaPlayer.pause();
					}
					if (!videoview.isPlaying()) {
						startPlayVideo();
					}
				}
				break;
			case id.detail_audio:
				if (audio_path == "" || audio_path == null)
					Toast.makeText(getApplicationContext(), "此处还没有上传音频", Toast.LENGTH_SHORT).show();
				else {
					if (videoview.isPlaying())
						videoview.stopPlayback();
					if (!mediaPlayer.isPlaying()) {
						if (flag) {
							startPlayRadio();
							flag = false;
						} else
							mediaPlayer.start();
					} else
						mediaPlayer.pause();
				}
				break;
			case id.videoview:
				/*
				 * if(dish.getVideopath()=="")
				 * Toast.makeText(getApplicationContext(),"此处还没有上传视频",Toast.
				 * LENGTH_SHORT).show(); else{ if(!videoview.isPlaying()){
				 * videoview.setMediaController(new
				 * MediaController(getApplicationContext())); Uri
				 * uri=Uri.parse(dish.getVideopath());
				 * videoview.setVideoURI(uri); videoview.start(); } }
				 */
				break;
			}
		}
	}

	public void startPlayRadio() {
		try {
			mediaPlayer.setDataSource(dish.getAudiopath());
			mediaPlayer.prepare();
			mediaPlayer.start();
			progressBar.setMax(mediaPlayer.getDuration());
			// progressBar.setProgress(mediaPlayer.getCurrentPosition());
			handler.post(updateThread);
			// handler.postDelayed(updateThread, 20);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void getData() {
		name = dish.getName();
		style = dish.getCategory();
		audio_path = dish.getAudiopath();
		video_path = dish.getVideopath();
		image = dish.getImagePath();
		food = dish.getMaterial();
		method = dish.getMethod();
	}

	public void startPlayVideo() {
		videoview.setMediaController(mediaController);
		Uri uri = Uri.parse(dish.getVideopath());
		videoview.setVideoURI(uri);
		videoview.start();
	}

}
