package com.orfid.internetcommunity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mofang.pb.Contacts;
import com.mofang.pb.ContactsAdapter;
import com.mofang.util.PinyinComparator;
import com.mofang.util.PinyinUtils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.AdapterView.OnItemClickListener;

public class AddFriendsActivity extends Activity implements OnClickListener{
	//���ѽ���
    private ImageView add_friends_back;
    private ImageButton ib_add_friends1,ib_add_friends2;
    private RelativeLayout rl_add_friends1;
    private RelativeLayout rl_add_friends2;
    private ListView lv;
    private ContactsAdapter adapter;
    
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
	}
	private void initWidget() {
		lv = (ListView) findViewById(R.id.lv_add_friends1);
		adapter = new ContactsAdapter(this, getMapList());
		lv.setAdapter(adapter);
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
	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.add_friends_back://����
			finish();
			break;
		case R.id.rl_add_friends1://��Ӻ���
			startActivity(new Intent(AddFriendsActivity.this,AddNewFriendsActivity.class));
			break;
		case R.id.ib_add_friends1://��Ӻ���
			startActivity(new Intent(AddFriendsActivity.this,AddNewFriendsActivity.class));
			break;
		case R.id.rl_add_friends2://�µĺ���
			startActivity(new Intent(AddFriendsActivity.this,NewFriendsActivity.class));
			break;
		case R.id.ib_add_friends2://�µĺ���
			startActivity(new Intent(AddFriendsActivity.this,NewFriendsActivity.class));
			break;
		}
	}
}
