package com.golfeven.firstGolf.base;

import net.tsz.afinal.FinalActivity;

import com.golfeven.AppContext;
import com.golfeven.AppManager;

import android.app.Activity;
import android.os.Bundle;

/**
 * 应用程序Activity的基类
 * @author liux (http://my.oschina.net/liux)
 * @version 1.0
 * @created 2012-9-18
 */
public class BaseActivity extends FinalActivity{
	public AppContext appContext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		appContext = (AppContext)getApplicationContext();
		super.onCreate(savedInstanceState);
		
		//添加Activity到堆栈
		AppManager.getAppManager().addActivity(this);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		
		//结束Activity&从堆栈中移除
		AppManager.getAppManager().finishActivity(this);
	}
	
}
