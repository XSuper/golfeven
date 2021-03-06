package com.golfeven.firstGolf.ui;

import net.tsz.afinal.annotation.view.ViewInject;
import net.tsz.afinal.http.AjaxCallBack;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.golfeven.firstGolf.R;
import com.golfeven.firstGolf.api.Api;
import com.golfeven.firstGolf.base.BaseActivity;
import com.golfeven.firstGolf.bean.User;
import com.golfeven.firstGolf.bean.WrongResponse;
import com.golfeven.firstGolf.common.MyLog;
import com.golfeven.firstGolf.common.NetUtil;
import com.golfeven.firstGolf.common.SharedPreferencesUtil;
import com.golfeven.firstGolf.common.StringUtils;
import com.golfeven.firstGolf.common.ValidateUtil;
import com.golfeven.firstGolf.widget.HeadBack;
import com.golfeven.firstGolf.widget.MyToast;
import com.golfeven.xmpp.utils.XmppRunnable;

public class RegisterActivity extends BaseActivity{
	@ViewInject(id=R.id.activity_register_id) EditText userid;
	@ViewInject(id=R.id.activity_register_name) EditText nick;
	@ViewInject(id=R.id.activity_register_upass) EditText pass;
	@ViewInject(id=R.id.activity_register_repeat) EditText rPass;
	@ViewInject(id=R.id.activity_register_agree) CheckBox agree;
	@ViewInject(id=R.id.activity_register_headback) HeadBack headBack;
	@ViewInject(id=R.id.activity_register_btn,click="register") Button register;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
	}
	public void register(View view){
		final String mUid=userid.getText().toString();
		final String mNick=nick.getText().toString();
		final String mPass=pass.getText().toString();
		String mRPass=rPass.getText().toString();
		int code = ValidateUtil.registerValidate(mUid,mNick ,mPass ,mRPass);
		if(code == 0){
			
			code = agree.isChecked()?0:ValidateUtil.NOTAGREE;
		}
		switch (code) {
		case ValidateUtil.UNAMEISEMPTY:
			MyToast.centerToast(appContext, "请输入用户名", Toast.LENGTH_SHORT);
			break;
		case ValidateUtil.UNAMEISWRONGFUL:
			MyToast.centerToast(appContext, "请输入邮箱或电话号码", Toast.LENGTH_SHORT);
			break;
		case ValidateUtil.NICKISEMPTY:
			MyToast.centerToast(appContext, "请输入昵称", Toast.LENGTH_SHORT);
			break;
		case ValidateUtil.UPASSISEMPTY:
			MyToast.centerToast(appContext, "请输入密码", Toast.LENGTH_SHORT);
			break;
		case ValidateUtil.UPASSISWRONGFUL:
			MyToast.centerToast(appContext, "请按要求输入密码", Toast.LENGTH_SHORT);
			break;
		case ValidateUtil.PASSWORDSDIFFER:
			MyToast.centerToast(appContext, "两次密码输入不一致", Toast.LENGTH_SHORT);
			break;
		case ValidateUtil.NOTAGREE:
			MyToast.centerToast(appContext, "请阅读并同意条款", Toast.LENGTH_SHORT);
			break;
		case ValidateUtil.UPASSISOK:
			headBack.setProgressVisible(true);
			Api.getInstance().register(mUid, mPass, mNick, new AjaxCallBack<String>() {

				@Override
				public void onSuccess(String t) {
					// TODO Auto-generated method stub
					super.onSuccess(t);
					try {
						appContext.user = JSON.parseObject(t,
								User.class);

					} catch (Exception e) {
						MyLog.e(getClass().getName(), e.toString());
					}
					if (appContext.user != null
							&& !StringUtils.isEmpty(appContext.user
									.getMid())) {

						appContext.upass = mPass;
						appContext.isLogin = true;
						appContext.user.setUserid(mUid);
						appContext.user.setUname(mNick);
						appContext.user.setSex("保密");
						appContext.user.setBirthday("1900-01-01");
						MainActivity mainActivity = MainActivity.getMainActivity();
						new XmppRunnable(mainActivity.loginHandler, XmppRunnable.LOGIN, new String[]{appContext.user.getMid(),mPass});
						Api.getInstance().addCredits(RegisterActivity.this, appContext.user, 0, "完成每天登录,");
						// 登陆成功后保存用户名和密码
						SharedPreferencesUtil.saveUser(appContext,
								appContext.user.getUname(),mPass );
						if (!StringUtils.isEmpty(appContext.longitude)
								|| !StringUtils
										.isEmpty(appContext.latitude)) {
							Api.getInstance().updatePlace(
									appContext.user,
									appContext.longitude,
									appContext.latitude);
						}
						finish();
					} else{
						WrongResponse wrongResponse = ValidateUtil
								.wrongResponse(t);
						if(wrongResponse.show){
							MyToast.centerToast(appContext, wrongResponse.msg, Toast.LENGTH_SHORT);
						}else{
							MyToast.centerToast(appContext, "注册失败", Toast.LENGTH_SHORT);
							MyLog.v("注册失败",wrongResponse.msg );
						}
					}
					headBack.setProgressVisible(false);
				}

				@Override
				public void onFailure(Throwable t, String strMsg) {
					// TODO Auto-generated method stub
					super.onFailure(t, strMsg);
					NetUtil.requestError(RegisterActivity.this, null);
					headBack.setProgressVisible(false);
				}
				
			});
		}
	}
	

}
