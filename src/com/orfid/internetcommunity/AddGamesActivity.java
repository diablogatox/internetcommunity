package com.orfid.internetcommunity;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

public class AddGamesActivity extends Activity {
	private ImageView iv_add_games_back;
	private GridView gv_add_games;//已添加游戏
	private GridView gv_add_games1;//未添加游戏
//	private Button btn_add_games_sure;
//	private EditText et_input_games;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_games);
		
		iv_add_games_back = (ImageView) findViewById(R.id.iv_add_games_back);
		gv_add_games = (GridView) findViewById(R.id.gv_add_games);
		gv_add_games1 = (GridView) findViewById(R.id.gv_add_games1);
//		btn_add_games_sure = (Button) findViewById(R.id.btn_add_games_sure);
//		et_input_games = (EditText) findViewById(R.id.et_input_games);
		
		gv_add_games.setAdapter(new MyAdapter());
		gv_add_games1.setAdapter(new MyAdapter1());
		
		gv_add_games.setOnItemLongClickListener(new OnItemLongClickListener() {
			
			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View view,
					int position, long id) {
				//已添加游戏的长按点击事件
				return false;
			}
		});
		gv_add_games1.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				//未添加游戏的点击事件
				
			}
		});
		//返回
		iv_add_games_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
//		btn_add_games_sure.setClickable(false);
//		btn_add_games_sure.setBackgroundResource(R.drawable.button_addgame_pen1);
//		if(et_input_games.getText().toString() != null){
//			btn_add_games_sure.setClickable(true);
//			btn_add_games_sure.setBackgroundResource(R.drawable.button_add_game_pen2);
//			btn_add_games_sure.setOnClickListener(new OnClickListener() {
//				
//				@Override
//				public void onClick(View arg0) {
//					et_input_games.setText("哈哈");
//				}
//			});
//		}else if(et_input_games.getText().toString() == null){
//			btn_add_games_sure.setClickable(false);
//			btn_add_games_sure.setBackgroundResource(R.drawable.button_addgame_pen1);
//		}
	}
	class MyAdapter extends BaseAdapter{
		//已添加
		@Override
		public int getCount() {
			return 4;
		}

		@Override
		public Object getItem(int arg0) {
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			PictureViewHolder viewHolder = null;
			if (convertView == null) {
				viewHolder = new PictureViewHolder();
				convertView = LayoutInflater.from(AddGamesActivity.this).inflate(
						R.layout.gridview_hf, parent, false);
				viewHolder.tv_game_bg = (TextView) convertView.findViewById(R.id.tv_game_bg);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (PictureViewHolder) convertView.getTag();
			}
			viewHolder.tv_game_bg.setText("英雄联盟");
			return convertView;
		}
		public class PictureViewHolder {
			TextView tv_game_bg;
		}
		
	}
	class MyAdapter1 extends BaseAdapter{
		//未添加
		@Override
		public int getCount() {
			return 5;
		}
		
		@Override
		public Object getItem(int arg0) {
			return null;
		}
		
		@Override
		public long getItemId(int arg0) {
			return 0;
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			PictureViewHolder viewHolder = null;
			if (convertView == null) {
				viewHolder = new PictureViewHolder();
				convertView = LayoutInflater.from(AddGamesActivity.this).inflate(
						R.layout.gridview_hf, parent, false);
				viewHolder.tv_game_bg = (TextView) convertView.findViewById(R.id.tv_game_bg);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (PictureViewHolder) convertView.getTag();
			}
			viewHolder.tv_game_bg.setText("英雄联盟");
			return convertView;
		}
		public class PictureViewHolder {
			TextView tv_game_bg;
		}
		
	}
}
