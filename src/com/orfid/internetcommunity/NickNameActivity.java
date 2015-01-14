package com.orfid.internetcommunity;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class NickNameActivity extends Activity implements OnClickListener{
	private ImageView iv_nickname_back;
	private ImageView iv_nickname_reset;
	private TextView tv_nickname_save;
	private EditText et_nickname;
	private SharedPreferences sp_nickname_info;
	private Editor ed_nickname_info;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.nickname);
		
		iv_nickname_back = (ImageView) findViewById(R.id.iv_nickname_back);
		tv_nickname_save = (TextView) findViewById(R.id.tv_nickname_save);
		et_nickname = (EditText) findViewById(R.id.et_nickname);
		iv_nickname_reset = (ImageView) findViewById(R.id.iv_nickname_reset);
		iv_nickname_back.setOnClickListener(this);
		tv_nickname_save.setOnClickListener(this);
		iv_nickname_reset.setOnClickListener(this);
		inintEditor();
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.iv_nickname_back://返回
			finish();
			break;
		case R.id.tv_nickname_save://保存
			writeLoginData();
			finish();
			break;
		case R.id.iv_nickname_reset://重新编辑昵称
			et_nickname.setText("");
			break;
		}
	}
	public void writeLoginData(){
		String nickname = et_nickname.getText().toString();
		ed_nickname_info.clear();
		ed_nickname_info.commit();
		ed_nickname_info.putString("nickname", nickname+"");
		ed_nickname_info.commit();
	}
	private void inintEditor() {
		sp_nickname_info=getSharedPreferences("nick",Context.MODE_PRIVATE);
		ed_nickname_info = sp_nickname_info.edit();
	}
}
