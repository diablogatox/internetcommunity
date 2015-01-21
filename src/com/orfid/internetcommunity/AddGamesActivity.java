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

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class AddGamesActivity extends Activity implements Runnable {
	private ImageView iv_add_games_back;
	private GridView gv_add_games;//已添加游戏
	private GridView gv_add_games1;//未添加游戏
	private Button btn_add_games_sure;
	private EditText et_input_games;
	
	private SharedPreferences sp;
	private String token;
	
	MyAdapter adapter;
	MyAdapter1 adapter1;
	
	List<GameItem> gameItems = new ArrayList<GameItem>();
	List<GameItem> gameItems1 = null;
	
	String gameList = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_games);
		
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		gameList = bundle.getString("gameList");
		
		iv_add_games_back = (ImageView) findViewById(R.id.iv_add_games_back);
		gv_add_games = (GridView) findViewById(R.id.gv_add_games);
		gv_add_games1 = (GridView) findViewById(R.id.gv_add_games1);
		btn_add_games_sure = (Button) findViewById(R.id.btn_add_games_sure);
		et_input_games = (EditText) findViewById(R.id.et_input_games);
		
		btn_add_games_sure.setClickable(true);
		btn_add_games_sure.setBackgroundResource(R.drawable.button_add_game_pen2);
		btn_add_games_sure.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
