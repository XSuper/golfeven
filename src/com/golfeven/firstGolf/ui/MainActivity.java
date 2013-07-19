package com.golfeven.firstGolf.ui;

import net.tsz.afinal.annotation.view.ViewInject;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.golfeven.AppManager;
import com.golfeven.firstGolf.R;
import com.golfeven.firstGolf.base.BaseActivity;
import com.golfeven.firstGolf.bean.BallPark;
import com.golfeven.firstGolf.widget.ScrollLayout;
import com.golfeven.firstGolf.widget.ScrollLayout.OnViewChangeListener;
import com.golfeven.firstGolf.widget.frame.HomeFrame;
import com.golfeven.firstGolf.widget.frame.PlayBallFrame;
import com.golfeven.firstGolf.widget.frame.SettingFrame;
import com.golfeven.weather.WeartherActivity;

public class MainActivity extends BaseActivity {
	
	public static final int PLAYBALLFRAMEGETBALLPARKCODE=0;//打球页面的得到球场码
	
	
	
	@ViewInject(id = R.id.main_weather_head, click = "toWeatherInfo")
	LinearLayout weatherHead;
	
	@ViewInject(id = R.id.head_weather_title)
	TextView weatherHeadTitle;
	@ViewInject(id = R.id.head_weather_place)
	TextView weatherHeadPlace;
	@ViewInject(id = R.id.head_weather_info)
	TextView weatherHeadInfo;
	@ViewInject(id = R.id.head_weather_progress)
	ProgressBar mprogressBar;

	// ******************************底部按钮start
	@ViewInject(id = R.id.main_footbar_radiogroup)
	RadioGroup radioGroup;
	@ViewInject(id = R.id.main_footbar_home)
	RadioButton rbtn_home;
	@ViewInject(id = R.id.main_footbar_playball)                                                                            
	RadioButton rbtn_playBall;
	@ViewInject(id = R.id.main_footbar_attention)
	RadioButton rbtn_attention;
	@ViewInject(id = R.id.main_footbar_grade)
	RadioButton rbtn_grade;
	@ViewInject(id = R.id.main_footbar_setting)
	RadioButton rbtn_setting;

	RadioButton[] mRbtns;
	// int radioIdS[]; //
	// ******************************底部按钮end

	// 页面初始化
	@ViewInject(id = R.id.main_scroll)
	public ScrollLayout mScrollLayout;// 页面滑动控制器

	@ViewInject(id = R.id.frame_home)
	View viewHome;
	@ViewInject(id = R.id.frame_playball)
	View viewPlayBall;
	@ViewInject(id = R.id.frame_grade)
	View viewGrade;
	@ViewInject(id = R.id.frame_setting)
	View viewSetting;
	@ViewInject(id = R.id.frame_attention)
	View viewAttention;

	// ******************页面初始化end

	int index = 0;
	/**
	 * 滑动屏幕时候的响应事件
	 * @author Administrator
	 *
	 */
	class mViewChangeListener implements OnViewChangeListener {

		@Override
		public void OnViewChange(int view) {
			index = view;
			((RadioButton) findViewById(radioGroup.getCheckedRadioButtonId()))
					.setChecked(false);
			mRbtns[view].setChecked(true);
			myResume(view);
			

		}

	}

	/**
	 * 初始化组件
	 */
	private void initCommon() {
		mRbtns =new RadioButton[]{rbtn_home,rbtn_playBall,rbtn_attention,rbtn_grade,rbtn_setting};
		mScrollLayout.setIsScroll(appContext.isScroll);
	}

	/**
	 * 下方导航按钮的点击事件
	 * 
	 * @author Administrator
	 * 
	 */
	class RadioGroupListener implements OnCheckedChangeListener {

		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			// TODO Auto-generated method stub
			switch (checkedId) {

			case R.id.main_footbar_home:
				mScrollLayout.scrollToScreen(0);
				break;
			case R.id.main_footbar_playball:
				mScrollLayout.scrollToScreen(1);
				break;
			case R.id.main_footbar_attention:
				mScrollLayout.scrollToScreen(2);
				break;
			case R.id.main_footbar_grade:
				mScrollLayout.scrollToScreen(3);
				break;
			case R.id.main_footbar_setting:
				mScrollLayout.scrollToScreen(4);
				
				break;

			}
		}

	}
	private void initFooter() {
		radioGroup.setOnCheckedChangeListener(new RadioGroupListener());
		mScrollLayout.SetOnViewChangeListener(new mViewChangeListener());
		
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		Intent intent = new Intent(appContext, mWelcomeActivity.class);
//		startActivity(intent);
		setContentView(R.layout.activity_main);
		initCommon();
		initWeatherHead();// 更新头部天气
		initFooter();

		
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		initWeatherHead();// 更新头部天气，更新地址信息
		myResume(index);
	}
	private void myResume(int index) {
		switch (index) {
		case 0:
			changeTitle(true, "第一高尔夫",true);
			break;
		case 1:
			changeTitle(false, "打球",true);
			break;
		case 2:
			changeTitle(false, "成绩",false);
			break;
		case 3:
			changeTitle(false, "关注",false);
			break;
		case 4:
			changeTitle(false, "系统设置",false);
			((SettingFrame)viewSetting).onResume();
			
			break;

		}
	}



	// 初始化天气头部
	private void initWeatherHead() {
		weatherHeadPlace.setText(this.appContext.city);
		weatherHeadInfo.setText(appContext.weatherInfo);
	}

	// 跳转到天气页面
	public void toWeatherInfo(View view) {
		Intent intent = new Intent(MainActivity.this, WeartherActivity.class);
		startActivity(intent);
	}

	

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode == KeyEvent.KEYCODE_BACK){
			AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
			builder.setIcon(android.R.drawable.ic_dialog_info);
			builder.setTitle("确定退出程序吗？");
			builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
					AppManager.getAppManager().AppExit(appContext);
					//退出
					
				}
			});
			builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});
			builder.show();
		}
		return super.onKeyDown(keyCode, event);
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		appContext.saveInfo();
		((HomeFrame)viewHome).onDestroy();
	}
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		((HomeFrame)viewHome).onStart();
	}
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		((HomeFrame)viewHome).onStop();
		super.onStop();
	}
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		((HomeFrame)viewHome).onStop();
		super.onPause();
	}
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(data == null){
			return;
		}
		if(requestCode==resultCode&&resultCode==PLAYBALLFRAMEGETBALLPARKCODE){
			BallPark mBallPark = data.getParcelableExtra("ballpark");
			if(mBallPark!=null){
				
				((PlayBallFrame)viewPlayBall).setBallPark(mBallPark);
			}
		}
	}
	
	
	
	
	/**
	 * 设置主页的progressbar 的显示情况
	 * 
	 * @param isDisplay
	 */
	public void setProgressDisplay(boolean isDisplay){
		mprogressBar.setVisibility(isDisplay?View.VISIBLE:View.INVISIBLE);
	}
	
	private void changeTitle(boolean place,String title,boolean weather){
		weatherHeadPlace.setVisibility(place?View.VISIBLE:View.INVISIBLE);
		weatherHeadInfo.setVisibility(weather?View.VISIBLE:View.INVISIBLE);
		weatherHeadTitle.setText(title);
	}
}
