package com.golfeven.firstGolf.bean;

import java.util.List;

public class Grade {

	private String id;
	private String gname;
	private String mid;
	private String markdate;
	private String objid;
	private String tee;
	private String totalpole;
	private String totalputter;
	private List<GradeOne> grade;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getGname() {
		return gname;
	}
	public void setGname(String gname) {
		this.gname = gname;
	}
	public String getMid() {
		return mid;
	}
	public void setMid(String mid) {
		this.mid = mid;
	}
	public String getMarkdate() {
		return markdate;
	}
	public void setMarkdate(String markdate) {
		this.markdate = markdate;
	}
	public String getObjid() {
		return objid;
	}
	public void setObjid(String objid) {
		this.objid = objid;
	}
	public String getTee() {
		return tee;
	}
	public void setTee(String tee) {
		this.tee = tee;
	}
	public String getTotalpole() {
		return totalpole;
	}
	public void setTotalpole(String totalpole) {
		this.totalpole = totalpole;
	}
	public String getTotalputter() {
		return totalputter;
	}
	public void setTotalputter(String totalputter) {
		this.totalputter = totalputter;
	}
	public List<GradeOne> getGrade() {
		return grade;
	}
	public void setGrade(List<GradeOne> grade) {
		this.grade = grade;
	}
	
}
