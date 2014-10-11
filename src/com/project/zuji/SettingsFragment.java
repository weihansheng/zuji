package com.project.zuji;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.project.zuji.activity.AboutActivity;
import com.project.zuji.activity.FeedBackActivity;

/**
 * User: special Date: 13-12-22 Time: 下午3:28 Mail: specialcyci@gmail.com
 */
public class SettingsFragment extends Fragment {
	private View aboutView;
	private View feedbackView;
	private View cleanView;
	private View rootView;
	MyOnClickListener click = new MyOnClickListener();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (rootView == null) {
			rootView = inflater.inflate(R.layout.settings, container, false);
		}
		ViewGroup parent = (ViewGroup) rootView.getParent();
		if (parent != null) {
			parent.removeView(rootView);
		}
		
		aboutView = rootView.findViewById(R.id.about);
		feedbackView = rootView.findViewById(R.id.feedbackview);
		cleanView = rootView.findViewById(R.id.cleanview);
		aboutView.setOnClickListener(click);
		feedbackView.setOnClickListener(click);
		return rootView;
	}

	private class MyOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent = new Intent();
			switch (v.getId()) {
			case R.id.about:
				intent.setClass(getActivity(), AboutActivity.class);
				startActivity(intent);
				break;
			case R.id.feedbackview:
				intent.setClass(getActivity(), FeedBackActivity.class);
				startActivity(intent);
				break;
			case R.id.cleanview:

				// showCleanDialog();
				break;

			default:
				break;
			}

		}
	}

}
