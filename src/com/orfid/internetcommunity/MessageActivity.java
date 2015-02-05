package com.orfid.internetcommunity;

import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.orfid.internetcommunity.SwipeMenuListView.OnMenuItemClickListener;
import com.orfid.internetcommunity.SwipeMenuListView.OnSwipeListener;

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
		.showStubImage(R.drawable.no_portrait)
		.showImageForEmptyUri(R.drawable.no_portrait).cacheInMemory()
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
//		lv.setOnItemClickListener(new OnItemClickListener() {
//
//			@Override
//			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
//					long id) {
//				Log.d("sid======>", id+"");
//				Intent intent = new Intent(MessageActivity.this,ChattingActivity.class);
//				intent.putExtra("sid", id+"");
//				startActivity(intent);
//			}
//		});
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Log.d("sid======>", id+"");
				View v1 = view.findViewById(R.id.unread_msg_count1);
				View v2 = view.findViewById(R.id.unread_msg_count2);
				v1.setBackgroundColor(Color.TRANSPARENT);
				v2.setBackgroundColor(Color.TRANSPARENT);
				Intent intent = new Intent(MessageActivity.this,ChattingActivity.class);
				intent.putExtra("sid", id+"");
				intent.putExtra("isGroup", adapter.getItem(position).getType().equals("2")?true:false);
				startActivity(intent);
			}
			
		});
		
		// step 1. create a MenuCreator
		SwipeMenuCreator creator = new SwipeMenuCreator() {

			@Override
			public void create(SwipeMenu menu) {
				// create "delete" item
				SwipeMenuItem deleteItem = new SwipeMenuItem(
						getApplicationContext());
				// set item background
				deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
						0x3F, 0x25)));
				// set item width
				deleteItem.setWidth(dp2px(90));
				// set a icon
				deleteItem.setIcon(R.drawable.ic_delete);
				// add to menu
				menu.addMenuItem(deleteItem);
			}
		};
		
		// set creator
//		lv.setMenuCreator(creator);

		// set SwipeListener
//		lv.setOnSwipeListener(new OnSwipeListener() {
//			
//			@Override
//			public void onSwipeStart(int position) {
//				// swipe start
//			}
//			
//			@Override
//			public void onSwipeEnd(int position) {
//				// swipe end
//			}
//		});
		
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

		HashMap<Integer,View> lmap = new HashMap<Integer,View>();

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			PictureViewHolder viewHolder = null;
			if (lmap.get(position)==null) {
				viewHolder = new PictureViewHolder();
				convertView = LayoutInflater.from(MessageActivity.this).inflate(
						R.layout.message1, parent, false);
				viewHolder.iv_messages1_pic = (ImageView) convertView.findViewById(R.id.iv_messages1_pic);
				viewHolder.tv_message1_title = (TextView) convertView.findViewById(R.id.tv_message1_title);
				viewHolder.tv_message1_content = (TextView) convertView.findViewById(R.id.tv_message1_content);
				viewHolder.tv_message1_time = (TextView) convertView.findViewById(R.id.tv_message1_time);
				viewHolder.group_pic = convertView.findViewById(R.id.group_pic);
				viewHolder.top_center = (ImageView) convertView.findViewById(R.id.top_center);
				viewHolder.bottom_left = (ImageView) convertView.findViewById(R.id.bottom_left);
				viewHolder.bottom_right = (ImageView) convertView.findViewById(R.id.bottom_right);
				viewHolder.unread_msg_count1 = convertView.findViewById(R.id.unread_msg_count1);
				viewHolder.unread_msg_count2 = convertView.findViewById(R.id.unread_msg_count2);
				viewHolder.count1 = (TextView) convertView.findViewById(R.id.count1);
				viewHolder.count2 = (TextView) convertView.findViewById(R.id.count2);

				lmap.put(position, convertView);
				convertView.setTag(viewHolder);
			} else {
				convertView = lmap.get(position);
				viewHolder = (PictureViewHolder) convertView.getTag();
			}
			
			objBean = items.get(position);
