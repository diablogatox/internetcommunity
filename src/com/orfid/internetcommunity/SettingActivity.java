package com.orfid.internetcommunity;

import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class SettingActivity extends Activity implements Runnable{
	private ImageView iv_setting_back;
	private Button btn_setting_tuichu;
	private SharedPreferences sp;
	private SharedPreferences.Editor et;
	private String token;
	private ProgressDialog pd;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting);
		
		sp = this.getSharedPreferences("icsp", Context.MODE_WORLD_READABLE);
        token = sp.getString("token", "");
        
		iv_setting_back = (ImageView) findViewById(R.id.iv_setting_back);
		btn_setting_tuichu = (Button) findViewById(R.id.btn_setting_tuichu);
		//����
		iv_setting_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		//�˳���¼
		btn_setting_tuichu.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				pd = new ProgressDialog(SettingActivity.this);
				pd.setTitle("正在登出...");
				pd.setMessage("请稍等.");
				pd.setCancelable(false);
				pd.setIndeterminate(true);
				pd.show();
				new Thread(SettingActivity.this).start();
			}
		});
		
	}

	
	@Override
	protected void onDestroy() {
		if (pd != null) {
			pd.dismiss();
		}
		super.onDestroy();
	}


	@Override
	public void run() {
		URL url=null;
		String result = "";
		try {
			url = new URL(AppConstants.USER_SIGNOUT);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();

			conn.setRequestMethod("POST");
			conn.setDoOutput(true);

			Writer writer = new OutputStreamWriter(conn.getOutputStream());
			String str = "token=" + token;
			writer.write(str);
			writer.flush();

			Reader is = new InputStreamReader(conn.getInputStream());

			StringBuilder sb = new StringBuilder();
			char c[] = new char[1024];
			int len=0;

			while ((len = is.read(c)) != -1) {
				sb.append(c, 0, len);
			}
			result = sb.toString();

		} catch (Exception e) {
			e.printStackTrace();
		}

		Message msg = handler.obtainMessage();
		msg.what = 0x11;
		msg.obj = result;
		msg.sendToTarget();
	}
	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			String result = (String) msg.obj;
			Log.i("TEST", "用户登出JSON---" + result);
			JSONObject object = null;
			if (!result.equals("")) {
				try {
					object = new JSONObject(result);
//					Log.i("TEST", "------sssss-----" +object.getInt("token") );
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			switch (msg.what) {
			case 0x11:
				if (object != null) {
					if (pd != null) pd.dismiss();
					try {
						if (1==object.getInt("status")) {
//							SettingActivity.this.finish();
							et = sp.edit();
							et.putBoolean("isLogin", false);
							et.commit();
							Intent intent = new Intent(SettingActivity.this, LoginActivity.class);
							intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK); 
							startActivity(intent);
						}else if(0==object.getInt("status")){
							Toast.makeText(SettingActivity.this,object.getString("text"),Toast.LENGTH_SHORT).show();
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			}
		}
	};
}
