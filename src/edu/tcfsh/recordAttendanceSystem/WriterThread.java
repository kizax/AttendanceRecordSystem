package edu.tcfsh.recordAttendanceSystem;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import jxl.Cell;
import jxl.CellType;
import jxl.NumberCell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

public class WriterThread extends Thread {

	private WritableWorkbook writableWorkbook;
	private WritableSheet writableSheet;
	private ArrayList<ClassRecord> classRecordList;

	public WriterThread(WritableWorkbook writableWorkbook,
			WritableSheet writableSheet, ArrayList<ClassRecord> classRecordList) {
		this.writableWorkbook = writableWorkbook;
		this.writableSheet = writableSheet;
		this.classRecordList = classRecordList;
	}

	@Override
	public void run() {
		super.run();
		try {

			Label classLabel = new Label(0, 0, "班級");
			writableSheet.addCell(classLabel);

			Label outOfOfficeLabel = new Label(1, 0, "公假");
			writableSheet.addCell(outOfOfficeLabel);

			Label sickDayLabel = new Label(2, 0, "病假");
			writableSheet.addCell(sickDayLabel);

			Label personalDayLabel = new Label(3, 0, "事假");
			writableSheet.addCell(personalDayLabel);

			Label unknownLabel = new Label(4, 0, "不明");
			writableSheet.addCell(unknownLabel);

			Label arriveLateLabel = new Label(5, 0, "遲到");
			writableSheet.addCell(arriveLateLabel);

			for (int grade = 1; grade <= 3; grade++) {

				for (int classNum = 1; classNum <= 25; classNum++) {

					int index = (grade - 1) * 25 + classNum;

					String gradeAndClass = classRecordList.get(index - 1)
							.getGradeAndClass();
					Label gradeAndClassLabel = new Label(0, index,
							gradeAndClass);
					writableSheet.addCell(gradeAndClassLabel);

					int outOfOfficeNum = classRecordList.get(index - 1)
							.getOutOfOfficeNum();
					Number number = new Number(1, index, outOfOfficeNum);
					writableSheet.addCell(number);

					int sickDayNum = classRecordList.get(index - 1)
							.getSickDayNum();
					number = new Number(2, index, sickDayNum);
					writableSheet.addCell(number);

					int personalDayNum = classRecordList.get(index - 1)
							.getPersonalDayNum();
					number = new Number(3, index, personalDayNum);
					writableSheet.addCell(number);

					int unknownNum = classRecordList.get(index - 1)
							.getUnknownNum();
					number = new Number(4, index, unknownNum);
					writableSheet.addCell(number);

					int arriveLateNum = classRecordList.get(index - 1)
							.getArriveLateNum();
					number = new Number(5, index, arriveLateNum);
					writableSheet.addCell(number);

				}
			}

			if (writableWorkbook != null) {
				synchronized (writableWorkbook) {
					// All sheets and cells added. Now write out the workbook
					writableWorkbook.write();
					writableWorkbook.close();
				}
			}

		}  catch (IOException e) {
			System.out.println("Read failed");
		} catch (RowsExceededException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (WriteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
