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
	ImageView icon_title_right;//Ⱥ��ͼ��
	Button btn_chatting_voice, btn_voice_keyboard, btn_expression_keyboard,
			btn_expression_more, btn_chatting_more;
	EditText et_chatting_input;
	ListView lv_chatting_history;//��������
	RelativeLayout rl_expression;//�����ͱ��顱����Բ���
	RelativeLayout rl_chatting_sent;//�����͡�����Բ���
	RelativeLayout rl_chatting_voice_big;//������������Բ���
	RelativeLayout rl_chatting_picturek;//����ᡱ����Բ���
	ViewPager vp_chatting_expression;//�����л�����ѡ��
	ImageView iv_dot0,iv_dot1;//�����л���������
	Button btn_chatting_sent;//���Ͱ�ť
	ImageView iv_chatting_voice_big;//������ͼ��
	ImageView iv_chatting_picturek;//���ͼ��

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chatting_from);

		findId();//Ѱ��ID
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
//				rl_expression.setVisibility(View.GONE);//�����顱����(����)
//				rl_chatting_sent.setVisibility(View.GONE);//�����͡�����(����)
//				rl_chatting_voice_big.setVisibility(View.GONE);//������������(����)
//				rl_chatting_picturek.setVisibility(View.GONE);//����ᡱ����(��ʾ)
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
		case R.id.btn_chatting_voice://����
			/*�������뷨*/
			InputMethodManager inputMethodManager =(InputMethodManager)ChattingActivity.this.getApplicationContext().
					getSystemService(Context.INPUT_METHOD_SERVICE); 
			inputMethodManager.hideSoftInputFromWindow(et_chatting_input.getWindowToken(), 0);
			btn_chatting_voice.setVisibility(View.GONE);//������ť(����)
			btn_expression_keyboard.setVisibility(View.GONE);//������̰�ť(����)
			btn_voice_keyboard.setVisibility(View.VISIBLE);//�������̰�ť(��ʾ)
			btn_expression_more.setVisibility(View.VISIBLE);//���鰴ť(��ʾ)
			btn_chatting_more.setVisibility(View.VISIBLE);//��Ӱ�ť(��ʾ)
			rl_expression.setVisibility(View.GONE);//�����顱����(����)
			rl_chatting_sent.setVisibility(View.GONE);//�����͡�����(����)
			rl_chatting_voice_big.setVisibility(View.VISIBLE);//������������(��ʾ)
			rl_chatting_picturek.setVisibility(View.GONE);//����ᡱ����(����)
			break;
		case R.id.btn_expression_keyboard://�������֮��ġ����̡���ť
			et_chatting_input.requestFocus();//�������ѡ��
//			((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)) .showInputMethodPicker();//ѡ�����뷨
			/*private void showInputMethodPicker() { 
			 * ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)) 
			 * .showInputMethodPicker(); }
			 * */
			break;
		case R.id.btn_chatting_more://���
			/*�������뷨*/
			InputMethodManager inputMethodManager1 =(InputMethodManager)ChattingActivity.this.getApplicationContext().
					getSystemService(Context.INPUT_METHOD_SERVICE); 
			inputMethodManager1.hideSoftInputFromWindow(et_chatting_input.getWindowToken(), 0);
			btn_chatting_voice.setVisibility(View.GONE);//������ť(����)
			btn_expression_keyboard.setVisibility(View.GONE);//������̰�ť(����)
			btn_voice_keyboard.setVisibility(View.VISIBLE);//�������̰�ť(��ʾ)
			btn_expression_more.setVisibility(View.VISIBLE);//���鰴ť(��ʾ)
			btn_chatting_more.setVisibility(View.VISIBLE);//��Ӱ�ť(��ʾ)
			rl_expression.setVisibility(View.GONE);//�����顱����(����)
			rl_chatting_sent.setVisibility(View.GONE);//�����͡�����(����)
			rl_chatting_voice_big.setVisibility(View.GONE);//������������(����)
			rl_chatting_picturek.setVisibility(View.VISIBLE);//����ᡱ����(��ʾ)
			break;
		case R.id.btn_expression_more://����
			/*�������뷨*/
			InputMethodManager inputMethodManager2 =(InputMethodManager)ChattingActivity.this.getApplicationContext().
					getSystemService(Context.INPUT_METHOD_SERVICE); 
			inputMethodManager2.hideSoftInputFromWindow(et_chatting_input.getWindowToken(), 0);
			btn_chatting_voice.setVisibility(View.VISIBLE);//������ť(��ʾ)
			btn_expression_keyboard.setVisibility(View.VISIBLE);//������̰�ť(��ʾ)
			btn_voice_keyboard.setVisibility(View.GONE);//�������̰�ť(����)
			btn_expression_more.setVisibility(View.GONE);//���鰴ť(����)
			btn_chatting_more.setVisibility(View.VISIBLE);//��Ӱ�ť(��ʾ)
			rl_expression.setVisibility(View.VISIBLE);//�����顱����(��ʾ)
			rl_chatting_sent.setVisibility(View.VISIBLE);//�����͡�����(��ʾ)
			rl_chatting_voice_big.setVisibility(View.GONE);//������������(����)
			rl_chatting_picturek.setVisibility(View.GONE);//����ᡱ����(����)
			break;
		case R.id.btn_voice_keyboard://�������֮��ġ����̡���ť
			et_chatting_input.requestFocus();//�������ѡ��
//			((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)) .showInputMethodPicker();//ѡ�����뷨 
			break;
		case R.id.et_chatting_input://�����
			rl_expression.setVisibility(View.GONE);//�����顱����(����)
			rl_chatting_sent.setVisibility(View.GONE);//�����͡�����(����)
			rl_chatting_voice_big.setVisibility(View.GONE);//������������(����)
			rl_chatting_picturek.setVisibility(View.GONE);//����ᡱ����(����)
			break;
		case R.id.icon_title_right://Ⱥ��ͼ��
			startActivity(new Intent(ChattingActivity.this,ChattingMessageActivity.class));
			break;
		case R.id.iv_chatting_picturek://
			startActivity(new Intent(ChattingActivity.this,SelectPicActivity.class));
			break;
		}
	}
	
}
