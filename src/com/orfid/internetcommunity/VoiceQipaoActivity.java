package com.orfid.internetcommunity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class VoiceQipaoActivity extends Activity {
	private ImageView iv_voice_back;
	private RelativeLayout rl_add_voice;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.voice_qipao);
		
		iv_voice_back = (ImageView) findViewById(R.id.iv_voice_back);
		rl_add_voice = (RelativeLayout) findViewById(R.id.rl_add_voice);
		//∑µªÿ
		iv_voice_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				MaoPaoActivity.instance.finish();
				finish();
			}
		});
		//ÃÌº””Ô“Ù
		rl_add_voice.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				startActivity(new Intent(VoiceQipaoActivity.this,VoiceNewActivity.class));
			}
		});
	}
}
