package com.orfid.internetcommunity;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SelectSpecificActivity extends Activity implements OnClickListener{
	private ImageView select_specific_back;
	private ImageView iv_ss_pic;
	private TextView tv_ss_name;
	private LinearLayout ll_select_specific;
	private String uid;
	ImageLoader imageLoader;
	private DisplayImageOptions options;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.select_specific);
		
		imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration
				.createDefault(SelectSpecificActivity.this));
        
		select_specific_back = (ImageView) findViewById(R.id.select_specific_back);
		iv_ss_pic = (ImageView) findViewById(R.id.iv_ss_pic);
		tv_ss_name = (TextView) findViewById(R.id.tv_ss_name);
		ll_select_specific = (LinearLayout) findViewById(R.id.ll_select_specific);
		
		select_specific_back.setOnClickListener(this);
		iv_ss_pic.setOnClickListener(this);
		ll_select_specific.setOnClickListener(this);
		
		Intent i = getIntent();
		Bundle bundle = i.getExtras();
		uid = bundle.getString("uid");
		tv_ss_name.setText(bundle.getString("username"));//�����û���
		if (bundle.getString("photo") != null) {
			imageLoader.displayImage(AppConstants.MAIN_DOMAIN + "/" + bundle.getString("photo"), iv_ss_pic,
					options, null);
		}
	}

	@Override
	public void onClick(View view) {
		
		Intent i = new Intent();
		i.setClass(SelectSpecificActivity.this,HomeFriendsPicActivity.class);
		i.putExtra("uid", uid);
		
		switch (view.getId()) {
		case R.id.select_specific_back://����
			finish();
			break;
		case R.id.iv_ss_pic:
			startActivity(i);
//			finish();
			break;
		case R.id.ll_select_specific:
			startActivity(i);
//			finish();
			break;
		}
	}
}
