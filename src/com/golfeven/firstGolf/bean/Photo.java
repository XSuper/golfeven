package com.golfeven.firstGolf.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.golfeven.firstGolf.base.BaseBean;

public class Photo extends BaseBean {
	private String id;
	private String pic;
	private String file;//上传文件时候储存返回字段file
	
	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}

	public static final Parcelable.Creator<Photo> CREATOR = new Creator<Photo>() {

		@Override
		public Photo createFromParcel(Parcel source) {
			return new Photo(source.readString(), source.readString());
		}

		@Override
		public Photo[] newArray(int size) {
			// TODO Auto-generated method stub
			return new Photo[size];
		}

	};

	public Photo(String id, String pic) {
		super();
		this.id = id;
		this.pic = pic;
	}

	public Photo() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPic() {
		return pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel arg0, int arg1) {
		// TODO Auto-generated method stub
		arg0.writeString(id);
		arg0.writeString(pic);

	}

}
