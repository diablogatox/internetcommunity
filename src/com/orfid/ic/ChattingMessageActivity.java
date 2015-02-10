package com.orfid.ic;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

public class ChattingMessageActivity extends Activity implements OnClickListener{
	ImageView chatting_member_back;
	TextView tv_chatting_members;//��������
	Button btn_delete_exit;
	GridView gv_chatting_member;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chatting_member);
		
		chatting_member_back = (ImageView) findViewById(R.id.chatting_member_back);
		tv_chatting_members = (TextView) findViewById(R.id.tv_chatting_members);
		btn_delete_exit = (Button) findViewById(R.id.btn_delete_exit);
		gv_chatting_member = (GridView) findViewById(R.id.gv_chatting_member);
		gv_chatting_member.setAdapter(new MyAdapter());
		
		chatting_member_back.setOnClickListener(this);
		btn_delete_exit.setOnClickListener(this);
	}
	
	class MyAdapter extends BaseAdapter{
		
		@Override
		public int getCount() {
			return 6;
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
				convertView = LayoutInflater.from(ChattingMessageActivity.this).inflate(
						R.layout.gridview_sf, parent, false);
				viewHolder.iv_gridview_sf = (ImageView) convertView
						.findViewById(R.id.iv_gridview_sf);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (PictureViewHolder) convertView.getTag();
			}
				viewHolder.iv_gridview_sf.setBackgroundResource(R.drawable.my_qq_pic);
			return convertView;
		}
		public class PictureViewHolder {
			ImageView iv_gridview_sf;
		}
		
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.chatting_member_back://����
			finish();
			break;
		case R.id.btn_delete_exit://ɾ���˳�
			finish();
			break;

		default:
			break;
		}
	}
}
