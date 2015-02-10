package com.orfid.ic;

import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class SexyActivity extends Activity {
	private ImageView iv_sexy_back;
	private ImageView iv_sexy_choice1;
	private ImageView iv_sexy_choice2;
	private RelativeLayout rl_sexy_boy;
	private RelativeLayout rl_sexy_girl;
	boolean boo = true;
	private SharedPreferences sp;
	private String token;
//	private SharedPreferences sp_sexy_info;
//	private Editor ed_sexy_info;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sexy);
		
		iv_sexy_back = (ImageView) findViewById(R.id.iv_sexy_back);
		iv_sexy_choice1 = (ImageView) findViewById(R.id.iv_sexy_choice1);
		iv_sexy_choice2 = (ImageView) findViewById(R.id.iv_sexy_choice2);
		rl_sexy_boy = (RelativeLayout) findViewById(R.id.rl_sexy_boy);
		rl_sexy_girl = (RelativeLayout) findViewById(R.id.rl_sexy_girl);
//		sp_sexy_info = getSharedPreferences("sexy", MODE_PRIVATE);
//		ed_sexy_info = sp_sexy_info.edit();
		
		sp = this.getSharedPreferences("icsp", Context.MODE_WORLD_READABLE);
        token = sp.getString("token", "");
        
        int gender = Integer.parseInt(sp.getString("sex", ""));
        if (gender == 1) {
        	iv_sexy_choice1.setVisibility(View.VISIBLE);
        } else if (gender == 2) {
        	iv_sexy_choice2.setVisibility(View.VISIBLE);
        }
        
		//男
		rl_sexy_boy.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				boo = true;
				if (!iv_sexy_choice1.isShown()) {
					// save change
					new SaveUserInfoTask(SexyActivity.this, null, null, boo?"1":"2", null).execute();
				}
				iv_sexy_choice1.setVisibility(View.VISIBLE);
				iv_sexy_choice2.setVisibility(View.GONE);
				
			}
		});
		//女
		rl_sexy_girl.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				boo = false;
				if (!iv_sexy_choice2.isShown()) {
					// save change
					new SaveUserInfoTask(SexyActivity.this, null, null, boo?"1":"2", null).execute();
				}
				iv_sexy_choice1.setVisibility(View.GONE);
				iv_sexy_choice2.setVisibility(View.VISIBLE);
			}
		});
		//返回
		iv_sexy_back.setOnClickListener(new OnClickListener() {
					
			@Override
			public void onClick(View arg0) {
//				writeLoginData();
				Intent intent = new Intent();
				int selectedGender = 0;
				if (iv_sexy_choice1.isShown()) {
					selectedGender = 1;
				} else if (iv_sexy_choice2.isShown()) {
					selectedGender = 2;
				}
				intent.putExtra("selectedGender", selectedGender);
				setResult(RESULT_OK, intent);
				finish();
			}
		});
	}
//	public void writeLoginData(){
//		ed_sexy_info.clear();
//		ed_sexy_info.commit();
//		if(boo){
//			ed_sexy_info.putString("nan", "1");
//		}else{
//			ed_sexy_info.putString("woman", "0");
//		}
//		ed_sexy_info.commit();
//	}
	
//	class SaveUserInfoTask extends AsyncTask<String, Void, String> {
//
//		private String username, birthday, sex, photo;
//		
//		public SaveUserInfoTask(String username, String birthday, String sex, String photo) {
//			this.username = username;
//			this.birthday = birthday;
//			this.sex = sex;
//			this.photo = photo;
//		}
//		
//		@Override
//		protected String doInBackground(String... params) {
//			URL url=null;
//			String result = "";
//			try {
//				url = new URL(AppConstants.SAVE_USER_INFO);
//				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//
//				conn.setRequestMethod("POST");
//				conn.setDoOutput(true);
//
//				Writer writer = new OutputStreamWriter(conn.getOutputStream());
//
//				String str = "token=" + token + "&username=" + (username!=null?username:sp.getString("username", "")) +
//						"&birthday=" + (birthday!=null?birthday:sp.getString("birthday", "")) +
//						"&sex=" + (sex!=null?sex:sp.getString("sex", "")) +
//						"&file=" + (photo!=null?photo:sp.getString("photo", ""));
//				Log.d("str=========>", str);
//				writer.write(str);
//				writer.flush();
//
//				Reader is = new InputStreamReader(conn.getInputStream());
//
//				StringBuilder sb = new StringBuilder();
//				char c[] = new char[1024];
//				int len=0;
//
//				while ((len = is.read(c)) != -1) {
//					sb.append(c, 0, len);
//				}
//				result = sb.toString();
//
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//			return result;
//		}
//
//		@Override
//		protected void onPreExecute() {
//			// TODO Auto-generated method stub
//			super.onPreExecute();
//		}
//
//		@Override
//		protected void onPostExecute(String result) {
//			Log.d("TEST", "保存用户信息资料 JSON---" + result);
//			JSONObject obj;
//			try {
//				obj = new JSONObject(result);
//				if (1==obj.getInt("status")) {
//					Toast.makeText(SexyActivity.this,obj.getString("text"),Toast.LENGTH_SHORT).show();
//				} else if (0==obj.getInt("status")) {
//					Toast.makeText(SexyActivity.this,obj.getString("text"),Toast.LENGTH_SHORT).show();
//				}
//			} catch (JSONException e) {
//				e.printStackTrace();
//			}
//		}
//		
//	}
}
