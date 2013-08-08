package com.golfeven.firstGolf.bean;

import net.tsz.afinal.annotation.sqlite.Id;


public class XunsaiType {
	@Id
	 private int typeid;
	 private String typeName;
	 private int tag;//标示 是资讯的还是图集的  1 表示资讯,2表示图集
	 
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
	public int getTag() {
		return tag;
	}
	public void setTag(int tag) {
		this.tag = tag;
	}
	 
}