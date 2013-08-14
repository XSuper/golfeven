package com.golfeven.firstGolf.base;

import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.FinalBitmap;

import com.golfeven.AppContext;
import com.golfeven.AppManager;
import com.golfeven.firstGolf.common.Constant;

import android.app.Activity;
import android.graphics.Bitmap.CompressFormat;
import android.os.Bundle;

/**
 * 应用程序Activity的基类
 * @author liux (http://my.oschina.net/liux)
 * @version 1.0
 * @created 2012-9-18
 */
public class BaseActivity extends FinalActivity{
	public AppContext appContext;
	public FinalBitmap fb;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		appContext = (AppContext)getApplicationContext();
		super.onCreate(savedInstanceState);
			if(fb==null){
				fb = FinalBitmap.createNew(this, Constant.IMG_CACHEPATH);
				fb.configLoadfailImage(Constant.IMG_LOADING_FAIL);
				fb.configLoadingImage(Constant.IMG_LOADING);
				fb.configBitmapLoadThreadSize(20);
				fb.configCompressFormat(CompressFormat.PNG);
				fb.configCalculateBitmapSizeWhenDecode(false);
				fb.setSquare(true);
			}
		//添加Activity到堆栈
		AppManager.getAppManager().addActivity(this);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		
		//结束Activity&从堆栈中移除
		AppManager.getAppManager().finishActivity(this);
		if(fb!= null){
			fb.clearMemoryCache();
			fb.clearMemoryCacheInBackgroud();
			//fb.onDestroy();
			fb= null;
		}
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if(fb!= null){
			fb.onResume();
		}
	}
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		if(fb!= null){
			fb.onPause();
		}
	}
}
