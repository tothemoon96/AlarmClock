package com.tothemoon.alarmclock;

import com.HsRinka.alarmclock.R;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;


public class AlarmAct extends Activity
{
	MediaPlayer alarmMusic;
	Button ok;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.alarmactl);
		// 加载指定音乐，并为之创建MediaPlayer对象
		alarmMusic = MediaPlayer.create(this, R.raw.alarm);
		alarmMusic.setLooping(true);
		// 播放音乐
		alarmMusic.start();
		ok = (Button)findViewById(R.id.button1);
		ok.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				alarmMusic.stop();
				AlarmAct.this.finish();	
			}
		});
		
//		{
//			@Override
//			public void onClick(View arg0) 
//			{
//				Toast.makeText(AlarmAct.this, "验证码错误 ヾ(*`ω`)ノ", Toast.LENGTH_SHORT).show();
//			}
//			
//		});
		/*
		 创建一个对话框
		new AlertDialog.Builder(AlarmAct.this).setTitle("闹钟")
			.setMessage("闹钟响了啊喂(ノ ゜Д゜)ノ ")
			.setPositiveButton("确定", new OnClickListener()
			{
				@Override
				public void onClick(DialogInterface dialog, int which)
				{
					// 停止音乐
					alarmMusic.stop();
					//AlarmManager alarm=(AlarmManager)getSystemService(ALARM_SERVICE);  
					//alarm.cancel(MainActivity.sender[1]);
					// 结束该Activity
					AlarmAct.this.finish();
				}
			}).show();*/
	}
}

