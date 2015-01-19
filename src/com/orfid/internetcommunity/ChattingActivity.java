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

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ChattingActivity extends Activity implements OnClickListener{
	ImageView icon_title_right;//群聊图标
	Button btn_chatting_voice, btn_voice_keyboard, btn_expression_keyboard,
			btn_expression_more, btn_chatting_more;
	EditText et_chatting_input;
	ListView lv_chatting_history;//聊天内容
	RelativeLayout rl_expression;//“发送表情”的相对布局
	RelativeLayout rl_chatting_sent;//“发送”的相对布局
	RelativeLayout rl_chatting_voice_big;//“语音”的相对布局
	RelativeLayout rl_chatting_picturek;//“相册”的相对布局
	ViewPager vp_chatting_expression;//表情切换界面选择
	ImageView iv_dot0,iv_dot1;//表情切换的两个点
	Button btn_chatting_sent;//发送按钮
	ImageView iv_chatting_voice_big;//大语音图标
	ImageView iv_chatting_picturek;//相册图标
	
	private String messageContent;
	private ChatAdapter chatAdapter;
	private List<ChatEntity> chatList;
	private SharedPreferences sp;
	private String token;
	private String uid;
	private String sid;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chatting_from);

		sp = this.getSharedPreferences("icsp", Context.MODE_WORLD_READABLE);
        token = sp.getString("token", "");
        
		Intent intent = getIntent();
		if (intent != null) {
			Bundle bundle = intent.getExtras();
			uid = bundle.getString("toUid");
			sid = bundle.getString("sid");
		}
		chatList = new ArrayList<ChatEntity>(); 
        chatAdapter = new ChatAdapter(this, chatList);
        
		findId();//寻找ID
		init();
		
		lv_chatting_history.setAdapter(chatAdapter);
		
		btn_chatting_voice.setOnClickListener(this);
		btn_voice_keyboard.setOnClickListener(this);
		btn_expression_keyboard.setOnClickListener(this);
		btn_expression_more.setOnClickListener(this);
