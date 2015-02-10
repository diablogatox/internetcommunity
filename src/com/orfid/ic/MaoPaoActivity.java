package com.orfid.ic;

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
	public static MaoPaoActivity instance = null;//设置一个静态的变量instance
	boolean isSignature = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mao_pao);
		
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		if (bundle != null) {
			isSignature = bundle.getBoolean("isSignature");
		}
		
		instance = this;//将instance初始化为this
		rl_maopao1 = (RelativeLayout) this.findViewById(R.id.rl_maopao1);   
		rl_maopao2 = (RelativeLayout) this.findViewById(R.id.rl_maopao2);   
		rl_maopao3 = (RelativeLayout) this.findViewById(R.id.rl_maopao3);
		layout = (LinearLayout) this.findViewById(R.id.ll_mao_pao);
		//添加选择窗口范围监听可以优先获取触点，即不再执行onTouchEvent()函数，点击其他地方时执行onTouchEvent()函数销毁Activity   
		 layout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
//				Toast.makeText(getApplicationContext(), "提示：点击窗口外部关闭窗口！",    
//					       Toast.LENGTH_SHORT).show();  
			}
		 });   
		 //文字
		 rl_maopao1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MaoPaoActivity.this,TextQipaoActivity.class);
				intent.putExtra("isSignature", isSignature);
				startActivityForResult(intent, 0);
			}
		});  
		 //语音
		 rl_maopao2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MaoPaoActivity.this, VoiceQipaoActivity.class);
				intent.putExtra("isSignature", isSignature);
				startActivityForResult(intent, 1);
			}
		});   
		 //取消
		 rl_maopao3.setOnClickListener(new OnClickListener() {
			
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
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent data) {
		if (requestCode == 0) {
			if (resultCode == RESULT_OK) {
				
				setResult(RESULT_OK, data);
				finish();
			}
		} else if (requestCode == 1) {
			if (resultCode == RESULT_OK) {
				data.putExtra("isVoiceRecord", true);
				setResult(RESULT_OK, data);
				finish();
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	} 
			
			
}
