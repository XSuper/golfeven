package com.golfeven.firstGolf.bean;

import net.tsz.afinal.annotation.sqlite.Id;


public class XunsaiType {
	@Id
	 private int typeid;
	 private String typeName;
	public int getTypeid() {
		return typeid;
	}
	public void setTypeid(int typeid) {
		this.typeid = typeid;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	 @Override
	public String toString() {
		// TODO Auto-generated method stub
		return typeName;
	}
}