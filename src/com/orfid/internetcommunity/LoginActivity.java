package com.orfid.internetcommunity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class LoginActivity extends Activity implements OnClickListener{
	private Button bt_login_qq;
	private Button bt_login_zhanghao;
	private Button bt_login_register;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		
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
		case R.id.bt_login_qq://QQµÇÂ¼
			startActivity(new Intent(LoginActivity.this,LoginMyActivity.class));
			break;
		case R.id.bt_login_zhanghao://ÕËºÅµÇÂ¼
			startActivity(new Intent(LoginActivity.this,LoginMyActivity.class));
			break;
		case R.id.bt_login_register://×¢²á
			startActivity(new Intent(LoginActivity.this,RegisterMyActivity.class));
			break;
		}
	}
	
}
