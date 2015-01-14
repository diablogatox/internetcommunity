package com.orfid.internetcommunity;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class VoiceStartActivity extends Activity {
	private RelativeLayout rl_voice_start,rl_voice_start1;
	private TextView tv_voice_start2,tv_voice_date;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.voice_start);
		rl_voice_start = (RelativeLayout) findViewById(R.id.rl_voice_start);
		rl_voice_start1 = (RelativeLayout) findViewById(R.id.rl_voice_start1);
		tv_voice_start2 = (TextView) findViewById(R.id.tv_voice_start2);
		tv_voice_date = (TextView) findViewById(R.id.tv_voice_date);
		
		tv_voice_start2.setText("12");//声音的时间计时
		tv_voice_date.setText("2014-12-18");//日期设置
		rl_voice_start.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
			}
		});
		rl_voice_start1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				//开始播放声音
			}
		});
		
	}
	@Override  
	public boolean onTouchEvent(MotionEvent event){   
	    finish();   
	    return true;   
	} 
}
