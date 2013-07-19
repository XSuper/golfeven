package com.golfeven.firstGolf.ui;

import net.tsz.afinal.annotation.view.ViewInject;
import android.R.color;
import android.graphics.Color;
import android.view.View;
import android.widget.Button;

import com.golfeven.firstGolf.R;
import com.golfeven.firstGolf.adapter.GolfInfoListAdapter;
import com.golfeven.firstGolf.base.BaseListActivity;
import com.golfeven.firstGolf.bean.GolfInfo;
import com.golfeven.firstGolf.widget.HeadBack;
import com.golfeven.firstGolf.widget.PullToRefreshListView;


public class GolfInfosActivity extends BaseListActivity {
	@ViewInject(id=R.id.activity_golfinfo_list_btn1,click="btnClick")
	View btn1;
	@ViewInject(id=R.id.activity_golfinfo_list_btn2,click="btnClick")
	View btn2;
	@ViewInject(id=R.id.activity_golfinfo_list_btn3,click="btnClick")
	View btn3;
	@ViewInject(id=R.id.activity_golfinfo_list_btn4,click="btnClick")
	View btn4;

	/**
	 * 必须按照这个顺序排列
	 */
	@Override
	public void mustInit() {
		this.layoutResid = R.layout.activity_golfinfo_list;
		setContentView(layoutResid);
		this.context = GolfInfosActivity.this;
		this.entityClass = GolfInfo.class ;
		this.adapter = new GolfInfoListAdapter(context, datas);
		this.headBack = (HeadBack)findViewById(R.id.activity_golfinfo_list_headback);
		this.listView = (PullToRefreshListView)findViewById(R.id.activity_golfinfo_list);
		this.params.put("cmd","golfInfo");
	}
	public void btnClick(View view){
		btn1.setBackgroundColor(Color.WHITE);
		btn2.setBackgroundColor(Color.WHITE);
		btn3.setBackgroundColor(Color.WHITE);
		btn4.setBackgroundColor(Color.WHITE);
		view.setBackgroundColor(Color.BLUE);
		String id ="137";
		if(view == btn1){
			id="137";
		}
		if(view == btn2){
			id="138";
		}
		if(view == btn3){
			id="139";
		}
		if(view == btn4){
			id="141";
		}
		((GolfInfoListAdapter)adapter).setTypeid(id);
		params.put("typeid",id);
		reset();
		listView.setTag(LOAD);
		requestData();
	}
	

}
