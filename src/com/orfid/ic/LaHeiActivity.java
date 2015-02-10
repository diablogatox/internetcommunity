package com.orfid.ic;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

public class LaHeiActivity extends Activity {
	private Button btn_lahei, btn_jubao, btn_quxiao;   
	private LinearLayout layout;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.la_hei);
		
		btn_lahei = (Button) this.findViewById(R.id.btn_lahei);   
		btn_jubao = (Button) this.findViewById(R.id.btn_jubao);   
		btn_quxiao = (Button) this.findViewById(R.id.btn_quxiao);   
		 layout=(LinearLayout)findViewById(R.id.ll_lahei);   
		 //���ѡ�񴰿ڷ�Χ�����������Ȼ�ȡ���㣬������ִ��onTouchEvent()��������������ط�ʱִ��onTouchEvent()��������Activity   
		 layout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
//				Toast.makeText(getApplicationContext(), "��ʾ����������ⲿ�رմ��ڣ�",    
//					       Toast.LENGTH_SHORT).show();  
			}
		 });   
		 //����
		 btn_lahei.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
			}
		});  
		 //���ڲ��ٱ�
		 btn_jubao.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
			}
		});   
		 //ȡ��
		 btn_quxiao.setOnClickListener(new OnClickListener() {
			
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
