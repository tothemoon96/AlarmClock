package com.tothemoon.data;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.os.Parcel;
import android.os.Parcelable;

public class AlarmData implements Parcelable {
	private int weekOfDay;
	private int hour;
	private int minute;
	private int alarmActivation;
	private int alarmRepetition;
	public int id;
	
	public AlarmData(int weekOfDay,int hour,int minute,int alarmActivation,int alarmRepetition){
		this.weekOfDay=weekOfDay;
		this.hour=hour;
		this.minute=minute;
		this.alarmActivation=alarmActivation;
		this.alarmRepetition=alarmRepetition;
		id=getID();
	}
	public AlarmData(){
		Calendar date = Calendar.getInstance();
		weekOfDay=date.get(Calendar.DAY_OF_WEEK);
		hour=date.get(Calendar.HOUR_OF_DAY);
		minute=date.get(Calendar.MINUTE);
		this.alarmActivation=0;
		this.alarmRepetition=0;
		id=getID();
	}
	public AlarmData(AlarmData alarmData){
		int[] tempInt=alarmData.getAlarmData();
		this.weekOfDay=tempInt[0];
		this.hour=tempInt[1];
		this.minute=tempInt[2];
		this.alarmActivation=tempInt[3];
		this.alarmRepetition=tempInt[4];
		id=getID();
	}
	public void setWeekOfDay(int weekOfDay){
		this.weekOfDay=weekOfDay;
		id=getID();
	}
	public void setTime(int hour,int minute){
		this.hour=hour;
		this.minute=minute;
		id=getID();
	}
	public void setAlarmActivation(){
		if(this.alarmActivation==0) this.alarmActivation=1;
		else this.alarmActivation=0;
	}
	public void setAlarmRepetition(){
		if(this.alarmRepetition==0) this.alarmRepetition=1;
		else this.alarmRepetition=0;
	}
	public void setAlarmData(int weekOfDay,int hour,int minute,int alarmActivation,int alarmRepetition){
		this.weekOfDay=weekOfDay;
		this.hour=hour;
		this.minute=minute;
		this.alarmActivation=alarmActivation;
		this.alarmRepetition=alarmRepetition;
		id=getID();
	}
	public int getID(){
		if(weekOfDay!=1) return (weekOfDay-1)*24*60+hour*60+minute;
		else return 7*24*60+hour*60+minute;		
	}
	public int[] getAlarmData(){
		return new int[]{weekOfDay,hour,minute,alarmActivation,alarmRepetition,id};
	}
	public int getAlarmActivation(){
		return alarmActivation;
	}
	public int getAlarmRepetition(){
		return alarmRepetition;
	}
	public String getWeekDay(){
		if (weekOfDay==1) return "周日|";
		else if (weekOfDay==2) return "周一|";
		else if (weekOfDay==3) return "周二|";
		else if (weekOfDay==4) return "周三|";
		else if (weekOfDay==5) return "周四|";
		else if (weekOfDay==6) return "周五|";
		else if (weekOfDay==7) return "周六|";
		else return null;
	}
	@SuppressWarnings("deprecation")
	@Override
	public String toString(){
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm",Locale.SIMPLIFIED_CHINESE);
		String timeStr = sdf.format(new Date(0,0,0,hour,minute));
		if (weekOfDay==1) return "周日|"+timeStr;
		else if (weekOfDay==2) return "周一|"+timeStr;
		else if (weekOfDay==3) return "周二|"+timeStr;
		else if (weekOfDay==4) return "周三|"+timeStr;
		else if (weekOfDay==5) return "周四|"+timeStr;
		else if (weekOfDay==6) return "周五|"+timeStr;
		else if (weekOfDay==7) return "周六|"+timeStr;
		else return null;
	}
	@Override
	public int describeContents() {
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(weekOfDay);
		dest.writeInt(hour);
		dest.writeInt(minute);
		dest.writeInt(alarmActivation);
		dest.writeInt(alarmRepetition);
		dest.writeInt(id);
	}
	public static final Parcelable.Creator<AlarmData> CREATOR = new Parcelable.Creator<AlarmData>() {

		@Override
		public AlarmData createFromParcel(Parcel source) {
			AlarmData alarmData = new AlarmData();
			alarmData.weekOfDay = source.readInt();
			alarmData.hour = source.readInt();
			alarmData.minute = source.readInt();
			alarmData.alarmActivation=source.readInt();
			alarmData.alarmRepetition = source.readInt();
			alarmData.id = source.readInt();
			return alarmData;
		}
		@Override
		public AlarmData[] newArray(int size) {
			return new AlarmData[size];
		}
	};
	public AlarmData clone() {
		AlarmData alarmData = new AlarmData();
		alarmData.setAlarmData(AlarmData.this.weekOfDay,AlarmData.this.hour,AlarmData.this.minute,
				AlarmData.this.alarmActivation,AlarmData.this.alarmRepetition);
		return alarmData;
	}
}
