package com.golfeven.firstGolf.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.golfeven.firstGolf.base.BaseBean;

public class Gallery extends BaseBean {
	private String id;
	private String typeid;
	private String title;
	private String sortrank;
	private String click;
	private String shorttitle;
	private String source;
	private String litpic;
	private String description;
	public static final Parcelable.Creator<Gallery> CREATOR = new Creator<Gallery>() {

		@Override
		public Gallery createFromParcel(Parcel source) {
			// TODO Auto-generated method stub
			return new Gallery(source.readString(), source.readString(),
					source.readString(), source.readString(),
					source.readString(), source.readString(),
					source.readString(), source.readString(),
					source.readString());
		}

		@Override
		public Gallery[] newArray(int size) {
			// TODO Auto-generated method stub
			return new Gallery[size];
		}

	};

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.id);
		dest.writeString(this.typeid);
		dest.writeString(this.title);
		dest.writeString(this.sortrank);
		dest.writeString(this.click);
		dest.writeString(this.shorttitle);
		dest.writeString(this.source);
		dest.writeString(this.litpic);
		dest.writeString(this.description);

	}

	public Gallery(String id, String typeid, String title, String sortrank,
			String click, String shorttitle, String source, String litpic,
			String description) {
		super();
		this.id = id;
		this.typeid = typeid;
		this.title = title;
		this.sortrank = sortrank;
		this.click = click;
		this.shorttitle = shorttitle;
		this.source = source;
		this.litpic = litpic;
		this.description = description;
	}

	public Gallery() {
		super();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public String getTypeid() {
		return typeid;
	}

	public void setTypeid(String typeid) {
		this.typeid = typeid;
	}

	public String getSortrank() {
		return sortrank;
	}

	public void setSortrank(String sortrank) {
		this.sortrank = sortrank;
	}

	public String getShorttitle() {
		return shorttitle;
	}

	public void setShorttitle(String shorttitle) {
		this.shorttitle = shorttitle;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getLitpic() {
		return litpic;
	}

	public void setLitpic(String litpic) {
		this.litpic = litpic;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

}
