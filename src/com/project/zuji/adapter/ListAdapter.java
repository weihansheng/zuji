package com.project.zuji.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.project.zuji.MyrouteFragment;
import com.project.zuji.R;
import com.project.zuji.activity.PlayMyRoute;
import com.project.zuji.entity.Line;
import com.project.zuji.service.DatebaseService;

public class ListAdapter extends BaseAdapter {
	/**
	 * context上下文,用来获得convertView
	 */
	/**
	 * 屏幕宽度,由于我们用的是HorizontalScrollView,所以按钮选项应该在屏幕外
	 */
	private Context context;
	private LayoutInflater mInflater;
	//DisplayImageOptions options;
	private List<Line> newsList;
	private Line news;
	private int mScreentWidth;
	private int downX = 0;
	private int upX = 0;

	/**
	 * 构造方法
	 * 
	 * @param context
	 * @param screenWidth
	 */

	public ListAdapter(Context context, List<Line> newsList,
			int screenWidth) {
		this.newsList = newsList;
		this.context = context;
		this.mInflater = LayoutInflater.from(context);
		mScreentWidth = screenWidth;

	}

	@Override
	public int getCount() {
		return newsList.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		// 如果没有设置过,初始化convertView
		news = newsList.get(position);
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mInflater.inflate(
					R.layout.listitem_collect, null);
			holder.time = (TextView) convertView
					.findViewById(R.id.list_item_time);
			holder.runTime = (TextView) convertView
					.findViewById(R.id.list_item_runTime);
			holder.distance = (TextView) convertView
					.findViewById(R.id.list_item_distance);
			holder.tripMethod = (TextView) convertView
					.findViewById(R.id.list_item_tripMethod);
			//holder.ivAuthor=(ImageView) convertView.findViewById(R.id.list_item_img);
			holder.tvId=(TextView) convertView.findViewById(R.id.list_item_id);
			holder.lineStr=(TextView) convertView.findViewById(R.id.list_item_lineStr);
			holder.aveSpeed=(TextView) convertView.findViewById(R.id.list_item_aveSpeed);

			// 初始化holder

			convertView.setTag(holder);
		} else// 有直接获得ViewHolder
		{
			holder = (ViewHolder) convertView.getTag();
		}

		holder.hSView = (HorizontalScrollView) convertView
				.findViewById(R.id.hsv);

		holder.action = convertView.findViewById(R.id.ll_action);
		holder.btTwo = (Button) convertView.findViewById(R.id.button2);
		holder.btTwo.setFocusable(false);

		// 把位置放到view中,这样点击事件就可以知道点击的是哪一条item
		holder.btTwo.setTag(position);
		//holder.tvId.setText(news.getId().toString());
		holder.time.setText(news.getTime());
		holder.runTime.setText(news.getRunTime());
		//holder.tvTime .setText(TimeUtil.dateToString(System. ,TimeUtil.FORMAT_DATE));
		holder.distance.setText(news.getDistance());
		holder.aveSpeed.setText(news.getAveSpeed());
		holder.tvId.setText(news.getId().toString());
		holder.lineStr.setText(news.getLineStr().toString());
		holder.tripMethod.setText(news.getTripMethod().toString());

		//给ImageView设置路径Tag,这是异步加载图片的小技巧
		convertView.setTag(holder);

		// 设置内容view的大小为屏幕宽度,这样按钮就正好被挤出屏幕外
		holder.content = convertView.findViewById(R.id.ll_content);
		LayoutParams lp = holder.content.getLayoutParams();
		lp.width = mScreentWidth;

		holder.btTwo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				DatebaseService service = new DatebaseService(context);
				service.delData(Long.parseLong(holder.tvId.getText().toString()));
				newsList.remove(position);
				ListAdapter.this.notifyDataSetChanged();

			}
		});
		// 设置监听事件
		convertView.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {

				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					downX = (int) event.getX();
					break;
				case MotionEvent.ACTION_UP:
					upX = (int) event.getX();
					// 获得ViewHolder
					ViewHolder viewHolder = (ViewHolder) v.getTag();

					// 获得HorizontalScrollView滑动的水平方向值.
					int scrollX = viewHolder.hSView.getScrollX();

					// 获得操作区域的长度
					int actionW = viewHolder.action.getWidth();
					if (Math.abs(upX - downX) < 10) {
						//Toast.makeText(mContext, "点击了listview", 0).show();
						holder.time.setTextColor(Color.GRAY);
						
						
						String lineStr=holder.lineStr.getText().toString();
						Intent intent = new Intent();
						intent.putExtra("lineStr", lineStr);
						System.out.println("lineStr===="+lineStr);
						intent.setClass(context, PlayMyRoute.class);
						MyrouteFragment.mactivity.startActivity(intent);

					}

					// 注意使用smoothScrollTo,这样效果看起来比较圆滑,不生硬
					// 如果水平方向的移动值<操作区域的长度的一半,就复原
					if (scrollX < actionW / 2) {
						viewHolder.hSView.smoothScrollTo(0, 0);
					} else// 否则的话显示操作区域
					{
						viewHolder.hSView.smoothScrollTo(actionW, 0);

					}
					break;

				}
				return false;
			}
		});

		// 这里防止删除一条item后,ListView处于操作状态,直接还原
		if (holder.hSView.getScrollX() != 0) {
			holder.hSView.scrollTo(0, 0);
		}

		return convertView;
	}

	// 主要是避免了不断的view获取初始化.
	class ViewHolder {
		public HorizontalScrollView hSView;

		public View content;
		public TextView time;
		public TextView runTime;
		public TextView aveSpeed;
		public TextView tvId;
		public TextView lineStr;
		//public ImageView ivAuthor;
		public TextView distance;
		public TextView tripMethod;
		public View action;
		public Button btTwo;
	}

}
