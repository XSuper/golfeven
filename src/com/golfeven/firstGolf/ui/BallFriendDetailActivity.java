package com.golfeven.firstGolf.ui;

import java.util.ArrayList;
import java.util.List;

import net.tsz.afinal.FinalBitmap;
import net.tsz.afinal.annotation.view.ViewInject;
import net.tsz.afinal.http.AjaxCallBack;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.golfeven.firstGolf.R;
import com.golfeven.firstGolf.api.Api;
import com.golfeven.firstGolf.base.BaseActivity;
import com.golfeven.firstGolf.bean.BallFriend;
import com.golfeven.firstGolf.bean.Photo;
import com.golfeven.firstGolf.bean.WrongResponse;
import com.golfeven.firstGolf.common.Constant;
import com.golfeven.firstGolf.common.MyLog;
import com.golfeven.firstGolf.common.NetUtil;
import com.golfeven.firstGolf.common.StringUtils;
import com.golfeven.firstGolf.common.ToOtherActivity;
import com.golfeven.firstGolf.common.Utils;
import com.golfeven.firstGolf.common.ValidateUtil;
import com.golfeven.firstGolf.widget.HeadBack;
import com.golfeven.firstGolf.widget.MyToast;

public class BallFriendDetailActivity extends BaseActivity implements OnClickListener{
	@ViewInject(id=R.id.activity_ballfriend_detail_headback) HeadBack headback;
	@ViewInject(id=R.id.activity_ballfriend_detail_photo) ImageView mPhoto;
	@ViewInject(id=R.id.activity_ballfriend_detail_face) ImageView face;
	
	@ViewInject(id=R.id.activity_ballfriend_detail_mcount) TextView imgCount;
	
	@ViewInject(id=R.id.activity_ballfriend_detail_name) TextView tname;
	@ViewInject(id=R.id.activity_ballfriend_detail_sex) ImageView tsex;
	@ViewInject(id=R.id.activity_ballfriend_detail_distance) TextView tdistance;
	@ViewInject(id=R.id.activity_ballfriend_detail_time) TextView ttime;
	@ViewInject(id=R.id.activity_ballfriend_detail_place) TextView tplace;
	@ViewInject(id=R.id.activity_ballfriend_detail_lovemsg) TextView tlovemsg;
	@ViewInject(id=R.id.activity_ballfriend_detail_tags) TextView ttags;
	
	
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
	
