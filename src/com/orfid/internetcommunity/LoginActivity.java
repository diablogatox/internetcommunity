package com.orfid.internetcommunity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class LoginActivity extends Activity implements OnClickListener{
	private Button bt_login_qq;
	private Button bt_login_zhanghao;
	private Button bt_login_register;
	private SharedPreferences sp;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		
		sp = this.getSharedPreferences("icsp", Context.MODE_WORLD_READABLE);
		bt_login_qq = (Button) findViewById(R.id.bt_login_qq);
		bt_login_zhanghao = (Button) findViewById(R.id.bt_login_zhanghao);
		bt_login_register = (Button) findViewById(R.id.bt_login_register);
		bt_login_qq.setOnClickListener(this);
		bt_login_zhanghao.setOnClickListener(this);
		bt_login_register.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View view) {
		
		switch (view.getId()) {
		case R.id.bt_login_qq://QQ��¼
			startActivity(new Intent(LoginActivity.this,LoginMyActivity.class));
			break;
		case R.id.bt_login_zhanghao://�˺ŵ�¼
			startActivity(new Intent(LoginActivity.this,LoginMyActivity.class));
			break;
		case R.id.bt_login_register://ע��
			startActivity(new Intent(LoginActivity.this,RegisterMyActivity.class));
			break;
		}
	}
	
}
