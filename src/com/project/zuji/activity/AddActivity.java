package com.project.zuji.activity;


import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

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
import com.baidu.mapapi.utils.DistanceUtil;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.project.zuji.R;
import com.project.zuji.entity.Const;
import com.project.zuji.entity.Line;
import com.project.zuji.entity.List_LineInfo;
import com.project.zuji.entity.PointOverlay;
import com.project.zuji.service.DatebaseService;
import com.project.zuji.tools.SymbolTools;
import com.project.zuji.tools.TextTool;
import com.project.zuji.tools.TimeUtil;
import com.project.zuji.tools.ToastTool;

public class AddActivity extends Activity implements BDLocationListener,
OnTouchListener{
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
	private Line line;
	private List<List_LineInfo> list = null;
	private List<List_LineInfo> line_list = new ArrayList<List_LineInfo>();
	private String tripMethod;

	private Handler handler = null;
	//记录时间
	private long mLongCurrentTime = 0;
	private long mLongRunTime = 0;
	private String startTime;

	/**
	 * 添加自定义View
	 */
	private final int ID_record = 0, ID_track = 1;
	private View v_record;
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
		tripMethod= getIntent().getStringExtra("tripMethod");
		mapman = new BMapManager(getApplication());
		mapman.init(Const.BDKey, null);
		setContentView(R.layout.add_layout);
		init();
		mapview.setBuiltInZoomControls(true);
		mapcontroller = mapview.getController();
		mapcontroller.setZoom(15);
		mapcontroller.enableClick(true);

		mylocoverlay = new MyLocationOverlay(mapview);
		mylocoverlay.enableCompass();

		locData = new LocationData();
		locClient = new LocationClient(getApplicationContext());
		locClient.registerLocationListener(AddActivity.this);
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

		// 记录轨迹按钮
		v_record = this.getLayoutInflater().inflate(R.layout.view_button, null);
		v_record.setBackgroundResource(R.drawable.foot_turnpoint);
		v_record.setId(ID_record);
		layoutparams_record = new MapView.LayoutParams(80, 80,
				(int) (width * 0.1), (int) (height - 200),
				MapView.LayoutParams.CENTER);
		mapview.addView(v_record, layoutparams_record);
		v_record.setOnTouchListener(this);

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
		mapview = (MapView) findViewById(R.id.bmapView2);
		backLayout=findViewById(R.id.addroute_back_layout);
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
		if (Const.isRecord) {
			if (Const.isFirst) {
				latlng_start = locData.latitude + "," + locData.longitude;
				Const.isFirst = false;
				DrawMarker(locData.latitude, locData.longitude,
						Const.MARKERT_START);
			} else {
				latlng_end = locData.latitude + "," + locData.longitude;
				System.out.println("list.size()------------->" + list.size());
				List_LineInfo item = new List_LineInfo(latlng_end, latlng_start);
				list.add(item);
				DrawLine(item);
				latlng_start = latlng_end;

			}
		}
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
				} else {
					if (Const.isRecord) {
						Const.isRecord = false;
						if (!Const.isFirst) {
							Const.isFirst = true;
						}
						System.out.println("记录结束");
						//得到记录的时间  单位毫秒
						mLongRunTime = (System.currentTimeMillis()- mLongCurrentTime);
						
						//System.out.println("结束时间===="+TimeUtil.getCurrentTime(TimeUtil.FORMAT_DATE_TIME_SECOND));
						//System.out.println("记录时间===="+formatter.format(ms-TimeZone.getDefault().getRawOffset()));
						//中国为东八区  需要减去8个小时
						System.out.println("记录时间===="+TimeUtil.longToString(mLongRunTime-TimeZone.getDefault().getRawOffset(), TimeUtil.FORMAT_TIME2));
						DrawMarker(locData.latitude, locData.longitude,
								Const.MARKERT_END);
						toast.show("巡示完成，可观看轨迹回放！");

						// 存到本地------------------------------------------------------

						/**
						 * -用来分隔起点和终点， ；用来分隔每一条线
						 */
						StringBuffer sb = new StringBuffer();
						if (list.size() > 1) {
							for (int i = 0; i < list.size(); i++) {
								if (i == list.size() - 1) {
									sb.append(list.get(i).getStart_latlng());
									sb.append("-");
									sb.append(list.get(i).getEnd_latlng());

								} else {
									sb.append(list.get(i).getStart_latlng());
									sb.append("-");
									sb.append(list.get(i).getEnd_latlng());
									sb.append(";");
								}

							}
						}
						if (mLongRunTime>2000) {
							//存储数据到本地
							DatebaseService service = new DatebaseService(
									getApplicationContext());
							//line.setLineStr(sb.toString());
							if (sb.toString()!=null) {
								readStr(sb.toString());
								System.out.println("line.getLineStr()"+line.getLineStr());
								System.out.println("line.getId()----------"+line.getId());
								//计算里程
								service.insertData(line);
							}else {
								System.out.println("路线为空");
							}
							
							
						}else {
							toast.show("记录太短！");
						}
						
						v_record.setBackgroundResource(R.drawable.foot_turnpoint);
					} else {
						System.out.println("开始记录");
						mLongCurrentTime = System.currentTimeMillis();
						startTime=TimeUtil.getCurrentTime(TimeUtil.FORMAT_DATE_TIME_SECOND);
						System.out.println("开始时间===="+TimeUtil.getCurrentTime(TimeUtil.FORMAT_DATE_TIME_SECOND));
						ClearLine();
						ClearMarkert();
						list = new ArrayList<List_LineInfo>();
						Const.isRecord = true;
						v_record.setBackgroundResource(R.drawable.navi_map_flash);

						toast.show("正在记录巡示轨迹！");
					}
				}
				break;
			
			}
		}
		return false;
	}
	private void readStr(String lineStr){
		String[] splitlist = lineStr.split(";");
		for (int i = 0; i < splitlist.length; i++) {

		
			// 把字符串数组lineList转化为List_LineInfo
			List_LineInfo list2 = new List_LineInfo();
			String[] str = splitlist[i].split("-");
			list2.setStart_latlng(str[0].toString());
			list2.setEnd_latlng(str[1].toString());
			// 把字符串数组lineList转化为GeoPoint数组用来计算两点距离
			String starLatitude = list2.getStart_latlng()
					.split(",")[0].substring(0, list2
					.getStart_latlng().split(",")[0].indexOf("."))
					+ list2.getStart_latlng().split(",")[0]
							.substring(list2.getStart_latlng()
									.split(",")[0].indexOf(".") + 1);
			String starLongitude = list2.getStart_latlng().split(
					",")[1].substring(0, list2.getStart_latlng()
					.split(",")[1].indexOf("."))
					+ list2.getStart_latlng().split(",")[1]
							.substring(list2.getStart_latlng()
									.split(",")[1].indexOf(".") + 1);
			String endLatitude = list2.getEnd_latlng().split(",")[0]
					.substring(0,
							list2.getEnd_latlng().split(",")[0]
									.indexOf("."))
					+ list2.getEnd_latlng().split(",")[0]
							.substring(list2.getEnd_latlng().split(
									",")[0].indexOf(".") + 1);
			String endLongitude = list2.getEnd_latlng().split(",")[1]
					.substring(0,
							list2.getEnd_latlng().split(",")[1]
									.indexOf("."))
					+ list2.getEnd_latlng().split(",")[1]
							.substring(list2.getEnd_latlng().split(
									",")[1].indexOf(".") + 1);
			GeoPoint geoPoint;
			geoPoint = new GeoPoint(Integer.parseInt(starLatitude),
					Integer.parseInt(starLongitude));
			myListGeo.add(geoPoint);
			geoPoint = new GeoPoint(Integer.parseInt(endLatitude),
					Integer.parseInt(endLongitude));
			myListGeo.add(geoPoint);
			List_LineInfo item = new List_LineInfo(
					list2.getEnd_latlng(), list2.getStart_latlng());
			line_list.add(item);

		}
		double distansce = 0;
		// 格式化经纬度
		for (int i = 0; i < myListGeo.size(); i++) {
			myListGeo.get(i).setLatitudeE6(
					TextTool.intLatitude(myListGeo.get(i)
							.getLatitudeE6()));
			myListGeo.get(i).setLongitudeE6(
					TextTool.intLongitude(myListGeo.get(i)
							.getLongitudeE6()));
		}
		for (int j = 0; j < myListGeo.size() - 1; j++) {
			// 计算两个位置点之间的距离
			double dTempDis = DistanceUtil.getDistance(
					myListGeo.get(j), myListGeo.get(j + 1));
			distansce = distansce + dTempDis;
		}
		//把米转化为千米
		String disStr = String.valueOf(distansce/1000);
		String temp = String.valueOf((distansce*3600*1000)/mLongRunTime);
		String str_avg_speed = temp.substring(0,temp.lastIndexOf(".")+2);
		System.out.println("平均速度："+str_avg_speed+"km/h");
		//里程保留一位小数
		disStr = disStr.substring(0, disStr.lastIndexOf(".")+2);
		System.out.println("disStr=====" + disStr);
		String runTime=TimeUtil.longToString(mLongRunTime-TimeZone.getDefault().getRawOffset(), TimeUtil.FORMAT_TIME2);
		line=new Line(lineStr, startTime, str_avg_speed, null, disStr, runTime,tripMethod);
		//line.setAveSpeed(str_avg_speed);
		//line.setDistance(disStr);
		//line.setTime(startTime);
		//line.setRunTime(TimeUtil.longToString(mLongRunTime-TimeZone.getDefault().getRawOffset(), TimeUtil.FORMAT_TIME2));
		//System.out.println("line.getAveSpeed："+line.getAveSpeed()+"km/h");
		//System.out.println("line.getDistance："+line.getDistance());
		//System.out.println("line.getTime："+line.getTime());
		System.out.println("line.tripMethod："+line.getTripMethod());
		
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

}
