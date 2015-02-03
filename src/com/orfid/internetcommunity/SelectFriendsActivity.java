package com.orfid.internetcommunity;

import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.mofang.pb.Contacts;
import com.mofang.pb.ContactsAdapterSF;
import com.mofang.util.PinyinComparator;
import com.mofang.util.PinyinUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

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
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class SelectFriendsActivity extends Activity implements Runnable {
	private TextView tv_ps_cancel,tv_ps_sure;
	private GridView gv_ps;
	private ListView lv_ps1;
	private ContactsAdapterSF adaptersf;
	private SharedPreferences sp;
	private String token;
	private List<Map<String, Object>> mapList, mapList2;
	private MyGVAdapter gvAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.public_speak);
		
		initWidget();
		tv_ps_cancel = (TextView) findViewById(R.id.tv_ps_cancel);
		tv_ps_sure = (TextView) findViewById(R.id.tv_ps_sure);
		gv_ps = (GridView) findViewById(R.id.gv_ps);
		lv_ps1 = (ListView) findViewById(R.id.lv_ps1);
		
		mapList2 = new ArrayList<Map<String, Object>>();
		gvAdapter = new MyGVAdapter(this, mapList2);
		gv_ps.setAdapter(gvAdapter);
		
		gv_ps.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Log.d("gv_index=====>", position+"");
