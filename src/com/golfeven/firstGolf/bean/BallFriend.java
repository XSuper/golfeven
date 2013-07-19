package com.golfeven.firstGolf.bean;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

import com.golfeven.firstGolf.base.BaseBean;

import net.tsz.afinal.annotation.sqlite.Id;

public class BallFriend extends BaseBean{
	@Id
	private String mid;
	private String mtype;
	private String userid;
	private String uname;
	private String sex;
	private String face;
	private String jointime;
	private String logintime;
	private String birthday;
	private String lovemsg;
	private String commplace;
	private String label;
	private String lat;
	private String lng;
	private String geohash;
	private String distance;
	private String isMyFriend;
	private String teamintroduce;

	public static final Parcelable.Creator<BallFriend> CREATOR = new Creator<BallFriend>(){

		@Override
		public BallFriend createFromParcel(Parcel source) {
			return new BallFriend(
					source.readString(), source.readString(),
					source.readString(), source.readString(),
					source.readString(), source.readString(),
					source.readString(), source.readString(),
					source.readString(), source.readString(),
					source.readString(), source.readString(),
					source.readString(), source.readString(),
					source.readString(), source.readString(), 
					source.readString(), source.readString()
					);
		}

		@Override
		public BallFriend[] newArray(int size) {
			// TODO Auto-generated method stub
			return new BallFriend[size];
		}
		
	};
	
	
	public BallFriend() {
		super();
		// TODO Auto-generated constructor stub
	}
	public BallFriend(String mid, String mtype, String userid, String uname,
			String sex, String face, String jointime, String logintime,
			String birthday, String lovemsg, String commplace, String label,
			String lat, String lng, String geohash, String distance,
			String isMyFriend, String teamintroduce) {
		super();
		this.mid = mid;
		this.mtype = mtype;
		this.userid = userid;
		this.uname = uname;
		this.sex = sex;
		this.face = face;
		this.jointime = jointime;
		this.logintime = logintime;
		this.birthday = birthday;
		this.lovemsg = lovemsg;
		this.commplace = commplace;
		this.label = label;
		this.lat = lat;
		this.lng = lng;
		this.geohash = geohash;
		this.distance = distance;
		this.isMyFriend = isMyFriend;
		this.teamintroduce = teamintroduce;
	}
	public String getIsMyFriend() {
		return isMyFriend;
	}
	public void setIsMyFriend(String isMyFriend) {
		this.isMyFriend = isMyFriend;
	}
	public String getMid() {
		return mid;
	}
	public void setMid(String mid) {
		this.mid = mid;
	}
	public String getMtype() {
		return mtype;
	}
	public void setMtype(String mtype) {
		this.mtype = mtype;
	}
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public String getUname() {
		return uname;
	}
	public void setUname(String uname) {
		this.uname = uname;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getFace() {
		return face;
	}
	public void setFace(String face) {
		this.face = face;
	}
	public String getJointime() {
		return jointime;
	}
	public void setJointime(String jointime) {
		this.jointime = jointime;
	}
	public String getLogintime() {
		return logintime;
	}
	public void setLogintime(String logintime) {
		this.logintime = logintime;
	}
	public String getBirthday() {
		return birthday;
	}
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	public String getLovemsg() {
		return lovemsg;
	}
	public void setLovemsg(String lovemsg) {
		this.lovemsg = lovemsg;
	}
	public String getCommplace() {
		return commplace;
	}
	public void setCommplace(String commplace) {
		this.commplace = commplace;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public String getLat() {
		return lat;
	}
	public void setLat(String lat) {
		this.lat = lat;
	}
	public String getLng() {
		return lng;
	}
	public void setLng(String lng) {
		this.lng = lng;
	}
	public String getGeohash() {
		return geohash;
	}
	public void setGeohash(String geohash) {
		this.geohash = geohash;
	}
	public String getDistance() {
		return distance;
	}
	public void setDistance(String distance) {
		this.distance = distance;
	}
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int arg1) {
		// TODO Auto-generated method stub
		dest.writeString(this.mid );
		dest.writeString(this.mtype);
		dest.writeString(this.userid );
		dest.writeString(this.uname);
		dest.writeString(this.sex );
		dest.writeString(this.face );
		dest.writeString(this.jointime);
		dest.writeString(this.logintime);
		dest.writeString(this.birthday );
		dest.writeString(this.lovemsg);
		dest.writeString(this.commplace);
		dest.writeString(this.label);
		dest.writeString(this.lat );
		dest.writeString(this.lng );
		dest.writeString(this.geohash );
		dest.writeString(this.distance );
		dest.writeString(this.isMyFriend);
		dest.writeString(this.teamintroduce );
		
	}
	

}
