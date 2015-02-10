package com.orfid.internetcommunity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;

@SuppressLint("HandlerLeak")
public class ToastActivity extends Activity {
	public final static int CLOSE_ACTIVITY=1001;
	public final static int TOUCH_DOWN=1002;
	Handler mHandler=new Handler(){
		@Override
		public void handleMessage(android.os.Message msg){
			switch (msg.what) {
			case ToastActivity.CLOSE_ACTIVITY:			
				finish();
				break;
			case ToastActivity.TOUCH_DOWN:
				mHandler.removeMessages(TOUCH_DOWN);
				finish();		
				break;
			}
			super.handleMessage(msg);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//�����ޱ���
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.toast_add_friends);
		mHandler.postDelayed(new Runnable() {			
			@Override
			public void run() {
				mHandler.sendEmptyMessage(ToastActivity.CLOSE_ACTIVITY);
			}
		}, 2000);

	}
}
