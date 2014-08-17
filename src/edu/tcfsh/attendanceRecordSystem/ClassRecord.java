package edu.tcfsh.attendanceRecordSystem;
import android.util.Log;


public class ClassRecord {
	private int grade;
	private int classNum;
	private int outOfOfficeNum;
	private int sickDayNum;
	private int personalDayNum;
	private int unknownNum;
	private int arriveLateNum;

	public ClassRecord(int grade, int classNum){
		this.grade = grade;
		this.classNum = classNum;
		this.outOfOfficeNum = 0;
		this.sickDayNum = 0;
		this.personalDayNum = 0;
		this.unknownNum = 0;
		this.arriveLateNum = 0;

	}

	public String getRecord(){
		String record = String.format("%1$d%2$02d, %3$d, %4$d, %5$d, %6$d, %7$d\n",  grade,
				classNum,
				outOfOfficeNum,
				sickDayNum,
				personalDayNum,
				unknownNum,
				arriveLateNum );

		return record;
	}

	public int getGrade() {
		return grade;
	}

	public int getClassNum() {
		return classNum;
	}

	public int getOutOfOfficeNum() {
		return outOfOfficeNum;
	}
	public void setOutOfOfficeNum(int outOfOfficeNum) {
		this.outOfOfficeNum = outOfOfficeNum;
	}
	public int getSickDayNum() {
		return sickDayNum;
	}
	public void setSickDayNum(int sickDayNum) {
		this.sickDayNum = sickDayNum;
	}
	public int getPersonalDayNum() {
		return personalDayNum;
	}
	public void setPersonalDayNum(int personalDayNum) {
		this.personalDayNum = personalDayNum;
	}
	public int getUnknownNum() {
		return unknownNum;
	}
	public void setUnknownNum(int unknownNum) {
		this.unknownNum = unknownNum;
	}
	public int getArriveLateNum() {
		return arriveLateNum;
	}
	public void setArriveLateNum(int arriveLateNum) {
		this.arriveLateNum = arriveLateNum;
	}

}
