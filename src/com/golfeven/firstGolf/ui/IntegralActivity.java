package com.golfeven.firstGolf.ui;

import java.util.List;

import net.tsz.afinal.annotation.view.ViewInject;
import net.tsz.afinal.http.AjaxCallBack;
import android.R.integer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.golfeven.AppManager;
import com.golfeven.firstGolf.R;
import com.golfeven.firstGolf.api.Api;
import com.golfeven.firstGolf.base.BaseActivity;
import com.golfeven.firstGolf.bean.Integral;
import com.golfeven.firstGolf.bean.WrongResponse;
import com.golfeven.firstGolf.common.MyLog;
import com.golfeven.firstGolf.common.NetUtil;
import com.golfeven.firstGolf.common.Utils;
import com.golfeven.firstGolf.common.ValidateUtil;
import com.golfeven.firstGolf.widget.HeadBack;
import com.golfeven.firstGolf.widget.MyToast;

public class IntegralActivity extends BaseActivity{
	@ViewInject(id=R.id.activity_integral_headback) HeadBack headBack;
	
	@ViewInject(id=R.id.activity_integral_completeCount_text)TextView t1;
	@ViewInject(id=R.id.activity_integral_evaluate_text)TextView t2;
	@ViewInject(id=R.id.activity_integral_lable_text)TextView t3;
	@ViewInject(id=R.id.activity_integral_loveMsg_text)TextView t4;
	@ViewInject(id=R.id.activity_integral_place_text)TextView t5;
	@ViewInject(id=R.id.activity_integral_totle)TextView t6;
	@ViewInject(id=R.id.activity_integral_uploadGrade_text)TextView t7;
	@ViewInject(id=R.id.activity_integral_uploadImage_text)TextView t8;
	
	
	@ViewInject(id=R.id.activity_integral_completeCount_btn ,click="btnClick")Button b1;
	@ViewInject(id=R.id.activity_integral_evaluate_btn,click="btnClick")Button b2;
	@ViewInject(id=R.id.activity_integral_lable_btn,click="btnClick")Button b3;
	@ViewInject(id=R.id.activity_integral_loveMsg_btn,click="btnClick")Button b4;
	@ViewInject(id=R.id.activity_integral_place_btn,click="btnClick")Button b5;
	@ViewInject(id=R.id.activity_integral_uploadGrade_btn,click="btnClick")Button b7;
	@ViewInject(id=R.id.activity_integral_uploadImage_btn,click="btnClick")Button b8;
	
	List<Integral> integrals ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_integral);
		
		b1.setClickable(false);
		b2.setClickable(false);
		b3.setClickable(false);
		b4.setClickable(false);
		b5.setClickable(false);
		b7.setClickable(false);
		b8.setClickable(false);
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		Api.getInstance().readCredits(appContext.user, new AjaxCallBack<String>() {

			@Override
			public void onSuccess(String t) {
				// TODO Auto-generated method stub
				super.onSuccess(t);
				headBack.setProgressVisible(false);
				if(t.trim().startsWith("[")||t.trim().startsWith("{")){
					integrals = JSON.parseArray(t,Integral.class);
					init();
				}else{
					WrongResponse wrongResponse = ValidateUtil
							.wrongResponse(t);
					if(wrongResponse.show){
						MyToast.centerToast(appContext, wrongResponse.msg, Toast.LENGTH_SHORT);
					}else{
						MyToast.centerToast(appContext, "陆失败", Toast.LENGTH_SHORT);
						MyLog.v("登陆失败",wrongResponse.msg );
					}
				}
			}

			@Override
			public void onFailure(Throwable t, String strMsg) {
				// TODO Auto-generated method stub
				super.onFailure(t, strMsg);
				headBack.setProgressVisible(false);
				NetUtil.requestError(appContext, null);
			}
			
		});
		
	}
	private void init() {
		// TODO Auto-generated method stub
		b1.setClickable(true);
		b2.setClickable(true);
		b3.setClickable(true);
		b4.setClickable(true);
		b5.setClickable(true);
		b7.setClickable(true);
		b8.setClickable(true);
		int totle = Utils.getToTleIntegral(integrals);
		t6.setText(totle+"");
		for (Integral integral : integrals) {
			Button btn = null;
			switch (integral.getType()) {
			case 1://完善个人资料
				btn=b1;
				break;
			case 2://上传真实图片
				btn=b8;
				break;
			case 3://上传成绩
				btn=b7;
				break;
			case 5://常出没地
				btn=b5;
				break;
			case 6://完成签名
				btn=b4;
				break;
			case 7://完成标签
				btn=b3;
				break;
			case 8://客户端好评
				btn=b2;
				break;
			}
			if(btn!=null){
				
				btn.setClickable(false);
				btn.setText("已完成");
			}
		}

	}
	public void btnClick(View view){
		Button btn = (Button)view;
		//完善账户
		if(btn==b1){
			
		}
		//评价
		if(btn==b2){
			
		}
		//标签
		//上传照片
		if(btn==b3||btn==b4||btn==b5||btn==b8){
			Utils.ToActivity(this, MyDetailActivity.class, false);
		}
		
		//上传成绩
		if(btn==b7){
			finish();
			MainActivity activity = (MainActivity)AppManager.getAppManager().getActivity(MainActivity.class);
			activity.mScrollLayout.scrollToScreen(1);
		}
		
		
		
	}

}
