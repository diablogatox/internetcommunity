package com.orfid.internetcommunity;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class RightProvision extends Activity {

	TextView textView;
	ImageView backBtn;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.right_provision);
		textView = (TextView) findViewById(R.id.main_content);
		backBtn = (ImageView) findViewById(R.id.back);
		backBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
				
			}
			
		});
		
		textView.setText(Html.fromHtml(getResources().getString(R.string.right_provision)));
	}
	
}
