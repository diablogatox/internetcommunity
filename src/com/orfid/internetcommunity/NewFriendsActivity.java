package com.orfid.internetcommunity;

import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class NewFriendsActivity extends Activity implements Runnable{
	//新的好友界面
	private ImageView new_friends_back;
	private ListView lv;
	private FriendRequestsRowAdapter adapter;
	private TextView emptyView;
	private SharedPreferences sp;
	private String token;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_friends);
		
		new_friends_back = (ImageView) findViewById(R.id.new_friends_back);
		lv = (ListView) findViewById(R.id.lv_new_friends);
		emptyView = (TextView) findViewById(R.id.empty_view);
		lv.setEmptyView(emptyView);
		lv.setAdapter(adapter);
		new_friends_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		
		sp = this.getSharedPreferences("icsp", Context.MODE_WORLD_READABLE);
        token = sp.getString("token", "");
        
        new LoadFriendRequestTask().execute();
	}
	class MyAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return 2;
		}

		@Override
		public Object getItem(int arg0) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			PictureViewHolder viewHolder= null;
			if (convertView == null) {
				viewHolder = new PictureViewHolder();
				convertView = LayoutInflater.from(NewFriendsActivity.this).inflate(
						R.layout.new_friends1, parent, false);
				viewHolder.iv_new_friends = (ImageView) convertView
						.findViewById(R.id.iv_new_friends);
				viewHolder.tv_new_friends1 = (TextView) convertView
						.findViewById(R.id.tv_new_friends1);
				viewHolder.tv_new_friends2 = (TextView) convertView
						.findViewById(R.id.tv_new_friends2);
				viewHolder.tv_accept = (Button) convertView
						.findViewById(R.id.tv_accept);
				viewHolder.btn_accept = (Button) convertView
						.findViewById(R.id.btn_accept);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (PictureViewHolder) convertView.getTag();
			}

			viewHolder.tv_new_friends1.setText("周黑猫");// 好友名字
			viewHolder.tv_new_friends2.setText("不加信不信我打我打");// 好友签名
			viewHolder.iv_new_friends.setBackgroundResource(R.drawable.ic_launcher);//好友头像
			final Button temp = viewHolder.tv_accept;
			viewHolder.btn_accept.setOnClickListener(new OnClickListener() {
				//接受好友添加请求
				@Override
				public void onClick(View view) {
					view.setVisibility(View.GONE);//点击使按钮隐藏
					temp.setVisibility(View.VISIBLE);//显示“已添加”
					new Thread(NewFriendsActivity.this).start();
				}
			});
			return convertView;
		}
		public class PictureViewHolder {
			ImageView iv_new_friends;
			TextView tv_new_friends1;
			TextView tv_new_friends2;
			Button tv_accept;
			Button btn_accept;
		}
		
	}
	@Override
	public void run() {
		URL url=null;
		String result = "";
		try {
			url = new URL("http://sww.yxkuaile.com/message/UserAction");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();

			conn.setRequestMethod("POST");
			conn.setDoOutput(true);

			Writer writer = new OutputStreamWriter(conn.getOutputStream());

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
			Log.i("TEST", "---同意加为好友---JSON" + result);
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
							Toast.makeText(NewFriendsActivity.this,"已成功添加其为好友",Toast.LENGTH_SHORT).show();
						}else if(0==object.getInt("status")){
							Toast.makeText(NewFriendsActivity.this,"添加好友失败",Toast.LENGTH_SHORT).show();
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			}
		}
	};
	
	private class LoadFriendRequestTask extends AsyncTask<String, Void, String> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}

		@Override
		protected void onPostExecute(String result) {
			Log.d("TEST", "好友请求消息列表JSON---" + result);
			JSONObject obj;
			try {
				obj = new JSONObject(result);
				if (1==obj.getInt("status")) {
					Toast.makeText(NewFriendsActivity.this,obj.getString("text"),Toast.LENGTH_SHORT).show();

		        		
		        		MyJSONParser myJsonParser = new MyJSONParser();

		    	        List<FriendRequest> friendRequests = null;

		    	        try{

		    	        	friendRequests = myJsonParser.parse(obj);
		    	        	Log.d("friendRequests=============", friendRequests.toString());
		    	        }catch(Exception e){
		    	        	Log.d("Exception",e.toString());
		    	        }

		    	        FriendRequestsRowAdapter adapter = new FriendRequestsRowAdapter(NewFriendsActivity.this,
		    					R.layout.new_friends1, friendRequests);

		    			lv.setAdapter(adapter);
		    			lv.setOnItemClickListener(new OnItemClickListener() {
		    				@Override
		    				public void onItemClick(AdapterView<?> parent, View view, int position,
		    						long id) {
		    				}
		    			});

				}else if(0==obj.getInt("status")){
					Toast.makeText(NewFriendsActivity.this,obj.getString("text"),Toast.LENGTH_SHORT).show();
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		@Override
		protected String doInBackground(String... params) {
			URL url=null;
			String result = "";
			try {
				url = new URL(AppConstants.FRIEND_REQUEST_LIST);
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
			return result;
		}
		
	}
	
	
	public class FriendRequestsRowAdapter extends ArrayAdapter<FriendRequest> {

		private Activity activity;
		private List<FriendRequest> items;
		private FriendRequest objBean;
		private int row;
		private Context context;

		public FriendRequestsRowAdapter(Context context, int resource, List<FriendRequest> arrayList) {
			super(context, resource, arrayList);
			this.context = context;
			this.row = resource;
			this.items = arrayList;
		}


		@Override
		public long getItemId(int position) {
			return items.get(position).getMsgid();
		}


		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			PictureViewHolder viewHolder= null;
			if (convertView == null) {
				viewHolder = new PictureViewHolder();
				convertView = LayoutInflater.from(NewFriendsActivity.this).inflate(
						R.layout.new_friends1, parent, false);
				viewHolder.iv_new_friends = (ImageView) convertView
						.findViewById(R.id.iv_new_friends);
				viewHolder.tv_new_friends1 = (TextView) convertView
						.findViewById(R.id.tv_new_friends1);
				viewHolder.tv_new_friends2 = (TextView) convertView
						.findViewById(R.id.tv_new_friends2);
				viewHolder.tv_accept = (Button) convertView
						.findViewById(R.id.tv_accept);
				viewHolder.btn_accept = (Button) convertView
						.findViewById(R.id.btn_accept);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (PictureViewHolder) convertView.getTag();
			}

			objBean = items.get(position);
			
			viewHolder.tv_new_friends1.setText(objBean.getUsername());// 好友名字
			viewHolder.tv_new_friends2.setText(objBean.getText());// 好友签名
			viewHolder.iv_new_friends.setBackgroundResource(R.drawable.ic_launcher);//好友头像
			final Button temp = viewHolder.tv_accept;
			if (objBean.getAction() == 1) {
				viewHolder.btn_accept.setVisibility(View.GONE);//点击使按钮隐藏
				temp.setVisibility(View.VISIBLE);//显示“已添加”
			}
			viewHolder.btn_accept.setOnClickListener(new OnClickListener() {
				//接受好友添加请求
				@Override
				public void onClick(View view) {
					Log.d("msgid======>", objBean.getMsgid()+"");
					view.setVisibility(View.GONE);//点击使按钮隐藏
					temp.setVisibility(View.VISIBLE);//显示“已添加”
//					new Thread(NewFriendsActivity.this).start();
					new ReplyFriendRequestTask(objBean.getMsgid()).execute();
				}
			});
			return convertView;
		}
		
		public class PictureViewHolder {
			ImageView iv_new_friends;
			TextView tv_new_friends1;
			TextView tv_new_friends2;
			Button tv_accept;
			Button btn_accept;
		}

	}
	
	class ReplyFriendRequestTask extends AsyncTask<String, Void, String> {

		private int msgid;
		
		public ReplyFriendRequestTask(int msgid) {
			this.msgid = msgid;
		}
		
		@Override
		protected String doInBackground(String... params) {
			URL url=null;
			String result = "";
			try {
				url = new URL(AppConstants.REPLY_FRIEND_REQUEST);
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();

				conn.setRequestMethod("POST");
				conn.setDoOutput(true);

				Writer writer = new OutputStreamWriter(conn.getOutputStream());

				String str = "token=" + token + "&id=" + msgid + "&action=1";
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
			// TODO Auto-generated method stub
			super.onPostExecute(result);
		}
		
	}

}
