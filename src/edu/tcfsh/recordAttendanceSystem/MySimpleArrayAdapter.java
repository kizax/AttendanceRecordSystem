package edu.tcfsh.recordAttendanceSystem;

import java.util.ArrayList;

import edu.tcfsh.attendanceRecordSystem.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;



public class MySimpleArrayAdapter extends ArrayAdapter<ClassRecord> {
	private final Context context;
	private final ArrayList<ClassRecord> classRecordList;




	public MySimpleArrayAdapter(Context context, ArrayList<ClassRecord> classRecordList) {
		super(context, R.layout.num_item_listview, classRecordList);
		this.context = context;
		this.classRecordList = classRecordList;
	}

	@Override
	public void notifyDataSetChanged()
	{
		super.notifyDataSetChanged();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder;

		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.num_item_listview, parent, false);

			holder = new ViewHolder();

			holder.gradeText = (TextView) convertView.findViewById(R.id.gradeText);
			holder.classText = (TextView) convertView.findViewById(R.id.classText);

			holder.outOfOfficeText = (TextView) convertView.findViewById(R.id.outOfOfficeText);
			holder.outOfOfficePlusButton = (Button) convertView.findViewById(R.id.outOfOfficePlusButton);
			holder.outOfOfficeMinusButton = (Button) convertView.findViewById(R.id.outOfOfficeMinusButton);
			
			holder.sickDayText = (TextView) convertView.findViewById(R.id.sickDayText);
			holder.sickDayPlusButton = (Button) convertView.findViewById(R.id.sickDayPlusButton);
			holder.sickDayMinusButton = (Button) convertView.findViewById(R.id.sickDayMinusButton);
			
			holder.personalDayText = (TextView) convertView.findViewById(R.id.personalDayText);
			holder.personalDayPlusButton = (Button) convertView.findViewById(R.id.personalDayPlusButton);
			holder.personalDayMinusButton = (Button) convertView.findViewById(R.id.personalDayMinusButton);
			
			holder.unknownText = (TextView) convertView.findViewById(R.id.unknownText);
			holder.unknownPlusButton = (Button) convertView.findViewById(R.id.unknownPlusButton);
			holder.unknownMinusButton = (Button) convertView.findViewById(R.id.unknownMinusButton);
			
			holder.arriveLateText = (TextView) convertView.findViewById(R.id.arriveLateText);
			holder.arriveLatePlusButton = (Button) convertView.findViewById(R.id.arriveLatePlusButton);
			holder.arriveLateMinusButton = (Button) convertView.findViewById(R.id.arriveLateMinusButton);

