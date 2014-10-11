package com.project.zuji.tools;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.Toast;

public class ToastTool {
	private Context context;
	private Toast toast = null;

	public ToastTool(Context context) {
		super();
		this.context = context;
	}

	@SuppressLint("ShowToast")
	public void show(String text) {
		if (toast == null) {
			toast = Toast.makeText(context, text, 1000);
		} else {
			toast.setText(text);
		}
		toast.show();
	}
}
