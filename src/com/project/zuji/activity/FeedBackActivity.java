package com.project.zuji.activity;

import org.apache.http.Header;

import android.app.Activity;
import android.graphics.Bitmap.Config;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.project.zuji.R;

public class FeedBackActivity extends Activity{
	private View backlayout;
	private EditText editText;
	private Button save_btn;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_feedback);
		backlayout=findViewById(R.id.feedback_back_layout);
		editText = (EditText) findViewById(R.id.et_opinion);
		save_btn = (Button) findViewById(R.id.save_btn);
		backlayout.setOnClickListener(onClick);
		save_btn.setOnClickListener(onClick);
	}
	// 上传反馈
	private void addOpinion() {
/*
		RequestParams params = new RequestParams();
		params.put("op.content", editText.getText().toString());
		TwitterRestClient.post(Config.AC_OPINION, params,
				new AsyncHttpResponseHandler() {

					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2,
							Throwable arg3) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] data) {
						// 加载成功
						String response = new String(data);
						if(Config.SUCCESS.equals(response)){
							ToastUtil.showSuccessMsg(FeedBackActivity.this, R.string.success_opinion);
						}
						else if(Config.LOGIN.equals(response)){
							LoginUtil.Login(FeedBackActivity.this);
						}

					}
		});*/
	}
	 OnClickListener onClick  = new  OnClickListener(){

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			
			switch (v.getId()) {
			case R.id.feedback_back_layout:
				finish();
				break;
			case R.id.save_btn:
				if(TextUtils.isEmpty(editText.getText())){
					//ToastUtil.showErrorMsg(FeedBackActivity.this, R.string.error_opinion_null);
				}else if (editText.getText().toString().length()>150){
					//ToastUtil.showErrorMsg(FeedBackActivity.this, R.string.error_opinion_tolong);
				}
				else{
					addOpinion();
				}
				break;

			default:
				break;
			}
		}};
	 

}
