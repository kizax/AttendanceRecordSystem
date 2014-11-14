package edu.tcfsh.recordAttendanceSystem;

import edu.tcfsh.attendanceRecordSystem.R;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import jxl.Cell;
import jxl.CellType;
import jxl.NumberCell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import android.os.Bundle;
import android.os.Environment;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class RecordAttendanceActivity extends Activity {

	private Bundle bundle;
	private int dayOfMonth;
	private int month;
	private int year;

	private WritableWorkbook writableWorkbook;
	private WritableSheet writableSheet;
	private ArrayList<ClassRecord> classRecordList;

	private TextView titleText;
	private ListView myList;
	private Button commitButton;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_record_attendance);
		getBundle();
		initialize();
		generateAllClassesList();
		initializeFileWriter();
		initializeText();
		setListener();
	}

	private void generateAllClassesList() {

		for (int grade = 1; grade <= 3; grade++) {
			for (int classNum = 1; classNum <= 25; classNum++) {
				ClassRecord tempClassRecord = new ClassRecord(grade, classNum);
				classRecordList.add(tempClassRecord);
			}
		}

	}

	View.OnClickListener commitButtonListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			writerTask();
			showToast("檔案已儲存!");
			finish();

		}
	};

	private void setListener() {
		MySimpleArrayAdapter adapter = new MySimpleArrayAdapter(
				myList.getContext(), classRecordList);

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

	private String getAttendanceRecordFileName() {

		String attendanceRecordFileName = "";
		String dateStr = year + "." + month + "." + dayOfMonth;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");

		try {
			Date date = sdf.parse(dateStr);
			long timeInMilliseconds = date.getTime();

			attendanceRecordFileName = String.format(
					"%1$tY年%1$tb%1$td日 %1$tA 缺曠紀錄.xls", timeInMilliseconds);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return attendanceRecordFileName;
	}

	private void initializeFileWriter() {

		// 指定xls存檔檔名
		String attendanceRecordFileName = getAttendanceRecordFileName();

		File SDCardpath = Environment.getExternalStorageDirectory();
		File attendanceRecord = new File(SDCardpath.getAbsolutePath()
				+ "/attendance Record/" + attendanceRecordFileName);
		// File copyOfAttendanceRecord = new File(SDCardpath.getAbsolutePath()
		// + "/attendance Record/" + attendanceRecordFileName + "_copy");

		Log.d("kizax", SDCardpath.getAbsolutePath() + "/attendance Record/"
				+ attendanceRecordFileName);

		// 檢查路徑是否存在
		if (!attendanceRecord.getParentFile().exists()) {
			attendanceRecord.getParentFile().mkdirs();
			try {
				attendanceRecord.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		// 建立FileWriter
		try {
			if (attendanceRecord.exists()) {
				Workbook workbook = Workbook.getWorkbook(attendanceRecord);
				Sheet sheet = workbook.getSheet(0);

				for (int grade = 1; grade <= 3; grade++) {
					for (int classNum = 1; classNum <= 25; classNum++) {

						int index = (grade - 1) * 25 + classNum;

						Cell outOfOfficeNumCell = sheet.getCell(1, index);
						if (outOfOfficeNumCell.getType() == CellType.NUMBER) {
							NumberCell nc = (NumberCell) outOfOfficeNumCell;
							int outOfOfficeNum = (int) nc.getValue();
							classRecordList.get(index - 1).setOutOfOfficeNum(
									outOfOfficeNum);
						}

						Cell sickDayNumCell = sheet.getCell(2, index);
						if (sickDayNumCell.getType() == CellType.NUMBER) {
							NumberCell nc = (NumberCell) sickDayNumCell;
							int sickDayNum = (int) nc.getValue();
							classRecordList.get(index - 1).setSickDayNum(
									sickDayNum);
						}

						Cell personalDayNumCell = sheet.getCell(3, index);
						if (personalDayNumCell.getType() == CellType.NUMBER) {
							NumberCell nc = (NumberCell) personalDayNumCell;
							int personalDayNum = (int) nc.getValue();
							classRecordList.get(index - 1).setPersonalDayNum(
									personalDayNum);
						}

						Cell unknownNumCell = sheet.getCell(4, index);
						if (unknownNumCell.getType() == CellType.NUMBER) {
							NumberCell nc = (NumberCell) unknownNumCell;
							int unknownNum = (int) nc.getValue();
							classRecordList.get(index - 1).setUnknownNum(
									unknownNum);
						}

						Cell arriveLateNumCell = sheet.getCell(5, index);
						if (arriveLateNumCell.getType() == CellType.NUMBER) {
							NumberCell nc = (NumberCell) arriveLateNumCell;
							int arriveLateNum = (int) nc.getValue();
							classRecordList.get(index - 1).setArriveLateNum(
									arriveLateNum);
						}

					}
				}

			}

			writableWorkbook = Workbook.createWorkbook(attendanceRecord);
			writableSheet = writableWorkbook.createSheet("attendanceRecord", 0);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (BiffException e) {
			e.printStackTrace();
		}

	}

	private void showToast(String msg) {

		Context context = getApplicationContext();
		CharSequence text = msg;
		int duration = Toast.LENGTH_SHORT;

		Toast toast = Toast.makeText(context, text, duration);
		toast.show();
	}

	private void initializeText() {

		String title = "";
		String givenDateString = year + "." + month + "." + dayOfMonth;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
		try {
			Date date = sdf.parse(givenDateString);
			long timeInMilliseconds = date.getTime();

			title = String.format("%1$tY年%1$tb%1$td日 %1$tA 缺曠紀錄",
					timeInMilliseconds);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		titleText.setText(title);
	}

	private void writerTask() {
		WriterThread writerThread = new WriterThread(writableWorkbook,
				writableSheet, classRecordList);
		writerThread.start();
	}

	@Override
	public void onResume() {
		super.onResume();

	}

	@Override
	public void onDestroy() {
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
		new AlertDialog.Builder(this).setTitle("提示")
				.setMessage("確定不儲存紀錄，直接離開?")
				.setPositiveButton("確定", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface arg0, int arg1) {
						try {
							if (writableWorkbook != null) {
								synchronized (writableWorkbook) {
									// All sheets and cells added. Now write out
									// the workbook
									writableWorkbook.write();
									writableWorkbook.close();
								}
							}
						} catch (WriteException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						}
						finish();
					}
				})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface arg0, int arg1) {
					}
				}).show();
	}

}
