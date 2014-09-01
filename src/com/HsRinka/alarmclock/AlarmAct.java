package com.HsRinka.alarmclock;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;

import com.HsRinka.alarmclock.R;
import com.HsRinka.data.AlarmData;
import com.HsRinka.io.AlarmDatebaseUtil;


public class AlarmAct extends Activity
{
	MediaPlayer alarmMusic;
	//AlarmData alarmData;
	int id2;
	Button ok;
	List<AlarmData> allAlarmDataList;
	AlarmDatebaseUtil alarmDatebaseUtil;
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.alarmactl);
		alarmMusic = MediaPlayer.create(this, R.raw.alarm);
		alarmMusic.setLooping(true);
		alarmMusic.start();
		allAlarmDataList=new ArrayList<AlarmData>();
		alarmDatebaseUtil=new AlarmDatebaseUtil(this);
		//获取当前时间
		Calendar calendar=Calendar.getInstance();
		int minute=calendar.get(Calendar.MINUTE);
		int hour=calendar.get(Calendar.HOUR_OF_DAY);
		int dayOfWeek=calendar.get(Calendar.DAY_OF_WEEK);
		//计算响铃闹钟的ID
		if(dayOfWeek!=1) id2=(dayOfWeek-1)*24*60+hour*60+minute;
		else id2=7*24*60+hour*60+minute;
		//若当前闹钟不重复响铃  将其从列表中关闭响铃
		allAlarmDataList=alarmDatebaseUtil.getAllAlarmData();
		for (AlarmData aD:allAlarmDataList) {
			if((id2==aD.id)&&(aD.getAlarmRepetition()==0)&&(aD.getAlarmActivation()==1)){
				AlarmData tAD=new AlarmData(aD);
				tAD.setAlarmActivation();
				alarmDatebaseUtil.editAlarmData(aD,tAD);
			} ;
		}
		//按键停止响铃
		ok = (Button)findViewById(R.id.button1);
		ok.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				alarmMusic.stop();
				alarmMusic.release();
				
				AlarmAct.this.finish();	
			}
		});

	}
	
}

