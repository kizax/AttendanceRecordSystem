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
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class ExperimentUpDownActivity extends FragmentActivity{
	private TextView titleText;
	private ListView myList;
	private Bundle bundle;
	private int dayOfMonth;
	private int month;
	private int year;
	private FileWriter txtFileWriter;
	private ArrayList<ClassRecord> classRecordList;

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

	private void setListener() {
		MySimpleArrayAdapter adapter = new MySimpleArrayAdapter(myList.getContext(), classRecordList);

		myList.setAdapter(adapter);

		myList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> av, final View view, final int i, long i2) {

				tempClassRecord = classRecordList.get(i);

				View classRecordView = view.findViewById(R.layout.num_item_listview);
				outOfOfficeText = (TextView) classRecordView.findViewById(R.id.outOfOfficeText);
				outOfOfficePlusButton = (Button) classRecordView.findViewById(R.id.outOfOfficePlusButton);
				outOfOfficeMinusButton = (Button) classRecordView.findViewById(R.id.outOfOfficeMinusButton);

				outOfOfficePlusButton.setOnClickListener(new Button.OnClickListener(){ 

					@Override

					public void onClick(View v) {

						int num = Integer.parseInt((String) outOfOfficeText.getText());
						num++;
						outOfOfficeText.setText(num);
						tempClassRecord.setOutOfOfficeNum(num);

					}}
						);

				sickDayText = (TextView) classRecordView.findViewById(R.id.sickDayText);
				sickDayPlusButton = (Button) classRecordView.findViewById(R.id.sickDayPlusButton);
				sickDayMinusButton = (Button) classRecordView.findViewById(R.id.sickDayMinusButton);

				personalDayText = (TextView) classRecordView.findViewById(R.id.personalDayText);
				personalDayPlusButton = (Button) classRecordView.findViewById(R.id.personalDayPlusButton);
				personalDayMinusButton = (Button) classRecordView.findViewById(R.id.personalDayMinusButton);

				unknownText = (TextView) classRecordView.findViewById(R.id.unknownText);
				unknownPlusButton = (Button) classRecordView.findViewById(R.id.unknownPlusButton);
				unknownMinusButton = (Button) classRecordView.findViewById(R.id.unknownMinusButton);

				arriveLateText = (TextView) classRecordView.findViewById(R.id.arriveLateText);
				arriveLatePlusButton = (Button) classRecordView.findViewById(R.id.arriveLatePlusButton);
				arriveLateMinusButton = (Button) classRecordView.findViewById(R.id.arriveLateMinusButton);




			}
		});
	}



	//				long currentTime = System.currentTimeMillis();
	//				long currentNanoTime = System.nanoTime();
	//				
	//				//每次點擊完數字後，回復列表位置
	//				if (mode.equals("UpDown")) {
	//					//回到頂端
	//					myList.setSelection(0);
	//				} else if (mode.equals("DownUp")) {
	//					//回到底端
	//					myList.setSelection(numStrList.size() - 1);
	//				}
	//
	//				String flickRec, oriRec, touchRec;
	//				flickRec = userId + "," + type + "," + mode + ","
	//						+ sessionCount + "," + numCount + "," + specificNum
	//						+ "," + numStrList.get(arg2) + "," + flickCount
	//						+ ",click," + currentTime + ","
	//						+ currentNanoTime + ",";
	//
	//				oriRec = "0,0,0,";
	//
	//				touchRec = "0,0,0,0";
	//
	//				writerTask(flickRec + oriRec + touchRec);
	//
	//				//檢查所有實驗是否做完
	//				if (numCount == totalSpecificNum
	//						&& sessionCount == totalSessionNum) {
	//					if (mode.equals("UpDown")) {
	//						Toast.makeText(ExperimentUpDownActivity.this,
	//								"垂直閱覽-由上至下 實驗結束\nEnd of the UPDOWN experiment", Toast.LENGTH_LONG).show();
	//						bundle.putBoolean("UpDown", false);
	//					} else if (mode.equals("DownUp")) {
	//						Toast.makeText(ExperimentUpDownActivity.this,
	//								"垂直閱覽-由下至上 實驗結束\nEnd of the DOWNUP experiment", Toast.LENGTH_LONG).show();
	//						bundle.putBoolean("DownUp", false);
	//					}
	//					
	//					//實驗已做完，回到SelectExperimentModeActivity
	//					Intent intent = new Intent();
	//					intent.setClass(ExperimentUpDownActivity.this,
	//							SelectExperimentModeActivity.class);
	//					intent.putExtras(bundle);
	//					startActivity(intent);
	//					ExperimentUpDownActivity.this.finish();
	//				}
	//
	//				flickCount = 0;
	//				//檢查此session實驗是否結束
	//				if (numCount == totalSpecificNum) {
	//					//此session實驗結束，從下個session的第一個點擊數字開始
	//					numCount = 1;
	//					sessionCount++;
	//					
	//					//此session實驗結束，跳出休息訊息
	//					showRestMsg2Dialog();
	//					showRestMsgDialog();
	//				} else {
	//
	//					numCount++;
	//
	//				}
	//				Log.d("kizax", "NUMCOUNT: " + numCount);
	//				
	//				//更新左上角label
	//				if (sessionCount <= totalSessionNum) {
	//					//實驗還沒做完，更新左上角label
	//					specificNum = specifiedNumMetrix[sessionCount - 1][numCount - 1];
	//					textSessionCounter.setText("SESSION: " + sessionCount);
	//					textNumCounter.setText("NUM: " + numCount);
	//					textFlickCounter.setText("FLICK: " + flickCount);
	//					textSpecificNum.setText(Integer.toString(specificNum));
	//				} else {
	//					//實驗已做完，顯示實驗結束訊息
	//					textSessionCounter.setText("實驗結束\nEnd of the experiment");
	//					textNumCounter.setText(" ");
	//					textFlickCounter.setText("");
	//					textSpecificNum.setText("??");
	//
	//				}


	@SuppressLint("SimpleDateFormat")
	private void initialize() {
		titleText = (TextView) findViewById(R.id.titleText);
		myList = (ListView) findViewById(R.id.list);
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
					"%1$tY年%1$tb%1$td%1$tA_缺曠紀錄.csv",
					timeInMilliseconds);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		File SDCardpath = Environment.getExternalStorageDirectory();
		File myDataPath = new File(SDCardpath.getAbsolutePath()
				+ "/expData/expData_" + txtFileName);

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
		titleText.setText(year+"年"+month+"月"+dayOfMonth+"日 缺曠紀錄");
	}

	private void getBundle() {
		bundle = this.getIntent().getExtras();
		dayOfMonth = bundle.getInt("DayOfMonth");
		month = bundle.getInt("Month");
		year = bundle.getInt("Year");
	}

	public ArrayList<String> getNumStringList() {

		//超過此數字時flicking會失速
		int totalNum = 90; 
		ArrayList<String> numStringList = new ArrayList<String>();

		String numStr;
		for (int i = 1; i <= totalNum; i++) {
			if (i < 10) {
				numStr = "0" + Integer.toString(i);
			} else {
				numStr = Integer.toString(i);
			}
			numStringList.add(numStr);
		}

		return numStringList;
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
