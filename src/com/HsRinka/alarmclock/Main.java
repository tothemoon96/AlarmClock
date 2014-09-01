package com.HsRinka.alarmclock;

import java.net.IDN;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;





import com.HsRinka.alarmclock.R;
import com.HsRinka.data.AlarmData;
import com.HsRinka.io.AlarmDatebaseUtil;

import android.os.Bundle;
import android.os.Parcelable;
import android.provider.Telephony.Mms.Addr;
import android.R.integer;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.AlarmManager;
import android.app.FragmentTransaction;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Picture;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

@SuppressLint("NewApi")
public class Main extends FragmentActivity implements ActionBar.TabListener,DummyFragment.Callbacks{
	public static final String ALARM_DATA = "alarmdata";
	public static final String TEMP_ALARM_DATA = "tempalarmdata";
	
	ViewPager viewPager;
	ActionBar actionBar;
	AlarmDatebaseUtil alarmDatebaseUtil;
	
	List<AlarmData> activatedAlarmDataList;
	List<AlarmData> allAlarmDataList;
	MyFragmentPagerAdapter pagerAdapter;
	
	class MyFragmentPagerAdapter extends FragmentStatePagerAdapter{
		private FragmentManager fm;
		private int mChildCount = 0;
		
		public MyFragmentPagerAdapter(FragmentManager fm){
			super(fm);
			this.fm=fm;
			// TODO Auto-generated constructor stub
		}
		@Override
		public Fragment getItem(int position){
			Fragment fragment = new DummyFragment();
			Bundle args = new Bundle();
			if (position==0){
				args.putParcelableArrayList(ALARM_DATA,
						(ArrayList<? extends Parcelable>) activatedAlarmDataList);
				fragment.setArguments(args);
				return fragment;
			}
			else{
				args.putParcelableArrayList(ALARM_DATA,
						(ArrayList<? extends Parcelable>) allAlarmDataList);
				fragment.setArguments(args);
				return fragment;
			}
		}
		@Override
		public int getCount(){
			return 2;
		}
		@Override
		public CharSequence getPageTitle(int position){
			switch (position)
			{
				case 0:
					return "当前闹钟";
				case 1:
					return "所有闹钟";
			}
			return null;
		}
		@Override  
		public int getItemPosition(Object object) {  
			return POSITION_NONE;  
		}
		@Override
	     public void notifyDataSetChanged() {         
	           mChildCount = getCount();
	           super.notifyDataSetChanged();
	    }
		
	}
	
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		activatedAlarmDataList=new ArrayList<AlarmData>();	
		allAlarmDataList=new ArrayList<AlarmData>();
		alarmDatebaseUtil=new AlarmDatebaseUtil(this);
		
		getAlarmData();
				
