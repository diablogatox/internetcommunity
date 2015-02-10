package com.orfid.ic;

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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class LoginMyActivity extends Activity implements Runnable {
	private EditText et_login_username;
	private EditText et_login_password;
	private ImageView login_my_back;
	private Button btn_register;
	private Button btn_login;
	private SharedPreferences sp;
	private SharedPreferences.Editor et;
	ProgressDialog pd;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_my);
		
		sp = this.getSharedPreferences("icsp", Context.MODE_WORLD_READABLE);
		et_login_username = (EditText) findViewById(R.id.et_login_username);
		et_login_password = (EditText) findViewById(R.id.et_login_password);
		btn_register = (Button) findViewById(R.id.btn_register);
		btn_login = (Button) findViewById(R.id.btn_login);
		login_my_back = (ImageView) findViewById(R.id.login_my_back);
		
		login_my_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
			
		});
		
		//ע��
		btn_register.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				startActivity(new Intent(LoginMyActivity.this,RegisterMyActivity.class));
			}
		});
		//��¼
		btn_login.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				String username = et_login_username.getText().toString();
				String password = et_login_password.getText().toString();
				if(username.equals("")||username==null){
					Toast.makeText(LoginMyActivity.this, "请输入用户名", Toast.LENGTH_SHORT).show();
					et_login_username.requestFocus();
					return;
				}else if(password.equals("")||password==null){
					Toast.makeText(LoginMyActivity.this, "密码不能为空", Toast.LENGTH_SHORT).show();
					et_login_password.requestFocus();
					return;
				}else{
					new Thread(LoginMyActivity.this).start();
					
					pd = new ProgressDialog(LoginMyActivity.this);
					pd.setTitle("正在登录");
					pd.setMessage("请稍等.");
					pd.setCancelable(true);
					pd.setIndeterminate(true);
					pd.show();
				}
			}
		});
	}

	@Override
	public void run() {
		URL url=null;
		String result = "";
		try {
			url = new URL("http://sww.yxkuaile.com/user/login");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();

			conn.setRequestMethod("POST");
			conn.setDoOutput(true);

			Writer writer = new OutputStreamWriter(conn.getOutputStream());

			String str = "username=" + et_login_username.getText().toString()
					+ "&password=" + et_login_password.getText().toString();
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
			Log.i("TEST", "��¼��ϢJSON---" + result);
			JSONObject object = null;
			if (!result.equals("")) {
				try {
					object = new JSONObject(result);
//					Log.i("TEST", "��¼��Ϣtoken---" + object.getInt("token"));
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			if (pd != null) pd.dismiss();
			switch (msg.what) {
			case 0x11:
				if (object != null) {
					try {
						if (1==object.getInt("status")) {
							
							et = sp.edit();
							et.putString("token", object.getString("token"));
							
							JSONObject data = new JSONObject(object.getString("data"));
							et.putString("uid", data.getString("uid"));
							et.putString("username", data.getString("username"));
							et.putString("password", et_login_password.getText().toString());
							et.putString("photo", data.getString("photo"));
							et.putBoolean("isLogin", true);
							et.commit();
//							Toast.makeText(LoginMyActivity.this,object.getString("text"),Toast.LENGTH_SHORT).show();
//							Log.d("uid", sp.getString("uid", ""));
							Intent intent = new Intent(LoginMyActivity.this,HomeActivity.class);
							intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK); 
							startActivity(intent);
							
						}else if(0==object.getInt("status")){
							Toast.makeText(LoginMyActivity.this,object.getString("text"),Toast.LENGTH_SHORT).show();
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			}
		}
	};
	
}