	//public int state = -2;//拉黑前的状态
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ballfriend_detail);
		fb = appContext.getFB();
		Intent intent = getIntent();
		ballFriend = intent.getParcelableExtra("ballFriend");
		if(appContext.isLogin == true&&appContext.user!= null){
			if(ballFriend.getMid().equals(appContext.user.getMid())){
				background.setVisibility(View.GONE);
			}
			
		}
		
		imgCount = (TextView)findViewById(R.id.activity_ballfriend_detail_mcount);
		
		android.widget.LinearLayout.LayoutParams params = new android.widget.LinearLayout.LayoutParams(Utils.getScreenWith(BallFriendDetailActivity.this)/4,Utils.getScreenWith(BallFriendDetailActivity.this)/4);
		params.setMargins(10, 10, 10, 10);
		face.setLayoutParams(params);
		int width = Utils.getScreenWith(BallFriendDetailActivity.this);
		LayoutParams params2 = new LayoutParams(LayoutParams.MATCH_PARENT,width/2);
		mPhoto.setLayoutParams(params2);
		
		load();
		initValue();
	}
	
	private void initValue() {
		
		fb.isSquare = true;
		fb.display(face,Constant.URL_IMG_BASE+ballFriend.getFace());
		
		tname.setText(ballFriend.getUname());
		tdistance.setText("距离我: "+Utils.getDestance(ballFriend.getDistance()));
		String timeStr = StringUtils.friendly_time(StringUtils
				.strToDataStr(ballFriend.getLogintime()+"000"));
		ttime.setText("上次登录时间："+timeStr);
		
		if("男".equals(ballFriend.getSex().trim())){
			tsex.setBackgroundResource(R.drawable.qy_man);
		}
		else if("女".equals(ballFriend.getSex().trim())){
			tsex.setBackgroundResource(R.drawable.qy_girl);
		}else {
			tsex.setBackgroundResource(R.drawable.qy_sec);
		}
		tplace.setText(ballFriend.getCommplace());
		tlovemsg.setText(ballFriend.getLovemsg());

		ttags.setText(ballFriend.getLabel());
//		try {
//			state = Integer.parseInt((ballFriend.getMemberRelation().split(","))[0]);
//		} catch (Exception e) {
//			// TODO: handle exception
//		}
		changeBottom();
		attention.setOnClickListener(this);
		msg.setOnClickListener(this);
		pullblack.setOnClickListener(this);
		
	}

	
	/**
	 * 更改底部显示状体
	 */
	private void changeBottom() {
		attention.setVisibility(View.VISIBLE);
		pullblack.setVisibility(View.VISIBLE);
		msg.setVisibility(View.VISIBLE);
		
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
		//background.postInvalidate();
	}
	Api api = Api.getInstance();
	private void load() {
		// TODO Auto-generated method stub
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
				try {
					
					photos = JSON.parseArray(t, Photo.class);
				} catch (Exception e) {
					// TODO: handle exception
				}
				if(photos!=null&&photos.size()!=0){
					
					FinalBitmap fbi = FinalBitmap.createNew(appContext,Constant.IMG_CACHEPATH);
					fbi.proportion=2;
					fbi.display(mPhoto, Constant.URL_IMG_BASE+photos.get(0).getPic());
					imgCount.setText(photos.size()+"");
					
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
		if(v==attention){
			int tag =(Integer)attention.getTag();
			switch (tag) {
			case -1:
				
				attention.setClickable(false);
				api.removeFocus(appContext.user, ballFriend.getMid(), new AjaxCallBack<String>() {

					@Override
					public void onSuccess(String t) {
						// TODO Auto-generated method stub
						super.onSuccess(t);
						Log.v(getClass().getName(), t);
						attention.setClickable(true);
						WrongResponse response = null;
						
						try {
							response = JSON.parseObject(t,WrongResponse.class);
						} catch (Exception e) {
							// TODO: handle exception
						}
						if(response!= null&&response.code==0){
//							attentionTex.setText("取消关注");
//							attention.setTag(1);
							// -2不存在关系
							// -1 我把球友拉黑
							// 0 我加球友为普通好友，
							// 1我加球友为关注好友
							changeMTO(-2);
							changeBottom();
						}else{
							WrongResponse wrongResponse = ValidateUtil
									.wrongResponse(t);
							if (wrongResponse.show) {
								MyToast.centerToast(appContext,
										wrongResponse.msg,
										Toast.LENGTH_SHORT);
							} else {
								MyToast.centerToast(appContext, "取消关注失败",
										Toast.LENGTH_SHORT);
								MyLog.v("取消关注失败", wrongResponse.msg);
							}
						}
						
					}

					@Override
					public void onFailure(Throwable t, String strMsg) {
						// TODO Auto-generated method stub
						super.onFailure(t, strMsg);
						attention.setClickable(true);
						NetUtil.requestError(appContext, null);
					}
					
				});
				break;
			case 1:
				//关注
				attention.setClickable(false);
				api.addFocus(appContext.user, ballFriend.getMid(), new AjaxCallBack<String>() {

					@Override
					public void onSuccess(String t) {
						// TODO Auto-generated method stub
						super.onSuccess(t);
						Log.v(getClass().getName(), t);
						attention.setClickable(true);
						WrongResponse response = null;
						
						try {
							response = JSON.parseObject(t,WrongResponse.class);
						} catch (Exception e) {
							// TODO: handle exception
						}
						if(response!= null&&response.code==0){
//							attentionTex.setText("取消关注");
//							attention.setTag(1);
							// -2不存在关系
							// -1 我把球友拉黑
							// 0 我加球友为普通好友，
							// 1我加球友为关注好友
							changeMTO(1);
							changeBottom();
						}else{
							WrongResponse wrongResponse = ValidateUtil
									.wrongResponse(t);
							if (wrongResponse.show) {
								MyToast.centerToast(appContext,
										wrongResponse.msg,
										Toast.LENGTH_SHORT);
							} else {
								MyToast.centerToast(appContext, "关注失败",
										Toast.LENGTH_SHORT);
								MyLog.v("关注失败", wrongResponse.msg);
							}
						}
						
					}

					@Override
					public void onFailure(Throwable t, String strMsg) {
						// TODO Auto-generated method stub
						super.onFailure(t, strMsg);
						attention.setClickable(true);
						NetUtil.requestError(appContext, null);
					}
					
				});
				break;
			}
		}
		if(v==msg){
			msg.setClickable(false);
			int tag =(Integer)msg.getTag();
			switch (tag) {
			case -1:
				
				break;
			case 1:
				break;
			}
		}
		if(v==pullblack){
			pullblack.setClickable(false);
			MyLog.v("pullback", pullblackTex.getText().toString());
			int tag =(Integer)pullblack.getTag();
			switch (tag) {
			case -1:
				api.updateStatus(appContext.user, ballFriend.getMid(), false, new AjaxCallBack<String>() {

					@Override
					public void onSuccess(String t) {
						// TODO Auto-generated method stub
						super.onSuccess(t);
						MyLog.v(getClass().getName()+"pullblack",t);
						pullblack.setClickable(true);
						WrongResponse response = null;
						
						try {
							response = JSON.parseObject(t,WrongResponse.class);
						} catch (Exception e) {
							// TODO: handle exception
						}
						if(response!= null&&response.code==0){
							pullblackTex.setText("拉黑");
							// -2不存在关系
							// -1 我把球友拉黑
							// 0 我加球友为普通好友，
							// 1我加球友为关注好友
//							changeMTO(state);
							changeMTO(-2);
							changeBottom();
						}else{
							WrongResponse wrongResponse = ValidateUtil
									.wrongResponse(t);
							if (wrongResponse.show) {
								MyToast.centerToast(appContext,
										wrongResponse.msg,
										Toast.LENGTH_SHORT);
							} else {
								MyToast.centerToast(appContext, "洗白失败",
										Toast.LENGTH_SHORT);
								MyLog.v("洗白失败", wrongResponse.msg);
							}
						}
						
					}

					@Override
					public void onFailure(Throwable t, String strMsg) {
						// TODO Auto-generated method stub
						super.onFailure(t, strMsg);
						pullblack.setClickable(true);
						NetUtil.requestError(appContext, null);
					}
					
				});
				break;
			case 1:
				
				api.updateStatus(appContext.user, ballFriend.getMid(), true, new AjaxCallBack<String>() {

					@Override
					public void onSuccess(String t) {
						// TODO Auto-generated method stub
						super.onSuccess(t);
						pullblack.setClickable(true);
						WrongResponse response = null;
						MyLog.v(getClass().getName()+"pullblack",t);
						try {
							response = JSON.parseObject(t,WrongResponse.class);
						} catch (Exception e) {
							// TODO: handle exception
						}
						if(response!= null&&response.code==0){
							pullblackTex.setText("洗白");
							//pullblack.setTag(-1);
							// -2不存在关系
							// -1 我把球友拉黑
							// 0 我加球友为普通好友，
							// 1我加球友为关注好友
							changeMTO(-1);
							changeBottom();
						}else{
							WrongResponse wrongResponse = ValidateUtil
									.wrongResponse(t);
							if (wrongResponse.show) {
								MyToast.centerToast(appContext,
										wrongResponse.msg,
										Toast.LENGTH_SHORT);
							} else {
								MyToast.centerToast(appContext, "拉黑失败",
										Toast.LENGTH_SHORT);
								MyLog.v("拉黑失败", wrongResponse.msg);
							}
						}
						
					}

					@Override
					public void onFailure(Throwable t, String strMsg) {
						// TODO Auto-generated method stub
						super.onFailure(t, strMsg);
						pullblack.setClickable(true);
						NetUtil.requestError(appContext, null);
					}
					
				});
				break;
			}
		}
	}
	/**
	 * 改变我相对别人的状态
	 */
	public void changeMTO(int code){
		String str =ballFriend.getMemberRelation();
		String old = str.substring(0,str.indexOf(","));
		String mnew = str.replaceFirst(old, code+"");
		ballFriend.setMemberRelation(mnew);
		MyLog.v("00",ballFriend.getMemberRelation());
	}
}