			convertView.setTag(holder);
		}else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.outOfOfficePlusButton.setTag(position);
		holder.outOfOfficeMinusButton.setTag(position);
		holder.outOfOfficeText.setTag(position);
		
		holder.sickDayPlusButton.setTag(position);
		holder.sickDayMinusButton.setTag(position);
		holder.sickDayText.setTag(position);
		
		holder.personalDayPlusButton.setTag(position);
		holder.personalDayMinusButton.setTag(position);
		holder.personalDayText.setTag(position);
		
		holder.unknownPlusButton.setTag(position);
		holder.unknownMinusButton.setTag(position);
		holder.unknownText.setTag(position);
		
		holder.arriveLatePlusButton.setTag(position);
		holder.arriveLateMinusButton.setTag(position);
		holder.arriveLateText.setTag(position);


		View.OnClickListener OutOfOfficePlusButtonListener = new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				int pos = (Integer) v.getTag();

				int num = classRecordList.get(pos).getOutOfOfficeNum();
				num++;

				classRecordList.get(pos).setOutOfOfficeNum(num);
				notifyDataSetChanged();
			}
		};

		View.OnClickListener OutOfOfficeMinusButtonListener = new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				
				int pos = (Integer) v.getTag();

				int num = classRecordList.get(pos).getOutOfOfficeNum();
				if(num>0){
					num--;
				}

				classRecordList.get(pos).setOutOfOfficeNum(num);
				notifyDataSetChanged();
			}
		};
		
		View.OnClickListener sickDayPlusButtonListener = new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				int pos = (Integer) v.getTag();

				int num = classRecordList.get(pos).getSickDayNum();
				num++;

				classRecordList.get(pos).setSickDayNum(num);
				notifyDataSetChanged();
			}
		};

		View.OnClickListener sickDayMinusButtonListener = new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				
				int pos = (Integer) v.getTag();

				int num = classRecordList.get(pos).getSickDayNum();
				if(num>0){
					num--;
				}

				classRecordList.get(pos).setSickDayNum(num);
				notifyDataSetChanged();
			}
		};
		
		View.OnClickListener personalDayPlusButtonListener = new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				int pos = (Integer) v.getTag();

				int num = classRecordList.get(pos).getPersonalDayNum();
				num++;

				classRecordList.get(pos).setPersonalDayNum(num);
				notifyDataSetChanged();
			}
		};

		View.OnClickListener personalDayMinusButtonListener = new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				
				int pos = (Integer) v.getTag();

				int num = classRecordList.get(pos).getPersonalDayNum();
				if(num>0){
					num--;
				}

				classRecordList.get(pos).setPersonalDayNum(num);
				notifyDataSetChanged();
			}
		};
		
		View.OnClickListener unknownPlusButtonListener = new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				int pos = (Integer) v.getTag();

				int num = classRecordList.get(pos).getUnknownNum();
				num++;

				classRecordList.get(pos).setUnknownNum(num);
				notifyDataSetChanged();
			}
		};

		View.OnClickListener unknownMinusButtonListener = new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				
				int pos = (Integer) v.getTag();

				int num = classRecordList.get(pos).getUnknownNum();
				if(num>0){
					num--;
				}

				classRecordList.get(pos).setUnknownNum(num);
				notifyDataSetChanged();
			}
		};
		
		View.OnClickListener arriveLatePlusButtonListener = new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				int pos = (Integer) v.getTag();

				int num = classRecordList.get(pos).getArriveLateNum();
				num++;

				classRecordList.get(pos).setArriveLateNum(num);
				notifyDataSetChanged();
			}
		};

		View.OnClickListener arriveLateMinusButtonListener = new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				
				int pos = (Integer) v.getTag();

				int num = classRecordList.get(pos).getArriveLateNum();
				if(num>0){
					num--;
				}

				classRecordList.get(pos).setArriveLateNum(num);
				notifyDataSetChanged();
			}
		};

		holder.outOfOfficePlusButton.setOnClickListener(OutOfOfficePlusButtonListener);
		holder.outOfOfficeMinusButton.setOnClickListener(OutOfOfficeMinusButtonListener);
		holder.sickDayPlusButton.setOnClickListener(sickDayPlusButtonListener);
		holder.sickDayMinusButton.setOnClickListener(sickDayMinusButtonListener);
		holder.personalDayPlusButton.setOnClickListener(personalDayPlusButtonListener);
		holder.personalDayMinusButton.setOnClickListener(personalDayMinusButtonListener);
		holder.unknownPlusButton.setOnClickListener(unknownPlusButtonListener);
		holder.unknownMinusButton.setOnClickListener(unknownMinusButtonListener);
		holder.arriveLatePlusButton.setOnClickListener(arriveLatePlusButtonListener);
		holder.arriveLateMinusButton.setOnClickListener(arriveLateMinusButtonListener);

		holder.gradeText.setText(String.valueOf(classRecordList.get(position).getGrade()));
		holder.classText.setText(String.valueOf(classRecordList.get(position).getClassNum()));
		
		holder.outOfOfficeText.setText(String.valueOf(classRecordList.get(position).getOutOfOfficeNum()));
		holder.sickDayText.setText(String.valueOf(classRecordList.get(position).getSickDayNum()));
		holder.personalDayText.setText(String.valueOf(classRecordList.get(position).getPersonalDayNum()));
		holder.unknownText.setText(String.valueOf(classRecordList.get(position).getUnknownNum()));
		holder.arriveLateText.setText(String.valueOf(classRecordList.get(position).getArriveLateNum()));

		return convertView;
	}
} 
