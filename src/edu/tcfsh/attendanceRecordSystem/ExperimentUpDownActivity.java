package edu.tcfsh.attendanceRecordSystem;

import edu.tcfsh.attendanceRecordSystem.R;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Environment;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class ExperimentUpDownActivity extends FragmentActivity implements
		OnTouchListener, SensorEventListener {
	protected float[] orientationValues;
	private TextView textOriX;
	private TextView textOriY;
	private TextView textOriZ;
	private TextView textRawX;
	private TextView textRawY;
	private TextView textPressure;
	private TextView textSize;
	private TextView textSessionCounter;
	private TextView textNumCounter;
	private TextView textFlickCounter;
	private TextView textSpecificNum;
	private SensorManager sensorMgt;
	private Sensor accelerometer;
	private Sensor magnetometer;
	private ListView myList;
	private Bundle bundle;
	private int sessionCount;
	private int numCount;
	private int flickCount;
	private String userId;
	private String session;
	private String mode;
	private String type;
	private int specificNum;
	private ArrayList<String> numStrList;
	private int[][] specifiedNumMetrix;
	private SpecificNumFileReader numFileReader = new SpecificNumFileReader();
	private int totalSessionNum = 0;
	private int totalSpecificNum = 0;
	private FileWriter txtFileWriter;
	private float[] mGravity;
	private float[] mGeomagnetic;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_experiment_up_down);
		initialize();

		getBundle();
		getTotalSessionNum(mode);
		getTotalSpecificNum(mode);
		getSpecifiedNumMetrix(mode);
		initializeText();
		setListener();

	}

	private void getTotalSessionNum(String mode) {
		totalSessionNum = numFileReader.getTotalSessionNum(mode);
	}

	private void getSpecifiedNumMetrix(String mode) {
		specifiedNumMetrix = numFileReader.getSpecificNumMetrix(mode);
	}

	private void getTotalSpecificNum(String mode) {
		totalSpecificNum = numFileReader.getTotalSpecificNum(mode);
	}

	private void initializeText() {
		sessionCount = Integer.parseInt(session);
		numCount = 1;
		flickCount = 0;
		specificNum = specifiedNumMetrix[sessionCount - 1][numCount - 1];
		textSessionCounter.setText("SESSION: " + sessionCount);
		textNumCounter.setText("NUM: " + numCount);
		textFlickCounter.setText("FLICK: " + flickCount);
		textSpecificNum.setText(Integer.toString(specificNum));
	}

	private void setListener() {

		numStrList = getNumStringList();

		myList.setAdapter(new ArrayAdapter<String>(this,
				R.layout.num_item_listview, numStrList));
		myList.setOnTouchListener(this);
		myList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {

				long currentTime = System.currentTimeMillis();
				long currentNanoTime = System.nanoTime();
				
				//每次點擊完數字後，回復列表位置
				if (mode.equals("UpDown")) {
					//回到頂端
					myList.setSelection(0);
				} else if (mode.equals("DownUp")) {
					//回到底端
					myList.setSelection(numStrList.size() - 1);
				}

				String flickRec, oriRec, touchRec;
				flickRec = userId + "," + type + "," + mode + ","
						+ sessionCount + "," + numCount + "," + specificNum
						+ "," + numStrList.get(arg2) + "," + flickCount
						+ ",click," + currentTime + ","
						+ currentNanoTime + ",";

				oriRec = "0,0,0,";

				touchRec = "0,0,0,0";

				writerTask(flickRec + oriRec + touchRec);

				//檢查所有實驗是否做完
				if (numCount == totalSpecificNum
						&& sessionCount == totalSessionNum) {
					if (mode.equals("UpDown")) {
						Toast.makeText(ExperimentUpDownActivity.this,
								"垂直閱覽-由上至下 實驗結束\nEnd of the UPDOWN experiment", Toast.LENGTH_LONG).show();
						bundle.putBoolean("UpDown", false);
					} else if (mode.equals("DownUp")) {
						Toast.makeText(ExperimentUpDownActivity.this,
								"垂直閱覽-由下至上 實驗結束\nEnd of the DOWNUP experiment", Toast.LENGTH_LONG).show();
						bundle.putBoolean("DownUp", false);
					}
					
					//實驗已做完，回到SelectExperimentModeActivity
					Intent intent = new Intent();
					intent.setClass(ExperimentUpDownActivity.this,
							SelectExperimentModeActivity.class);
					intent.putExtras(bundle);
					startActivity(intent);
					ExperimentUpDownActivity.this.finish();
				}

				flickCount = 0;
				//檢查此session實驗是否結束
				if (numCount == totalSpecificNum) {
					//此session實驗結束，從下個session的第一個點擊數字開始
					numCount = 1;
					sessionCount++;
					
					//此session實驗結束，跳出休息訊息
					showRestMsg2Dialog();
					showRestMsgDialog();
				} else {

					numCount++;

				}
				Log.d("kizax", "NUMCOUNT: " + numCount);
				
				//更新左上角label
				if (sessionCount <= totalSessionNum) {
					//實驗還沒做完，更新左上角label
					specificNum = specifiedNumMetrix[sessionCount - 1][numCount - 1];
					textSessionCounter.setText("SESSION: " + sessionCount);
					textNumCounter.setText("NUM: " + numCount);
					textFlickCounter.setText("FLICK: " + flickCount);
					textSpecificNum.setText(Integer.toString(specificNum));
				} else {
					//實驗已做完，顯示實驗結束訊息
					textSessionCounter.setText("實驗結束\nEnd of the experiment");
					textNumCounter.setText(" ");
					textFlickCounter.setText("");
					textSpecificNum.setText("??");

				}
			}
		});
	}

	private void initialize() {
		textSpecificNum = (TextView) findViewById(R.id.textSpecificNum);
		textSessionCounter = (TextView) findViewById(R.id.textSessionCounter);
		textNumCounter = (TextView) findViewById(R.id.textNumCounter);
		textFlickCounter = (TextView) findViewById(R.id.textFlickCounter);
		textOriX = (TextView) findViewById(R.id.textOriX);
		textOriY = (TextView) findViewById(R.id.textOriY);
		textOriZ = (TextView) findViewById(R.id.textOriZ);
		textRawX = (TextView) findViewById(R.id.textRawX);
		textRawY = (TextView) findViewById(R.id.textRawY);
		textPressure = (TextView) findViewById(R.id.textPressure);
		textSize = (TextView) findViewById(R.id.textSize);


		myList = (ListView) findViewById(R.id.list);

		//指定csv存檔檔名
		String txtFileName = String.format(
				"%1$tY年%1$tb%1$td_%1$tk-%1$tM-%1$tS_cellphone_API4.csv",
				System.currentTimeMillis());
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

	private void getBundle() {
		bundle = this.getIntent().getExtras();
		userId = bundle.getString("UserId");
		session = bundle.getString("Session");
		mode = bundle.getString("Mode");
		type = bundle.getString("Type");
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

	private void setOriText(float[] orientationValues) {
		String format = "%1$s: %2$6.3f";
		textOriX.setText(String.format(format, "X pitch", orientationValues[1]));
		textOriY.setText(String.format(format, "Y roll", orientationValues[2]));
		textOriZ.setText(String.format(format, "Z azi", orientationValues[0]));

	}

	private void setTouchText(float rawX, float rawY, float pressure, float size) {
		String format = "%1$s: %2$6.3f";
		textRawX.setText(String.format(format, "rawX", rawX));
		textRawY.setText(String.format(format, "rawY", rawY));
		textPressure.setText(String.format(format, "pressure", pressure));
		textSize.setText(String.format(format, "size", size));
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if (orientationValues != null) {
			String flickRec, oriRec, touchRec;
			long currentTime = System.currentTimeMillis();
			long currentNanoTime = System.nanoTime();
			float rawX = event.getRawX();
			float rawY = event.getRawY();
			float touchPressure = event.getPressure();
			float touchSize = event.getSize();
			double azimut = Math.toDegrees(orientationValues[0]);
			double pitch = Math.toDegrees(orientationValues[1]);
			double roll = Math.toDegrees(orientationValues[2]);
			
			long flickStartTime = 0, flickEndTime = 0;

			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				
				flickStartTime = currentTime;
				
				flickCount++;

				setOriText(orientationValues);
				setTouchText(rawX, rawY, touchPressure, touchSize);

				flickRec = userId + "," + type + "," + mode + ","
						+ sessionCount + "," + numCount + "," + specificNum
						+ "," + "-1" + "," + flickCount + ",down,"
						+ currentTime + "," + currentNanoTime + ",";

				oriRec = pitch + "," + roll + "," + azimut + ",";

				touchRec = Float.toString(rawX) + "," + Float.toString(rawY)
						+ "," + Float.toString(touchPressure) + ","
						+ Float.toString(touchSize);

				writerTask(flickRec + oriRec + touchRec);

				break;
			case MotionEvent.ACTION_MOVE:
				setOriText(orientationValues);
				setTouchText(rawX, rawY, touchPressure, touchSize);

				flickRec = userId + "," + type + "," + mode + ","
						+ sessionCount + "," + numCount + "," + specificNum
						+ "," + "-1" + "," + flickCount + ",move,"
						+ currentTime + "," + currentNanoTime + ",";

				oriRec = pitch + "," + roll + "," + azimut + ",";

				touchRec = Float.toString(rawX) + "," + Float.toString(rawY)
						+ "," + Float.toString(touchPressure) + ","
						+ Float.toString(touchSize);

				writerTask(flickRec + oriRec + touchRec);
				break;
			case MotionEvent.ACTION_UP:
				
				flickEndTime = currentTime;
				
				//檢查Timer是否crash
				if (flickEndTime - flickStartTime <= 0){
					showTimerCrashMsgDialog();
				}
				
				setOriText(orientationValues);
				setTouchText(rawX, rawY, touchPressure, touchSize);

				flickRec = userId + "," + type + "," + mode + ","
						+ sessionCount + "," + numCount + "," + specificNum
						+ "," + "-1" + "," + flickCount + ",up," + currentTime
						+ "," + currentNanoTime + ",";

				oriRec = pitch + "," + roll + "," + azimut + ",";

				touchRec = Float.toString(rawX) + "," + Float.toString(rawY)
						+ "," + Float.toString(touchPressure) + ","
						+ Float.toString(touchSize);

				writerTask(flickRec + oriRec + touchRec);

				textSessionCounter.setText("SESSION: " + sessionCount);
				textNumCounter.setText("NUM: " + numCount);
				textFlickCounter.setText("FLICK: " + flickCount);
				textSpecificNum.setText(Integer.toString(specificNum));
				break;
			}
		} else {
			textOriX.setText("sensor目前沒有更新");
			textOriY.setText("sensor目前沒有更新");
			textOriZ.setText("sensor目前沒有更新");
		}
		return super.onTouchEvent(event);

	}

	private void showRestMsgDialog() {
		TempStopDialog dialog = new TempStopDialog();
		dialog.show(getSupportFragmentManager(), null);
	}
	
	private void showRestMsg2Dialog() {
		TempStopDialog2 dialog = new TempStopDialog2();
		dialog.show(getSupportFragmentManager(), null);
	}
	
	private void showTimerCrashMsgDialog() {
		TimerCrashMsgDialog dialog = new TimerCrashMsgDialog();
		dialog.show(getSupportFragmentManager(), null);
	}

	private void showExperimentEndDialog() {
		ExperimentEndDialog dialog = new ExperimentEndDialog();
		dialog.show(getSupportFragmentManager(), null);
	}

	private void showConnectionMsgDialog(String msg) {
		ConnectionMsgDialog dialog = new ConnectionMsgDialog(msg);
		dialog.show(getSupportFragmentManager(), null);
	}

	private void writerTask(String record) {
		WriterThread writerThread = new WriterThread(txtFileWriter, record);
		writerThread.start();
	}

	@Override
	public void onResume(){
		//回復activity時需註冊Listener
		
		sensorMgt = (SensorManager) getSystemService(SENSOR_SERVICE);
		accelerometer = sensorMgt.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		magnetometer = sensorMgt.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
		sensorMgt.registerListener(this, accelerometer,
				SensorManager.SENSOR_DELAY_FASTEST);
		sensorMgt.registerListener(this, magnetometer,
				SensorManager.SENSOR_DELAY_FASTEST);
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
		sensorMgt.unregisterListener(this);
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

	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		long currentTime = System.currentTimeMillis();
		
		//使用API 4寫法取得orientation sensor值
		if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
			mGravity = event.values;
		}
		if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
			mGeomagnetic = event.values;
		}
		if (mGravity != null && mGeomagnetic != null) {
			float R[] = new float[9];
			float I[] = new float[9];
			boolean success = SensorManager.getRotationMatrix(R, I, mGravity,
					mGeomagnetic);
			if (success) {
				float orientation[] = new float[3];
				SensorManager.getOrientation(R, orientation);

				orientationValues = orientation;
			}
		}
	}
}
