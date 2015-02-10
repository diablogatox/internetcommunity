package com.orfid.ic;

import java.io.IOException;
import java.util.UUID;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mofang.util.RecordUtil;

public class VoiceNewActivity extends Activity {
	private ImageButton ib_voice_big;
	private ImageButton ib_voice_close;
	private TextView status_hint_text;
	private RelativeLayout rl_add_voice;
	private boolean isSignature = false;
	
	private int mRecord_State = 0; // 录音的状态
	private static final int RECORD_NO = 0; // 不在录音
	private static final int RECORD_ING = 1; // 正在录音
	private static final int RECORD_ED = 2; // 完成录音
	private String mRecordPath;// 录音的存储名称
	private static final String PATH = "/sdcard/internetcommunity/Record/";// 录音存储路径
	private RecordUtil mRecordUtil;
	private float mRecord_Time;// 录音的时间
	private static final int MAX_TIME = 60;// 最长录音时间
	private static final int MIN_TIME = 2;// 最短录音时间
	private double mRecord_Volume;// 麦克风获取的音量值
	private TextView mRecordTime;
	private ProgressBar mRecordProgressBar;
	private int mMAXVolume;// 最大音量高度
	private int mMINVolume;// 最小音量高度
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.voice_new);
		
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		isSignature = bundle.getBoolean("isSignature");
		
		status_hint_text = (TextView) findViewById(R.id.status_hint_text);
//		rcChat_popup = findViewById(R.id.rcChat_popup);
//		volume = (ImageView) findViewById(R.id.volume);
		ib_voice_big = (ImageButton) findViewById(R.id.ib_voice_big);
		ib_voice_close = (ImageButton) findViewById(R.id.ib_voice_close);
		rl_add_voice = (RelativeLayout) findViewById(R.id.rl_add_voice);
		mRecordTime = (TextView) findViewById(R.id.voice_record_time);
		mRecordProgressBar = (ProgressBar) findViewById(R.id.voice_record_progressbar);
		
