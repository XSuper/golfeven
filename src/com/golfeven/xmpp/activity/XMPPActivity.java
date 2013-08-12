package com.golfeven.xmpp.activity;

import org.jivesoftware.smack.XMPPException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.golfeven.firstGolf.R;
import com.golfeven.xmpp.entity.FriendInfo;
import com.golfeven.xmpp.service.XmppService;
import com.golfeven.xmpp.utils.Logs;
import com.golfeven.xmpp.utils.MyToast;
import com.golfeven.xmpp.xmppmanager.XmppUtils;

/**
 * 
 * @author by_wsc
 * @email wscnydx@gmail.com
 * @date 日期：2013-4-15 时间：下午10:28:33
 */
public  class XMPPActivity extends Activity {

	private EditText username;
	private EditText password;
	private EditText host;
	private EditText port;
	private EditText servername;
	private Button login;
	private Button register;
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
//				Intent intent = new Intent(MainActivity.this,FriendListActivity.class);
//				startActivity(intent);
				
				new Thread(){
					public void run() {
						XmppUtils.getInstance().sendOnLine();
					};
				}.start();
				
				FriendInfo info = new FriendInfo();
				info.setUsername("admin");
				info.setNickname("admin");
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
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//setContentView(R.layout.main);
//		findView();
//		
//		setListener();
		int loginCode= 200;
		try {
			if(XmppUtils.getInstance().getConnection() == null||!XmppUtils.getInstance().getConnection().isConnected()){
				XmppUtils.getInstance().createConnection();
				XmppUtils.getInstance().getConnection().login("a","a","golfeven");
			}
		} catch (XMPPException ex) {
			ex.printStackTrace();
			if (ex.getXMPPError() != null) {
				loginCode = ex.getXMPPError().getCode();
			}
			//这里可能还有一些是没有写出来
			switch (loginCode) {
			case 409://重复登录
				loginCode = XmppUtils.LOGIN_ERROR_REPEAT;
				break;
			case 502://
				loginCode = XmppUtils.LOGIN_ERROR_NET;
				break;
			case 401://认证错误
				loginCode = XmppUtils.LOGIN_ERROR_PWD;
				break;
			default://未知
				loginCode = XmppUtils.LOGIN_ERROR;
				break;
			}
		} catch (Exception e){
			loginCode = XmppUtils.LOGIN_ERROR_NET;
		}
		//loginHandler.sendEmptyMessage(MainActivity.DIALOG_CANCLE);
		loginHandler.sendEmptyMessage(loginCode);
		
	}

	
}