//		btn_chatting_more.setOnClickListener(this);
		btn_chatting_more.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!et_chatting_input.getText().toString().trim().equals("")) {
					//发送消息
					messageContent = et_chatting_input.getText().toString().trim();
					Log.d("message=======>", messageContent);
					send();
					new SendMessageTask().execute();

				} else {
					Toast.makeText(ChattingActivity.this, "请先输入内容", Toast.LENGTH_SHORT).show();
				}
			}
			
		});
		iv_chatting_picturek.setOnClickListener(this);
		icon_title_right.setOnClickListener(this);
		et_chatting_input.setOnClickListener(this);
		
		new LoadMessageListTask().execute();
	}

	
	// 发布消息
 	public void send() {
     	ChatEntity chatEntity = new ChatEntity();
//     	chatEntity.setChatTime(Utils.getLocalTime());
     	chatEntity.setContent(messageContent);
//     	chatEntity.setUserImage(sp.getString("portrait", ""));
     	chatEntity.setComeMsg(false);
     	chatList.add(chatEntity);
     	chatAdapter.notifyDataSetChanged();
     	lv_chatting_history.setSelection(chatList.size() - 1);
     	et_chatting_input.setText("");
     }
	 	
	private void init() {
		
//		et_chatting_input.setOnTouchListener(new OnTouchListener() {
//			
//			@Override
//			public boolean onTouch(View v, MotionEvent event) {
//				int inType = et_chatting_input.getInputType(); 
//				et_chatting_input.setInputType(InputType.TYPE_NULL);    
//				et_chatting_input.onTouchEvent(event);      
//				et_chatting_input.setInputType(inType);
//				rl_expression.setVisibility(View.GONE);//“表情”布局(隐藏)
//				rl_chatting_sent.setVisibility(View.GONE);//“发送”布局(隐藏)
//				rl_chatting_voice_big.setVisibility(View.GONE);//“语音”布局(隐藏)
//				rl_chatting_picturek.setVisibility(View.GONE);//“相册”布局(显示)
//				return true;
//			}
//		});
	}

	private void findId() {
		icon_title_right = (ImageView) findViewById(R.id.icon_title_right);
		et_chatting_input = (EditText) findViewById(R.id.et_chatting_input);
		iv_dot0 = (ImageView) findViewById(R.id.iv_dot0);
		iv_dot1 = (ImageView) findViewById(R.id.iv_dot1);
		iv_chatting_voice_big = (ImageView) findViewById(R.id.iv_chatting_voice_big);
		iv_chatting_picturek = (ImageView) findViewById(R.id.iv_chatting_picturek);
		btn_chatting_voice = (Button) findViewById(R.id.btn_chatting_voice);
		btn_voice_keyboard = (Button) findViewById(R.id.btn_voice_keyboard);
		btn_chatting_sent = (Button) findViewById(R.id.btn_chatting_sent);
		btn_expression_keyboard = (Button) findViewById(R.id.btn_expression_keyboard);
		btn_expression_more = (Button) findViewById(R.id.btn_expression_more);
		btn_chatting_more = (Button) findViewById(R.id.btn_chatting_more);
		lv_chatting_history = (ListView) findViewById(R.id.lv_chatting_history);
		rl_expression = (RelativeLayout) findViewById(R.id.rl_expression);
		rl_chatting_sent = (RelativeLayout) findViewById(R.id.rl_chatting_sent);
		rl_chatting_voice_big = (RelativeLayout) findViewById(R.id.rl_chatting_voice_big);
		rl_chatting_picturek = (RelativeLayout) findViewById(R.id.rl_chatting_picturek);
		vp_chatting_expression = (ViewPager) findViewById(R.id.vp_chatting_expression);
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.btn_chatting_voice://语音
			/*隐藏输入法*/
			InputMethodManager inputMethodManager =(InputMethodManager)ChattingActivity.this.getApplicationContext().
					getSystemService(Context.INPUT_METHOD_SERVICE); 
			inputMethodManager.hideSoftInputFromWindow(et_chatting_input.getWindowToken(), 0);
			btn_chatting_voice.setVisibility(View.GONE);//语音按钮(隐藏)
			btn_expression_keyboard.setVisibility(View.GONE);//表情键盘按钮(隐藏)
			btn_voice_keyboard.setVisibility(View.VISIBLE);//语音键盘按钮(显示)
			btn_expression_more.setVisibility(View.VISIBLE);//表情按钮(显示)
			btn_chatting_more.setVisibility(View.VISIBLE);//添加按钮(显示)
			rl_expression.setVisibility(View.GONE);//“表情”布局(隐藏)
			rl_chatting_sent.setVisibility(View.GONE);//“发送”布局(隐藏)
			rl_chatting_voice_big.setVisibility(View.VISIBLE);//“语音”布局(显示)
			rl_chatting_picturek.setVisibility(View.GONE);//“相册”布局(隐藏)
			break;
		case R.id.btn_expression_keyboard://点击表情之后的“键盘”按钮
			et_chatting_input.requestFocus();//让输入框被选中
//			((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)) .showInputMethodPicker();//选择输入法
			/*private void showInputMethodPicker() { 
			 * ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)) 
			 * .showInputMethodPicker(); }
			 * */
			break;
		case R.id.btn_chatting_more://添加
			/*隐藏输入法*/
			InputMethodManager inputMethodManager1 =(InputMethodManager)ChattingActivity.this.getApplicationContext().
					getSystemService(Context.INPUT_METHOD_SERVICE); 
			inputMethodManager1.hideSoftInputFromWindow(et_chatting_input.getWindowToken(), 0);
			btn_chatting_voice.setVisibility(View.GONE);//语音按钮(隐藏)
			btn_expression_keyboard.setVisibility(View.GONE);//表情键盘按钮(隐藏)
			btn_voice_keyboard.setVisibility(View.VISIBLE);//语音键盘按钮(显示)
			btn_expression_more.setVisibility(View.VISIBLE);//表情按钮(显示)
			btn_chatting_more.setVisibility(View.VISIBLE);//添加按钮(显示)
			rl_expression.setVisibility(View.GONE);//“表情”布局(隐藏)
			rl_chatting_sent.setVisibility(View.GONE);//“发送”布局(隐藏)
			rl_chatting_voice_big.setVisibility(View.GONE);//“语音”布局(隐藏)
			rl_chatting_picturek.setVisibility(View.VISIBLE);//“相册”布局(显示)
			break;
		case R.id.btn_expression_more://表情
			/*隐藏输入法*/
			InputMethodManager inputMethodManager2 =(InputMethodManager)ChattingActivity.this.getApplicationContext().
					getSystemService(Context.INPUT_METHOD_SERVICE); 
			inputMethodManager2.hideSoftInputFromWindow(et_chatting_input.getWindowToken(), 0);
			btn_chatting_voice.setVisibility(View.VISIBLE);//语音按钮(显示)
			btn_expression_keyboard.setVisibility(View.VISIBLE);//表情键盘按钮(显示)
			btn_voice_keyboard.setVisibility(View.GONE);//语音键盘按钮(隐藏)
			btn_expression_more.setVisibility(View.GONE);//表情按钮(隐藏)
			btn_chatting_more.setVisibility(View.VISIBLE);//添加按钮(显示)
			rl_expression.setVisibility(View.VISIBLE);//“表情”布局(显示)
			rl_chatting_sent.setVisibility(View.VISIBLE);//“发送”布局(显示)
			rl_chatting_voice_big.setVisibility(View.GONE);//“语音”布局(隐藏)
			rl_chatting_picturek.setVisibility(View.GONE);//“相册”布局(隐藏)
			break;
		case R.id.btn_voice_keyboard://点击语音之后的“键盘”按钮
			et_chatting_input.requestFocus();//让输入框被选中
//			((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)) .showInputMethodPicker();//选择输入法 
			break;
		case R.id.et_chatting_input://输入框
			rl_expression.setVisibility(View.GONE);//“表情”布局(隐藏)
			rl_chatting_sent.setVisibility(View.GONE);//“发送”布局(隐藏)
			rl_chatting_voice_big.setVisibility(View.GONE);//“语音”布局(隐藏)
			rl_chatting_picturek.setVisibility(View.GONE);//“相册”布局(隐藏)
			break;
		case R.id.icon_title_right://群聊图标
			startActivity(new Intent(ChattingActivity.this,ChattingMessageActivity.class));
			break;
		case R.id.iv_chatting_picturek://
			startActivity(new Intent(ChattingActivity.this,SelectPicActivity.class));
			break;
		}
	}
	
	private  class ChatAdapter extends BaseAdapter{
     	private Context context = null;
     	private List<ChatEntity> chatList = null;
     	private LayoutInflater inflater = null;
     	private int COME_MSG = 0;
     	private int TO_MSG = 1;
//     	private int NOTIFY_MSG = 2;
     	
// 		private DisplayImageOptions options;
// 		private ImageLoader imageLoader;
     	
     	public ChatAdapter(Context context,List<ChatEntity> chatList){
     		this.context = context;
     		this.chatList = chatList;
     		inflater = LayoutInflater.from(this.context);
//     		options = new DisplayImageOptions.Builder()
// 				.showStubImage(R.drawable.no_portrait_male)
// 				.showImageForEmptyUri(R.drawable.no_portrait_male).cacheInMemory()
// 				.cacheOnDisc().build();
//     		imageLoader = ImageLoader.getInstance();
     	}

 		@Override
 		public int getCount() {
 			return chatList.size();
 		}

 		@Override
 		public Object getItem(int position) {
 			return chatList.get(position);
 		}

 		@Override
 		public long getItemId(int position) {
 			return position;
 		}
 		
 		@Override
 		public int getItemViewType(int position) {
 			// 区别两种view的类型，标注两个不同的变量来分别表示各自的类型
 		 	ChatEntity entity = chatList.get(position);
 		 	if (entity.isComeMsg())
 		 	{
 		 		return COME_MSG;
// 		 	}else if (entity.isNofityMsg()){
// 		 		return NOTIFY_MSG;
 		 	} else {
 		 		return TO_MSG;
 		 	}
 		}

 		@Override
 		public int getViewTypeCount() {
 			// 这个方法默认返回1，如果希望listview的item都是一样的就返回1，我们这里有两种风格，返回2
 			return 2;
 		}

 		@Override
 		public View getView(int position, View convertView, ViewGroup parent) {
 			ChatHolder chatHolder = null;
 			if (convertView == null) {
 				chatHolder = new ChatHolder();
 				if (chatList.get(position).isComeMsg()) {
 					convertView = inflater.inflate(R.layout.chat_ta_item, null);
// 				}else if (chatList.get(position).isNofityMsg()) {
// 					convertView = inflater.inflate(R.layout.chat_notify_item, null);
 				} else {
 					convertView = inflater.inflate(R.layout.chat_me_item, null);
 				}
 				
// 				if (!chatList.get(position).isNofityMsg()) {
//	 				chatHolder.timeTextView = (TextView) convertView.findViewById(R.id.tv_time);
	 				chatHolder.contentTextView = (TextView) convertView.findViewById(R.id.tv_content);
	 				chatHolder.userImageView = (ImageView) convertView.findViewById(R.id.iv_user_image);
// 				} else {
// 					chatHolder.notifyTextView = (TextView) convertView.findViewById(R.id.tv_notify);
// 				}
 				convertView.setTag(chatHolder);
 			}else {
 				chatHolder = (ChatHolder)convertView.getTag();
 			}
 			
// 			if (!chatList.get(position).isNofityMsg()) {
//	 			chatHolder.timeTextView.setText(chatList.get(position).getChatTime());
	 			chatHolder.contentTextView.setText(chatList.get(position).getContent());
	 					
//	 			imageLoader.init(ImageLoaderConfiguration
//	 					.createDefault(context));
//	 			imageLoader.displayImage(chatList.get(position).getUserImage(), chatHolder.userImageView,
// 					options);
// 			} else {
// 				chatHolder.notifyTextView.setText(chatList.get(position).getContent());
// 			}
 			
 			return convertView;
 		}
 		
 		
 		
 		private class ChatHolder{
// 			private TextView timeTextView;
 			private ImageView userImageView;
 			private TextView contentTextView;
// 			private TextView notifyTextView;
 		}
     	
     }
	
	private class SendMessageTask extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... params) {
			URL url=null;
			String result = "";
			try {
				url = new URL(AppConstants.SEND_MESSAGE);
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();

				conn.setRequestMethod("POST");
				conn.setDoOutput(true);

				Writer writer = new OutputStreamWriter(conn.getOutputStream());

				String str = "token=" + token + "&toUid=" + uid + "&sid=" + sid + "&text=" + messageContent
						+ "&file=" + null;
				Log.d("str-=========>", str);
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
			Log.d("TEST", "发送消息JSON---" + result);
			JSONObject obj;
			try {
				obj = new JSONObject(result);
				if (1==obj.getInt("status")) {
//					Toast.makeText(ChattingActivity.this,obj.getString("text"),Toast.LENGTH_SHORT).show();
				}else if(0==obj.getInt("status")){
//					Toast.makeText(ChattingActivity.this,obj.getString("text"),Toast.LENGTH_SHORT).show();
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	private class LoadMessageListTask extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... params) {
			URL url=null;
			String result = "";
			try {
				url = new URL(AppConstants.LOAD_MESSAGE_LIST);
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();

				conn.setRequestMethod("POST");
				conn.setDoOutput(true);

				Writer writer = new OutputStreamWriter(conn.getOutputStream());

				String str = "token=" + token + "&toUid=" + uid + "&sid=" + sid;
				Log.d("str-=========>", str);
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
			Log.d("TEST", "消息列表JSON---" + result);
			JSONObject obj;
			try {
				obj = new JSONObject(result);
				if (1==obj.getInt("status")) {
//					Toast.makeText(ChattingActivity.this,obj.getString("text"),Toast.LENGTH_SHORT).show();
				}else if(0==obj.getInt("status")){
//					Toast.makeText(ChattingActivity.this,obj.getString("text"),Toast.LENGTH_SHORT).show();
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		
	}
	
}
