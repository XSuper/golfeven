package com.golfeven.firstGolf.ui;


import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import net.tsz.afinal.annotation.view.ViewInject;
import net.tsz.afinal.http.AjaxCallBack;
import android.R.integer;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.golfeven.AppManager;
import com.golfeven.firstGolf.R;
import com.golfeven.firstGolf.api.Api;
import com.golfeven.firstGolf.base.BaseActivity;
import com.golfeven.firstGolf.bean.WrongResponse;
import com.golfeven.firstGolf.common.Constant;
import com.golfeven.firstGolf.common.MyLog;
import com.golfeven.firstGolf.common.NetUtil;
import com.golfeven.firstGolf.common.StringUtils;
import com.golfeven.firstGolf.common.Utils;
import com.golfeven.firstGolf.common.ValidateUtil;
import com.golfeven.firstGolf.widget.HeadBack;
import com.golfeven.firstGolf.widget.MyToast;

public class MyInfoActivity extends BaseActivity{
	@ViewInject(id=R.id.activity_myinfo_headback) HeadBack headBack;
	@ViewInject(id=R.id.activity_myinfo_qq)EditText qq;
	@ViewInject(id=R.id.activity_myinfo_email)EditText email;
	
	ProgressDialog dialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_myinfo);
		
		
		
		headBack.setRbtn2ClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String mqq = qq.getText().toString();
				String memail = email.getText().toString();
				// TODO Auto-generated method stub
				if(StringUtils.isEmpty(mqq)||StringUtils.isEmpty(memail)){
					MyToast.centerToast(appContext, "请输入完整信息", Toast.LENGTH_SHORT);
					return;
				}else{
					if(!StringUtils.isEmail(memail)){
						MyToast.centerToast(appContext, "请输入正确的电子邮箱", Toast.LENGTH_SHORT);
						return;
					}
				}
				HashMap<String, String> data = new HashMap<String, String>();
				data.put("email",memail);
				data.put("qq",mqq);
				Set<Integer> typeSet = new HashSet<Integer>();
				typeSet.add(1);
				dialog = Utils.initWaitingDialog(
						MyInfoActivity.this,
						"正在更新资料");
				Api.getInstance().updateInfo(MyInfoActivity.this, appContext.user, data, typeSet, new AjaxCallBack<String>() {
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
									MyInfoActivity.this,
									Toast.LENGTH_SHORT,
									MyToast.TOAST_MSG_SUCCESS_TITLE,
									"资料更新成功",
									Constant.TOAST_IMG_SUCCESS);
							finish();
//							MainActivity m = (MainActivity)AppManager.getAppManager().getActivity(MainActivity.class);
//							m.mScrollLayout.scrollToScreen(3);

						} else {
							WrongResponse wrongResponse = ValidateUtil
									.wrongResponse(t);
							if (wrongResponse.show) {
								MyToast.customToast(
										MyInfoActivity.this,
										Toast.LENGTH_SHORT,
										MyToast.TOAST_MSG_ERROR_TITLE,
										wrongResponse.msg,
										Constant.TOAST_IMG_ERROR);
								
							} else {
								MyToast.customToast(
										MyInfoActivity.this,
										Toast.LENGTH_SHORT,
										MyToast.TOAST_MSG_ERROR_TITLE,
										"资料更新失败",
										Constant.TOAST_IMG_ERROR);
								MyLog.v("资料更新失败", wrongResponse.msg);
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
		});
	}
	
	

}
