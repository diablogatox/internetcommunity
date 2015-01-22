package com.orfid.internetcommunity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Chronometer.OnChronometerTickListener;

public class VoiceNewActivity extends Activity {
	private ImageButton ib_voice_big;
	private ImageButton ib_voice_close;
	private TextView status_hint_text;
	private RelativeLayout rl_add_voice;
	private boolean isSignature = false;
	private int flag=1;
	private View rcChat_popup;
	private Handler mHandler = new Handler();
	private long startVoiceT, endVoiceT;
	private String voiceName;
	private SoundMeter mSensor;
	private Chronometer timedown;//显示倒计时
	private long timeTotalInS = 0;
	private long timeLeftInS = 0;
	private MediaPlayer player;
	private ImageView  volume;
	private static final int POLL_INTERVAL = 300;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.voice_new);
		
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		isSignature = bundle.getBoolean("isSignature");
		
		status_hint_text = (TextView) findViewById(R.id.status_hint_text);
		rcChat_popup = findViewById(R.id.rcChat_popup);
		ib_voice_big = (ImageButton) findViewById(R.id.ib_voice_big);
		ib_voice_close = (ImageButton) findViewById(R.id.ib_voice_close);
		rl_add_voice = (RelativeLayout) findViewById(R.id.rl_add_voice);
//		ib_voice_big.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View arg0) {
//				
//			}
//		});
		ib_voice_big.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(event.getAction()==MotionEvent.ACTION_DOWN) {
					status_hint_text.setText("松开结束");
					int[] location = new int[2];
					ib_voice_big.getLocationInWindow(location); // 获取在当前窗口内的绝对坐标
					int btn_rc_Y = location[1];
					int btn_rc_X = location[0];
					if(flag==1){
						if (!Environment.getExternalStorageDirectory().exists()) {
							Toast.makeText(VoiceNewActivity.this, "No SDCard", Toast.LENGTH_LONG).show();
							return false;
						}
//						System.out.println("2");
//						System.out.println(event.getY()+"..."+btn_rc_Y+"...."+event.getX() +"...."+btn_rc_X);
						if (event.getY() < btn_rc_Y && event.getX() > btn_rc_X) {//判断手势按下的位置是否是语音录制按钮的范围内
//							System.out.println("3");
							rcChat_popup.setVisibility(View.VISIBLE);
							mHandler.postDelayed(new Runnable() {
								public void run() {
								}
							}, 300);
							startVoiceT = SystemClock.currentThreadTimeMillis();
//							voiceName = startVoiceT + ".amr";
							start(voiceName);
							//设置录音时间
							timedown.setVisibility(View.VISIBLE);
							initTimer(5);
							timedown.start();
							flag = 2;
						}
					}
				} else if(event.getAction()==MotionEvent.ACTION_UP) {
//					bntRecord.setBackgroundColor(getResources().getColor(R.color.white));
//					bntRecord.setText("按住  说话");
//					bntRecord.setTextColor(Color.BLACK);
//					timedown.stop();
//					if(flag==2){
//						rcChat_popup.setVisibility(View.GONE);
//						timedown.setVisibility(View.GONE);
//						stop();
//						flag = 1;
//						soundUse(voiceName);

				} else {
//						voice_rcd_hint_rcding.setVisibility(View.GONE);
//						stop();
//						endVoiceT = SystemClock.currentThreadTimeMillis();
//						flag = 1;
//						int time = (int) ((endVoiceT - startVoiceT) / 1000);
//						System.out.println(time);
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
	}
	@Override  
	public boolean onTouchEvent(MotionEvent event){   
	    finish();   
	    return true;   
	}
	
	private Runnable mSleepTask = new Runnable() {
		public void run() {
			stop();
		}
	};
	private Runnable mPollTask = new Runnable(){
		public void run() {
			double amp = mSensor.getAmplitude();
			updateDisplay(amp);
			mHandler.postDelayed(mPollTask, POLL_INTERVAL);
		}
	};
	
	private void start(String name) {
		mSensor.start(name);
		mHandler.postDelayed(mPollTask, POLL_INTERVAL);
	}
	
	private void stop() {
		mHandler.removeCallbacks(mSleepTask);
		mHandler.removeCallbacks(mPollTask);
		mSensor.stop();
		volume.setImageResource(R.drawable.amp1);
	}
	
	/**
	 * 初始化计时器，计时器是通过widget.Chronometer来实现的
	 * @param total 一共多少秒
	 */
	private void initTimer(long total) {
		this.timeTotalInS = total;
		this.timeLeftInS = total;
		timedown.setOnChronometerTickListener(new OnChronometerTickListener() {
			@Override
			public void onChronometerTick(Chronometer chronometer) {
				if (timeLeftInS <= 0) {
					Toast.makeText(VoiceNewActivity.this, "录音时间到", Toast.LENGTH_SHORT).show();
					timedown.stop();
					//录音停止
					stop();
					rcChat_popup.setVisibility(View.GONE);
					timedown.setVisibility(View.GONE);
					return;
				}
				timeLeftInS--;
				refreshTimeLeft();
			}
		});
	}
	private void refreshTimeLeft() {
		this.timedown.setText("录音时间剩余：" + timeLeftInS);
		//TODO 格式化字符串
	}
	
	private void updateDisplay(double signalEMA) {
		
		switch ((int) signalEMA) {
		case 0:
		case 1:
			volume.setImageResource(R.drawable.amp1);
			break;
		case 2:
		case 3:
			volume.setImageResource(R.drawable.amp2);
			break;
		case 4:
		case 5:
			volume.setImageResource(R.drawable.amp3);
			break;
		case 6:
		case 7:
			volume.setImageResource(R.drawable.amp4);
			break;
		case 8:
		case 9:
			volume.setImageResource(R.drawable.amp5);
			break;
		case 10:
		case 11:
			volume.setImageResource(R.drawable.amp6);
			break;
		default:
			volume.setImageResource(R.drawable.amp7);
			break;
		}
	}
}
