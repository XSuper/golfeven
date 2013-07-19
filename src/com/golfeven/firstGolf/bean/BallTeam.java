package com.golfeven.firstGolf.bean;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

import com.golfeven.firstGolf.base.BaseBean;

public class BallTeam extends BaseBean {
	private String id;
	private String uid;
	private String mobile;
	private String isvisible;
	private String apply;
	private String introduce;
	private String nativeplace;
	private String logo;
	private String number;
	private String dateline;

	public static final Parcelable.Creator<BallTeam> CREATOR = new Creator<BallTeam>() {

		@Override
		public BallTeam createFromParcel(Parcel source) {
			// TODO Auto-generated method stub
			return new BallTeam(source.readString(), source.readString(),
					source.readString(), source.readString(),
					source.readString(), source.readString(),
					source.readString(), source.readString(),
					source.readString(), source.readString());
		}

		@Override
		public BallTeam[] newArray(int size) {
			// TODO Auto-generated method stub
			return new BallTeam[size];
		}

	};

	public BallTeam() {
		super();
		// TODO Auto-generated constructor stub
	}

	public BallTeam(String id, String uid, String mobile, String isvisible,
			String apply, String introduce, String nativeplace, String logo,
			String number, String dateline) {
		super();
		this.id = id;
		this.uid = uid;
		this.mobile = mobile;
		this.isvisible = isvisible;
		this.apply = apply;
		this.introduce = introduce;
		this.nativeplace = nativeplace;
		this.logo = logo;
		this.number = number;
		this.dateline = dateline;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.id);
		dest.writeString(this.uid);
		dest.writeString(this.mobile);
		dest.writeString(this.isvisible);
		dest.writeString(this.apply);
		dest.writeString(this.introduce);
		dest.writeString(this.nativeplace);
		dest.writeString(this.logo);
		dest.writeString(this.number);
		dest.writeString(this.dateline);

	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getIsvisible() {
		return isvisible;
	}

	public void setIsvisible(String isvisible) {
		this.isvisible = isvisible;
	}

	public String getApply() {
		return apply;
	}

	public void setApply(String apply) {
		this.apply = apply;
	}

	public String getIntroduce() {
		return introduce;
	}

	public void setIntroduce(String introduce) {
		this.introduce = introduce;
	}

	public String getNativeplace() {
		return nativeplace;
	}

	public void setNativeplace(String nativeplace) {
		this.nativeplace = nativeplace;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getDateline() {
		return dateline;
	}

	public void setDateline(String dateline) {
		this.dateline = dateline;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

}