//				
//				int lvIndex = Integer.parseInt(mapList2.get(position).get("lvIndex").toString());
//				Log.d("lv_index======>", lvIndex+"");
//				Map<String, Object> contact = mapList.get(lvIndex);
//				mapList2.remove(position);
//				contact.put("check", false);
			}
			
		});
		
		lv_ps1.setOnItemClickListener(new OnItemClickListener() {
			
			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int position,
					long id) {
				//
			}
		});
		//取锟斤拷
		tv_ps_cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		//确锟斤拷
		tv_ps_sure.setOnClickListener(new OnClickListener() {
			
			Map inviteMap = new LinkedHashMap();
			
			@Override
			public void onClick(View arg0) {
				if (gv_ps.getChildCount() !=0) {
					List  inviteeUnRegisteredUserList = new LinkedList();
					for (int i=0; i<mapList2.size(); i++) {
						inviteMap.put("uid", mapList2.get(i).get("uid"));
						
						JSONObject obj1 = new JSONObject(inviteMap);
						inviteeUnRegisteredUserList.add(obj1);
					}
					
					Log.d("inviteeUnRegisteredUserList=========>", inviteeUnRegisteredUserList.toString());
					
					new CreateGroupTask(inviteeUnRegisteredUserList.toString()).execute();
					//JSONArray inviteUnRegisteredUsers = new JSONArray(inviteeUnRegisteredUserList);
					
//					new InvitationTask(inviteeUnRegisteredUserList.toString()).execute(Constants._Invitation_inviteUsers);
					
				} else {
					Toast.makeText(SelectFriendsActivity.this, "还没有添加邀请人", Toast.LENGTH_SHORT).show();
				}
				
//				startActivity(new Intent(SelectFriendsActivity.this,ChattingActivity.class));
			}
		});
		
		sp = this.getSharedPreferences("icsp", Context.MODE_WORLD_READABLE);
        token = sp.getString("token", "");
        
        new Thread(SelectFriendsActivity.this).start();
	}
	
	private void initWidget() {
		lv_ps1 = (ListView) findViewById(R.id.lv_ps1);
//		adaptersf = new ContactsAdapterSF(this, getMapList());
		lv_ps1.setAdapter(adaptersf);
	}
	/**
	 * 锟斤拷始锟斤拷锟斤拷锟斤拷listVIew锟斤拷锟斤拷要锟斤拷锟斤拷锟斤拷 锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟狡ワ拷锟�
	 */
	@SuppressWarnings("unchecked")
	private List<Map<String, Object>> getMapList(List<Friend> friends) {
		
		mapList = new ArrayList<Map<String, Object>>();

//		Contacts[] ContactsArray = new Contacts[] {
//				new Contacts(R.drawable.my_qq_pic, "锟斤拷僧", null, PinyinUtils.getAlpha("锟斤拷僧")),
//				new Contacts(R.drawable.my_qq_pic, "锟斤拷师锟斤拷", null, PinyinUtils.getAlpha("锟斤拷师锟斤拷")),
//				new Contacts(R.drawable.my_qq_pic, "锟斤拷锟斤拷", null, PinyinUtils.getAlpha("锟斤拷锟斤拷")),
//				new Contacts(R.drawable.my_qq_pic, "8899", null, PinyinUtils.getAlpha("8899")),
//				new Contacts(R.drawable.my_qq_pic, "锟斤拷锟斤拷锟�", null, PinyinUtils.getAlpha("锟斤拷锟斤拷锟�")),
//				new Contacts(R.drawable.my_qq_pic, "小锟斤拷", null, PinyinUtils.getAlpha("小锟斤拷")),
//				new Contacts(R.drawable.my_qq_pic, "锟斤拷锟�", null, PinyinUtils.getAlpha("锟斤拷锟�")),
//				new Contacts(R.drawable.my_qq_pic, "锟斤拷锟斤拷", null, PinyinUtils.getAlpha("锟斤拷锟斤拷")),
//				new Contacts(R.drawable.my_qq_pic, "锟斤拷锟斤拷", null, PinyinUtils.getAlpha("锟斤拷锟斤拷")),
//				new Contacts(R.drawable.my_qq_pic, "锟斤拷锟斤拷", null, PinyinUtils.getAlpha("锟斤拷锟斤拷")),
//				new Contacts(R.drawable.my_qq_pic, "锟斤拷锟斤拷", null, PinyinUtils.getAlpha("锟斤拷锟斤拷")),
//				new Contacts(R.drawable.my_qq_pic, "锟斤拷伟锟斤拷", null, PinyinUtils.getAlpha("锟斤拷伟锟斤拷")),
//				new Contacts(R.drawable.my_qq_pic, "强强", null, PinyinUtils.getAlpha("强强")),
//				new Contacts(R.drawable.my_qq_pic, "锟杰硷拷", null, PinyinUtils.getAlpha("锟杰硷拷")),
//				new Contacts(R.drawable.my_qq_pic, "锟斤拷锟斤拷", null, PinyinUtils.getAlpha("锟斤拷锟斤拷")),
//				new Contacts(R.drawable.my_qq_pic, "锟斤拷锟斤拷", null, PinyinUtils.getAlpha("锟斤拷锟斤拷")),
//				new Contacts(R.drawable.my_qq_pic,"锟斤拷锟斤拷", null, PinyinUtils.getAlpha("锟斤拷锟斤拷")),
//				new Contacts(R.drawable.my_qq_pic,"锟斤拷锟斤拷", null, PinyinUtils.getAlpha("锟斤拷锟斤拷"))
//			};
		
		ArrayList<Contacts> mylist = new ArrayList<Contacts>();
		
		for (int i = 0; i < friends.size(); i++) {
			String username = friends.get(i).getUsername();
			String uid = friends.get(i).getUid();
			String icon = friends.get(i).getPhoto();
			mylist.add(new Contacts(uid, icon, username, null, PinyinUtils.getAlpha(username), false));
		}
		
		Contacts[] ContactsArray = mylist.toArray(new Contacts[mylist.size()]);
		
		//锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟�
		Arrays.sort(ContactsArray, new PinyinComparator());
		
		for (Contacts contacts : ContactsArray) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("uid", contacts.getUid());
			map.put("icon", contacts.getIcon());
			map.put("name", contacts.getName());
			map.put("info", contacts.getInfo());
			map.put("check", contacts.getCheck());
			mapList.add(map);
		}
		return mapList;
	}
	
	class MyGVAdapter extends BaseAdapter{

		private Context context;
		private List<Map<String, Object>> list;
		ImageLoader imageLoader;
		private DisplayImageOptions options;

		public MyGVAdapter(Context context, List<Map<String, Object>> list) {
			this.context = context;
			this.list = list;
			imageLoader = ImageLoader.getInstance();
	        imageLoader.init(ImageLoaderConfiguration
					.createDefault(context));
		}
		
		@Override
		public int getCount() {
			return list == null ? 0 : list.size(); 
		}

		@Override
		public Object getItem(int position) {
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			PictureViewHolder1 viewHolder = null;
			if (convertView == null) {
				viewHolder = new PictureViewHolder1();
				convertView = LayoutInflater.from(SelectFriendsActivity.this).inflate(
						R.layout.gridview_sf, parent, false);
				viewHolder.iv_gridview_sf = (ImageView) convertView
						.findViewById(R.id.iv_gridview_sf);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (PictureViewHolder1) convertView.getTag();
			}
			
			imageLoader.displayImage(AppConstants.MAIN_DOMAIN + "/" + list.get(position).get("icon"), viewHolder.iv_gridview_sf,
					options, null);
			return convertView;
		}
		public class PictureViewHolder1 {
			ImageView iv_gridview_sf;
		}
		
	}
	@Override
	public void run() {
		URL url=null;
		String result = "";
		try {
			url = new URL(AppConstants.FRIEND_LIST);
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

		Message msg = handler.obtainMessage();
		msg.what = 0x11;
		msg.obj = result;
		msg.sendToTarget();
		
	}
	
	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			String result = (String) msg.obj;
			Log.d("TEST", "锟斤拷锟斤拷锟叫憋拷JSON---" + result);
			JSONObject object = null;
			if (!result.equals("")) {
				try {
					object = new JSONObject(result);
//					Log.i("TEST", "锟斤拷录锟斤拷息token---" + object.getInt("token"));
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			switch (msg.what) {
			case 0x11:
				if (object != null) {
					try {
						if (1==object.getInt("status")) {

							FriendJSONParser friendJsonParser = new FriendJSONParser();

			    	        List<Friend> friends = null;

			    	        try{

			    	        	friends = friendJsonParser.parse(object);
			    	        	Log.d("friends=============", friends.toString());
			    	        }catch(Exception e){
			    	        	Log.d("Exception",e.toString());
			    	        }

			    	        adaptersf = new ContactsAdapterSF(SelectFriendsActivity.this, getMapList(friends));
			    	        lv_ps1.setAdapter(adaptersf);
			    	        
//			    	        lv_ps1.setAdapter(adapter);
			    	        lv_ps1.setOnItemClickListener(new OnItemClickListener() {
			    	        	
			    				@Override
			    				public void onItemClick(AdapterView<?> parent, View view, int position,
			    						long id) {
			    					
			    					ImageView iv = (ImageView) view.findViewById(R.id.iv_select_friends);
			    					Map<String, Object> contact = mapList.get(position);
			    					Log.d("contact=======>", contact.toString());
			    					boolean isChecked = Boolean.parseBoolean(contact.get("check").toString());
			    					if (!isChecked) {
			    						iv.setImageResource(R.drawable.select_friends_checked);
			    						contact.put("check", true);
			    						// add this contact to grid
			    						Map<String,Object> item = new HashMap<String, Object>();
			    						Log.d("lvIndex==========>", position+"");
			    						Log.d("uid==========>", contact.get("uid").toString());
			    						item.put("lvIndex", position);
			    						item.put("uid", contact.get("uid").toString());
			    						item.put("icon", contact.get("icon").toString());
			    						mapList2.add(item);
			    						gvAdapter.notifyDataSetChanged();
			    						
			    					} else {
			    						iv.setImageResource(R.drawable.select_friends);
			    						contact.put("check", false);
			    						// remove this contact grid
//			    						mapList2.remove(position);
			    						int pos = 0;
			    						for (int i=0; i<mapList2.size(); i++) {
			    							if (position == Integer.parseInt(mapList2.get(i).get("lvIndex").toString())) {
			    								pos = i;
			    							}
			    						}
			    						mapList2.remove(pos);
			    						gvAdapter.notifyDataSetChanged();
			    					}
			    					
			    				}
			    			});
							
						}else if(0==object.getInt("status")){
							Toast.makeText(SelectFriendsActivity.this,object.getString("text"),Toast.LENGTH_SHORT).show();
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			}
		}
	};
	
	private class CreateGroupTask extends AsyncTask<String, Void, String> {

		private String members;
		
		public CreateGroupTask(String members) {
			this.members = members;
		}
		
		@Override
		protected String doInBackground(String... params) {
			URL url=null;
			String result = "";
			try {
				url = new URL(AppConstants.CREATE_MESSAGE_GROUP);
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();

				conn.setRequestMethod("POST");
				conn.setDoOutput(true);

				Writer writer = new OutputStreamWriter(conn.getOutputStream());

				String str = "token=" + token;
				JSONArray jArray = new JSONArray(members);
				String tmp = "&members[]=" + sp.getString("uid", "");
				for (int i=0; i<jArray.length(); i++) {
					tmp += "&members[]=" + ((JSONObject)jArray.get(i)).getString("uid");
				}
				Log.d("test============>", str+tmp);
				writer.write(str+tmp);
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
			Log.d("TEST", "创建多人聊天(群组)JSON---" + result);
			JSONObject obj;
			try {
				obj = new JSONObject(result);
				if (1==obj.getInt("status")) {
//					Toast.makeText(SelectFriendsActivity.this,obj.getString("text"),Toast.LENGTH_SHORT).show();
////					startActivity(new Intent(LoginMyActivity.this,HomeActivity.class));
//					// 加载附近用户列表
//					new LoadNearbyUsersTask().excute();
					JSONObject jObj = new JSONObject(obj.getString("data"));
					if (jObj != null) {
						int sid = jObj.getInt("sid");
						Intent i = new Intent();
						i.setClass(SelectFriendsActivity.this, ChattingActivity.class);
						i.putExtra("sid", sid+"");
						startActivity(i);
					}
				}else if(0==obj.getInt("status")){
					Toast.makeText(SelectFriendsActivity.this,obj.getString("text"),Toast.LENGTH_SHORT).show();
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		
	}
	
}
