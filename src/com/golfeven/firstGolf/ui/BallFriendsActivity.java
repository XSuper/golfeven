package com.golfeven.firstGolf.ui;

import net.tsz.afinal.annotation.view.ViewInject;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.golfeven.firstGolf.R;
import com.golfeven.firstGolf.adapter.BallFriendAttentionListAdapter;
import com.golfeven.firstGolf.adapter.BallFriendListAdapter;
import com.golfeven.firstGolf.base.BaseListActivity;
import com.golfeven.firstGolf.bean.BallFriend;
import com.golfeven.firstGolf.widget.HeadBack;
import com.golfeven.firstGolf.widget.PullToRefreshListView;


public class BallFriendsActivity extends BaseListActivity {
	@ViewInject(id=R.id.activity_ballfriend_list_spinner_age)Spinner age;  
	@ViewInject(id=R.id.activity_ballfriend_list_spinner_sex)Spinner sex;  

	private ArrayAdapter ageAdapter;
	private ArrayAdapter sexAdapter;

	/**
	 * 必须按照这个顺序排列
	 */
	@Override
	public void mustInit() {
		this.layoutResid = R.layout.activity_ballfriend_list;
		setContentView(layoutResid);
		this.context = BallFriendsActivity.this;
		this.entityClass = BallFriend.class ;
		this.adapter = new BallFriendAttentionListAdapter(context, datas);
		this.headBack = (HeadBack)findViewById(R.id.activity_ballfriend_list_headback);
		this.listView = (PullToRefreshListView)findViewById(R.id.activity_ballfriend_list);
		this.params.put("cmd","Member.getMember");
		if(appContext.isLogin){
			
			this.params.put("mid",appContext.user.getMid());
		}
		init();
	}
	private void init() {
		// TODO Auto-generated method stub
		ageAdapter = ArrayAdapter.createFromResource(appContext, R.array.spinner_age, R.layout.spinner_text);
		sexAdapter = ArrayAdapter.createFromResource(appContext, R.array.spinner_sex, R.layout.spinner_text);
		
		ageAdapter.setDropDownViewResource(R.layout.spinner_checkedtextview);
		sexAdapter.setDropDownViewResource(R.layout.spinner_checkedtextview);
		
		age.setAdapter(ageAdapter);
		sex.setAdapter(sexAdapter);
		OnItemSelectedListener onItemSelectedListener = new SpinnerListener();
		age.setOnItemSelectedListener(onItemSelectedListener);
		sex.setOnItemSelectedListener(onItemSelectedListener);
		
	}
	
	
	class SpinnerListener implements OnItemSelectedListener{

		@Override
		public void onItemSelected(AdapterView<?> parent, View view, int position,
				long id) {
			// TODO Auto-generated method stub
			
			if(parent ==age){
				String value = age.getItemAtPosition(position).toString();
				String ageStr = "选择年龄";
				if("选择年龄".equals(value)){
					ageStr = "选择年龄";
					params.remove("age");
				}else{
					params.put("age",value);
				}
			}
			if(parent ==sex){
				String value = sex.getItemAtPosition(position).toString();
				String sexStr = "全部";
				if("全部".equals(value)){
					sexStr = "全部";
					params.remove("sex");
				}
				if("男".equals(value)){
					sexStr = "男";
				}
				if("女".equals(value)){
					sexStr = "女";
				}
				if(!sexStr.equals("全部")){
					
					params.put("sex",sexStr);
				}
				
			}
			reset();
			listView.setTag(LOAD);
			requestData();
			
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		reset();
		listView.setTag(LOAD);
		requestData();
	}
	
	@Override
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		super.onNewIntent(intent);
		reset();
		listView.setTag(LOAD);
		requestData();
	}

}
