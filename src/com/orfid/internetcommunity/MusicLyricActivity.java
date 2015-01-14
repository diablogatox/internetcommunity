package com.orfid.internetcommunity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MusicLyricActivity extends Activity {
	private TextView tv_music_lyric1,tv_music_lyric2;
	private RelativeLayout rl_music_lyric;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.music_lyric);
		
		tv_music_lyric1 = (TextView) findViewById(R.id.tv_music_lyric1);
		tv_music_lyric2 = (TextView) findViewById(R.id.tv_music_lyric2);
		rl_music_lyric = (RelativeLayout) findViewById(R.id.rl_music_lyric);
		rl_music_lyric=(RelativeLayout)findViewById(R.id.rl_music_lyric);   
		 //���ѡ�񴰿ڷ�Χ�����������Ȼ�ȡ���㣬������ִ��onTouchEvent()��������������ط�ʱִ��onTouchEvent()��������Activity   
		rl_music_lyric.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
			}
		});  
		
		Intent intent = getIntent();
	    String musicLyric = intent.getStringExtra("one");  
		tv_music_lyric1.setText(musicLyric);
		tv_music_lyric2.setText("2014-12-18");
	}
	@Override  
	public boolean onTouchEvent(MotionEvent event){   
	    finish();   
	    return true;   
	} 
}