//			Log.d("message users=============>", objBean.getUsers().length+"");

			Message msg = objBean.getMessage();
			if (msg != null) {
				Friend fd = msg.getUser();
				Log.d("type======>test", objBean.getType());

				if (objBean.getType().equals("1")) {
					String photo = null, name = null;
					Friend[] users = objBean.getUsers();
					for (int i=0; i<users.length; i++) {
						if (!users[i].getUid().equals(sp.getString("uid", ""))) {
							photo = users[i].getPhoto();
							name = users[i].getUsername();
						}
					}
					
					if (photo == null || photo.equals("") || photo.equals("null")) {
						viewHolder.iv_messages1_pic.setImageResource(R.drawable.no_portrait);//头像
					} else {
						imageLoader.displayImage(AppConstants.MAIN_DOMAIN + "/" + photo, viewHolder.iv_messages1_pic,
								options, null);
					}
					
					if (Integer.parseInt(objBean.getNewmsg()) > 0) {
						viewHolder.unread_msg_count1.setVisibility(View.VISIBLE);
						viewHolder.count1.setText(objBean.getNewmsg());
					}
					
					viewHolder.tv_message1_title.setText(name);//标题
				} else if (objBean.getType().equals("2")) {
					viewHolder.iv_messages1_pic.setVisibility(View.GONE);
					viewHolder.group_pic.setVisibility(View.VISIBLE);
					Log.d("message users=============>", objBean.getUsers().length + "");
					Friend[] users = objBean.getUsers();
					Log.d("users[0]===photo=====>", users[0].getPhoto());
					Log.d("users[1]===photo=====>", users[1].getPhoto());
					Log.d("users[2]===photo=====>", users[2].getPhoto());
					if (users[0].getPhoto().trim().equals("") || users[0].getPhoto().equals("null")) {
						viewHolder.iv_messages1_pic.setImageResource(R.drawable.no_portrait);
					} else {
						imageLoader.displayImage(AppConstants.MAIN_DOMAIN + "/" + users[0].getPhoto(), viewHolder.top_center,
								options, null);
					}
					if (users[1].getPhoto().trim().equals("") || users[1].getPhoto().equals("null")) {
						viewHolder.iv_messages1_pic.setImageResource(R.drawable.no_portrait);
					} else {
						imageLoader.displayImage(AppConstants.MAIN_DOMAIN + "/" + users[1].getPhoto(), viewHolder.bottom_left,
								options, null);
					}
					if (users[2].getPhoto().trim().equals("") || users[2].getPhoto().equals("null")) {
						viewHolder.iv_messages1_pic.setImageResource(R.drawable.no_portrait);
					} else {
						imageLoader.displayImage(AppConstants.MAIN_DOMAIN + "/" + users[2].getPhoto(), viewHolder.bottom_right,
								options, null);
					}
					
					if (Integer.parseInt(objBean.getNewmsg()) > 0) {
						viewHolder.unread_msg_count2.setVisibility(View.VISIBLE);
						viewHolder.count2.setText(objBean.getNewmsg());
					}
					
					viewHolder.tv_message1_title.setText(fd.getUsername());//标题
				}

				
				viewHolder.tv_message1_content.setText(msg.getText()); //内容
				viewHolder.tv_message1_time.setText(Utils.covertTimestampToDate(Long.parseLong(msg.getSendtime()) * 1000)); //时间
			}
			return convertView;
		}
		public class PictureViewHolder {
			ImageView iv_messages1_pic;
			TextView tv_message1_title;
			TextView tv_message1_content;
			TextView tv_message1_time;
			View group_pic;
			ImageView top_center, bottom_left, bottom_right;
			View unread_msg_count1, unread_msg_count2;
			TextView count1, count2;
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
					
					// step 2. listener item click event
//					lv.setOnMenuItemClickListener(new OnMenuItemClickListener() {
//						@Override
//						public void onMenuItemClick(int position, SwipeMenu menu, int index) {
//							Log.d("positoin==========>", position+"");
//							messageSessionItems.remove(position);
//							adapter.notifyDataSetChanged();
//						}
//					});
				}else if(0==obj.getInt("status")){
					Toast.makeText(MessageActivity.this,obj.getString("text"),Toast.LENGTH_SHORT).show();
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	private int dp2px(int dp) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
				getResources().getDisplayMetrics());
	}
}
