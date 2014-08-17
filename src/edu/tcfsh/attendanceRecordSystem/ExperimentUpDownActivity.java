package edu.tcfsh.attendanceRecordSystem;

import edu.tcfsh.attendanceRecordSystem.R;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import android.os.Bundle;
import android.os.Environment;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class ExperimentUpDownActivity extends FragmentActivity{

	private Bundle bundle;
	private int dayOfMonth;
	private int month;
	private int year;
	private FileWriter txtFileWriter;
	private ArrayList<ClassRecord> classRecordList;

	private TextView titleText;
	private ListView myList;
	private Button commitButton;

	private TextView outOfOfficeText;
	private Button outOfOfficePlusButton;
	private Button outOfOfficeMinusButton;

	private TextView sickDayText;
	private Button sickDayPlusButton;
	private Button sickDayMinusButton;

	private TextView personalDayText;
	private Button personalDayPlusButton;
	private Button personalDayMinusButton;

	private TextView unknownText;
	private Button unknownPlusButton;
	private Button unknownMinusButton;

	private TextView arriveLateText;
	private Button arriveLatePlusButton;
	private Button arriveLateMinusButton;

	private ClassRecord tempClassRecord;



	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_experiment_up_down);
		getBundle();
		initialize();
		initializeFileWriter();
		initializeText();
		generateAllClassesList();
		setListener();
	}

	private void generateAllClassesList() {

		for (int grade = 1;grade<=3;grade++){
			for(int classNum = 1;classNum<=25;classNum++){
				ClassRecord tempClassRecord = new ClassRecord(grade, classNum);
				classRecordList.add(tempClassRecord);
			}
		}

	}

	View.OnClickListener commitButtonListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			String record = "";
			
			for(ClassRecord classRecord: classRecordList){
				record+=classRecord.getRecord();
			}
			
			writerTask(record);
		}
	};

	private void setListener() {
		MySimpleArrayAdapter adapter = new MySimpleArrayAdapter(myList.getContext(), classRecordList);

		myList.setAdapter(adapter);

		commitButton.setOnClickListener(commitButtonListener);
	}

	private void getBundle() {
		bundle = this.getIntent().getExtras();
		dayOfMonth = bundle.getInt("DayOfMonth");
		month = bundle.getInt("Month");
		year = bundle.getInt("Year");
	}

	@SuppressLint("SimpleDateFormat")
	private void initialize() {
		titleText = (TextView) findViewById(R.id.titleText);
		myList = (ListView) findViewById(R.id.list);
		commitButton = (Button) findViewById(R.id.commitButton);
		classRecordList = new ArrayList<ClassRecord>();
	}

	private void initializeFileWriter() {

		//指定csv存檔檔名
		String txtFileName = "";
		String givenDateString = year+"."+month+"."+dayOfMonth; 
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
		try {
			Date date = sdf.parse(givenDateString);
			long timeInMilliseconds = date.getTime();

			txtFileName = String.format(
					"%1$tY年%1$tb%1$td日_%1$tA_缺曠紀錄.csv",
					timeInMilliseconds);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		File SDCardpath = Environment.getExternalStorageDirectory();
		File myDataPath = new File(SDCardpath.getAbsolutePath()
				+ "/attendance Record/" + txtFileName);

		Log.d("kizax",SDCardpath.getAbsolutePath()
				+ "/attendance Record/" + txtFileName);

		//檢查路徑是否存在
		if (!myDataPath.getParentFile().exists()) {
			myDataPath.getParentFile().mkdirs();
			try {
				myDataPath.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		//建立FileWriter
		try {
			txtFileWriter = new FileWriter(myDataPath);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void initializeText() {
		
		String title = "";
		String givenDateString = year+"."+month+"."+dayOfMonth; 
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
		try {
			Date date = sdf.parse(givenDateString);
			long timeInMilliseconds = date.getTime();

			title = String.format(
					"%1$tY年%1$tb%1$td日 %1$tA 缺曠紀錄",
					timeInMilliseconds);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		titleText.setText(title);
	}

	private void writerTask(String record) {
		WriterThread writerThread = new WriterThread(txtFileWriter, record);
		writerThread.start();
	}

	@Override
	public void onResume(){
		super.onResume();
	}

	@Override
	public void onDestroy() {

		//離開activity時需關掉FileWriter且unregister Listener
		try {
			txtFileWriter.flush();
			txtFileWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		super.onDestroy();
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onStop() {
		super.onStop();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_experiment, menu);
		return true;
	}

	@Override
	public void onBackPressed() {
		new AlertDialog.Builder(this).setTitle("提示").setMessage("確定離開實驗?")
		.setPositiveButton("確定", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface arg0, int arg1) {
				finish();
			}
		})
		.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface arg0, int arg1) {
			}
		}).show();
	}


}
