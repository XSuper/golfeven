package com.golfeven.firstGolf.ui;

import net.tsz.afinal.annotation.view.ViewInject;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.golfeven.firstGolf.R;
import com.golfeven.firstGolf.base.BaseActivity;
import com.golfeven.firstGolf.bean.BallTeam;
import com.golfeven.firstGolf.widget.HeadBack;

public class BallTeamDetailActivity extends BaseActivity { 
	private @ViewInject(id=R.id.activity_ballteam_detail_headback) HeadBack headBack;
	private @ViewInject(id=R.id.activity_ballteam_detail_title)TextView title;
	private @ViewInject(id=R.id.activity_ballteam_detail_introduce)TextView introduce;
	private @ViewInject(id=R.id.activity_ballteam_detail_join)Button joinBtn;
	private BallTeam ballTeam;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ballteam_detail);
		
		Intent intent = getIntent();
		ballTeam = intent.getParcelableExtra("ballTeam");
		setValues();
	}
	private void setValues() {
		title.setText(ballTeam.getIntroduce());
		introduce.setText(ballTeam.getNativeplace());

	}

}
