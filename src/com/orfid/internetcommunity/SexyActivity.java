package com.orfid.internetcommunity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class SexyActivity extends Activity {
	private ImageView iv_sexy_back;
	private ImageView iv_sexy_choice1;
	private ImageView iv_sexy_choice2;
	private RelativeLayout rl_sexy_boy;
	private RelativeLayout rl_sexy_girl;
	boolean boo = true;
	private SharedPreferences sp_sexy_info;
	private Editor ed_sexy_info;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sexy);
		
		iv_sexy_back = (ImageView) findViewById(R.id.iv_sexy_back);
		iv_sexy_choice1 = (ImageView) findViewById(R.id.iv_sexy_choice1);
		iv_sexy_choice2 = (ImageView) findViewById(R.id.iv_sexy_choice2);
		rl_sexy_boy = (RelativeLayout) findViewById(R.id.rl_sexy_boy);
		rl_sexy_girl = (RelativeLayout) findViewById(R.id.rl_sexy_girl);
		sp_sexy_info = getSharedPreferences("sexy", MODE_PRIVATE);
		ed_sexy_info = sp_sexy_info.edit();
		
		//ÄÐ
		rl_sexy_boy.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				boo = true;
				iv_sexy_choice1.setVisibility(View.VISIBLE);
				iv_sexy_choice2.setVisibility(View.GONE);
			}
		});
		//Å®
		rl_sexy_girl.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				boo = false;
				iv_sexy_choice1.setVisibility(View.GONE);
				iv_sexy_choice2.setVisibility(View.VISIBLE);
			}
		});
		//·µ»Ø
		iv_sexy_back.setOnClickListener(new OnClickListener() {
					
			@Override
			public void onClick(View arg0) {
				writeLoginData();
				finish();
			}
		});
	}
	public void writeLoginData(){
		ed_sexy_info.clear();
		ed_sexy_info.commit();
		if(boo){
			ed_sexy_info.putString("nan", "1");
		}else{
			ed_sexy_info.putString("woman", "0");
		}
		ed_sexy_info.commit();
	}
	
}
