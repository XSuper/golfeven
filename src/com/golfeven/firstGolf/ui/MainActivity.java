package com.golfeven.firstGolf.ui;

import java.io.IOException;
import java.io.InputStream;

import net.tsz.afinal.annotation.view.ViewInject;
import net.tsz.afinal.http.AjaxCallBack;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.golfeven.AppContext;
import com.golfeven.AppManager;
import com.golfeven.firstGolf.R;
import com.golfeven.firstGolf.api.Api;
import com.golfeven.firstGolf.base.BaseActivity;
import com.golfeven.firstGolf.bean.BallPark;
import com.golfeven.firstGolf.bean.User;
import com.golfeven.firstGolf.common.MyLog;
import com.golfeven.firstGolf.common.StringUtils;
import com.golfeven.firstGolf.common.Utils;
import com.golfeven.firstGolf.widget.MyToast;
import com.golfeven.firstGolf.widget.ScrollLayout;
import com.golfeven.firstGolf.widget.ScrollLayout.OnViewChangeListener;
import com.golfeven.firstGolf.widget.frame.AttentitonFrame;
import com.golfeven.firstGolf.widget.frame.GradeFrame;
import com.golfeven.firstGolf.widget.frame.HomeFrame;
import com.golfeven.firstGolf.widget.frame.PlayBallFrame;
import com.golfeven.firstGolf.widget.frame.SettingFrame;
import com.golfeven.weather.WeartherActivity;
import com.golfeven.xmpp.service.XmppService;
import com.golfeven.xmpp.xmppmanager.XmppUtils;

public class MainActivity extends BaseActivity {

	private static MainActivity mainActivity;
	public static final int PLAYBALLFRAMEGETBALLPARKCODE = 0;// 打球页面的得到球场码

	@ViewInject(id = R.id.main_weather_head)
	FrameLayout weatherHead;

	@ViewInject(id = R.id.head_weather_title)
	TextView weatherHeadTitle;
	@ViewInject(id = R.id.head_weather_place)
	TextView weatherHeadPlace;
	@ViewInject(id = R.id.head_weather_info, click = "toWeatherInfo")
	ImageView weatherHeadInfo;

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

	@ViewInject(id = R.id.head_weather_progress)
	public ProgressBar mprogressBar;
	// ******************页面初始化end

	int index = 0;
	int current = 0;

	/**
	 * 滑动屏幕时候的响应事件
	 * 
	 * @author Administrator
	 * 
	 */
	class mViewChangeListener implements OnViewChangeListener {

		@Override
		public void OnViewChange(int view) {
//			if(view == current){
//				return;
//			}
//			current=view;
			setProgressDisplay(false);
			index = view;
			((RadioButton) findViewById(radioGroup.getCheckedRadioButtonId()))
					.setChecked(false);
			mRbtns[view].setChecked(true);
			//showLogon = view;
		//	Toast.makeText(appContext, new Random().nextInt()+"", Toast.LENGTH_SHORT).show();
			myResume(view);

		}

	}

