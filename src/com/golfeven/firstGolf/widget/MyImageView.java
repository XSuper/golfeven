package com.golfeven.firstGolf.widget;

import com.golfeven.firstGolf.R;
import com.golfeven.firstGolf.common.StringUtils;
import com.golfeven.firstGolf.common.Utils;
import com.golfeven.firstGolf.ui.MainActivity;
import com.tencent.mm.sdk.platformtools.Util;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

public class MyImageView extends LinearLayout{
	private String describe="";
	
	private ImageView imageView;
	private TextView textView;
	

	public boolean isFriend =true;
	
	/**
	 * 首页图片
	 * @param context
	 * @return
	 */
	public static MyImageView CreateHomeImageView(Context context,boolean isfriend){
		
		
		return new MyImageView(context,isfriend);
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
	public MyImageView(Context context,boolean isfriend) {
		super(context);
		this.isFriend = isfriend;
		init(context);
		// TODO Auto-generated constructor stub
	}
	private void init(Context context) {
		// TODO Auto-generated method stub
		View view = LayoutInflater.from(context).inflate(R.layout.widget_myimage,this);
		imageView = (ImageView)view.findViewById(R.id.myimage_img);
		int w  =Utils.getScreenWith((MainActivity)context);
		if(isFriend){
			LayoutParams params = new LayoutParams((int)(w/2.2/1.5),(int)(w/2.2/1.5));
			imageView.setLayoutParams(params);
		}else{
			LayoutParams params = new LayoutParams((int)(w/2.2),(int)(w/2.2/1.5));
			imageView.setLayoutParams(params);
			
		}
		textView   =(TextView)view.findViewById(R.id.myimage_text);
		textView.setText(describe);

	}
	public ImageView getImageView(){
		return imageView;
	}
	



	
	

}
