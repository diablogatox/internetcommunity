package com.orfid.internetcommunity;

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
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class TextQipaoActivity extends Activity {
	private ImageView iv_qipao_back;
	private EditText et_fabu;
	private TextView tv_qipao_allnum;
	private TextView tv_qipao_fabu;
	int num = 30;//限制的最大字数
	boolean isSignature = false;
	private SharedPreferences sp;
	private String token;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.text_qipao);

		sp = this.getSharedPreferences("icsp", Context.MODE_WORLD_READABLE);
        token = sp.getString("token", "");
        
		Intent intent = getIntent();
		if (intent != null) {
			isSignature = intent.getExtras().getBoolean("isSignature");
		}
		
		iv_qipao_back = (ImageView) findViewById(R.id.iv_qipao_back);
		et_fabu = (EditText) findViewById(R.id.et_fabu);
		tv_qipao_allnum = (TextView) findViewById(R.id.tv_qipao_allnum);
		tv_qipao_fabu = (TextView) findViewById(R.id.tv_qipao_fabu);
//		inintEditor();
		
		tv_qipao_allnum.setText("0/"+num);
		et_fabu.addTextChangedListener(new TextWatcher() {
			private CharSequence temp;  
			private int selectionStart;  
			private int selectionEnd;
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				temp = s;
			}
			
			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				int number = num - s.length();   
				tv_qipao_allnum.setText(number+"/30");   
				selectionStart = et_fabu.getSelectionStart();   
				selectionEnd = et_fabu.getSelectionEnd();   
				if (temp.length() > num) {    
					s.delete(selectionStart - 1, selectionEnd);    
					int tempSelection = selectionEnd;    
					et_fabu.setText(s);    
					et_fabu.setSelection(tempSelection);//设置光标在最后
				}
			}
		});
		
		//返回
		iv_qipao_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				MaoPaoActivity.instance.finish();
				finish();
			}
		});
		
		//发布
		tv_qipao_fabu.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
//				String str = et_fabu.getText().toString();
//				editor.putString("user_sign", str);
//				editor.commit();
//				finish();
//				MaoPaoActivity.instance.finish();
				Log.d("isSignature=======>", isSignature?"yes":"no");
				String str = et_fabu.getText().toString().trim();
				new UserSignatureTask(str).execute();
			}
		});
	}

//	private void inintEditor() {
//		preferences=getSharedPreferences("user",Context.MODE_PRIVATE);
//		editor = preferences.edit();
//	}
	
	private class UserSignatureTask extends AsyncTask<String, Void, String> {

		private String text;
		
		public UserSignatureTask(String text) {
			this.text = text;
		}
		
		@Override
		protected String doInBackground(String... params) {
			URL url=null;
			String result = "";
			try {
				url = new URL(AppConstants.USER_SIGNATURE);
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();

				conn.setRequestMethod("POST");
				conn.setDoOutput(true);

				Writer writer = new OutputStreamWriter(conn.getOutputStream());

				String str = "token=" + token + "&text=" + text;
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
			Log.d("TEST", "用户签名JSON---" + result);
			JSONObject obj;
			try {
				obj = new JSONObject(result);
				if (1==obj.getInt("status")) {
//					Toast.makeText(TextQipaoActivity.this,obj.getString("text"),Toast.LENGTH_SHORT).show();
////					startActivity(new Intent(LoginMyActivity.this,HomeActivity.class));
					Intent intent = new Intent();
					intent.putExtra("text", text);
					setResult(RESULT_OK, intent);
					finish();
				}else if(0==obj.getInt("status")){
					Toast.makeText(TextQipaoActivity.this,obj.getString("text"),Toast.LENGTH_SHORT).show();
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		
	}
}
