package com.orfid.internetcommunity;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

public class RightProvision extends Activity {

	TextView textView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.right_provision);
		textView = (TextView) findViewById(R.id.main_content);
		
		textView.setText(Html.fromHtml(getResources().getString(R.string.right_provision)));
	}
	
}
