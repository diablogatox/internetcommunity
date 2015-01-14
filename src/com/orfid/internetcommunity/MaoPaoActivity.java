package com.orfid.internetcommunity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class MaoPaoActivity extends Activity {
	private RelativeLayout rl_maopao1,rl_maopao2,rl_maopao3;
	private LinearLayout layout;
	public static MaoPaoActivity instance = null;//����һ����̬�ı���instance
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mao_pao);
		instance = this;//��instance��ʼ��Ϊthis
		rl_maopao1 = (RelativeLayout) this.findViewById(R.id.rl_maopao1);   
		rl_maopao2 = (RelativeLayout) this.findViewById(R.id.rl_maopao2);   
		rl_maopao3 = (RelativeLayout) this.findViewById(R.id.rl_maopao3);
		layout = (LinearLayout) this.findViewById(R.id.ll_mao_pao);
		//���ѡ�񴰿ڷ�Χ�����������Ȼ�ȡ���㣬������ִ��onTouchEvent()��������������ط�ʱִ��onTouchEvent()��������Activity   
		 layout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
//				Toast.makeText(getApplicationContext(), "��ʾ����������ⲿ�رմ��ڣ�",    
//					       Toast.LENGTH_SHORT).show();  
			}
		 });   
		 //����
		 rl_maopao1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(new Intent(MaoPaoActivity.this,TextQipaoActivity.class));
			}
		});  
		 //����
		 rl_maopao2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(new Intent(MaoPaoActivity.this,VoiceQipaoActivity.class));
			}
		});   
		 //ȡ��
		 rl_maopao3.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
	//ʵ��onTouchEvent���������������Ļʱ���ٱ�Activity   
			@Override  
			public boolean onTouchEvent(MotionEvent event){   
			    finish();   
			    return true;   
			} 
}
