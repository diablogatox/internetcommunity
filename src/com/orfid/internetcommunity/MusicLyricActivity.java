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
		 //添加选择窗口范围监听可以优先获取触点，即不再执行onTouchEvent()函数，点击其他地方时执行onTouchEvent()函数销毁Activity   
		rl_music_lyric.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
			}
		});  
		
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();

		tv_music_lyric1.setText(bundle.getString("content"));
		if (bundle.getString("time") != null) {
			tv_music_lyric2.setText(Utils.covertTimestampToDate(Long.parseLong(bundle.getString("time")) * 1000));
		}
	}
	@Override  
	public boolean onTouchEvent(MotionEvent event){   
	    finish();   
	    return true;   
	} 
}
