package com.orfid.internetcommunity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mofang.pb.Contacts;
import com.mofang.pb.ContactsAdapterSF;
import com.mofang.util.PinyinComparator;
import com.mofang.util.PinyinUtils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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
import android.widget.AdapterView.OnItemClickListener;

public class SelectFriendsActivity extends Activity {
	private TextView tv_ps_cancel,tv_ps_sure;
	private GridView gv_ps;
	private ListView lv_ps1;
	private ContactsAdapterSF adaptersf;
	
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
	}
	private void initWidget() {
		lv_ps1 = (ListView) findViewById(R.id.lv_ps1);
		adaptersf = new ContactsAdapterSF(this, getMapList());
		lv_ps1.setAdapter(adaptersf);
	}
	/**
	 * ��ʼ������listVIew����Ҫ������ �����������ƥ��
	 */
	@SuppressWarnings("unchecked")
	private List<Map<String, Object>> getMapList() {
		
		List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();

		Contacts[] ContactsArray = new Contacts[] {
				new Contacts(R.drawable.my_qq_pic, "��ɮ", null, PinyinUtils.getAlpha("��ɮ")),
				new Contacts(R.drawable.my_qq_pic, "��ʦ��", null, PinyinUtils.getAlpha("��ʦ��")),
				new Contacts(R.drawable.my_qq_pic, "����", null, PinyinUtils.getAlpha("����")),
				new Contacts(R.drawable.my_qq_pic, "8899", null, PinyinUtils.getAlpha("8899")),
				new Contacts(R.drawable.my_qq_pic, "�����", null, PinyinUtils.getAlpha("�����")),
				new Contacts(R.drawable.my_qq_pic, "С��", null, PinyinUtils.getAlpha("С��")),
				new Contacts(R.drawable.my_qq_pic, "���", null, PinyinUtils.getAlpha("���")),
				new Contacts(R.drawable.my_qq_pic, "����", null, PinyinUtils.getAlpha("����")),
				new Contacts(R.drawable.my_qq_pic, "����", null, PinyinUtils.getAlpha("����")),
				new Contacts(R.drawable.my_qq_pic, "����", null, PinyinUtils.getAlpha("����")),
				new Contacts(R.drawable.my_qq_pic, "����", null, PinyinUtils.getAlpha("����")),
				new Contacts(R.drawable.my_qq_pic, "��ΰ��", null, PinyinUtils.getAlpha("��ΰ��")),
				new Contacts(R.drawable.my_qq_pic, "ǿǿ", null, PinyinUtils.getAlpha("ǿǿ")),
				new Contacts(R.drawable.my_qq_pic, "�ܼ�", null, PinyinUtils.getAlpha("�ܼ�")),
				new Contacts(R.drawable.my_qq_pic, "����", null, PinyinUtils.getAlpha("����")),
				new Contacts(R.drawable.my_qq_pic, "����", null, PinyinUtils.getAlpha("����")),
				new Contacts(R.drawable.my_qq_pic,"����", null, PinyinUtils.getAlpha("����")),
				new Contacts(R.drawable.my_qq_pic,"����", null, PinyinUtils.getAlpha("����"))
			};
		
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
	
}
