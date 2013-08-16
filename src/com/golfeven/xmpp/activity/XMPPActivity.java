package com.golfeven.xmpp.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.golfeven.xmpp.entity.FriendInfo;
import com.golfeven.xmpp.service.XmppService;
import com.golfeven.xmpp.utils.MyToast;
import com.golfeven.xmpp.utils.XmppRunnable;
import com.golfeven.xmpp.xmppmanager.XmppUtils;

/**
 * 
 * @author by_wsc
 * @email wscnydx@gmail.com
 * @date 日期：2013-4-15 时间：下午10:28:33
 */
public  class XMPPActivity extends Activity {

	
	public static final int DIALOG_SHOW = 0;
	public static final int DIALOG_CANCLE = 1;
	ProgressDialog dialog ;
	private Handler loginHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case XmppUtils.LOGIN_ERROR:
				MyToast.showToast(XMPPActivity.this, "登录失败");
				break;
			case XmppUtils.LOGIN_ERROR_NET:
				MyToast.showToast(XMPPActivity.this, "连接服务器失败");
				break;
			case XmppUtils.LOGIN_ERROR_PWD:
				MyToast.showToast(XMPPActivity.this, "密码错误");
				break;
			case XmppUtils.LOGIN_ERROR_REPEAT:
				MyToast.showToast(XMPPActivity.this, "重复登录");
				break;
			case 200:
				MyToast.showToast(XMPPActivity.this, "登录成功");
				Intent intentService = new Intent(XMPPActivity.this,XmppService.class);
				startService(intentService);
				new Thread(){
					public void run() {
						XmppUtils.getInstance().sendOnLine();
					};
				}.start();
				
				FriendInfo info = new FriendInfo();
				info.setUsername("11");
				info.setNickname("110");
				Intent intent = new Intent(XMPPActivity.this,Chat.class);
				intent.putExtra("info", info);
				startActivity(intent);
				finish();
				break;

			case DIALOG_SHOW:
				dialog = new ProgressDialog(XMPPActivity.this);
				dialog.setMessage("Loding");
				dialog.show();
				break;
			case DIALOG_CANCLE:
				if(dialog != null){
					dialog.dismiss();
					dialog = null;
				}
				break;
			}
		};
	};
	
	int loginCode= 200;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		new XmppRunnable(loginHandler, XmppRunnable.LOGIN, new String[]{"19","114"});
		
	}

	
}