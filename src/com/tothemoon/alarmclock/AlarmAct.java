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
		// ����ָ�����֣���Ϊ֮����MediaPlayer����
		alarmMusic = MediaPlayer.create(this, R.raw.alarm);
		alarmMusic.setLooping(true);
		// ��������
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
//				Toast.makeText(AlarmAct.this, "��֤����� �d(*`��`)��", Toast.LENGTH_SHORT).show();
//			}
//			
//		});
		/*
		 ����һ���Ի���
		new AlertDialog.Builder(AlarmAct.this).setTitle("����")
			.setMessage("�������˰�ι(�� �b���b)�� ")
			.setPositiveButton("ȷ��", new OnClickListener()
			{
				@Override
				public void onClick(DialogInterface dialog, int which)
				{
					// ֹͣ����
					alarmMusic.stop();
					//AlarmManager alarm=(AlarmManager)getSystemService(ALARM_SERVICE);  
					//alarm.cancel(MainActivity.sender[1]);
					// ������Activity
					AlarmAct.this.finish();
				}
			}).show();*/
	}
}

