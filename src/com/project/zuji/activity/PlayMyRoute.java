package com.project.zuji.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.map.Geometry;
import com.baidu.mapapi.map.Graphic;
import com.baidu.mapapi.map.GraphicsOverlay;
import com.baidu.mapapi.map.LocationData;
import com.baidu.mapapi.map.MKMapViewListener;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationOverlay;
import com.baidu.mapapi.map.OverlayItem;
import com.baidu.mapapi.map.Symbol;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.project.zuji.R;
import com.project.zuji.entity.Const;
import com.project.zuji.entity.List_LineInfo;
import com.project.zuji.entity.PointOverlay;
import com.project.zuji.tools.SymbolTools;
import com.project.zuji.tools.ToastTool;

public class PlayMyRoute extends Activity implements BDLocationListener,
		OnTouchListener {
	private View backLayout;
	private BMapManager mapman = null;
	private MapView mapview = null;

	private MapController mapcontroller;
	// 我的位置相关
	private MyLocationOverlay mylocoverlay;
	private MKMapViewListener mylistener;
	// 定位相关
	private LocationClient locClient;
	private LocationClientOption option;
	private LocationData locData;

	/**
	 * 记录行走轨迹跟播放行走轨迹相关
	 */
	// 画线相关
	private GraphicsOverlay graphicsoverlay;
	Double startLat, startLng, endLat, endLng;
	String latlng_start, latlng_end;
	GeoPoint point_end, point_start;
	List<GeoPoint> myListGeo = new ArrayList<GeoPoint>();;

	String[] startlatlng = new String[2];
	String[] endlatlng = new String[2];
	// 轨迹相关
	private List<List_LineInfo> list = null;
	private List<List_LineInfo> line_list = new ArrayList<List_LineInfo>();

	private Handler handler = null;

	/**
	 * 添加自定义View
	 */
	private final int ID_record = 0, ID_track = 1;
	private View v_track;
	private MapView.LayoutParams layoutparams_record, layoutparams_track;

	// 提示
	ToastTool toast = new ToastTool(this);
	private int count = 0;

	/**
	 * 画Markert
	 */
	private PointOverlay pointOVerlay = null;
	private OverlayItem overlay_item = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//获取轨迹
		getMyroute();
		mapman = new BMapManager(getApplication());
		mapman.init(Const.BDKey, null);
		setContentView(R.layout.play_route);
		init();
		mapview.setBuiltInZoomControls(true);
		mapcontroller = mapview.getController();
		mapcontroller.setZoom(15);
		mapcontroller.enableClick(true);

		mylocoverlay = new MyLocationOverlay(mapview);
		mylocoverlay.enableCompass();

		locData = new LocationData();
		locClient = new LocationClient(getApplicationContext());
		locClient.registerLocationListener(PlayMyRoute.this);
		// 初始化定位
		init_location();
		locClient.start();
		locClient.requestLocation();

		// 地图添加:画线图层、画Markert图层和我的位置图层
		graphicsoverlay = new GraphicsOverlay(mapview);
		mapview.getOverlays().add(graphicsoverlay);
		pointOVerlay = new PointOverlay(null, this, mapview);
		mapview.getOverlays().add(pointOVerlay);
		mapview.getOverlays().add(mylocoverlay);
		mapview.regMapViewListener(mapman, mylistener);

		// 地图上添加自定义控件
		Display display = getWindowManager().getDefaultDisplay();
		@SuppressWarnings("deprecation")
		int width = display.getWidth();
		int height = display.getHeight();

		layoutparams_record = new MapView.LayoutParams(80, 80,
				(int) (width * 0.1), (int) (height - 200),
				MapView.LayoutParams.CENTER);
		// 播放轨迹按钮
		v_track = this.getLayoutInflater().inflate(R.layout.view_button, null);
		v_track.setBackgroundResource(R.drawable.video_blue);
		v_track.setId(ID_track);
		layoutparams_track = new MapView.LayoutParams(80, 80,
				(int) (width * 0.1), (int) (height - 300),
				MapView.LayoutParams.CENTER);
		mapview.addView(v_track, layoutparams_track);
		v_track.setOnTouchListener(this);
		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case Const.MESSAGE_DRAWLINE:

					break;
				case Const.MESSAGE_DRAWTRACK:

					
					if (line_list != null && line_list.size() != 0) {
						toast.show("正在播放轨迹...");
						ClearLine();
						ClearMarkert();
						Const.isTrack = true;
						v_track.setBackgroundResource(R.drawable.video_red);
						locData.latitude = Double.parseDouble(line_list
								.get(count).getStart_latlng().split(",")[0]);
						locData.longitude = Double.parseDouble(line_list
								.get(count).getStart_latlng().split(",")[1]);
						DrawMarker(locData.latitude, locData.longitude,
								Const.MARKERT_START);
						mylocoverlay.setData(locData);
						new Thread() {
							public void run() {
								while (count < line_list.size()
										&& Const.isTrack) {

									locData.latitude = Double
											.parseDouble(line_list.get(count)
													.getEnd_latlng().split(",")[0]);
									locData.longitude = Double
											.parseDouble(line_list.get(count)
													.getEnd_latlng().split(",")[1]);
									mylocoverlay.setData(locData);

									DrawLine(line_list.get(count));
									try {
										// 设置划线间隔
										Thread.sleep(200);
									} catch (InterruptedException e) {
										e.printStackTrace();
									}
									count++;
								}
								DrawMarker(locData.latitude, locData.longitude,
										Const.MARKERT_END);
								count = 0;
								handler.obtainMessage(Const.MESSAGE_TRACKOVER)
										.sendToTarget();
							};
						}.start();
					} else {
						toast.show("没有轨迹数据！");
					}
					break;
				case Const.MESSAGE_TRACKOVER:
					Const.isTrack = false;
					v_track.setBackgroundResource(R.drawable.video_blue);
					toast.show("轨迹播放完成！");
					break;
				}
				super.handleMessage(msg);
			}
		};

	}

	@Override
	protected void onResume() {

		if (mapman != null) {
			mapman.start();
		}
		mapview.onResume();
		super.onResume();
	}

	@Override
	protected void onPause() {
		if (mapman != null) {
			mapman.stop();
		}
		mapview.onPause();

		super.onPause();
	}

	@Override
	protected void onDestroy() {
		if (Const.isTrack) {
			Const.isTrack = false;
			v_track.setBackgroundResource(R.drawable.video_blue);
		}
		if (locClient != null) {
			locClient.stop();
			locClient = null;
		}
		if (mylocoverlay != null) {
			mylocoverlay = null;
		}

		if (mapman != null) {
			mapman.stop();
			mapman = null;
		}
		mapview.destroy();
		super.onDestroy();
	}

	private void init_location() {
		option = new LocationClientOption();
		option.setOpenGps(true);
		option.setCoorType("bd09ll");
		option.setScanSpan(5 * 1000);
		option.disableCache(true);
		locClient.setLocOption(option);
	}

	private void init() {
		mapview = (MapView) findViewById(R.id.bmapView_playRoute);
		backLayout=findViewById(R.id.play_route_back_layout);
		backLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

	}

	@Override
	public void onReceiveLocation(BDLocation location) {
		locData.latitude = location.getLatitude();
		locData.longitude = location.getLongitude();

		mylocoverlay.setData(locData);
		mapview.refresh();
		GeoPoint point = new GeoPoint((int) (locData.latitude * 1e6),
				(int) (locData.longitude * 1e6));

		mapcontroller.animateTo(point);
	}

	@Override
	public void onReceivePoi(BDLocation arg0) {

	}

	// 画Markert
	private boolean DrawMarker(double lat, double lng, int index) {
		GeoPoint point = new GeoPoint((int) (lat * 1E6), (int) (lng * 1E6));
		if (index == Const.MARKERT_START) {
			overlay_item = new OverlayItem(point, "起点", "起点");
			overlay_item.setMarker(getResources().getDrawable(
					R.drawable.v3_icon_qidian));
		} else {
			overlay_item = new OverlayItem(point, "终点", "终点");
			overlay_item.setMarker(getResources().getDrawable(
					R.drawable.v3_icon_zhongdian));
		}
		pointOVerlay.onCenter(overlay_item);
		pointOVerlay.addItem(overlay_item);
		mapview.refresh();
		return true;
	}

	// 用于任务中画轨迹
	private boolean DrawLine(List_LineInfo item) {
		Symbol lineSymbol = SymbolTools.getSymbol_line(0, 0, 255, 255, 4);
		Graphic lineGraphic = new Graphic(getGeometrytLine(item), lineSymbol);
		graphicsoverlay.setData(lineGraphic);
		mapview.refresh();
		return true;
	}

	// 根据两点经纬度坐标得到一条线
	private Geometry getGeometrytLine(List_LineInfo item) {
		endlatlng = item.getEnd_latlng().split(",");
		endLng = Double.parseDouble(endlatlng[1]);
		endLat = Double.parseDouble(endlatlng[0]);

		startlatlng = item.getStart_latlng().split(",");

		startLng = Double.parseDouble(startlatlng[1]);
		startLat = Double.parseDouble(startlatlng[0]);

		// 构建点并显示
		Geometry linegeometry = new Geometry();
		GeoPoint[] linegeopoint = new GeoPoint[2];
		linegeopoint[0] = new GeoPoint((int) (endLat * 1E6),
				(int) (endLng * 1E6));
		linegeopoint[1] = new GeoPoint((int) (startLat * 1E6),
				(int) (startLng * 1E6));
		linegeometry.setPolyLine(linegeopoint);

		return linegeometry;
	}

	// 地图上的自定义控件触摸监听
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_UP) {
			switch (v.getId()) {
			case ID_record:

				break;
			case ID_track:

				break;
			}
		}
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			switch (v.getId()) {
			case ID_record:
				if (Const.isTrack) {
					toast.show("正在播放轨迹！");
				}
				break;
			case ID_track:
				if (Const.isRecord) {
					toast.show("正在记录巡示轨迹...");
				} else {
					if (Const.isTrack) {
						Const.isTrack = false;
						v_track.setBackgroundResource(R.drawable.video_blue);
					} else {
						handler.obtainMessage(Const.MESSAGE_DRAWTRACK)
								.sendToTarget();
					}

				}
				break;
			}
		}
		return false;
	}

	private void ClearLine() {
		if (graphicsoverlay.getAllGraphics().size() != 0) {
			graphicsoverlay.removeAll();
			mapview.refresh();
		}
	}

	private void ClearMarkert() {
		if (pointOVerlay.getAllItem().size() != 0) {
			pointOVerlay.removeAll();
			mapview.refresh();
		}
	}

	private void getMyroute(){
		// 获取数据-----------------------------------
		String lineStr = getIntent().getStringExtra("lineStr");
		System.out.println("lineStr=====" + lineStr.toString());
		String[] splitlist = lineStr.split(";");
		for (int i = 0; i < splitlist.length; i++) {
			// 把字符串数组lineList转化为List_LineInfo
			List_LineInfo list2 = new List_LineInfo();
			String[] str = splitlist[i].split("-");
			String[] laststr={"dd","dsa"};
			if (i!=0) {
				laststr = splitlist[i-1].split("-");
			}
			
			String thisStr=str[0].toString()+str[1].toString();
			String lastStr=laststr[0].toString()+laststr[1].toString();
			System.out.println("str=====" + thisStr);
			System.out.println("laststr=====" +lastStr);
			if (!thisStr.equals(lastStr)) {
				System.out.println("不相等");
				list2.setStart_latlng(str[0].toString());
				list2.setEnd_latlng(str[1].toString());
				List_LineInfo item = new List_LineInfo(
						list2.getEnd_latlng(), list2.getStart_latlng());
				System.out.println("item=====" + item.toString());
				line_list.add(item);
			}
		}

	}
}
