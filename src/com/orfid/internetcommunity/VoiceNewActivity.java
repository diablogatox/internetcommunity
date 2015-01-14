package com.orfid.internetcommunity;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

public class VoiceNewActivity extends Activity {
	private ImageButton ib_voice_big;
	private ImageButton ib_voice_close;
	private RelativeLayout rl_add_voice;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.voice_new);
		
		ib_voice_big = (ImageButton) findViewById(R.id.ib_voice_big);
		ib_voice_close = (ImageButton) findViewById(R.id.ib_voice_close);
		rl_add_voice = (RelativeLayout) findViewById(R.id.rl_add_voice);
		ib_voice_big.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
			}
		});
		ib_voice_close.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		//ø™ º”Ô“Ù
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
}
