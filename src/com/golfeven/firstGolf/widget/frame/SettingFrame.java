package com.golfeven.firstGolf.widget.frame;

import java.io.File;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.golfeven.AppContext;
import com.golfeven.firstGolf.R;
import com.golfeven.firstGolf.common.Constant;
import com.golfeven.firstGolf.common.FileUtils;
import com.golfeven.firstGolf.common.SharedPreferencesUtil;
import com.golfeven.firstGolf.common.ToOtherActivity;
import com.golfeven.firstGolf.common.Utils;
import com.golfeven.firstGolf.ui.AboutActivity;
import com.golfeven.firstGolf.ui.IntegralActivity;
import com.golfeven.firstGolf.ui.LoginActivity;
import com.golfeven.firstGolf.ui.MainActivity;
import com.golfeven.firstGolf.ui.MyDetailActivity;
import com.golfeven.firstGolf.widget.MyToast;

public class SettingFrame extends LinearLayout implements
		android.view.View.OnClickListener {
	private View mydetail, integral,// 积分
			evaluate,// 评价
			update, about;
	Button clear, login;
	TextView my, clearTex;
	private AppContext appContext;
	Context context;
	Handler handler;

	public final static int CALCING = 0;// 正在计算
	public final static int CALCED = 1;// 计算完毕
	public final static int CLEARING = 2;// 正在清除
	public final static int CLEARSUCCESS = 3;// 清除完成
	public final static int CLEARERROR = -2;// 清除失败

	public SettingFrame(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		handler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				switch (msg.what) {
				case CALCING:
					clearTex.setText("图片缓存(计算...)");
					break;
				case CALCED:
					if (msg.obj != null) {
						clearTex.setText("图片缓存(" + msg.obj + ")");
					} else {
						clearTex.setText("图片缓存");
					}
					break;
				case CLEARING:
					clear.setText("正在清理...");
					break;
				case CLEARSUCCESS:
					MyToast.customToast(getContext(), Toast.LENGTH_SHORT,
							MyToast.TOAST_MSG_SUCCESS_TITLE, "图片缓存已清除",
							Constant.TOAST_IMG_SUCCESS);
					clear.setText("清理完成");
					clearTex.setText("图片缓存(0M)");
					break;
				case CLEARERROR:
					MyToast.customToast(getContext(), Toast.LENGTH_SHORT,
							MyToast.TOAST_MSG_ERROR_TITLE, "图片缓存清除失败",
							Constant.TOAST_IMG_ERROR);
					clear.setText("清理失败");
					break;

				}
			}

		};
		initViews(context);
		appContext = (AppContext) context.getApplicationContext();
	}

	public void onResume() {

		new Thread() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				handler.sendEmptyMessage(CALCING);
				long size = FileUtils.getDirSize(new File(
						Constant.IMG_CACHEPATH));
				Message msg = new Message();
				msg.obj = size / (1024 * 1024) + "M";
				msg.what = CALCED;
				handler.sendMessage(msg);
			}
		}.start();

		clear.setText("清除");

		login.setOnClickListener(this);
		mydetail.setOnClickListener(this);
		if (appContext.isLogin) {
//			mydetail.setOnClickListener(new ToOtherActivity(
//					(MainActivity) getContext(), MyDetailActivity.class));
//			mydetail.setClickable(true);
			my.setText(appContext.user.getUname());
			login.setText("注销");

		} else {
//			mydetail.setOnClickListener(new ToOtherActivity(
//					(MainActivity) getContext(), LoginActivity.class));
			//mydetail.setClickable(false);
			login.setText("登陆");
			my.setText("请先登陆");
		}
	}

	private void initViews(final Context context) {

		View view = LayoutInflater.from(context).inflate(
				R.layout.frame_setting, this);
		login = (Button) view.findViewById(R.id.frame_setting_login);
		mydetail = view.findViewById(R.id.frame_setting_mydetail);
		integral = view.findViewById(R.id.frame_setting_integral);
		evaluate = view.findViewById(R.id.frame_setting_evaluate);
		clear = (Button) view.findViewById(R.id.frame_setting_clear);
		clearTex = (TextView) view.findViewById(R.id.frame_setting_clearcache);
		update = view.findViewById(R.id.frame_setting_updating);
		about = view.findViewById(R.id.frame_setting_about);
		my = (TextView) view.findViewById(R.id.frame_setting_mydetail_text);
		integral.setOnClickListener(this);
		clear.setOnClickListener(this);
		evaluate.setOnClickListener(this);
		about.setOnClickListener(new ToOtherActivity((MainActivity) context,
				AboutActivity.class));
	}

	@Override
	public void onClick(View view) {
		if (view == login) {
			if (appContext.isLogin) {

				appContext.isLogin = false;
				appContext.user = null;
				SharedPreferencesUtil.clearUser(appContext);
				
				my.setText("请先登陆");
				login.setText("登陆");
			} else {
				
				Utils.ToActivity((Activity) getContext(), LoginActivity.class,
						false);
				((Activity)getContext()).overridePendingTransition(R.anim.slide_right, R.anim.slide_right_out);
			}
		}
		if(view == mydetail){
			if (appContext.isLogin) {
				Utils.ToActivity((Activity) getContext(), MyDetailActivity.class,
						false);
				
			} else {
				
				Utils.ToActivity((Activity) getContext(), LoginActivity.class,
						false);
				((Activity)getContext()).overridePendingTransition(R.anim.slide_right, R.anim.slide_right_out);
			}
		}
		if (view == integral) {
			// TODO Auto-generated method stub
			if (appContext.isLogin) {
				Utils.ToActivity((Activity) getContext(),
						IntegralActivity.class, false);
			} 
		}
		if (view == clear) {

			handler.sendEmptyMessage(CLEARING);
			Runnable r = new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					boolean flag = FileUtils.deleteDirectory("/golfeven_cache");
					if (flag) {
						handler.sendEmptyMessage(CLEARSUCCESS);

					} else {
						handler.sendEmptyMessage(CLEARERROR);
					}
				}
			};
			handler.post(r);
		}
		if (view == evaluate) {
			// TODO Auto-generated method stub
			try {
				String packetName = appContext.getPackageName();
				Uri uri = Uri.parse("market://details?id=" + packetName);
				Intent intent = new Intent(Intent.ACTION_VIEW, uri);
				// intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				context.startActivity(intent);

			} catch (Exception e) {
				MyToast.customToast(context, Toast.LENGTH_SHORT,
						MyToast.TOAST_MSG_ERROR_TITLE, "启动商店失败",
						Constant.TOAST_IMG_ERROR);
			}
		}

	}

}
