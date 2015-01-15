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
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.mofang.pb.Contacts;
import com.mofang.pb.ContactsAdapter;
import com.mofang.util.PinyinComparator;
import com.mofang.util.PinyinUtils;

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
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class AddFriendsActivity extends Activity implements OnClickListener, Runnable {
	//好友界面
    private ImageView add_friends_back;
    private ImageButton ib_add_friends1,ib_add_friends2;
    private RelativeLayout rl_add_friends1;
    private RelativeLayout rl_add_friends2;
    private ListView lv;
    private ContactsAdapter adapter;
    private SharedPreferences sp;
	private String token;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_friends);
		
		initWidget();
		add_friends_back = (ImageView) findViewById(R.id.add_friends_back);
		ib_add_friends1 = (ImageButton) findViewById(R.id.ib_add_friends1);
		ib_add_friends2 = (ImageButton) findViewById(R.id.ib_add_friends2);
		rl_add_friends1 = (RelativeLayout) findViewById(R.id.rl_add_friends1);
		rl_add_friends2 = (RelativeLayout) findViewById(R.id.rl_add_friends2);
		add_friends_back.setOnClickListener(this);
		rl_add_friends1.setOnClickListener(this);
		rl_add_friends2.setOnClickListener(this);
		ib_add_friends1.setOnClickListener(this);
		ib_add_friends2.setOnClickListener(this);
		lv = (ListView) findViewById(R.id.lv_add_friends1);
        lv.setOnItemClickListener(new OnItemClickListener() {
			
			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int position,
					long id) {
				//
			}
		});
        
        sp = this.getSharedPreferences("icsp", Context.MODE_WORLD_READABLE);
        token = sp.getString("token", "");
        
        new Thread(AddFriendsActivity.this).start();
	}
	private void initWidget() {
		lv = (ListView) findViewById(R.id.lv_add_friends1);
//		adapter = new ContactsAdapter(this, getMapList());
		lv.setAdapter(adapter);
	}
	/**
	 * 初始化加载listVIew所需要的数据 并进行排序和匹配
	 */
	@SuppressWarnings("unchecked")
