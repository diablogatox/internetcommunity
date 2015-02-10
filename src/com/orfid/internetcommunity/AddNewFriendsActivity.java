package com.orfid.internetcommunity;

import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class AddNewFriendsActivity extends Activity implements Runnable{
	//添加好友界面
	private ImageView add_new_friends_back;
	private LinearLayout ll_find_user_id;
	private EditText et_add_new_friends;
	private SharedPreferences sp;
	private String token;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_new_friends);
		
		sp = this.getSharedPreferences("icsp", Context.MODE_WORLD_READABLE);
        token = sp.getString("token", "");
        
		add_new_friends_back = (ImageView) findViewById(R.id.add_new_friends_back);
		ll_find_user_id = (LinearLayout) findViewById(R.id.ll_find_user_id);
		et_add_new_friends = (EditText) findViewById(R.id.et_add_new_friends);
		
		//返回
		add_new_friends_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		//按ID搜索好友
		ll_find_user_id.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				String use_id = et_add_new_friends.getText().toString();
				Log.d("searched user id======>", use_id);
				if(use_id.equals("")||use_id==null){
					Toast.makeText(AddNewFriendsActivity.this, "请输入用户id", Toast.LENGTH_SHORT).show();
					et_add_new_friends.requestFocus();
					return;
				}else{
					new Thread(AddNewFriendsActivity.this).start();
				}
				
			}
		});
	}

	@Override
	public void run() {
		URL url=null;
		String result = "";
		try {
			url = new URL(AppConstants.FIND_USER_BY_UID);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();

			conn.setRequestMethod("POST");
			conn.setDoOutput(true);

			Writer writer = new OutputStreamWriter(conn.getOutputStream());

			String str = "token=" + token + "&uid=" + et_add_new_friends.getText().toString();
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
			Log.i("TEST", "用户ID--JSON---" + result);
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
						if (1==object.getInt("status")) {
//							Toast.makeText(AddNewFriendsActivity.this,object.getString("data"),Toast.LENGTH_SHORT).show();
							JSONArray arr = new JSONArray(object.getString("data"));
							JSONObject obj = arr.getJSONObject(0);
							Intent intent = new Intent();
							intent.setClass(AddNewFriendsActivity.this,SelectSpecificActivity.class);
							intent.putExtra("uid", obj.getString("uid"));
							intent.putExtra("username", obj.getString("username"));
							intent.putExtra("photo", obj.getString("photo"));
							startActivity(intent);
						}else if(0==object.getInt("status")){
							Toast.makeText(AddNewFriendsActivity.this,"用户不存在",Toast.LENGTH_SHORT).show();
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			}
		}
	};
}
