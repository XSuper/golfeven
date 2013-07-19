package com.golfeven.firstGolf.bean;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

import com.golfeven.firstGolf.base.BaseBean;

public class GolfInfo extends BaseBean{
	private String id;
	private String typename;
	private String title;
	private String description;
	private String click;
	private String pubdate;
	private String litpic;
	
	
	
	public GolfInfo() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public GolfInfo(String id, String typename, String title,
			String description, String click, String pubdate, String litpic) {
		super();
		this.id = id;
		this.typename = typename;
		this.title = title;
		this.description = description;
		this.click = click;
		this.pubdate = pubdate;
		this.litpic = litpic;
	}

	public static final Parcelable.Creator<GolfInfo> CREATOR = new Creator<GolfInfo>() {

		@Override
		public GolfInfo createFromParcel(Parcel source) {
			// TODO Auto-generated method stub
			return new GolfInfo(source.readString(), source.readString(),
					source.readString(), source.readString(),
					source.readString(), source.readString(),
					source.readString());
		}

		@Override
		public GolfInfo[] newArray(int size) {
			// TODO Auto-generated method stub
			return new GolfInfo[size];
		}

	};
	
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.id);
		dest.writeString(this.typename);
		dest.writeString(this.title);
		dest.writeString(this.description);
		dest.writeString(this.click);
		dest.writeString(this.pubdate);
		dest.writeString(this.litpic);
		
	}
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	
	
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTypename() {
		return typename;
	}
	public void setTypename(String typename) {
		this.typename = typename;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getClick() {
		return click;
	}
	public void setClick(String click) {
		this.click = click;
	}
	public String getPubdate() {
		return pubdate;
	}
	public void setPubdate(String pubdate) {
		this.pubdate = pubdate;
	}
	public String getLitpic() {
		return litpic;
	}
	public void setLitpic(String litpic) {
		this.litpic = litpic;
	}
	
	

}
