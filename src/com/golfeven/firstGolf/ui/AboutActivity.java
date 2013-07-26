package com.golfeven.firstGolf.ui;

import net.tsz.afinal.annotation.view.ViewInject;
import android.os.Bundle;
import android.webkit.WebView;

import com.golfeven.firstGolf.R;
import com.golfeven.firstGolf.base.BaseActivity;

public class AboutActivity extends BaseActivity {
	@ViewInject(id=R.id.activity_about_body) WebView body;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
		body.loadUrl("file:///android_asset/html/about.html");
	}

}
