package com.golfeven.xmpp.utils;

import com.golfeven.AppContext;
import com.golfeven.firstGolf.common.MyLog;
import com.golfeven.firstGolf.ui.MainActivity;
import com.golfeven.xmpp.service.XmppService;
import com.golfeven.xmpp.xmppmanager.XmppUtils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo.State;
import android.util.Log;
import android.widget.Toast;

public class NetworkReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.e("网络状态的", "网络状态改变");

		boolean success = false;

		// 获得网络连接服务
		ConnectivityManager connManager = (ConnectivityManager) context
				.getSystemService(context.CONNECTIVITY_SERVICE);
		// State state = connManager.getActiveNetworkInfo().getState();
		State state = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
				.getState(); // 获取网络连接状态
		if (State.CONNECTED == state) { // 判断是否正在使用WIFI网络
			success = true;
		}

		state = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
				.getState(); // 获取网络连接状态
		if (State.CONNECTED != state) { // 判断是否正在使用GPRS网络
			success = true;
		}

		if (!success) {
			MyLog.e("000","no net");
		}else{
			AppContext appContext = (AppContext)context.getApplicationContext();
			if(appContext!= null&&appContext.isLogin&&appContext.user != null){
//				try {
//					XmppUtils.getInstance().createConnection();
//				} catch (XMPPException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//					MyLog.e("XMPP", "登录前链接错误");
//				}
				Intent intentService = new Intent(context,XmppService.class);
				context.stopService(intentService);
				if(!XmppUtils.getInstance().isLogin()){
					MainActivity mainActivity = MainActivity.getMainActivity();
					new XmppRunnable(mainActivity.loginHandler, XmppRunnable.LOGIN, new String[]{appContext.user.getMid(),appContext.upass});
				}
			}
		}
		

	}
}
