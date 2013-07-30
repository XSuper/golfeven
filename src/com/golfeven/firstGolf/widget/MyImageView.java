package com.golfeven.firstGolf.widget;

import com.golfeven.firstGolf.R;
import com.golfeven.firstGolf.common.StringUtils;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MyImageView extends LinearLayout{
	private String describe="";
	
	private ImageView imageView;
	private TextView textView;
	

	/**
	 * 首页图片
	 * @param context
	 * @return
	 */
	public static MyImageView CreateHomeImageView(Context context){
		
		
		return new MyImageView(context);
	}
	
	
	public String getDescribe() {
		return describe;
	}
	/**
	 * 设置在图片复层展示的文字
	 * 
	 * @param describe
	 */
	public void setDescribe(String describe) {
		this.describe = describe;
		if(textView!= null){
			textView.setText(describe);
		}
		//invalidate();
	}
	
	


	

	public MyImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
		// TODO Auto-generated constructor stub
	}


	public MyImageView(Context context) {
		super(context);
		init(context);
		// TODO Auto-generated constructor stub
	}
	private void init(Context context) {
		// TODO Auto-generated method stub
		View view = LayoutInflater.from(context).inflate(R.layout.widget_myimage,this);
		imageView = (ImageView)view.findViewById(R.id.myimage_img);
		textView   =(TextView)view.findViewById(R.id.myimage_text);
		textView.setText(describe);

	}
	public ImageView getImageView(){
		return imageView;
	}


	
	

}
