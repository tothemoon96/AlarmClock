package com.HsRinka.alarmclock;

import java.util.ArrayList;
import java.util.HashMap;

import com.HsRinka.alarmclock.R;
import com.HsRinka.alarmclock.Main.MyFragmentPagerAdapter;
import com.HsRinka.data.AlarmData;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

@SuppressLint("UseSparseArrays")
public class DummyFragment extends Fragment 
{
	public static final String ALARM_DATA = "alarmdata";
	public static final String TEMP_ALARM_DATA = "tempalarmdata";
	
	private AlarmData[] alarmDataArray;
	private HashMap<Integer,Integer> isSelected1;
	private HashMap<Integer,Integer> isSelected2;
	private ArrayList<String> list;
	private AlarmAdapter alarmAdapter;
	ListView alarmList;
	
	public interface Callbacks{
		public void onAlarmDeleted(AlarmData deletedAlarmData);

		void DeleteAlarm(AlarmData deletedAlarmData);
	}
	private Callbacks mCallbacks;

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		View v=inflater.inflate(R.layout.dummyfragment_listview, container,false);
		alarmList = (ListView) v.findViewById(R.id.alarmlist);
		
		Bundle args = getArguments();
		ArrayList<Parcelable> alarmDataList=args.getParcelableArrayList(ALARM_DATA);
		alarmDataArray=alarmDataList.toArray(new AlarmData[alarmDataList.size()]);
		
		list=new ArrayList<String>();
		isSelected1=new HashMap<Integer,Integer>();
		isSelected2=new HashMap<Integer,Integer>();
		//将alarmDataArray中的信息提取出来，变成时间和是否设定闹钟，构造ListView
		for(int i=0;i<alarmDataArray.length;i++){
			list.add(alarmDataArray[i].toString());
			isSelected1.put(i, alarmDataArray[i].getAlarmActivation());
			isSelected2.put(i, alarmDataArray[i].getAlarmRepetition());
		}
		alarmAdapter=new AlarmAdapter(list,isSelected1,isSelected2,getActivity());
		alarmList.setAdapter(alarmAdapter);
		alarmList.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long id){
				alarmList.setClickable(false);
				Intent intent = new Intent(getActivity(),SetAlarm.class);
				intent.putExtra(ALARM_DATA,alarmDataArray[position]);
				getActivity().startActivityForResult(intent,2);
				alarmList.setClickable(true);
			}
		});
		alarmList.setOnItemLongClickListener(new OnItemLongClickListener(){

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					final int position, long id) {
				alarmList.setClickable(false);
				AlertDialog.Builder builder =new AlertDialog.Builder(getActivity());
				builder
				.setTitle("删除闹钟")
				.setPositiveButton("确定",
				new DialogInterface.OnClickListener()
				{
					@Override
					public void onClick(DialogInterface dialog,int which){
						mCallbacks=(Callbacks)getActivity();
						mCallbacks.onAlarmDeleted(alarmDataArray[position]);
					}
				}).setNegativeButton("取消", null).show();
				alarmList.setClickable(true);
				return false;
			}
			
		});
		
		return alarmList;
	}
	
}

class AlarmAdapter extends BaseAdapter{
	private ArrayList<String> list;
	private HashMap<Integer,Integer> isSelected1;
	private HashMap<Integer,Integer> isSelected2;
	private Context context;
	private LayoutInflater inflater = null;
	class ViewHolder{
		public CheckBox checkBox1;
		public CheckBox checkBox2;
		public TextView textView;
	}
	
	public AlarmAdapter(ArrayList<String> list,HashMap<Integer, Integer> isSelected1,HashMap<Integer,Integer> isSelected2,
			Context context) {
        this.context = context;
        this.list = list;
        this.isSelected1 = isSelected1;
        this.isSelected2 = isSelected2;
        inflater = LayoutInflater.from(this.context);
    }
	
	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position){
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.dummyfragment_listview_alarmview, null);
            holder.checkBox1 = (CheckBox) convertView.findViewById(R.id.alarmCheckBox);
            holder.checkBox2=(CheckBox) convertView.findViewById(R.id.alarmRepeatBox);
            holder.textView = (TextView) convertView.findViewById(R.id.alarmTextView);
            holder.checkBox1.setClickable(false);
            holder.checkBox2.setClickable(false);
            holder.textView.setClickable(false);
            convertView.setTag(holder);
        } 
		else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		if(getIsSelected1().get(position)==0) holder.checkBox1.setChecked(false);
		else holder.checkBox1.setChecked(true);
		
		if(getIsSelected2().get(position)==0) holder.checkBox2.setChecked(false);
		else holder.checkBox2.setChecked(true);
		
		holder.textView.setText(list.get(position));
        return convertView;
	}
	
	public HashMap<Integer,Integer> getIsSelected1() {
        return isSelected1;
	}
	public HashMap<Integer,Integer> getIsSelected2() {
        return isSelected2;
    }
}