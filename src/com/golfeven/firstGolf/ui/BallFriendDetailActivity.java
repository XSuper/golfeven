package com.golfeven.firstGolf.ui;

import java.util.ArrayList;
import java.util.List;

import net.tsz.afinal.FinalBitmap;
import net.tsz.afinal.annotation.view.ViewInject;
import net.tsz.afinal.http.AjaxCallBack;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.golfeven.firstGolf.R;
import com.golfeven.firstGolf.api.Api;
import com.golfeven.firstGolf.base.BaseActivity;
import com.golfeven.firstGolf.bean.BallFriend;
import com.golfeven.firstGolf.bean.Photo;
import com.golfeven.firstGolf.common.Constant;
import com.golfeven.firstGolf.common.MyLog;
import com.golfeven.firstGolf.common.StringUtils;
import com.golfeven.firstGolf.common.ToOtherActivity;
import com.golfeven.firstGolf.common.Utils;
import com.golfeven.firstGolf.widget.HeadBack;

public class BallFriendDetailActivity extends BaseActivity implements OnClickListener{
	@ViewInject(id=R.id.activity_ballfriend_detail_headback) HeadBack headback;
	@ViewInject(id=R.id.activity_ballfriend_detail_photo) ImageView mPhoto;
	@ViewInject(id=R.id.activity_ballfriend_detail_face) ImageView face;
	
	@ViewInject(id=R.id.activity_ballfriend_detail_name) TextView tname;
	@ViewInject(id=R.id.activity_ballfriend_detail_sex) ImageView tsex;
	@ViewInject(id=R.id.activity_ballfriend_detail_distance) TextView tdistance;
	@ViewInject(id=R.id.activity_ballfriend_detail_time) TextView ttime;
	@ViewInject(id=R.id.activity_ballfriend_detail_place) TextView tplace;
	@ViewInject(id=R.id.activity_ballfriend_detail_lovemsg) TextView tlovemsg;
	@ViewInject(id=R.id.activity_ballfriend_detail_tags) TextView ttags;
	@ViewInject(id=R.id.activity_ballfriend_detail_teams) TextView tteams;
	
	
	@ViewInject(id=R.id.activity_ballfriend_detail_background) View background;
	@ViewInject(id=R.id.activity_ballfriend_detail_msg) View msg;
	@ViewInject(id=R.id.activity_ballfriend_detail_msg_img) ImageView msgImg;
	@ViewInject(id=R.id.activity_ballfriend_detail_msg_tex) TextView msgTex;
	@ViewInject(id=R.id.activity_ballfriend_detail_attention)View attention;
	@ViewInject(id=R.id.activity_ballfriend_detail_attention_img)ImageView attentionImg;
	@ViewInject(id=R.id.activity_ballfriend_detail_attention_tex)TextView attentionTex;
	@ViewInject(id=R.id.activity_ballfriend_detail_pullblack)View pullblack;
	@ViewInject(id=R.id.activity_ballfriend_detail_pullblack_img)ImageView pullblackImg;
	@ViewInject(id=R.id.activity_ballfriend_detail_pullblack_tex)TextView pullblackTex;
	
	
	private BallFriend ballFriend;
	
	private FinalBitmap fb;
	private List<Photo> photos;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ballfriend_detail);
		fb = appContext.getFB();
		Intent intent = getIntent();
		ballFriend = intent.getParcelableExtra("ballFriend");
		if(ballFriend.getMid().equals(appContext.user.getMid())){
			background.setVisibility(View.GONE);
		}
		load();
		initValue();
	}
	
	private void initValue() {
		fb.display(face,Constant.URL_IMG_BASE+ballFriend.getFace());
		tname.setText(ballFriend.getUname());
		tdistance.setText("距离我"+ballFriend.getDistance()+"公里");
		String timeStr = StringUtils.friendly_time(StringUtils
				.strToDataStr(ballFriend.getLogintime()));
		ttime.setText("上次登录时间："+timeStr);
		tplace.setText(ballFriend.getCommplace());
		tlovemsg.setText(ballFriend.getLovemsg());

		ttags.setText(ballFriend.getLabel());
		int m[] = Utils.judgeShow(ballFriend.getMemberRelation());
		//控制关注显示
		attention.setTag(m[0]);
		switch (m[0]) {
		case -1:
			attentionTex.setText("取消关注");
			break;
		case 0:
			attention.setVisibility(View.GONE);
			break;
		case 1:
			attentionTex.setText("关注");
			break;
		}
		//控制消息显示
		msg.setTag(m[1]);
		switch (m[1]) {
		case -1:
			msgTex.setText("查看消息");
			break;
		case 0:
			msg.setVisibility(View.GONE);
			break;
		case 1:
			msgTex.setText("发送消息");
			break;
		}
		//控制拉黑显示
		pullblack.setTag(m[2]);
		switch (m[2]) {
		case -1:
			pullblackTex.setText("洗白");
			break;
		case 0:
			pullblack.setVisibility(View.GONE);
			break;
		case 1:
			pullblackTex.setText("拉黑");
			break;
		}
		
		
		
		
//		
//		attention.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				Api.getInstance().addFocus(appContext.user,ballFriend.getMid() , new AjaxCallBack<String>() {
//
//					@Override
//					public void onSuccess(String t) {
//						// TODO Auto-generated method stub
//						super.onSuccess(t);
//						Toast.makeText(appContext, t, Toast.LENGTH_LONG).show();
//					}
//
//					@Override
//					public void onFailure(Throwable t, String strMsg) {
//						// TODO Auto-generated method stub
//						super.onFailure(t, strMsg);
//						Toast.makeText(appContext, strMsg+"|||"+t.toString(), Toast.LENGTH_LONG).show();
//					}
//					
//				});
//				
//			}
//		});
	}
	private void load() {
		// TODO Auto-generated method stub
		Api api = Api.getInstance();
		
		//加载详细信息
		api.getBallFriendDetail(ballFriend.getMid(), appContext.user==null?null:appContext.user.getMid(), appContext.latitude, appContext.longitude, new AjaxCallBack<String>() {

			@Override
			public void onSuccess(String t) {
				// TODO Auto-generated method stub
				super.onSuccess(t);
				headback.setProgressVisible(false);
				try {
					ballFriend = JSON.parseObject(t, BallFriend.class);
					
				} catch (Exception e) {
					// TODO: handle exception
				}
				MyLog.v("ballfriend",t);
				initValue();
			}

			@Override
			public void onFailure(Throwable t, String strMsg) {
				// TODO Auto-generated method stub
				super.onFailure(t, strMsg);
				headback.setProgressVisible(false);
			}
			
		});
		//加载个人相册
		api.getPhoto(ballFriend.getMid(), new AjaxCallBack<String>() {

			@Override
			public void onSuccess(String t) {
				// TODO Auto-generated method stub
				super.onSuccess(t);
				photos = JSON.parseArray(t, Photo.class);
				if(photos!=null&&photos.size()!=0){
					fb.display(mPhoto, Constant.URL_IMG_BASE+photos.get(0).getPic());
					Intent intent = new Intent(BallFriendDetailActivity.this,PhotoShowActivity.class);
					intent.putParcelableArrayListExtra("photos", (ArrayList<Photo>)photos);
					mPhoto.setOnClickListener(new ToOtherActivity(BallFriendDetailActivity.this, intent, false));
				}
			}
		});
		

		

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}

	
	
}
