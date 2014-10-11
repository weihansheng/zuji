package com.project.zuji.entity;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.baidu.mapapi.map.ItemizedOverlay;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.OverlayItem;
import com.baidu.platform.comapi.basestruct.GeoPoint;

public class PointOverlay extends ItemizedOverlay<OverlayItem> {
	public List<OverlayItem> geo = new ArrayList<OverlayItem>();
	private Context context = null;

	public PointOverlay(Drawable arg0, Context context, MapView arg1) {
		super(arg0, arg1);
		this.context = context;
	}

	@Override
	protected boolean onTap(int index) {

		return true;
	}

	@Override
	public boolean onTap(GeoPoint arg0, MapView arg1) {
		super.onTap(arg0, arg1);
		return false;
	}

	public boolean onCenter(List<OverlayItem> item) {
		geo = item;
		if (geo.size() <= 0) {
			return false;
		} else {
			for (int i = 0; i < geo.size(); i++) {
				geo.get(i).setAnchor(0.5f, 1f);
			}
			return true;
		}
	}

	public boolean onCenter(OverlayItem item) {
		item.setAnchor(0.5f, 1f);
		return true;
	}

}