		actionBar = getActionBar();
		viewPager = (ViewPager) findViewById(R.id.pager);
		pagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager());
		
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		for (int i = 0; i < pagerAdapter.getCount(); i++)
		{
			actionBar.addTab(actionBar.newTab()
				.setText(pagerAdapter.getPageTitle(i))
				.setTabListener(this));
		}
		viewPager.setOnPageChangeListener(
			new ViewPager.SimpleOnPageChangeListener()
			{
				@Override
				public void onPageSelected(int position)
				{
					actionBar.setSelectedNavigationItem(position);
				}
			});
		viewPager.setAdapter(pagerAdapter);
	}
	
	@Override
	public void onDestroy()
	{
		super.onDestroy();
		alarmDatebaseUtil.close();
	}
	
	@Override
	public void onTabUnselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction)
	{
	}
	
	@Override
	public void onTabSelected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction)
	{
		viewPager.setCurrentItem(tab.getPosition());  //②
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction)
	{
	}

	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public void addAlarm(View v){
		Intent intent = new Intent(this,SetAlarm.class);
		intent.putExtra(ALARM_DATA,new AlarmData());
		startActivityForResult(intent,1);
	}
	
	@Override
	public void onActivityResult(int requestCode,int resultCode,Intent intent){
		AlarmData alarmData;
		AlarmData tempAlarmData;
		//添加闹钟
		//RE1-0 ADD CONFIRM 		1-1 ADD CANCEL  2-0 EDIT CONFIRM 2-1EDIT CANCEL
		if (requestCode == 1 && resultCode == 0){
			alarmData=intent.getParcelableExtra(ALARM_DATA);
			if(checkID(alarmData)==null){
				alarmDatebaseUtil.addAlarmData(alarmData);
				getAlarmData();
				viewPager.getAdapter().notifyDataSetChanged();
			//以alarmData添加一个闹钟
				AddAlarm(alarmData);
			}
			else Toast.makeText(Main.this,"闹钟重复，添加失败",Toast.LENGTH_SHORT).show();
	
		}
		if (requestCode == 1 && resultCode == 1){
		}
		//修改闹钟
		//如果不是重复响铃的闹钟，响铃之后修改闹钟调用这段代码就可以了
		if (requestCode == 2 && resultCode == 0){
			alarmData=intent.getParcelableExtra(ALARM_DATA);
			tempAlarmData=intent.getParcelableExtra(TEMP_ALARM_DATA);
			if(checkID(alarmData)==null||tempAlarmData.getAlarmActivation()!=alarmData.getAlarmActivation()||
					tempAlarmData.getAlarmRepetition()!=alarmData.getAlarmRepetition()){
				alarmDatebaseUtil.editAlarmData(tempAlarmData, alarmData);
				getAlarmData();
				viewPager.getAdapter().notifyDataSetChanged();
				AddAlarm(alarmData);
				DeleteAlarm(tempAlarmData);
				}
			else Toast.makeText(Main.this,"闹钟重复，修改失败",Toast.LENGTH_SHORT).show();
		}
		if (requestCode == 2 && resultCode == 1){
		}
	}
	
	public void getAlarmData(){
		allAlarmDataList=alarmDatebaseUtil.getAllAlarmData();
		Comparator<AlarmData> comparator = new Comparator<AlarmData>(){
		   public int compare(AlarmData aD1, AlarmData aD2) {
		     return aD1.id-aD2.id;
		   }
		};
		Collections.sort(allAlarmDataList,comparator);
		activatedAlarmDataList.clear();
		for(AlarmData aD:allAlarmDataList){
			if(aD.getAlarmActivation()==1) activatedAlarmDataList.add(aD);
		}
	}
	
	public void AddAlarm(AlarmData addedAlarmData) {
		//获得一个Calendar对象
				int alarmInfo[]=addedAlarmData.getAlarmData(); //int[]{weekOfDay,hour,minute,alarmActivation,alarmRepetition,id}
				if (alarmInfo[3]==1) 
				{
		Calendar calendar=Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_WEEK,alarmInfo[0]);
		calendar.set(Calendar.HOUR_OF_DAY, alarmInfo[1]);
		calendar.set(Calendar.MINUTE, alarmInfo[2]);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		
		int delay; int now; int timeWillSet;
		Calendar datenow=Calendar.getInstance();
		now = datenow.get(Calendar.DAY_OF_WEEK)*10000 + datenow.get(Calendar.HOUR_OF_DAY)*1000 + datenow.get(Calendar.MINUTE)*100 +10;
		timeWillSet =alarmInfo[0]*10000 +alarmInfo[1]*1000 +alarmInfo[2]*100;
	    //如果当前时间在设定时间之后  设定延迟。
		if (now>timeWillSet) delay=7*24*60*60*1000; else delay=0;
		 //以获得的Calendar为对象设定一个新闹钟
		AlarmManager alarmManager =(AlarmManager)getSystemService(ALARM_SERVICE);
		Intent newAlarmIntent =new Intent(Main.this,AlarmReceiver.class);
		int id=addedAlarmData.id;
		PendingIntent pendingIntentNew=PendingIntent.getBroadcast(Main.this, id,newAlarmIntent, 0);
		if (alarmInfo[4]==1){
			alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis()+delay, (7*24*60*60*1000), pendingIntentNew);
			}
		else{	alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis()+delay, pendingIntentNew);
		}
		}
		
	}
	public void DeleteAlarm(AlarmData deletedAlarmData) {
		int id=deletedAlarmData.id;
		int act=deletedAlarmData.getAlarmActivation();
		if (act==1)
		{
			AlarmManager alarmManager =(AlarmManager)getSystemService(ALARM_SERVICE);
			Intent intent =new Intent(Main.this,AlarmReceiver.class);
			PendingIntent pendingIntentDelete=PendingIntent.getBroadcast(Main.this, id, intent, 0);
			alarmManager.cancel(pendingIntentDelete);
		};
	}
	//删除闹钟
	@Override
	public void onAlarmDeleted(AlarmData deletedAlarmData) {
		// TODO Auto-generated method stub
		alarmDatebaseUtil.deleteAlarmData(deletedAlarmData);
		DeleteAlarm(deletedAlarmData);
		getAlarmData();
		viewPager.getAdapter().notifyDataSetChanged();
	}
	
	public AlarmData checkID(AlarmData checkedAlarmData){
		for(AlarmData aD:allAlarmDataList){
			if(checkedAlarmData.id==aD.id) return aD;
		}
		return null;
	}
}


