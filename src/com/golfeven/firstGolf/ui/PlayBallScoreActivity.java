package com.golfeven.firstGolf.ui;

import java.util.Date;
import java.util.List;

import net.tsz.afinal.annotation.view.ViewInject;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.golfeven.firstGolf.R;
import com.golfeven.firstGolf.adapter.ScoreAdapter;
import com.golfeven.firstGolf.base.BaseActivity;
import com.golfeven.firstGolf.base.MBaseAdapter;
import com.golfeven.firstGolf.bean.BallPark;
import com.golfeven.firstGolf.bean.Score;
import com.golfeven.firstGolf.common.StringUtils;
import com.golfeven.firstGolf.common.Utils;
import com.golfeven.firstGolf.widget.MyToast;

public class PlayBallScoreActivity extends BaseActivity {
	@ViewInject(id = R.id.activity_playball_scoring_title)
	private TextView title;
	@ViewInject(id = R.id.activity_playball_scoring_time, click = "showTime")
	private TextView time;
	@ViewInject(id = R.id.activity_playball_scoring_place)
	private TextView place;
	@ViewInject(id = R.id.activity_playball_scoring_list)
	private ListView list;


	private List<Score> datas;
	private Intent intent;
	
	private Date date;
	private int year;
	private int month;
	private int day;
	
	private BallPark ballpark;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_playball_scoring);
		ballpark = getIntent().getParcelableExtra("ballpark");
		initValues();

	}

	private void initValues() {
		// TODO Auto-generated method stub
		title.setText(ballpark.getTitle());
		place.setText(ballpark.getTypename());
		
		date =new Date(System.currentTimeMillis());
		year = date.getYear()+1900;
		month = date.getMonth();
		day = date.getDate();
		String timeStr = StringUtils.getDateStr(date);
		time.setText(timeStr);
		datas = Utils.getScores(getIntent().getStringExtra("hole"));

		MBaseAdapter adapter = new ScoreAdapter(PlayBallScoreActivity.this,
				datas);
		list.setAdapter(adapter);

	}

	public void showTime(View view) {
		MyToast.centerToast(appContext, year+"", Toast.LENGTH_LONG);
		DatePickerDialog dateDialog = new DatePickerDialog(PlayBallScoreActivity.this, new OnDateSetListener() {
			
			@Override
			public void onDateSet(DatePicker view, int myear, int monthOfYear,
					int dayOfMonth) {
				year = myear;
				month = monthOfYear+1;
				day = dayOfMonth;
				time.setText(year + "-" + (month>9?month:"0"+month) + "-" + (day>9?day:"0"+day));
				
			}
		}, year, month, day);
		dateDialog.show();
		
//		AlertDialog.Builder builder = new AlertDialog.Builder(PlayBallScoreActivity.this);
//		builder.setIcon(android.R.drawable.ic_dialog_info);
//		final DatePicker timeView = new DatePicker(PlayBallScoreActivity.this);
//		builder.setView(timeView);
//		builder.setTitle("请选择");
//		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//			public void onClick(DialogInterface dialog, int which) {
//				dialog.dismiss();
//				int year = timeView.getYear();
//				int month = timeView.getMonth();
//				int day = timeView.getDayOfMonth();
//				time.setText(year + "年" + month + "月" + day + "日");
//
//			}
//		});
//		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
//			public void onClick(DialogInterface dialog, int which) {
//				dialog.dismiss();
//			}
//		});
//		builder.show();
//
	}

}
