package com.orfid.internetcommunity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chatting_from);

		findId();//寻找ID
		init();
		btn_chatting_voice.setOnClickListener(this);
		btn_voice_keyboard.setOnClickListener(this);
		btn_expression_keyboard.setOnClickListener(this);
		btn_expression_more.setOnClickListener(this);
		btn_chatting_more.setOnClickListener(this);
		iv_chatting_picturek.setOnClickListener(this);
		icon_title_right.setOnClickListener(this);
		et_chatting_input.setOnClickListener(this);
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
	
}
