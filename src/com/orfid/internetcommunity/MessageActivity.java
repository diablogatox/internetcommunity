package com.orfid.internetcommunity;

import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.orfid.internetcommunity.PersonalActivity.MyAdapter.PictureViewHolder;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MessageActivity extends Activity {
	private ListView lv;
	private ImageView iv_message_back;
	private MyAdapter adapter;
	List<MessageSession> messageSessionItems = new ArrayList<MessageSession>();
	private SharedPreferences sp;
	private String token;
	ImageLoader imageLoader;
	private DisplayImageOptions options;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.message);
		
		sp = this.getSharedPreferences("icsp", Context.MODE_WORLD_READABLE);
        token = sp.getString("token", "");
        
        options = new DisplayImageOptions.Builder()
		.showStubImage(R.drawable.my_qq_pic)
		.showImageForEmptyUri(R.drawable.my_qq_pic).cacheInMemory()
		.cacheOnDisc().build();
		imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration
				.createDefault(MessageActivity.this));
        
		lv = (ListView) findViewById(R.id.lv_message);
		iv_message_back = (ImageView) findViewById(R.id.iv_message_back);
		iv_message_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
//		lv.setAdapter(new MyAdapter());
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long id) {
				Log.d("sid======>", id+"");
				Intent intent = new Intent(MessageActivity.this,ChattingActivity.class);
				intent.putExtra("sid", id+"");
				startActivity(intent);
			}
		});
		
		new LoadMessageSessionTask().execute();
	}
	
	
	
	
	class MyAdapter extends ArrayAdapter<MessageSession>{
		
		private List<MessageSession> items;
		private MessageSession objBean;
		
		public MyAdapter(Context context, int resource, List<MessageSession> arrayList) {
			super(context, resource, arrayList);
			this.items = arrayList;
		}

		
		@Override
		public int getCount() {
			return items == null ? 0: items.size();
		}


		@Override
		public MessageSession getItem(int position) {
			return items.get(position);
		}

		@Override
		public long getItemId(int position) {
			return Long.parseLong(items.get(position).getId());
		}


		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			PictureViewHolder viewHolder = null;
			if (convertView == null) {
				viewHolder = new PictureViewHolder();
				convertView = LayoutInflater.from(MessageActivity.this).inflate(
						R.layout.message1, parent, false);
				viewHolder.iv_messages1_pic = (ImageView) convertView.findViewById(R.id.iv_messages1_pic);
				viewHolder.tv_message1_title = (TextView) convertView.findViewById(R.id.tv_message1_title);
				viewHolder.tv_message1_content = (TextView) convertView.findViewById(R.id.tv_message1_content);
				viewHolder.tv_message1_time = (TextView) convertView.findViewById(R.id.tv_message1_time);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (PictureViewHolder) convertView.getTag();
			}
			
			objBean = items.get(position);
			Message msg = objBean.getMessage();
			Friend fd = msg.getUser();
			Log.d("photo======>test", fd.getPhoto());
			if (fd.getPhoto().trim().equals("") || fd.getPhoto().equals("null")) {
				viewHolder.iv_messages1_pic.setImageResource(R.drawable.my_qq_pic);//头像
			} else {
				imageLoader.displayImage(AppConstants.MAIN_DOMAIN + "/" + fd.getPhoto(), viewHolder.iv_messages1_pic,
						options, null);
			}
			viewHolder.tv_message1_title.setText(fd.getUsername());//标题
			viewHolder.tv_message1_content.setText(msg.getText()); //内容
			viewHolder.tv_message1_time.setText(Utils.covertTimestampToDate(Long.parseLong(msg.getSendtime()) * 1000)); //时间
			return convertView;
		}
		public class PictureViewHolder {
			ImageView iv_messages1_pic;
			TextView tv_message1_title;
			TextView tv_message1_content;
			TextView tv_message1_time;
		}
		
	}

//	class MyAdapter extends BaseAdapter{
//
//		@Override
//		public int getCount() {
//			return 2;
//		}
//
//		@Override
//		public Object getItem(int position) {
//			return null;
//		}
//
//		@Override
//		public long getItemId(int position) {
//			return position;
//		}
//
//		@Override
//		public View getView(int position, View convertView, ViewGroup parent) {
//			PictureViewHolder viewHolder = null;
//			if (convertView == null) {
//				viewHolder = new PictureViewHolder();
//				convertView = LayoutInflater.from(MessageActivity.this).inflate(
//						R.layout.message1, parent, false);
//				viewHolder.iv_messages1_pic = (ImageView) convertView.findViewById(R.id.iv_messages1_pic);
//				viewHolder.tv_message1_title = (TextView) convertView.findViewById(R.id.tv_message1_title);
//				viewHolder.tv_message1_content = (TextView) convertView.findViewById(R.id.tv_message1_content);
//				viewHolder.tv_message1_time = (TextView) convertView.findViewById(R.id.tv_message1_time);
//				convertView.setTag(viewHolder);
//			} else {
//				viewHolder = (PictureViewHolder) convertView.getTag();
//			}
////			viewHolder.iv_messages1_pic.setImageResource(R.drawable.my_qq_pic);//头像
//			viewHolder.tv_message1_title.setText("大家群聊来看看呗");//标题
//			viewHolder.tv_message1_content.setText("加入哈哈哈哈哈哈啊哈哈哈"); //内容
//			viewHolder.tv_message1_time.setText("12:53"); //时间
//			return convertView;
//		}
//		public class PictureViewHolder {
//			ImageView iv_messages1_pic;
//			TextView tv_message1_title;
//			TextView tv_message1_content;
//			TextView tv_message1_time;
//		}
//	
//}

	class LoadMessageSessionTask extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... params) {
			URL url=null;
			String result = "";
			try {
				url = new URL(AppConstants.MESSAGE_SESSION);
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

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}

		@Override
		protected void onPostExecute(String result) {
			Log.d("TEST", "消息会话列表JSON---" + result);
			JSONObject obj;
			try {
				obj = new JSONObject(result);
				if (1==obj.getInt("status")) {
//					Toast.makeText(MessageActivity.this,obj.getString("text"),Toast.LENGTH_SHORT).show();
					MessageSessionJSONParser parser = new MessageSessionJSONParser();
					JSONObject jObj = new JSONObject(obj.getString("data"));
					messageSessionItems = parser.parse(jObj);
					adapter = new MyAdapter(MessageActivity.this, R.layout.message1, messageSessionItems);
					lv.setAdapter(adapter);
				}else if(0==obj.getInt("status")){
					Toast.makeText(MessageActivity.this,obj.getString("text"),Toast.LENGTH_SHORT).show();
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		
	}
}
