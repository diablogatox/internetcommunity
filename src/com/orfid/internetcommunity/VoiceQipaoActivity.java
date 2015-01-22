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
				startActivity(intent);
			}
		});
	}
}
