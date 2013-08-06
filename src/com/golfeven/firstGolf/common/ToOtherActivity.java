package com.golfeven.firstGolf.common;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;

/**
 *点击跳转页面
 * @author ISuper
 *
 */

public class ToOtherActivity implements OnClickListener{
	Activity activity;
	Class c;
	boolean f=false;
	Intent intent;
	

	public ToOtherActivity(Activity activity,Class c,boolean finish) {
		super();
		this.activity = activity;
		this.c = c;
		this.f = f;
		// TODO Auto-generated constructor stub
	}
	public ToOtherActivity(Activity activity,Class c) {
		super();
		this.activity = activity;
		this.c = c;
		// TODO Auto-generated constructor stub
	}
	public ToOtherActivity(Activity activity,Intent intent,boolean f) {
		super();
		this.activity = activity;
		this.c = c;
		this.f = f;
		this.intent = intent;
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onClick(View v) {
		if(intent == null){
			
			Utils.ToActivity(activity, c, f);
		}else {
			Utils.ToActivity(activity, intent, f);
		}
		
	}

}
