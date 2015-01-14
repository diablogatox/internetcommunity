package com.orfid.internetcommunity;

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
		 //添加选择窗口范围监听可以优先获取触点，即不再执行onTouchEvent()函数，点击其他地方时执行onTouchEvent()函数销毁Activity   
		 layout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
//				Toast.makeText(getApplicationContext(), "提示：点击窗口外部关闭窗口！",    
//					       Toast.LENGTH_SHORT).show();  
			}
		 });   
		 //拉黑
		 btn_lahei.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
			}
		});  
		 //拉黑并举报
		 btn_jubao.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
			}
		});   
		 //取消
		 btn_quxiao.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
	//实现onTouchEvent触屏函数但点击屏幕时销毁本Activity   
		@Override  
		public boolean onTouchEvent(MotionEvent event){   
		    finish();   
		    return true;   
		} 
}
