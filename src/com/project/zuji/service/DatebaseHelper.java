package com.project.zuji.service;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatebaseHelper extends SQLiteOpenHelper {
	public final static String NAME="drafts.db";
	 public final static int VERSION=1;
	 public DatebaseHelper(Context context) {
	    super(context, NAME, null, VERSION);
	 }

	 @Override
	 public void onCreate(SQLiteDatabase db) {
	  // TODO Auto-generated method stub
	   db.execSQL("CREATE TABLE list (listid integer primary key autoincrement, line varchar,time varchar,aveSpeed varchar,runTime varchar,distance varchar,tripMethod varchar)");
	 }

	 @Override
	 public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
	  // TODO Auto-generated method stub
	    db.execSQL("DROP TABLE IF EXISTS list");
	    onCreate(db);
	 }

	}