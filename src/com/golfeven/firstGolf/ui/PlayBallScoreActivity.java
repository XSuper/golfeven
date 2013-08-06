package com.golfeven.firstGolf.ui;

import java.util.Date;
import java.util.List;

import net.tsz.afinal.annotation.view.ViewInject;
import net.tsz.afinal.http.AjaxCallBack;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.golfeven.AppManager;
import com.golfeven.firstGolf.R;
import com.golfeven.firstGolf.adapter.ScoreAdapter;
import com.golfeven.firstGolf.api.Api;
import com.golfeven.firstGolf.base.BaseActivity;
import com.golfeven.firstGolf.base.MBaseAdapter;
import com.golfeven.firstGolf.bean.BallPark;
import com.golfeven.firstGolf.bean.Score;
import com.golfeven.firstGolf.bean.WrongResponse;
import com.golfeven.firstGolf.common.Constant;
import com.golfeven.firstGolf.common.MyLog;
import com.golfeven.firstGolf.common.NetUtil;
import com.golfeven.firstGolf.common.StringUtils;
import com.golfeven.firstGolf.common.Utils;
import com.golfeven.firstGolf.common.ValidateUtil;
import com.golfeven.firstGolf.widget.HeadBack;
import com.golfeven.firstGolf.widget.MyToast;

public class PlayBallScoreActivity extends BaseActivity {

	@ViewInject(id = R.id.activity_playball_scoring_headback)
	private HeadBack mHeadBack;
	@ViewInject(id = R.id.activity_playball_scoring_title)
	private TextView title;
	@ViewInject(id = R.id.activity_playball_scoring_time, click = "showTime")
	private TextView time;
	@ViewInject(id = R.id.activity_playball_scoring_img)
	private ImageView img;

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
		appContext.getFB().display(img,
				Constant.URL_IMG_BASE + ballpark.getLitpic());

		date = new Date(System.currentTimeMillis());
		year = date.getYear() + 1900;
		month = date.getMonth();
		day = date.getDate();
		String timeStr = StringUtils.getDateStr(date);
		time.setText(timeStr);
		datas = Utils.getScores(getIntent().getStringExtra("hole"));

		MBaseAdapter adapter = new ScoreAdapter(PlayBallScoreActivity.this,
				datas);
		list.setAdapter(adapter);
		mHeadBack.setRbtn2ClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(!appContext.isLogin&&appContext.user==null){
					MyToast.centerToast(appContext, "请先登录后提交成绩", Toast.LENGTH_SHORT);
					return;
				}
				if (!Utils.scoreOk(datas)) {
					MyToast.centerToast(appContext, "请输入全部成绩",
							Toast.LENGTH_SHORT);
					return;
				}
				if (appContext.isLogin) {

					Api.getInstance().uploadScore(appContext.user,
							time.getText().toString(), ballpark.getId(), datas,
							new AjaxCallBack<String>() {
								ProgressDialog dialog;

								@Override
								public void onStart() {
									// TODO Auto-generated method stub
									super.onStart();
									dialog = Utils.initWaitingDialog(
											PlayBallScoreActivity.this,
											"正在上传成绩");
								}

								@Override
								public void onSuccess(String t) {
									// TODO Auto-generated method stub
									super.onSuccess(t);
									dialog.dismiss();
									WrongResponse response = null;

									try {
										response = JSON.parseObject(t,
												WrongResponse.class);
									} catch (Exception e) {
										// TODO: handle exception
									}
									if (response != null && response.code == 0) {

										MyToast.customToast(
												PlayBallScoreActivity.this,
												Toast.LENGTH_SHORT,
												MyToast.TOAST_MSG_SUCCESS_TITLE,
												"成绩提交成功",
												Constant.TOAST_IMG_SUCCESS);
										Api.getInstance().addCredits(PlayBallScoreActivity.this, appContext.user, 3, "首次上传成绩,");
										finish();
										MainActivity m = (MainActivity)AppManager.getAppManager().getActivity(MainActivity.class);
										m.mScrollLayout.scrollToScreen(3);

									} else {
										WrongResponse wrongResponse = ValidateUtil
												.wrongResponse(t);
										if (wrongResponse.show) {
											MyToast.customToast(
													PlayBallScoreActivity.this,
													Toast.LENGTH_SHORT,
													MyToast.TOAST_MSG_ERROR_TITLE,
													wrongResponse.msg,
													Constant.TOAST_IMG_ERROR);
											
										} else {
											MyToast.customToast(
													PlayBallScoreActivity.this,
													Toast.LENGTH_SHORT,
													MyToast.TOAST_MSG_ERROR_TITLE,
													"成绩提交失败",
													Constant.TOAST_IMG_ERROR);
											MyLog.v("成绩提交失败", wrongResponse.msg);
										}
										
									}
								}

								@Override
								public void onFailure(Throwable t, String strMsg) {
									// TODO Auto-generated method stub
									super.onFailure(t, strMsg);
									dialog.dismiss();
									NetUtil.requestError(appContext, null);
								}

							});
				}

			}
		});

	}

	public void showTime(View view) {
		DatePickerDialog dateDialog = new DatePickerDialog(
				PlayBallScoreActivity.this, new OnDateSetListener() {

					@Override
					public void onDateSet(DatePicker view, int myear,
							int monthOfYear, int dayOfMonth) {
						year = myear;
						month = monthOfYear + 1;
						day = dayOfMonth;
						time.setText(year + "-"
								+ (month > 9 ? month : "0" + month) + "-"
								+ (day > 9 ? day : "0" + day));

					}
				}, year, month, day);
		dateDialog.show();

		// AlertDialog.Builder builder = new
		// AlertDialog.Builder(PlayBallScoreActivity.this);
		// builder.setIcon(android.R.drawable.ic_dialog_info);
		// final DatePicker timeView = new
		// DatePicker(PlayBallScoreActivity.this);
		// builder.setView(timeView);
		// builder.setTitle("请选择");
		// builder.setPositiveButton("确定", new DialogInterface.OnClickListener()
		// {
		// public void onClick(DialogInterface dialog, int which) {
		// dialog.dismiss();
		// int year = timeView.getYear();
		// int month = timeView.getMonth();
		// int day = timeView.getDayOfMonth();
		// time.setText(year + "年" + month + "月" + day + "日");
		// }
		// });
		// builder.setNegativeButton("取消", new DialogInterface.OnClickListener()
		// {
		// public void onClick(DialogInterface dialog, int which) {
		// dialog.dismiss();
		// }
		// });
		// builder.show();
		//
	}

}
