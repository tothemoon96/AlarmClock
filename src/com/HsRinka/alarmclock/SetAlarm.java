package com.HsRinka.alarmclock;

import com.HsRinka.alarmclock.R;
import com.HsRinka.data.AlarmData;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TimePicker;
import android.widget.TimePicker.OnTimeChangedListener;

public class SetAlarm extends Activity{
	ListView setAlarmList;
	AlarmData alarmData;
	AlarmData tempAlarmData;
	
	public static final String ALARM_DATA = "alarmdata";
	public static final String TEMP_ALARM_DATA = "tempalarmdata";
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setalarm);
		Intent intent = getIntent();
		alarmData=intent.getParcelableExtra(ALARM_DATA);
		tempAlarmData=new AlarmData(alarmData);
		
		SetAlarmAdapter setAlarmAdapter=new SetAlarmAdapter(alarmData,this);
		setAlarmList=(ListView) findViewById(R.id.setalarmlist);
		setAlarmList.setAdapter(setAlarmAdapter);
		setAlarmList.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long id){
				setAlarmList.setClickable(false);
				if(position==0){
					CheckBox cB=(CheckBox) view.findViewById(R.id.setalarmcheckbox);
					cB.setOnCheckedChangeListener(new OnCheckedChangeListener(){
						@Override
						public void onCheckedChanged(CompoundButton buttonView,
								boolean isChecked) {
							// TODO Auto-generated method stub
							alarmData.setAlarmActivation();
						}
					});
					if(cB.isChecked()) cB.setChecked(false);
					else cB.setChecked(true);
				}
				if(position==1){
					CheckBox cB=(CheckBox) view.findViewById(R.id.setalarmcheckbox);
					cB.setOnCheckedChangeListener(new OnCheckedChangeListener(){
						@Override
						public void onCheckedChanged(CompoundButton buttonView,
								boolean isChecked) {
							// TODO Auto-generated method stub
							alarmData.setAlarmRepetition();
						}
					});
					if(cB.isChecked()) cB.setChecked(false);
					else cB.setChecked(true);
				}
				if(position==2){
					final int[] alarm = alarmData.getAlarmData();
					View setTime = getLayoutInflater().inflate(R.layout.setalarm_textlist_timepicker, null);
					TimePicker tP=(TimePicker) setTime.findViewById(R.id.timepicker);
					tP.setIs24HourView(true);
					tP.setCurrentHour(alarm[1]);
					tP.setCurrentMinute(alarm[2]);
					tP.setOnTimeChangedListener(new OnTimeChangedListener(){
						@Override
						public void onTimeChanged(TimePicker view,int hourOfDay, int minute) {
							alarmData.setTime(hourOfDay, minute);
						}
					});
					
					RadioGroup rG=(RadioGroup) setTime.findViewById(R.id.rg);
					final RadioButton monday=(RadioButton)setTime.findViewById(R.id.monday);
					final RadioButton tuesday=(RadioButton)setTime.findViewById(R.id.tuesday);
					final RadioButton wednesday=(RadioButton)setTime.findViewById(R.id.wednesday);
					final RadioButton thursday=(RadioButton)setTime.findViewById(R.id.thursday);
					final RadioButton friday=(RadioButton)setTime.findViewById(R.id.friday);
					final RadioButton saturday=(RadioButton)setTime.findViewById(R.id.saturday);
					final RadioButton sunday=(RadioButton)setTime.findViewById(R.id.sunday);
					if(alarm[0]==1) rG.check(sunday.getId());		
					else if(alarm[0]==2) rG.check(monday.getId());
					else if(alarm[0]==3) rG.check(tuesday.getId());
					else if(alarm[0]==4) rG.check(wednesday.getId());
					else if(alarm[0]==5) rG.check(thursday.getId());
					else if(alarm[0]==6) rG.check(friday.getId());
					else rG.check(saturday.getId());
					rG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){
						@Override
						public void onCheckedChanged(RadioGroup group,int checkedId) {
							// TODO Auto-generated method stub
							if(checkedId==sunday.getId()) alarmData.setWeekOfDay(1);
							if(checkedId==monday.getId()) alarmData.setWeekOfDay(2);
							if(checkedId==tuesday.getId()) alarmData.setWeekOfDay(3);
							if(checkedId==wednesday.getId()) alarmData.setWeekOfDay(4);
							if(checkedId==thursday.getId()) alarmData.setWeekOfDay(5);
							if(checkedId==friday.getId()) alarmData.setWeekOfDay(6);
							if(checkedId==saturday.getId()) alarmData.setWeekOfDay(7);
						}
						
					});
					AlertDialog.Builder builder =new AlertDialog.Builder(SetAlarm.this);
					builder
					.setTitle("设定时间")
					.setView(setTime)
					.setPositiveButton("确定",
					new DialogInterface.OnClickListener()
					{
						@Override
						public void onClick(DialogInterface dialog,int which){
							SetAlarmAdapter tempAlarmAdapter=new SetAlarmAdapter(alarmData,SetAlarm.this);
							setAlarmList.setAdapter(tempAlarmAdapter);
						}
					}).setNegativeButton("取消", null).show();
				}
				setAlarmList.setClickable(true);
			}
		});
	}
	public void onBackPressed() {
		Intent intent = new Intent();
		SetAlarm.this.setResult(1, intent);
		SetAlarm.this.finish();
}
	public void setalarmcancelok(View v){
		Intent intent = getIntent();
		intent.putExtra(ALARM_DATA, alarmData);
		intent.putExtra(TEMP_ALARM_DATA, tempAlarmData);
		SetAlarm.this.setResult(0,intent);
		SetAlarm.this.finish();
	}
	public void setalarmcancel(View v){
		Intent intent = getIntent();
		SetAlarm.this.setResult(1,intent);
		SetAlarm.this.finish();
	}
}

class SetAlarmAdapter extends BaseAdapter{
	private AlarmData alarmData;
	private Context context;
	private LayoutInflater inflater = null;
	
	public SetAlarmAdapter(AlarmData alarmData,Context context){
		this.alarmData=alarmData;
		this.context = context;
		inflater = LayoutInflater.from(this.context);
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 3;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View v = null;
		int[] tempInt=alarmData.getAlarmData();
		if(position==0){
			v=inflater.inflate(R.layout.setalarm_checkbox, null);
			CheckBox cB=(CheckBox) v.findViewById(R.id.setalarmcheckbox);
			if(tempInt[3]==0) cB.setChecked(false);
			else cB.setChecked(true);
			cB.setText("启动闹钟");
			cB.setClickable(false);
		}
		else if(position==1){
			v=inflater.inflate(R.layout.setalarm_checkbox, null);
			CheckBox cB=(CheckBox) v.findViewById(R.id.setalarmcheckbox);
			if(tempInt[4]==0) cB.setChecked(false);
			else cB.setChecked(true);
			cB.setText("重复");
			cB.setClickable(false);
		}
		else if(position==2){
			v=inflater.inflate(R.layout.setalarm_textlist, null);
			TextView tV1=(TextView) v.findViewById(R.id.setalarmtext1);
			TextView tV2=(TextView) v.findViewById(R.id.setalarmtext2);
			tV1.setText("时间");
			tV2.setText(alarmData.toString());
			tV1.setClickable(false);
			tV2.setClickable(false);
		}
		return v;
	}
	
}