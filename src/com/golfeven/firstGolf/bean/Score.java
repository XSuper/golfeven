package com.golfeven.firstGolf.bean;

/**
 *	计分卡的计分实体 
 * @author ISuper
 *
 */
public class Score {
	private int id ;
	private String totleCount="";
	private String tuiCount="";
	
	public Score() {
		super();
	}
	public Score(int id) {
		super();
		this.id = id;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTotleCount() {
		return totleCount;
	}
	public void setTotleCount(String totleCount) {
		this.totleCount = totleCount;
	}
	public String getTuiCount() {
		return tuiCount;
	}
	public void setTuiCount(String tuiCount) {
		this.tuiCount = tuiCount;
	}
	

}
