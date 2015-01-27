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
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class RegisterMyActivity extends Activity implements Runnable{
	ImageView iv_register_back;
	ImageView iv_text_application;
	TextView tv_text_application;
	EditText et_register_username;//�û���
	EditText et_register_password;//����
	EditText et_confirm_password;//ȷ������
	Button btn_accomplished;//���ע��
	private SharedPreferences sp;
	private SharedPreferences.Editor et;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register_my);
		
		sp = this.getSharedPreferences("icsp", Context.MODE_WORLD_READABLE);
		
		iv_register_back = (ImageView) findViewById(R.id.iv_register_back);
		iv_text_application = (ImageView) findViewById(R.id.iv_text_application);
		tv_text_application = (TextView) findViewById(R.id.tv_text_application);
		et_register_username = (EditText) findViewById(R.id.et_register_username);
		et_register_password = (EditText) findViewById(R.id.et_register_password);
		et_confirm_password = (EditText) findViewById(R.id.et_confirm_password);
		//���»���
		tv_text_application.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
		tv_text_application.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// �������������Ȧ���ı�Э��
				
			}
		});
		btn_accomplished = (Button) findViewById(R.id.btn_accomplished);
		//����
		iv_register_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		//���ע��
		btn_accomplished.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String username = et_register_username.getText().toString();
				String password = et_register_password.getText().toString();
				String password_confirm = et_confirm_password.getText().toString();
				if(username.equals("")||username==null){
					Toast.makeText(RegisterMyActivity.this, "�������û���", Toast.LENGTH_SHORT).show();
					et_register_username.requestFocus();
					return;
				}else if(password.equals("")||password==null){
					Toast.makeText(RegisterMyActivity.this, "����������", Toast.LENGTH_SHORT).show();
					et_register_password.requestFocus();
					return;
				}else if(password_confirm.equals("")||password_confirm==null){
					Toast.makeText(RegisterMyActivity.this, "��ȷ����������", Toast.LENGTH_SHORT).show();
					et_confirm_password.requestFocus();
					return;
				}else if(!password_confirm.equals(password)){
					Toast.makeText(RegisterMyActivity.this, "�������벻һ��", Toast.LENGTH_SHORT).show();
					et_confirm_password.requestFocus();
					return;
				}else{
					new Thread(RegisterMyActivity.this).start();
				}
			}
		});
	}
	
	
	
	@Override
	protected void onRestart() {
		super.onRestart();
		if (sp.getBoolean("isLogin", false)) {
			Intent intent = new Intent(this, HomeActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK); 
			startActivity(intent);
			finish();
		}
	}



	@Override
	public void run() {
		URL url;
		String result = null;
		try {
			url = new URL("http://sww.yxkuaile.com/user/register");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();

			conn.setRequestMethod("POST");
			conn.setDoOutput(true);

			Writer writer = new OutputStreamWriter(conn.getOutputStream());

			String str = "username=" + et_register_username.getText().toString() 
					+ "&password=" + et_register_password.getText().toString()
					+ "&password_confirm=" + et_confirm_password.getText().toString();
			writer.write(str);
			writer.flush();

			Reader is = new InputStreamReader(conn.getInputStream());

			StringBuilder sb = new StringBuilder();
			char c[] = new char[1024];
			int len;

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
			Log.i("TEST", "ע����ϢJSON---" + result);
			JSONObject object = null;
			if (!result.equals("")) {
				try {
					object = new JSONObject(result);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			switch (msg.what) {
			case 0x11:
				if (object != null) {
					try {
						//ע��ɹ�
						if (1==object.getInt("status")) {
							Toast.makeText(RegisterMyActivity.this,
									object.getString("text"),
									Toast.LENGTH_SHORT).show();
//							startActivity(new Intent(RegisterMyActivity.this,
//									LoginMyActivity.class));
							// 自动登录
							new AutoLoginTask().execute();
						}//ע��ʧ��
						else if(0==object.getInt("status")){
							Toast.makeText(RegisterMyActivity.this,
									object.getString("text"),
									Toast.LENGTH_SHORT).show();
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			}
		}
	};
	
	private class AutoLoginTask extends AsyncTask<String, Void, String> {
		
		@Override
		protected String doInBackground(String... params) {
			URL url=null;
			String result = "";
			try {
				url = new URL(AppConstants.USER_LOGIN);
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();

				conn.setRequestMethod("POST");
				conn.setDoOutput(true);

				Writer writer = new OutputStreamWriter(conn.getOutputStream());

				String str = "username=" + et_register_username.getText().toString()
						+ "&password=" + et_register_password.getText().toString();
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
			return result;
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}

		@Override
		protected void onPostExecute(String result) {
			JSONObject object;
			try {
				object = new JSONObject(result);
				et = sp.edit();
				et.putString("token", object.getString("token"));
				
				JSONObject data = new JSONObject(object.getString("data"));
				et.putString("uid", data.getString("uid"));
				et.putString("username", data.getString("username"));
				et.putString("photo", data.getString("photo"));
				et.putBoolean("isLogin", true);
				et.commit();
				
				Intent intent = new Intent(RegisterMyActivity.this,
						PersonalActivity.class);
				intent.putExtra("uid", data.getString("uid"));
				startActivity(intent);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
	}

}
