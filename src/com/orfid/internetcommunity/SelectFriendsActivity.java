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
import com.mofang.pb.ContactsAdapterSF;
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
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.public_speak);
		
		initWidget();
		tv_ps_cancel = (TextView) findViewById(R.id.tv_ps_cancel);
		tv_ps_sure = (TextView) findViewById(R.id.tv_ps_sure);
		gv_ps = (GridView) findViewById(R.id.gv_ps);
		lv_ps1 = (ListView) findViewById(R.id.lv_ps1);
		
		gv_ps.setAdapter(new MyGVAdapter());
		lv_ps1.setOnItemClickListener(new OnItemClickListener() {
			
			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int position,
					long id) {
				//
			}
		});
		//ȡ��
		tv_ps_cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		//ȷ��
		tv_ps_sure.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				startActivity(new Intent(SelectFriendsActivity.this,ChattingActivity.class));
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
	 * ��ʼ������listVIew����Ҫ������ �����������ƥ��
	 */
	@SuppressWarnings("unchecked")
	private List<Map<String, Object>> getMapList(List<Friend> friends) {
		
		List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();

//		Contacts[] ContactsArray = new Contacts[] {
//				new Contacts(R.drawable.my_qq_pic, "��ɮ", null, PinyinUtils.getAlpha("��ɮ")),
//				new Contacts(R.drawable.my_qq_pic, "��ʦ��", null, PinyinUtils.getAlpha("��ʦ��")),
//				new Contacts(R.drawable.my_qq_pic, "����", null, PinyinUtils.getAlpha("����")),
//				new Contacts(R.drawable.my_qq_pic, "8899", null, PinyinUtils.getAlpha("8899")),
//				new Contacts(R.drawable.my_qq_pic, "�����", null, PinyinUtils.getAlpha("�����")),
//				new Contacts(R.drawable.my_qq_pic, "С��", null, PinyinUtils.getAlpha("С��")),
//				new Contacts(R.drawable.my_qq_pic, "���", null, PinyinUtils.getAlpha("���")),
//				new Contacts(R.drawable.my_qq_pic, "����", null, PinyinUtils.getAlpha("����")),
//				new Contacts(R.drawable.my_qq_pic, "����", null, PinyinUtils.getAlpha("����")),
//				new Contacts(R.drawable.my_qq_pic, "����", null, PinyinUtils.getAlpha("����")),
//				new Contacts(R.drawable.my_qq_pic, "����", null, PinyinUtils.getAlpha("����")),
//				new Contacts(R.drawable.my_qq_pic, "��ΰ��", null, PinyinUtils.getAlpha("��ΰ��")),
//				new Contacts(R.drawable.my_qq_pic, "ǿǿ", null, PinyinUtils.getAlpha("ǿǿ")),
//				new Contacts(R.drawable.my_qq_pic, "�ܼ�", null, PinyinUtils.getAlpha("�ܼ�")),
//				new Contacts(R.drawable.my_qq_pic, "����", null, PinyinUtils.getAlpha("����")),
//				new Contacts(R.drawable.my_qq_pic, "����", null, PinyinUtils.getAlpha("����")),
//				new Contacts(R.drawable.my_qq_pic,"����", null, PinyinUtils.getAlpha("����")),
//				new Contacts(R.drawable.my_qq_pic,"����", null, PinyinUtils.getAlpha("����"))
//			};
		
		ArrayList<Contacts> mylist = new ArrayList<Contacts>();
		
		for (int i = 0; i < friends.size(); i++) {
			String username = friends.get(i).getUsername();
			mylist.add(new Contacts(R.drawable.my_qq_pic, username, null, PinyinUtils.getAlpha(username)));
		}
		
		Contacts[] ContactsArray = mylist.toArray(new Contacts[mylist.size()]);
		
		//�������������
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
	
	class MyGVAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return 6;
		}

		@Override
		public Object getItem(int arg0) {
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(int arg0, View convertView, ViewGroup parent) {

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
			Log.d("TEST", "�����б�JSON---" + result);
			JSONObject object = null;
			if (!result.equals("")) {
				try {
					object = new JSONObject(result);
//					Log.i("TEST", "��¼��Ϣtoken---" + object.getInt("token"));
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

			    	        ContactsAdapterSF adapter = new ContactsAdapterSF(SelectFriendsActivity.this, getMapList(friends));

			    	        lv_ps1.setAdapter(adapter);
			    	        lv_ps1.setOnItemClickListener(new OnItemClickListener() {
			    				@Override
			    				public void onItemClick(AdapterView<?> parent, View view, int position,
			    						long id) {
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
	
}
