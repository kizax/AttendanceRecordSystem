package edu.tcfsh.attendanceRecordSystem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MySimpleArrayAdapter extends ArrayAdapter<String> {
	  private final Context context;
	  private final String[] values;

	  public MySimpleArrayAdapter(Context context, String[] values) {
	    super(context, R.layout.num_item_listview, values);
	    this.context = context;
	    this.values = values;
	  }

	  @Override
	  public View getView(int position, View convertView, ViewGroup parent) {
	    LayoutInflater inflater = (LayoutInflater) context
	        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    View rowView = inflater.inflate(R.layout.num_item_listview, parent, false);
	    TextView gradeText = (TextView) rowView.findViewById(R.id.gradeText);
	    TextView classText = (TextView) rowView.findViewById(R.id.classText);

	    gradeText.setText(String.valueOf(position));
	    classText.setText(String.valueOf(position));
	   

	    return rowView;
	  }
	} 
