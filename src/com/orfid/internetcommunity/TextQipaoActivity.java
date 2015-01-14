package com.orfid.internetcommunity;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class TextQipaoActivity extends Activity {
	private ImageView iv_qipao_back;
	private EditText et_fabu;
	private TextView tv_qipao_allnum;
	private TextView tv_qipao_fabu;
	int num = 30;//限制的最大字数
	SharedPreferences preferences;
	Editor editor;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.text_qipao);

		iv_qipao_back = (ImageView) findViewById(R.id.iv_qipao_back);
		et_fabu = (EditText) findViewById(R.id.et_fabu);
		tv_qipao_allnum = (TextView) findViewById(R.id.tv_qipao_allnum);
		tv_qipao_fabu = (TextView) findViewById(R.id.tv_qipao_fabu);
		inintEditor();
		
		tv_qipao_allnum.setText("0/"+num);
		et_fabu.addTextChangedListener(new TextWatcher() {
			private CharSequence temp;  
			private int selectionStart;  
			private int selectionEnd;
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				temp = s;
			}
			
			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				int number = num - s.length();   
				tv_qipao_allnum.setText(number+"/30");   
				selectionStart = et_fabu.getSelectionStart();   
				selectionEnd = et_fabu.getSelectionEnd();   
				if (temp.length() > num) {    
					s.delete(selectionStart - 1, selectionEnd);    
					int tempSelection = selectionEnd;    
					et_fabu.setText(s);    
					et_fabu.setSelection(tempSelection);//设置光标在最后
				}
			}
		});
		
		//返回
		iv_qipao_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				MaoPaoActivity.instance.finish();
				finish();
			}
		});
		
		//发布
		tv_qipao_fabu.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				String str = et_fabu.getText().toString();
				editor.putString("user_sign", str);
				editor.commit();
				finish();
				MaoPaoActivity.instance.finish();
			}
		});
	}

	private void inintEditor() {
		preferences=getSharedPreferences("user",Context.MODE_PRIVATE);
		editor = preferences.edit();
	}
}
