package com.project.zuji.activity;

import com.project.zuji.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

public class AboutActivity extends Activity {
	private View back_layout;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
		back_layout=(View) findViewById(R.id.setting_back_layout);
		back_layout.setOnClickListener(new android.view.View.OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
	
}