//	private List<Map<String, Object>> getMapList() {
//		
//		List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
//
//		Contacts[] ContactsArray = new Contacts[] {
//				new Contacts(R.drawable.my_qq_pic, "唐僧", null, PinyinUtils.getAlpha("唐僧")),
//				new Contacts(R.drawable.my_qq_pic, "猪师弟", null, PinyinUtils.getAlpha("猪师弟")),
//				new Contacts(R.drawable.my_qq_pic, "阿呆", null, PinyinUtils.getAlpha("阿呆")),
//				new Contacts(R.drawable.my_qq_pic, "8899", null, PinyinUtils.getAlpha("8899")),
//				new Contacts(R.drawable.my_qq_pic, "孙悟空", null, PinyinUtils.getAlpha("孙悟空")),
//				new Contacts(R.drawable.my_qq_pic, "小明", null, PinyinUtils.getAlpha("小明")),
//				new Contacts(R.drawable.my_qq_pic, "大哥", null, PinyinUtils.getAlpha("大哥")),
//				new Contacts(R.drawable.my_qq_pic, "董策", null, PinyinUtils.getAlpha("董策")),
//				new Contacts(R.drawable.my_qq_pic, "丽丽", null, PinyinUtils.getAlpha("丽丽")),
//				new Contacts(R.drawable.my_qq_pic, "丽君", null, PinyinUtils.getAlpha("丽君")),
//				new Contacts(R.drawable.my_qq_pic, "米线", null, PinyinUtils.getAlpha("米线")),
//				new Contacts(R.drawable.my_qq_pic, "王伟洋", null, PinyinUtils.getAlpha("王伟洋")),
//				new Contacts(R.drawable.my_qq_pic, "强强", null, PinyinUtils.getAlpha("强强")),
//				new Contacts(R.drawable.my_qq_pic, "周佳", null, PinyinUtils.getAlpha("周佳")),
//				new Contacts(R.drawable.my_qq_pic, "东芳", null, PinyinUtils.getAlpha("东芳")),
//				new Contacts(R.drawable.my_qq_pic, "香兰", null, PinyinUtils.getAlpha("香兰")),
//				new Contacts(R.drawable.my_qq_pic,"阿三", null, PinyinUtils.getAlpha("阿三")),
//				new Contacts(R.drawable.my_qq_pic,"张三", null, PinyinUtils.getAlpha("张三"))
//			};
//		
//		//对数组进行排序
//		Arrays.sort(ContactsArray, new PinyinComparator());
//		
//		for (Contacts contacts : ContactsArray) {
//			Map<String, Object> map = new HashMap<String, Object>();
//			map.put("icon", contacts.getIcon());
//			map.put("name", contacts.getName());
//			map.put("info", contacts.getInfo());
//			mapList.add(map);
//		}
//		
//		return mapList;
//	}
	
	private List<Map<String, Object>> getMapList(List<Friend> friends) {
		
		List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();

//		Contacts[] ContactsArray = new Contacts[] {
//				new Contacts(R.drawable.my_qq_pic, "唐僧", null, PinyinUtils.getAlpha("唐僧")),
//				new Contacts(R.drawable.my_qq_pic, "猪师弟", null, PinyinUtils.getAlpha("猪师弟")),
//				new Contacts(R.drawable.my_qq_pic, "阿呆", null, PinyinUtils.getAlpha("阿呆")),
//				new Contacts(R.drawable.my_qq_pic, "8899", null, PinyinUtils.getAlpha("8899")),
//				new Contacts(R.drawable.my_qq_pic, "孙悟空", null, PinyinUtils.getAlpha("孙悟空")),
//				new Contacts(R.drawable.my_qq_pic, "小明", null, PinyinUtils.getAlpha("小明")),
//				new Contacts(R.drawable.my_qq_pic, "大哥", null, PinyinUtils.getAlpha("大哥")),
//				new Contacts(R.drawable.my_qq_pic, "董策", null, PinyinUtils.getAlpha("董策")),
//				new Contacts(R.drawable.my_qq_pic, "丽丽", null, PinyinUtils.getAlpha("丽丽")),
//				new Contacts(R.drawable.my_qq_pic, "丽君", null, PinyinUtils.getAlpha("丽君")),
//				new Contacts(R.drawable.my_qq_pic, "米线", null, PinyinUtils.getAlpha("米线")),
//				new Contacts(R.drawable.my_qq_pic, "王伟洋", null, PinyinUtils.getAlpha("王伟洋")),
//				new Contacts(R.drawable.my_qq_pic, "强强", null, PinyinUtils.getAlpha("强强")),
//				new Contacts(R.drawable.my_qq_pic, "周佳", null, PinyinUtils.getAlpha("周佳")),
//				new Contacts(R.drawable.my_qq_pic, "东芳", null, PinyinUtils.getAlpha("东芳")),
//				new Contacts(R.drawable.my_qq_pic, "香兰", null, PinyinUtils.getAlpha("香兰")),
//				new Contacts(R.drawable.my_qq_pic,"阿三", null, PinyinUtils.getAlpha("阿三")),
//				new Contacts(R.drawable.my_qq_pic,"张三", null, PinyinUtils.getAlpha("张三"))
//			};
		
		ArrayList<Contacts> mylist = new ArrayList<Contacts>();
		
		for (int i = 0; i < friends.size(); i++) {
			String username = friends.get(i).getUsername();
			mylist.add(new Contacts(R.drawable.my_qq_pic, username, null, PinyinUtils.getAlpha(username), false));
		}
		
		Contacts[] ContactsArray = mylist.toArray(new Contacts[mylist.size()]);
		
		
		
		//对数组进行排序
		Arrays.sort(ContactsArray, new PinyinComparator());
		
		for (Contacts contacts : ContactsArray) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("icon", contacts.getIcon());
			map.put("name", contacts.getName());
			map.put("info", contacts.getInfo());
			mapList.add(map);
		}
		
		return mapList;
	}
	
	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.add_friends_back://返回
			finish();
			break;
		case R.id.rl_add_friends1://添加好友
			startActivity(new Intent(AddFriendsActivity.this,AddNewFriendsActivity.class));
			break;
		case R.id.ib_add_friends1://添加好友
			startActivity(new Intent(AddFriendsActivity.this,AddNewFriendsActivity.class));
			break;
		case R.id.rl_add_friends2://新的好友
			startActivity(new Intent(AddFriendsActivity.this,NewFriendsActivity.class));
			break;
		case R.id.ib_add_friends2://新的好友
			startActivity(new Intent(AddFriendsActivity.this,NewFriendsActivity.class));
			break;
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
			Log.d("TEST", "好友列表JSON---" + result);
			JSONObject object = null;
			if (!result.equals("")) {
				try {
					object = new JSONObject(result);
//					Log.i("TEST", "登录信息token---" + object.getInt("token"));
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

			    	        ContactsAdapter adapter = new ContactsAdapter(AddFriendsActivity.this, getMapList(friends));

			    			lv.setAdapter(adapter);
			    			lv.setOnItemClickListener(new OnItemClickListener() {
			    				@Override
			    				public void onItemClick(AdapterView<?> parent, View view, int position,
			    						long id) {
			    				}
			    			});
							
						}else if(0==object.getInt("status")){
							Toast.makeText(AddFriendsActivity.this,object.getString("text"),Toast.LENGTH_SHORT).show();
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			}
		}
	};

	
}
