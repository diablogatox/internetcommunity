package com.orfid.ic;

import java.io.File;
import java.io.IOException;

import com.mofang.util.UploadUtils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class VoiceQipaoActivity extends Activity {
	private ImageView iv_voice_back;
	private RelativeLayout rl_add_voice;
	private LinearLayout mDisplayVoiceLayout;
	private ImageView mDisplayVoicePlay;
	private ProgressBar mDisplayVoiceProgressBar;
	private TextView mDisplayVoiceTime;
	private TextView tv_voice_fabu;
	private boolean mPlayState; // 播放状态
	private MediaPlayer mMediaPlayer;
	private String mRecordPath;// 录音的存储名称
	private float mRecord_Time;// 录音的时间
	private int mPlayCurrentPosition;
	private boolean isSignature;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.voice_qipao);
		
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		isSignature = bundle.getBoolean("isSignature");
		
		iv_voice_back = (ImageView) findViewById(R.id.iv_voice_back);
		rl_add_voice = (RelativeLayout) findViewById(R.id.rl_add_voice);
		tv_voice_fabu = (TextView) findViewById(R.id.tv_voice_fabu);
		
		tv_voice_fabu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mRecordPath != null) {
					Log.d("record path to be uploaded=====>", mRecordPath);
					String apiUrl;
					if (isSignature) {
						apiUrl = AppConstants.USER_SIGNATURE;
					} else {
						apiUrl = AppConstants.SEND_BUBBLE;
					}
					new UploadRecordTask(VoiceQipaoActivity.this, mRecordPath, mRecord_Time).execute(apiUrl);
				}
			}
			
		});
		
		mDisplayVoiceLayout = (LinearLayout) findViewById(R.id.voice_display_voice_layout);
		mDisplayVoicePlay = (ImageView) findViewById(R.id.voice_display_voice_play);
		mDisplayVoiceProgressBar = (ProgressBar) findViewById(R.id.voice_display_voice_progressbar);
		mDisplayVoiceTime = (TextView) findViewById(R.id.voice_display_voice_time);
		
		//返回
		iv_voice_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				MaoPaoActivity.instance.finish();
				finish();
			}
		});
		//添加语音
		rl_add_voice.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(VoiceQipaoActivity.this,VoiceNewActivity.class);
				intent.putExtra("isSignature", isSignature);
				startActivityForResult(intent, 0);
			}
		});
		
		mDisplayVoicePlay.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// 播放录音
				if (!mPlayState) {
					mMediaPlayer = new MediaPlayer();
					try {
						// 添加录音的路径
						mMediaPlayer.setDataSource(mRecordPath);
						// 准备
						mMediaPlayer.prepare();
						// 播放
						mMediaPlayer.start();
						// 根据时间修改界面
						new Thread(new Runnable() {

							public void run() {

								mDisplayVoiceProgressBar
										.setMax((int) mRecord_Time);
								mPlayCurrentPosition = 0;
								while (mMediaPlayer.isPlaying()) {
									mPlayCurrentPosition = mMediaPlayer
											.getCurrentPosition() / 1000;
									mDisplayVoiceProgressBar
											.setProgress(mPlayCurrentPosition);
								}
							}
						}).start();
						// 修改播放状态
						mPlayState = true;
						// 修改播放图标
						mDisplayVoicePlay
								.setImageResource(R.drawable.globle_player_btn_stop);

						mMediaPlayer
								.setOnCompletionListener(new OnCompletionListener() {
									// 播放结束后调用
									public void onCompletion(MediaPlayer mp) {
										// 停止播放
										mMediaPlayer.stop();
										// 修改播放状态
										mPlayState = false;
										// 修改播放图标
										mDisplayVoicePlay
												.setImageResource(R.drawable.globle_player_btn_play);
										// 初始化播放数据
										mPlayCurrentPosition = 0;
										mDisplayVoiceProgressBar
												.setProgress(mPlayCurrentPosition);
									}
								});

					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					} catch (IllegalStateException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
				} else {
					if (mMediaPlayer != null) {
						// 根据播放状态修改显示内容
						if (mMediaPlayer.isPlaying()) {
							mPlayState = false;
							mMediaPlayer.stop();
							mDisplayVoicePlay
									.setImageResource(R.drawable.globle_player_btn_play);
							mPlayCurrentPosition = 0;
							mDisplayVoiceProgressBar
									.setProgress(mPlayCurrentPosition);
						} else {
							mPlayState = false;
							mDisplayVoicePlay
									.setImageResource(R.drawable.globle_player_btn_play);
							mPlayCurrentPosition = 0;
							mDisplayVoiceProgressBar
									.setProgress(mPlayCurrentPosition);
						}
					}
				}
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 0) {
			if (resultCode == RESULT_OK) {
				Bundle extras = data.getExtras();
				mRecordPath = extras.getString("recordPath");
				mRecord_Time = extras.getFloat("recordTime");
				
				mDisplayVoiceLayout.setVisibility(View.VISIBLE);
				mDisplayVoicePlay
						.setImageResource(R.drawable.globle_player_btn_play);
				mDisplayVoiceProgressBar.setMax((int) mRecord_Time);
				mDisplayVoiceProgressBar.setProgress(0);
				mDisplayVoiceTime.setText((int) mRecord_Time + "″");
			}
		}
	}
	
	
	public class UploadRecordTask extends AsyncTask<String, Void, String> {

		String recordPath;
		float recordTime;
		ProgressDialog pd;
		Context context;
		
		public UploadRecordTask(Context context, String recordPath, float recordTime) {
			this.context = context;
			this.recordPath = recordPath;
			this.recordTime = recordTime;
		}

		@Override
		protected String doInBackground(String... params) {
			File file=new File(recordPath);
			return UploadUtils.uploadFile(VoiceQipaoActivity.this, file, params[0]);
		}

		@Override
		protected void onPreExecute() {
			pd = new ProgressDialog(context);
			pd.setTitle("正在上传");
			pd.setMessage("请稍等.");
			pd.setCancelable(true);
			pd.setIndeterminate(true);
			pd.show();
		}

		@Override
		protected void onPostExecute(String result) {
			if(result.length() > 0){
				if (pd != null) {
					pd.dismiss();
				}
				Log.d("voice update result========>", result);
	        	Toast.makeText(VoiceQipaoActivity.this, "上传成功",Toast.LENGTH_LONG ).show();
	        	Intent intent = new Intent();
	        	intent.putExtra("recordPath", recordPath);
	        	intent.putExtra("recordTime", recordTime);
	        	setResult(RESULT_OK, intent);
	        	finish();

	        }else{
	        	Toast.makeText(VoiceQipaoActivity.this, "上传失败，请检查网络", Toast.LENGTH_LONG ).show();
	        }
		}

	}
	
	
}
