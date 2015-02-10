package com.orfid.ic;

import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class SaveUserInfoTask extends AsyncTask<String, Void, String> {

	private Context context;
	private SharedPreferences sp;
	private String token, username, birthday, sex, photo;
	
	public SaveUserInfoTask(Context context, String username, String birthday, String sex, String photo) {
		this.context = context;
		this.username = username;
		this.birthday = birthday;
		this.sex = sex;
		this.photo = photo;
		
		sp = context.getSharedPreferences("icsp", Context.MODE_WORLD_READABLE);
        token = sp.getString("token", "");
	}
	
	@Override
	protected String doInBackground(String... params) {
		URL url=null;
		String result = "";
		try {
			url = new URL(AppConstants.SAVE_USER_INFO);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();

			conn.setRequestMethod("POST");
			conn.setDoOutput(true);

			Writer writer = new OutputStreamWriter(conn.getOutputStream());

			String str = "token=" + token + "&username=" + (username!=null?username:sp.getString("username", "")) +
					"&birthday=" + (birthday!=null?birthday:sp.getString("birthday", "")) +
					"&sex=" + (sex!=null?sex:sp.getString("sex", "")) + 
					"&file=" + (photo!=null?photo:sp.getString("photo", ""));
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
		Log.d("TEST", "获取用户信息资料JSON---" + result);
		JSONObject obj;
		try {
			obj = new JSONObject(result);
			if (1==obj.getInt("status")) {
				Toast.makeText(context,obj.getString("text"),Toast.LENGTH_SHORT).show();
			}else if(0==obj.getInt("status")){
				Toast.makeText(context,obj.getString("text"),Toast.LENGTH_SHORT).show();
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

}
