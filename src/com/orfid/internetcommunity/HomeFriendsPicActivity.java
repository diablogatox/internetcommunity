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
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class HomeFriendsPicActivity extends Activity implements Runnable{
	private ImageView home_pic_back;
	private TextView tv_lahei1, tv_pic_name1, tv_name_id1, tv_pic_age;
	private Button btn_add_friends;
	private GridView gv_friends_pic_home;
	private String uid;
	private SharedPreferences sp;
	private String token;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home_friends_pic);
		
		home_pic_back = (ImageView) findViewById(R.id.home_pic_back);
		tv_lahei1 = (TextView) findViewById(R.id.tv_lahei1);
		tv_pic_name1 = (TextView) findViewById(R.id.tv_pic_name1);
		tv_name_id1 = (TextView) findViewById(R.id.tv_name_id1);
		tv_pic_age = (TextView) findViewById(R.id.tv_pic_age);
		btn_add_friends = (Button) findViewById(R.id.btn_add_friends);
		gv_friends_pic_home = (GridView) findViewById(R.id.gv_friends_pic_home);
		gv_friends_pic_home.setAdapter(new GameAdapter());
		gv_friends_pic_home.setFocusable(false);
		tv_lahei1.setText("拉黑/举报");
		// 返回
		home_pic_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		//拉黑/举报
		tv_lahei1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(new Intent(HomeFriendsPicActivity.this,LaHeiActivity.class));
			}
		});
		//添加好友
		btn_add_friends.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				new Thread(HomeFriendsPicActivity.this).start();
			}
		});
		
		sp = this.getSharedPreferences("icsp", Context.MODE_WORLD_READABLE);
        token = sp.getString("token", "");
		
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		uid = bundle.getString("uid");
		
		new FetchUserInfoTask().execute();
	}
	class GameAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return 4;
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			PictureViewHolder viewHolder = null;
			if (convertView == null) {
				viewHolder = new PictureViewHolder();
				convertView = LayoutInflater.from(HomeFriendsPicActivity.this).inflate(
						R.layout.gridview_hf, parent, false);
				viewHolder.tv_game_bg = (TextView) convertView
						.findViewById(R.id.tv_game_bg);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (PictureViewHolder) convertView.getTag();
			}
			viewHolder.tv_game_bg.setText("英雄联盟");
			return convertView;
		}
		public class PictureViewHolder{
			TextView tv_game_bg;
		}
		
	}
	@Override
	public void run() {
		URL url=null;
		String result = "";
		try {
			url = new URL("http://sww.yxkuaile.com/message/addfriend");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();

			conn.setRequestMethod("POST");
			conn.setDoOutput(true);

			Writer writer = new OutputStreamWriter(conn.getOutputStream());

			String str = "token=" + token + "&uid=" + uid;
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
			Log.i("TEST", "---加好友请求---JSON" + result);
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
							startActivity(new Intent(HomeFriendsPicActivity.this,ToastActivity.class));
						}else if(0==object.getInt("status")){
							Toast.makeText(HomeFriendsPicActivity.this,"网络异常",Toast.LENGTH_SHORT).show();
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			}
		}
	};
	
	private class FetchUserInfoTask extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... params) {
			URL url=null;
			String result = "";
			try {
				url = new URL(AppConstants.FETCH_USER_INFO);
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();

				conn.setRequestMethod("POST");
				conn.setDoOutput(true);

				Writer writer = new OutputStreamWriter(conn.getOutputStream());

				String str = "token=" + token + "&uid=" + uid;
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
//					Toast.makeText(HomeFriendsPicActivity.this,obj.getString("text"),Toast.LENGTH_SHORT).show();
////					startActivity(new Intent(LoginMyActivity.this,HomeActivity.class));
//					// 加载附近用户列表
//					new LoadNearbyUsersTask().excute();
					JSONObject jObj = new JSONObject(obj.getString("data"));
					tv_name_id1.setText(jObj.getString("uid"));
					tv_pic_name1.setText(jObj.getString("username"));
					int age = Utils.getAge(Long.parseLong(jObj.getString("birthday")) * 1000);
					tv_pic_age.setText(age+"");
					
//					age = Utils.getAge(jObj.getString("birthday"));
				}else if(0==obj.getInt("status")){
					Toast.makeText(HomeFriendsPicActivity.this,obj.getString("text"),Toast.LENGTH_SHORT).show();
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		
	}
	
}
