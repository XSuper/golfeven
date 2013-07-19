package com.golfeven.firstGolf.ui;

import java.util.HashMap;
import java.util.Map;

import net.tsz.afinal.annotation.view.ViewInject;
import net.tsz.afinal.http.AjaxCallBack;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.golfeven.firstGolf.R;
import com.golfeven.firstGolf.api.Api;
import com.golfeven.firstGolf.base.BaseActivity;
import com.golfeven.firstGolf.bean.User;
import com.golfeven.firstGolf.bean.WrongResponse;
import com.golfeven.firstGolf.common.Constant;
import com.golfeven.firstGolf.common.MyLog;
import com.golfeven.firstGolf.common.SharedPreferencesUtil;
import com.golfeven.firstGolf.common.ValidateUtil;
import com.golfeven.firstGolf.widget.HeadBack;
import com.golfeven.firstGolf.widget.MyToast;

public class LoginActivity extends BaseActivity {
	@ViewInject(id = R.id.activity_login_uname)
	EditText uname;
	@ViewInject(id = R.id.activity_login_password)
	EditText upass;
	@ViewInject(id = R.id.activity_login_register, click = "register")
	Button register;
	@ViewInject(id = R.id.activity_login_login, click = "login")
	Button login;
	@ViewInject(id = R.id.activity_login_headback)
	HeadBack headback;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
	}

	public void login(View view) {
		// TODO Auto-generated method stub
		switch (ValidateUtil.loginValidate(uname.getText().toString(), upass
				.getText().toString())) {
		case ValidateUtil.UNAMEISEMPTY:
			MyToast.centerToast(appContext, "用户名不能为空", Toast.LENGTH_SHORT);
			break;
		case ValidateUtil.UNAMEISWRONGFUL:
			MyToast.centerToast(appContext, "请输入邮箱或电话号码", Toast.LENGTH_SHORT);
			break;
		case ValidateUtil.UPASSISEMPTY:
			MyToast.centerToast(appContext, "密码不能为空", Toast.LENGTH_SHORT);
			break;
		case ValidateUtil.UPASSISWRONGFUL:
			MyToast.centerToast(appContext, "请按要求输入密码", Toast.LENGTH_SHORT);
			break;
		case ValidateUtil.UPASSISOK:
			headback.setProgressVisible(true);
			Api.getInstance().login(uname.getText().toString(),
					upass.getText().toString(), new AjaxCallBack<String>() {

						@Override
						public void onSuccess(String t) {
							// TODO Auto-generated method stub
							super.onSuccess(t);
							headback.setProgressVisible(false);
							if (t.trim().startsWith("{")) {
								appContext.user = JSON.parseObject(t, User.class);
								appContext.isLogin = true;
								//登陆成功后保存用户名和密码
								SharedPreferencesUtil.saveUser(appContext, uname.getText().toString(), upass.getText().toString());
								finish();
							} else{
								WrongResponse wrongResponse = ValidateUtil
										.wrongResponse(t);
								if(wrongResponse.show){
									MyToast.centerToast(appContext, wrongResponse.msg, Toast.LENGTH_SHORT);
								}else{
									MyToast.centerToast(appContext, "登陆失败", Toast.LENGTH_SHORT);
									MyLog.v("登陆失败",wrongResponse.msg );
								}
							}
						}

						@Override
						public void onFailure(Throwable t, String strMsg) {
							// TODO Auto-generated method stub
							super.onFailure(t, strMsg);
							headback.setProgressVisible(false);
							MyToast.centerToast(appContext, "网络连接失败", Toast.LENGTH_SHORT);
						}

					});
			break;

		}

	}

	public void register(View view) {
		Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
		finish();
		startActivity(intent);

	}

}
