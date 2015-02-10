package com.orfid.internetcommunity;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

public class MyApplication extends Application {

	SharedPreferences sp;
	
	@Override
	public void onCreate() {
		super.onCreate();
		sp = this.getSharedPreferences("icsp", Context.MODE_WORLD_READABLE);
		if (sp.getBoolean("isLogin", false)) {
			if (sp.getString("token", "") != null) {
				Intent intent = new Intent(this, HomeActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK); 
				startActivity(intent);
			} else {
				Intent intent = new Intent(this, LoginActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK); 
				startActivity(intent);
			}
		}
	}

}
