package com.golfeven.firstGolf.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;

import com.golfeven.firstGolf.R;
import com.golfeven.firstGolf.common.StringUtils;

public class MyImageView extends ImageView{
	private String describe="";
	

	/**
	 * 首页图片
	 * @param context
	 * @return
	 */
	public static MyImageView CreateHomeImageView(Context context){
		MyImageView img = new MyImageView(context);
		LayoutParams params = new LayoutParams(240,LayoutParams.MATCH_PARENT);
		//LayoutParams params = new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT,1);
		img.setLayoutParams(params);
		img.setImageResource(R.drawable.img_default);
		img.setMinimumWidth(240);
		img.setMinimumHeight(180);
		return img;
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
		//invalidate();
	}
	public MyImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}
	public MyImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	public MyImageView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		if(!StringUtils.isEmpty(describe)){
			
			Paint paint = new Paint();
			paint.setColor(Color.WHITE);
			paint.setAlpha(100);
			canvas.drawRect(0,getHeight()-30, getWidth(), getHeight(), paint);
			paint.setColor(Color.WHITE);
			paint.setTextSize(18);
			canvas.drawText(describe,20,this.getHeight()-10, paint);
		}
	}
	
	

}