//				Log.d("text=======>", et_input_games.getText().toString().trim());
				if (et_input_games.getText().toString().trim().length() > 0) {
//					gameItems.add(new GameItem(et_input_games.getText().toString().trim()));
//					adapter.notifyDataSetChanged();
					new AddNewGameTask(0, et_input_games.getText().toString().trim()).execute();
				} else {
					Toast.makeText(AddGamesActivity.this, "请先输入游戏名称", Toast.LENGTH_LONG).show();
				}
			}
			
		});
		
		GameJSONParser parser = new GameJSONParser();
		JSONObject object = null;
		try {
			object = new JSONObject(gameList);
			gameItems = parser.parse(object);
//			adapter.notifyDataSetChanged();
			adapter = new MyAdapter(this, R.layout.gridview_hf, gameItems);
			gv_add_games.setAdapter(adapter);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
//		Log.d("gameList==========>", gameList);

		
		
//		gv_add_games1.setAdapter(adapter1);
		
		gv_add_games.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					final int position, final long id) {
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
						AddGamesActivity.this);
		 
				// set title
				alertDialogBuilder.setTitle("提示");
	 
				// set dialog message
				alertDialogBuilder
					.setMessage("确定删除吗?")
					.setCancelable(true)
					.setPositiveButton(getResources().getString(android.R.string.ok), new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,int arg) {
							// delete it
							gameItems.remove(position);
							adapter.notifyDataSetChanged();
							
							// real delete here
							Log.d("delete_game_item__【id======>】", id+"");
							new RemoveGameTask(id).execute();
						}
					  })
					.setNegativeButton(getResources().getString(android.R.string.cancel), new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,int id) {
							dialog.cancel();
						}
					});
	 
					// create alert dialog
					AlertDialog alertDialog = alertDialogBuilder.create();
	 
					// show it
					alertDialog.show();
				}
			
		});
		
		gv_add_games1.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Log.d("id=======>", id+"");
				Log.d("name======>", gameItems1.get(position).getName());
				
				gameItems.add(gameItems1.get(position));
				adapter.notifyDataSetChanged();
				
				new AddNewGameTask(id, null).execute();
			}
			
		});
		
		//返回
		iv_add_games_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		
		sp = this.getSharedPreferences("icsp", Context.MODE_WORLD_READABLE);
        token = sp.getString("token", "");
        
        new Thread(AddGamesActivity.this).start();
        
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
	
	class MyAdapter extends ArrayAdapter<GameItem>{
		
		private List<GameItem> items;
		private GameItem objBean;
		
		public MyAdapter(Context context, int resource, List<GameItem> arrayList) {
			super(context, resource, arrayList);
			this.items = arrayList;
		}

		
		@Override
		public int getCount() {
			return items == null ? 0: items.size();
		}


		@Override
		public GameItem getItem(int position) {
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
				convertView = LayoutInflater.from(AddGamesActivity.this).inflate(
						R.layout.gridview_hf, parent, false);
				viewHolder.tv_game_bg = (TextView) convertView.findViewById(R.id.tv_game_bg);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (PictureViewHolder) convertView.getTag();
			}
			
			objBean = items.get(position);
			viewHolder.tv_game_bg.setText(objBean.getName());
			return convertView;
		}
		public class PictureViewHolder {
			TextView tv_game_bg;
		}
		
	}
	
	class MyAdapter1 extends ArrayAdapter<GameItem>{
		
		private List<GameItem> items;
		private GameItem objBean;
		
		public MyAdapter1(Context context, int resource, List<GameItem> arrayList) {
			super(context, resource, arrayList);
			this.items = arrayList;
		}
		
		@Override
		public GameItem getItem(int position) {
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
				convertView = LayoutInflater.from(AddGamesActivity.this).inflate(
						R.layout.gridview_hf, parent, false);
				viewHolder.tv_game_bg = (TextView) convertView.findViewById(R.id.tv_game_bg);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (PictureViewHolder) convertView.getTag();
			}
			
			objBean = items.get(position);
			
			viewHolder.tv_game_bg.setText(objBean.getName());
			return convertView;
		}
		public class PictureViewHolder {
			TextView tv_game_bg;
		}
		
	}
	@Override
	public void run() {
		URL url=null;
		String result = "";
		try {
			url = new URL(AppConstants.GAME_LIST);
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
			Log.d("TEST", "游戏列表JSON---" + result);
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

							GameJSONParser parser = new GameJSONParser();
							gameItems1 = parser.parse(object);
							
							adapter1 = new MyAdapter1(AddGamesActivity.this, R.layout.gridview_hf, gameItems1);
							gv_add_games1.setAdapter(adapter1);
//							adapter1.notifyDataSetChanged();

						}else if(0==object.getInt("status")){
							Toast.makeText(AddGamesActivity.this,object.getString("text"),Toast.LENGTH_SHORT).show();
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			}
		}
	};
	
	private class AddNewGameTask extends AsyncTask<String, Void, String> {

		private long id;
		private String name;
		
		public AddNewGameTask(long id, String name) {
			this.id = id;
			this.name = name;
		}
		@Override
		protected String doInBackground(String... params) {
			URL url=null;
			String result = "";
			try {
				url = new URL(AppConstants.ADD_NEW_GAME);
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();

				conn.setRequestMethod("POST");
				conn.setDoOutput(true);

				Writer writer = new OutputStreamWriter(conn.getOutputStream());

				String str;
				if (name !=null) {
					str = "token=" + token + "&name=" + name;
				} else {
					str = "token=" + token + "&id=" + id;
				}
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
			Log.d("TEST", "用户添加游戏JSON---" + result);
			JSONObject obj;
			try {
				obj = new JSONObject(result);
				if (1==obj.getInt("status")) {
					Toast.makeText(AddGamesActivity.this,obj.getString("text"),Toast.LENGTH_SHORT).show();
					if (name != null) {
						JSONObject jObj = new JSONObject(obj.getString("data"));
						gameItems.add(new GameItem(jObj.getString("id"), jObj.getString("name")));
						adapter.notifyDataSetChanged();
						
						new AddNewGameTask(Long.parseLong(jObj.getString("id")), null).execute();
					}
				}else if(0==obj.getInt("status")){
					Toast.makeText(AddGamesActivity.this,obj.getString("text"),Toast.LENGTH_SHORT).show();
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	private class RemoveGameTask extends AsyncTask<String, Void, String> {

		private long id;
		
		public RemoveGameTask(long id) {
			this.id = id;
		}
		
		@Override
		protected String doInBackground(String... params) {
			URL url=null;
			String result = "";
			try {
				url = new URL(AppConstants.REMOVE_GAME);
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();

				conn.setRequestMethod("POST");
				conn.setDoOutput(true);

				Writer writer = new OutputStreamWriter(conn.getOutputStream());

				String str = "token=" + token + "&id=" + id;
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
			Log.d("TEST", "用户删除游戏JSON---" + result);
			JSONObject obj;
			try {
				obj = new JSONObject(result);
				if (1==obj.getInt("status")) {

				}else if(0==obj.getInt("status")){
					Toast.makeText(AddGamesActivity.this,obj.getString("text"),Toast.LENGTH_SHORT).show();
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		
	}
}
