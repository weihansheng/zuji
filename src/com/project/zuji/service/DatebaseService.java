package com.project.zuji.service;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.project.zuji.entity.Line;
import com.project.zuji.entity.List_LineInfo;

public class DatebaseService {
	private DatebaseHelper dbHelper;

	public DatebaseService(Context context) {
		this.dbHelper = new DatebaseHelper(context);
	}

	// 保存数据。
	public void save(List_LineInfo list) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("start", list.getStart_latlng());
		values.put("end", list.getEnd_latlng());
		db.insert("list", null, values);
	}

	/*public void saveNewsList(List<List_LineInfo> list) {

		for (List_LineInfo news : list) {
			SQLiteDatabase db = dbHelper.getWritableDatabase();
			ContentValues values = new ContentValues();
			values.put("title", news.getTitle());
			values.put("content", news.getContent());
			values.put("newsPics", news.getNewsPics());
			db.insert("news", null, values);
		}

	}
*/
	/**
	 * 向数据库中的表中插入数据
	 * 
	 * @param name
	 * @return 插入数据的id
	 */
	public long insertData(Line line) {
		SQLiteDatabase db = null;
		long id = -1;
		try {
			// 获取数据库对象
			db = dbHelper.getWritableDatabase();
			// 使用execSQL()方法向表中插入数据
			// db.execSQL("insert into user(name) values('"+name+"')");
			// 使用insert()方法向表中插入数据
			// 创建contentvalues对象存储“列名-列值”的映射
			ContentValues values = new ContentValues();
			//values.put("start", string.getStart_latlng());
			values.put("line", line.getLineStr());
			values.put("time", line.getTime());
			values.put("aveSpeed", line.getAveSpeed());
			values.put("distance", line.getDistance());
			values.put("runTime", line.getRunTime());
			values.put("tripMethod", line.getTripMethod());
			// 调用方法插入数据
		    id = db.insert("list", null, values);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (db != null && db.isOpen()) {
				db.close();
			}
		}

		return id;

	}

	/**
	 * 根据id删除数据
	 * 
	 * @param id
	 *            数据的id
	 */
	public void delData(long id) {
		SQLiteDatabase db = null;
		try {
			db = dbHelper.getWritableDatabase();
			// Object[] bingArg={id};
			// db.execSQL("delete from user where _id=?",bingArg);
			String whereClause = "listid=?";
			String[] whereArgs = { String.valueOf(id) };
			db.delete("list", whereClause, whereArgs);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (db != null && db.isOpen()) {
				db.close();
			}
		}

	}

	/**
	 * 根据id修改name字段的值
	 * 
	 * @param id
	 *            数据的id
	 * @param name
	 *            更新的name
	 * @return 更新的行数
	 */
	public long updateData(long id, String title, String content,
			String newsPics) {
		long row = 0;
		SQLiteDatabase db = null;
		try {
			db = dbHelper.getWritableDatabase();
			ContentValues cv = new ContentValues();
			cv.put("title", title);
			cv.put("content", content);
			cv.put("newsPics", newsPics);
			String where = "newsid=?";
			String[] whereValue = { String.valueOf(id) };
			row = db.update("news", cv, where, whereValue);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (db != null && db.isOpen()) {
				db.close();
			}
		}

		return row;

	}

	/**
	 * 
	 * 从数据库中查询数据
	 * 
	 * @return
	 */
	public List<Line> queryData() {
		List<Line> list = new ArrayList<Line>();
		Cursor cursor = null;
		SQLiteDatabase db = null;
		try {
			// 获取数据库对象，如果数据库不存在则创建
			db = dbHelper.getReadableDatabase();
			// 查询表中的数据，获取游标
			cursor = db.query("list", null, null, null, null, null,
					"listid desc");
			int idIndex = cursor.getColumnIndex("listid");
			int lineIndex=cursor.getColumnIndex("line");
			int timeIndex=cursor.getColumnIndex("time");
			int runTimeIndex=cursor.getColumnIndex("runTime");
			int distanceIndex=cursor.getColumnIndex("distance");
			int aveSpeedIndex=cursor.getColumnIndex("aveSpeed");
			int tripMethodIndex=cursor.getColumnIndex("tripMethod");
			for (cursor.moveToFirst(); !(cursor.isAfterLast()); cursor
					.moveToNext()) {
				Line map = new Line();

				// 出错的地方 title是数字的原因是因为写成了map.put("itemsTitle", idIndex);
				// 返回的是idIndex
				// map.put("tvTitle", cursor.getString(titleIndex));
				// map.put("tvContent", cursor.getString(contentIndex));
				map.setTime(cursor.getString(timeIndex));
				map.setDistance(cursor.getString(distanceIndex));
				map.setRunTime(cursor.getString(runTimeIndex));
				map.setAveSpeed(cursor.getString(aveSpeedIndex));
				map.setLineStr(cursor.getString(lineIndex));
				map.setId(cursor.getInt(idIndex));
				map.setTripMethod(cursor.getString(tripMethodIndex));
				list.add(map);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (cursor != null) {
				// 关闭游标
				cursor.close();
			}
			if (db != null && db.isOpen()) {
				// 关闭数据库对象
				db.close();
			}
		}
		return list;

	}

	/**
	 * 从数据库中查询数据
	 * 
	 * @param id
	 * @return
	 */
	public String queryDataById(long id) {
		String line=null;
		Cursor cursor = null;
		SQLiteDatabase db = null;
		try {
			// 获取数据库对象，如果数据库不存在则创建
			db = dbHelper.getReadableDatabase();
			// 查询表中的数据，获取游标
			String where = "listid =?";
			String[] whereValue = { String.valueOf(id) };
			cursor = db.query("list", null, where, whereValue, null, null, null);
			if (cursor == null) {
				return null;
			}
			if (cursor.moveToFirst()) {
				// 获取id列的索引
				int idIndex = cursor.getColumnIndex("listid");
				
				int lineIndex = cursor.getColumnIndex("line");
				
				line=cursor.getString(lineIndex);
				
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (cursor != null) {
				// 关闭游标
				cursor.close();
			}
			if (db != null && db.isOpen()) {
				// 关闭数据库对象
				db.close();
			}
		}
		return line;

	}

}
