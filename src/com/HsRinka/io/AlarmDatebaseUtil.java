package com.HsRinka.io;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.HsRinka.data.AlarmData;

public class AlarmDatebaseUtil {
	MyDatabaseHelper dbHelper;
	SQLiteDatabase db;
	public AlarmDatebaseUtil(Context context){
		dbHelper = new MyDatabaseHelper(context, "myAlarm.db3", 1);
		db=dbHelper.getReadableDatabase();
	}
	public ArrayList<AlarmData> getAllAlarmData(){ 
		ArrayList<AlarmData> allAlarmDataList=new ArrayList<AlarmData>();
		Cursor cursor = db.rawQuery("select * from alarm ",null);
		for(cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext())
		{
		    int weekOfDay = cursor.getInt(cursor.getColumnIndex("weekOfDay"));
		    int hour=cursor.getInt(cursor.getColumnIndex("hour"));
		    int minute=cursor.getInt(cursor.getColumnIndex("minute"));
		    int alarmActivation=cursor.getInt(cursor.getColumnIndex("alarmActivation"));
		    int alarmRepetition=cursor.getInt(cursor.getColumnIndex("alarmRepetition"));
		    AlarmData tempAlarmData=new AlarmData(weekOfDay,hour,minute,alarmActivation,alarmRepetition);
		    allAlarmDataList.add(tempAlarmData);
		}
		cursor.close();
		return allAlarmDataList;
	}
	public void editAlarmData(AlarmData originAlarmData,AlarmData newAlarmData){
		deleteAlarmData(originAlarmData);
		addAlarmData(newAlarmData);
	}
	@SuppressLint("UseValueOf")
	public void addAlarmData(AlarmData alarmData){
		int[] tempArray=alarmData.getAlarmData();
		Integer[] integer = new Integer[tempArray.length];
		for(int i=0;i<tempArray.length;i++){
			integer[i]=new Integer(tempArray[i]);
		}
		db.execSQL("insert into alarm values(null,?,?,?,?,?,?)",integer);
	}
	public void deleteAlarmData(AlarmData alarmData){
		db.execSQL("delete from alarm where id=?",new Integer[]{alarmData.id});
	}
	public void close(){
		if (dbHelper != null)
		{
			dbHelper.close();
		}
	}
}
