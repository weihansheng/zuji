package com.project.zuji;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.nhaarman.listviewanimations.swinginadapters.AnimationAdapter;
import com.nhaarman.listviewanimations.swinginadapters.prepared.SwingBottomInAnimationAdapter;
import com.project.zuji.adapter.ListAdapter;
import com.project.zuji.entity.Line;
import com.project.zuji.service.DatebaseService;
import com.project.zuji.xlistview.XListView;
import com.project.zuji.xlistview.XListView.IXListViewListener;
import com.special.ResideMenu.ResideMenu;

/**
 * User: special
 * Date: 13-12-22
 * Time: 下午1:31
 * Mail: specialcyci@gmail.com
 */
public class MyrouteFragment extends Fragment implements IXListViewListener{
	private View rootView;
	private ResideMenu resideMenu;
	private XListView xListview;
	public static MyrouteFragment mactivity;
	private ListAdapter adapter;
	private List<Line> newsList=new ArrayList<Line>();
	private LinearLayout emptylLayout;
	private LinearLayout listlLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	if (rootView == null) {
    		rootView=inflater.inflate(R.layout.profile, container, false);
		}
		ViewGroup parent = (ViewGroup) rootView.getParent();
		if (parent != null) {
			parent.removeView(rootView);
		}
    	
    	mactivity = this;
    	initView(rootView);
    	setUpViews();
        return rootView;
    }
    private void setUpViews() {
		MenuActivity parentActivity = (MenuActivity) getActivity();
		resideMenu = parentActivity.getResideMenu();

		// add gesture operation's ignored views
		View ignored_view = rootView.findViewById(R.id.ignored_view2);
		resideMenu.addIgnoredView(ignored_view);
	}
    private void initView(View parentView) {
		// TODO Auto-generated method stub

    	emptylLayout=(LinearLayout) parentView.findViewById(R.id.empty_layout);
    	listlLayout=(LinearLayout) parentView.findViewById(R.id.list_layout);
    	DatebaseService service = new DatebaseService(getActivity());
		newsList = service.queryData();
		//System.out.println("id--------->"+newsList.);
		System.out.println("newsList===="+newsList.toString());
		xListview = (XListView) parentView.findViewById(R.id.listview);

		// 获取屏幕分辨率
		DisplayMetrics dm = new DisplayMetrics();
		// getWindowManager().getDefaultDisplay().getMetrics(dm); 只能在activity中用
		getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
		// 加载延迟
		adapter = new ListAdapter(getActivity(), newsList,
				dm.widthPixels);
		AnimationAdapter animAdapter = new SwingBottomInAnimationAdapter(adapter);
		xListview.setPullLoadEnable(false);// 取消上拉加载
		xListview.setPullRefreshEnable(false);// 取消下拉刷新
		
		animAdapter.setAbsListView(xListview);
		animAdapter.setInitialDelayMillis(300);
		xListview.setXListViewListener(this);
		xListview.setAdapter(animAdapter);
		if (xListview.getCount()<=2) {
			listlLayout.setVisibility(View.GONE);
			emptylLayout.setVisibility(View.VISIBLE);
		}
	    //loadCollect();

	}
 // 设置下拉刷新时间
 	@Override
 	public void onRefresh() {
 		// TODO Auto-generated method stub
 		 //loadNews();
 	}

 	// 设置loadmore刷新时间
 	@Override
 	public void onLoadMore() {
 		// TODO Auto-generated method stub
 		 //loadCollect();

 	}
    
    
}