	/**
	 * 初始化组件
	 */
	private void initCommon() {
		mRbtns = new RadioButton[] { rbtn_home, rbtn_playBall, rbtn_attention,
				rbtn_grade, rbtn_setting };
		mScrollLayout.setIsScroll(appContext.isScroll);
	}

	
	public static MainActivity getMainActivity(){
		return mainActivity;
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

	private void loginAndUpdatePlace() {
		if(StringUtils.isEmpty(appContext.uname)||StringUtils.isEmpty(appContext.upass)){
			return;
		}
		
		Api.getInstance().login(appContext.uname, appContext.upass, new AjaxCallBack<String>() {

			@Override
			public void onSuccess(String t) {
				// TODO Auto-generated method stub
				super.onSuccess(t);
				if(t.trim().startsWith("{")){
					appContext.user = JSON.parseObject(t, User.class);
					appContext.isLogin = true;
					
					Api.getInstance().addCredits(appContext, appContext.user, 0, "完成每天登录,");
					if(!StringUtils.isEmpty(appContext.longitude)||!StringUtils.isEmpty(appContext.latitude)){
						Api.getInstance().updatePlace(appContext.user, appContext.longitude, appContext.latitude);
					}
//					Map<String, String> user = new HashMap<String, String>();
//					user.put(Constant.FILE_USER_USERID, uname);
//					user.put(Constant.FILE_USER_UPASS,upass);
//					SharedPreferencesUtil.keep(Constant.FILE_USER,
//							getApplicationContext(), user);
				}
			}

			@Override
			public void onFailure(Throwable t, String strMsg) {
				// TODO Auto-generated method stub
				super.onFailure(t, strMsg);
				MyToast.centerToast(getApplicationContext(),"自动登陆失败",Toast.LENGTH_SHORT);
			}
			
		});

	}
	
	

	public  Handler loginHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case XmppUtils.LOGIN_ERROR:
				MyToast.centerToast(MainActivity.this, "登录失败",Toast.LENGTH_SHORT);
				break;
			case XmppUtils.LOGIN_ERROR_NET:
				MyToast.centerToast(MainActivity.this, "连接服务器失败",Toast.LENGTH_SHORT);
				break;
			case XmppUtils.LOGIN_ERROR_PWD:
				MyToast.centerToast(MainActivity.this, "密码错误",Toast.LENGTH_SHORT);
				break;
			case XmppUtils.LOGIN_ERROR_REPEAT:
				MyToast.centerToast(MainActivity.this, "重复登录",Toast.LENGTH_SHORT);
				break;
			case 200:
				MyToast.centerToast(MainActivity.this, "登录成功",Toast.LENGTH_SHORT);
				Intent intentService = new Intent(MainActivity.this,XmppService.class);
				startService(intentService);
				
				new Thread(){
					public void run() {
						try {
							
							XmppUtils.getInstance().sendOnLine();
						} catch (Exception e) {
							// TODO: handle exception
							MyLog.e("在线",e.toString());
						}
					};
				}.start();
				
//				FriendInfo info = new FriendInfo();
//				info.setUsername("11");
//				info.setNickname("110");
//				Intent intent = new Intent(this,Chat.class);
//				intent.putExtra("info", info);
//				startActivity(intent);
				
//				break;
//
//			case DIALOG_SHOW:
//				dialog = new ProgressDialog(XMPPActivity.this);
//				dialog.setMessage("Loding");
//				dialog.show();
//				break;
//			case DIALOG_CANCLE:
//				if(dialog != null){
//					dialog.dismiss();
//					dialog = null;
//				}
//				break;
			}
		};
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Intent intent = new Intent(appContext, mWelcomeActivity.class);
		// startActivity(intent);
		mainActivity = this;
		
		setContentView(R.layout.activity_main);
		loginAndUpdatePlace();
		fb.configBitmapLoadThreadSize(60);
		fb.configMemoryCacheSize(200);

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

	int showLogon = -1;

	private void myResume(int index) {

		switch (index) {
		case 0:
			changeTitle(true, "第一高尔夫", true);
			break;
		case 1:
			changeTitle(false, "打球", true);
			break;
		case 2:
			changeTitle(false, "关注", false);
			((AttentitonFrame) viewAttention).onResume();
			if (!appContext.isLogin && showLogon != 2) {

				Utils.toLogin(MainActivity.this);
				showLogon = 2;
			}
			break;
		case 3:
			changeTitle(false, "成绩", false);
			((GradeFrame) viewGrade).onResume();
			if (!appContext.isLogin && showLogon != 3) {

				Utils.toLogin(MainActivity.this);
				showLogon = 3;
			}
			break;
		case 4:
			changeTitle(false, "系统设置", false);
			((SettingFrame) viewSetting).onResume();

			break;

		}
		showLogon = index;
	}

	// 初始化天气头部
	static String code = null;

	private void initWeatherHead() {

		weatherHeadPlace.setText(this.appContext.city);
		if (code != null && !code.equals(appContext.weatherInfo)) {

			InputStream in = null;
			try {
				in = getResources().getAssets().open(
						"weather/b" + appContext.weatherInfo + ".gif");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Bitmap bm = BitmapFactory.decodeStream(in);
			BitmapDrawable bitmapDrawable = (BitmapDrawable) weatherHeadInfo
					.getDrawable();
			// 如果图片还未回收，先强制回收该图片
			if (bitmapDrawable != null
					&& !bitmapDrawable.getBitmap().isRecycled()) {
				bitmapDrawable.getBitmap().recycle();
			}
			weatherHeadInfo.setImageBitmap(bm);
		}
		code = appContext.weatherInfo;

	}

	// 跳转到天气页面
	public void toWeatherInfo(View view) {
		Intent intent = new Intent(MainActivity.this, WeartherActivity.class);
		startActivity(intent);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			AlertDialog.Builder builder = new AlertDialog.Builder(
					MainActivity.this);
			builder.setIcon(android.R.drawable.ic_dialog_info);
			builder.setTitle("确定退出程序吗？");
			builder.setPositiveButton("确定",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
							AppManager.getAppManager().AppExit(appContext);
							//关闭xmpp 链接
							//XmppUtils.getInstance().closeConn();
							// 退出
							Intent service = new Intent(MainActivity.this,XmppService.class);
							stopService(service);

						}
					});
			builder.setNegativeButton("取消",
					new DialogInterface.OnClickListener() {
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
		((HomeFrame) viewHome).onDestroy();
		XmppUtils.getInstance().closeConn();
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		((HomeFrame) viewHome).onStart();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		((HomeFrame) viewHome).onStop();
		super.onStop();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		((HomeFrame) viewHome).onStop();
		super.onPause();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (data == null) {
			return;
		}
		if (requestCode == resultCode
				&& resultCode == PLAYBALLFRAMEGETBALLPARKCODE) {
			BallPark mBallPark = data.getParcelableExtra("ballpark");
			if (mBallPark != null) {

				((PlayBallFrame) viewPlayBall).setBallPark(mBallPark);
			}
		}
	}

	/**
	 * 设置主页的progressbar 的显示情况
	 * 
	 * @param isDisplay
	 */
	public void setProgressDisplay(boolean isDisplay) {
		if (mprogressBar == null) {
			return;
		}
		mprogressBar.setVisibility(isDisplay ? View.VISIBLE : View.INVISIBLE);
	}

	private void changeTitle(boolean place, String title, boolean weather) {
		weatherHeadPlace.setVisibility(place ? View.VISIBLE : View.INVISIBLE);
		weatherHeadInfo.setVisibility(weather ? View.VISIBLE : View.INVISIBLE);
		weatherHeadTitle.setText(title);
	}

	
}