//		voice_rcd_hint_rcding = (LinearLayout)this.findViewById(R.id.voice_rcd_hint_rcding);
//		timedown=(Chronometer)findViewById(R.id.timedown);
//		voiceName="MySound.mp3";
//		mSensor = new SoundMeter();
//		ib_voice_big.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View arg0) {
//				
//			}
//		});
		ib_voice_big.setOnTouchListener(new OnTouchListener() {

			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				// 开始录音
				case MotionEvent.ACTION_DOWN:
					if (mRecord_State != RECORD_ING) {
						status_hint_text.setText("松开 结束");
						// 开始动画效果
//						startRecordLightAnimation();
						// 修改录音状态
						mRecord_State = RECORD_ING;
						// 设置录音保存路径
						mRecordPath = PATH + UUID.randomUUID().toString()
								+ ".amr";
						// 实例化录音工具类
						mRecordUtil = new RecordUtil(mRecordPath);
						try {
							// 开始录音
							mRecordUtil.start();
						} catch (IOException e) {
							e.printStackTrace();
						}
						new Thread(new Runnable() {

							public void run() {
								// 初始化录音时间
								mRecord_Time = 0;
								while (mRecord_State == RECORD_ING) {
									// 大于最大录音时间则停止录音
									if (mRecord_Time >= MAX_TIME) {
										mRecordHandler.sendEmptyMessage(0);
									} else {
										try {
											// 每隔200毫秒就获取声音音量并更新界面显示
											Thread.sleep(200);
											mRecord_Time += 0.2;
											if (mRecord_State == RECORD_ING) {
												mRecord_Volume = mRecordUtil
														.getAmplitude();
												mRecordHandler
														.sendEmptyMessage(1);
											}
										} catch (InterruptedException e) {
											e.printStackTrace();
										}
									}
								}
							}
						}).start();
					}
					break;
				// 停止录音
				case MotionEvent.ACTION_UP:
					if (mRecord_State == RECORD_ING) {
						// 停止动画效果
//						stopRecordLightAnimation();
						
						// 修改录音状态
						mRecord_State = RECORD_ED;
						try {
							// 停止录音
							mRecordUtil.stop();
							// 初始录音音量
							mRecord_Volume = 0;
						} catch (IOException e) {
							e.printStackTrace();
						}
						// 如果录音时间小于最短时间
						if (mRecord_Time <= MIN_TIME) {
							// 显示提醒
							Toast.makeText(VoiceNewActivity.this, "录音时间过短",
									Toast.LENGTH_SHORT).show();
							// 修改录音状态
							mRecord_State = RECORD_NO;
							// 修改录音时间
							mRecord_Time = 0;
							// 修改显示界面
							mRecordTime.setText("0″");
							mRecordProgressBar.setProgress(0);
							status_hint_text.setText("按住 说话");
							// 修改录音声音界面
//							ViewGroup.LayoutParams params = mRecordVolume
//									.getLayoutParams();
//							params.height = 0;
//							mRecordVolume.setLayoutParams(params);
						} else {
							// 录音成功,则显示录音成功后的界面
//							mRecordLayout.setVisibility(View.GONE);
//							mRecord.setVisibility(View.GONE);
//							mDisplayVoiceLayout.setVisibility(View.VISIBLE);
//							mDisplayVoicePlay
//									.setImageResource(R.drawable.globle_player_btn_play);
//							mDisplayVoiceProgressBar.setMax((int) mRecord_Time);
//							mDisplayVoiceProgressBar.setProgress(0);
//							mDisplayVoiceTime.setText((int) mRecord_Time + "″");
//							send.setVisibility(View.VISIBLE);
							Log.d("record path======>", mRecordPath);
							Log.d("record time======>", mRecord_Time+"");
							Intent intent = new Intent();
							intent.putExtra("recordPath", mRecordPath);
							intent.putExtra("recordTime", mRecord_Time);
							setResult(RESULT_OK, intent);
							finish();
						}
					}
					break;
				}
				return false;
			}
		});
		
		ib_voice_close.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		//��ʼ����
		rl_add_voice.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
			}
		});
		
		init();
	}
	@Override  
	public boolean onTouchEvent(MotionEvent event){   
	    finish();   
	    return true;   
	}
	
	
	/**
	 * 用来控制录音
	 */
	Handler mRecordHandler = new Handler() {

		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				if (mRecord_State == RECORD_ING) {
					// 停止动画效果
//					stopRecordLightAnimation();
					// 修改录音状态
					mRecord_State = RECORD_ED;
					try {
						// 停止录音
						mRecordUtil.stop();
						// 初始化录音音量
						mRecord_Volume = 0;
					} catch (IOException e) {
						e.printStackTrace();
					}
					// 根据录音修改界面显示内容
//					mRecordLayout.setVisibility(View.GONE);
//					mRecord.setVisibility(View.GONE);
//					mDisplayVoiceLayout.setVisibility(View.VISIBLE);
//					mDisplayVoicePlay
//							.setImageResource(R.drawable.globle_player_btn_play);
//					mDisplayVoiceProgressBar.setMax((int) mRecord_Time);
//					mDisplayVoiceProgressBar.setProgress(0);
//					mDisplayVoiceTime.setText((int) mRecord_Time + "″");
				}
				break;

			case 1:
				// 根据录音时间显示进度条
				mRecordProgressBar.setProgress((int) mRecord_Time);
				// 显示录音时间
				mRecordTime.setText((int) mRecord_Time + "″");
				// 根据录音声音大小显示效果
//				ViewGroup.LayoutParams params = mRecordVolume.getLayoutParams();
//				if (mRecord_Volume < 200.0) {
//					params.height = mMINVolume;
//				} else if (mRecord_Volume > 200.0 && mRecord_Volume < 400) {
//					params.height = mMINVolume * 2;
//				} else if (mRecord_Volume > 400.0 && mRecord_Volume < 800) {
//					params.height = mMINVolume * 3;
//				} else if (mRecord_Volume > 800.0 && mRecord_Volume < 1600) {
//					params.height = mMINVolume * 4;
//				} else if (mRecord_Volume > 1600.0 && mRecord_Volume < 3200) {
//					params.height = mMINVolume * 5;
//				} else if (mRecord_Volume > 3200.0 && mRecord_Volume < 5000) {
//					params.height = mMINVolume * 6;
//				} else if (mRecord_Volume > 5000.0 && mRecord_Volume < 7000) {
//					params.height = mMINVolume * 7;
//				} else if (mRecord_Volume > 7000.0 && mRecord_Volume < 10000.0) {
//					params.height = mMINVolume * 8;
//				} else if (mRecord_Volume > 10000.0 && mRecord_Volume < 14000.0) {
//					params.height = mMINVolume * 9;
//				} else if (mRecord_Volume > 14000.0 && mRecord_Volume < 17000.0) {
//					params.height = mMINVolume * 10;
//				} else if (mRecord_Volume > 17000.0 && mRecord_Volume < 20000.0) {
//					params.height = mMINVolume * 11;
//				} else if (mRecord_Volume > 20000.0 && mRecord_Volume < 24000.0) {
//					params.height = mMINVolume * 12;
//				} else if (mRecord_Volume > 24000.0 && mRecord_Volume < 28000.0) {
//					params.height = mMINVolume * 13;
//				} else if (mRecord_Volume > 28000.0) {
//					params.height = mMAXVolume;
//				}
//				mRecordVolume.setLayoutParams(params);
				break;
			}
		}

	};
	
	private void init() {
		// 设置当前的最小声音和最大声音值
		mMINVolume = (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, 4.5f, getResources()
						.getDisplayMetrics());
		mMAXVolume = (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, 65f, getResources()
						.getDisplayMetrics());
	}
	
}
