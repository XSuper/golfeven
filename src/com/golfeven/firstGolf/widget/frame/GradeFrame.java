package com.golfeven.firstGolf.widget.frame;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.golfeven.firstGolf.R;
import com.golfeven.firstGolf.widget.HomeNavigation;

public class GradeFrame extends LinearLayout{
	

	public GradeFrame(Context context, AttributeSet attrs) {
		super(context, attrs);
		initViews(context);
	}


	private void initViews(Context context) {
		View view = LayoutInflater.from(context).inflate(R.layout.frame_grade, this);
		
	}
	

}
