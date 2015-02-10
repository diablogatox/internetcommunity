package com.orfid.ic;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class NickNameActivity extends Activity implements OnClickListener{
	private ImageView iv_nickname_back;
	private ImageView iv_nickname_reset;
	private TextView tv_nickname_save;
	private EditText et_nickname;
//	private SharedPreferences sp_nickname_info;
//	private Editor ed_nickname_info;
	
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
//		inintEditor();
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.iv_nickname_back://返回
			finish();
			break;
		case R.id.tv_nickname_save://保存
//			writeLoginData();
			if (et_nickname.getText().toString().trim().length() > 0) {
				try {
					new SaveUserInfoTask(NickNameActivity.this, et_nickname.getText().toString().trim(), null, null, null).execute();
					Intent intent = new Intent();
					intent.putExtra("modifiedNikename", et_nickname.getText().toString().trim());
					setResult(RESULT_OK, intent);
					finish();
				} catch (Exception e) {
					e.printStackTrace();
					Toast.makeText(this, "请检查网络后重试", Toast.LENGTH_LONG).show();
				}
			} else {
				Toast.makeText(this, "请先填写昵称", Toast.LENGTH_LONG).show();
			}
			//finish();
			break;
		case R.id.iv_nickname_reset://重新编辑昵称
			et_nickname.setText("");
			break;
		}
	}
//	public void writeLoginData(){
//		String nickname = et_nickname.getText().toString();
//		ed_nickname_info.clear();
//		ed_nickname_info.commit();
//		ed_nickname_info.putString("nickname", nickname+"");
//		ed_nickname_info.commit();
//	}
//	private void inintEditor() {
//		sp_nickname_info=getSharedPreferences("nick",Context.MODE_PRIVATE);
//		ed_nickname_info = sp_nickname_info.edit();
//	}
}
