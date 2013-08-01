package com.golfeven.firstGolf.adapter;

import java.util.List;

import net.tsz.afinal.FinalBitmap;
import net.tsz.afinal.http.AjaxCallBack;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.golfeven.AppContext;
import com.golfeven.firstGolf.R;
import com.golfeven.firstGolf.api.Api;
import com.golfeven.firstGolf.base.MBaseAdapter;
import com.golfeven.firstGolf.bean.BallFriend;
import com.golfeven.firstGolf.bean.WrongResponse;
import com.golfeven.firstGolf.common.Constant;
import com.golfeven.firstGolf.common.MyLog;
import com.golfeven.firstGolf.common.NetUtil;
import com.golfeven.firstGolf.common.StringUtils;
import com.golfeven.firstGolf.common.Utils;
import com.golfeven.firstGolf.common.ValidateUtil;
import com.golfeven.firstGolf.ui.BallFriendDetailActivity;
import com.golfeven.firstGolf.widget.MyToast;

public class BallFriendAttentionListAdapter extends MBaseAdapter {

	public FinalBitmap fb;
	public boolean isAttention= false;

	public BallFriendAttentionListAdapter(Context context,
			List<BallFriend> datas) {
		this.context = context;
		this.datas = datas;
		fb = getAppContext().getFB();
	}
	public BallFriendAttentionListAdapter(Context context,
			List<BallFriend> datas,boolean isAttention) {
		this.context = context;
		this.datas = datas;
		fb = getAppContext().getFB();
		this.isAttention = isAttention;
	}
	

	public FriendView friendView = null;

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.item_attention, null);

			ImageView face = (ImageView) convertView
					.findViewById(R.id.item_attention_face);
			TextView nick = (TextView) convertView
					.findViewById(R.id.item_attention_nick);
			TextView loveMsg = (TextView) convertView
					.findViewById(R.id.item_attention_loveMsg);
			ImageView sex = (ImageView) convertView
					.findViewById(R.id.item_attention_sex);
			TextView place = (TextView) convertView
					.findViewById(R.id.item_attention_distance);
			TextView age = (TextView) convertView
					.findViewById(R.id.item_attention_age);
			Button btn = (Button) convertView
					.findViewById(R.id.item_attention_btn);

			friendView = new FriendView(face, sex, nick, loveMsg, place, age,
					btn);
			convertView.setTag(friendView);
		} else {

			friendView = (FriendView) convertView.getTag();
		}
		// TODO Auto-generated method stub

		final BallFriend ballFriend = (BallFriend) datas.get(position);
		boolean flag = Utils.getMState(ballFriend.getMemberRelation());
		friendView.btn.setTag(flag);
		friendView.btn.setVisibility(View.VISIBLE);
		if(flag){
			friendView.btn.setText("取消关注");
		}else{
			friendView.btn.setText("关注");
			
		}
		boolean mflag = ((AppContext)context.getApplicationContext()).user.getMid().equals(ballFriend.getMid());
		boolean Bflag = ((AppContext)context.getApplicationContext()).user.getUname().equals(ballFriend.getUname());
		if(mflag&&Bflag){
			friendView.btn.setVisibility(View.INVISIBLE);
		}
			
		
		friendView.nick.setText(ballFriend.getUname());
		friendView.loveMsg.setText(ballFriend.getLovemsg());
		friendView.age.setText(ballFriend.getBirthday());
		if (ballFriend.getSex().trim().equals("男")) {
			friendView.sex.setBackgroundResource(R.drawable.qy_man);
		} else if (ballFriend.getSex().trim().equals("女")) {
			friendView.sex.setBackgroundResource(R.drawable.qy_girl);
		} else {
			friendView.sex.setBackgroundResource(R.drawable.qy_sec);
		}

		String distance = ballFriend.getDistance();
		if (!StringUtils.isEmpty(distance) && distance.indexOf("9999") > -1) {
			float m = Integer.parseInt(distance);
			double k = m / 1000;
			friendView.place.setText(k + "千米");
		} else {
			friendView.place.setText("未知");

		}
		fb.display(friendView.face,
				Constant.URL_IMG_BASE + ballFriend.getFace());
		friendView.btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				final Button btn = (Button) v;
				boolean tag = (Boolean)btn.getTag();
				btn.setText("请稍候");
				btn.setClickable(false);
				if(tag){
					Api.getInstance().removeFocus(
							((AppContext) context.getApplicationContext()).user,
							ballFriend.getMid(), new AjaxCallBack<String>() {

								@Override
								public void onSuccess(String t) {
									// TODO Auto-generated method stub
									super.onSuccess(t);
									btn.setText("关注");
									btn.setClickable(true);
									WrongResponse response = null;

									try {
										response = JSON.parseObject(t,
												WrongResponse.class);
									} catch (Exception e) {
										// TODO: handle exception
									}
									if (response != null && response.code == 0) {
										// attentionTex.setText("取消关注");
										// attention.setTag(1);
										// -2不存在关系
										// -1 我把球友拉黑
										// 0 我加球友为普通好友，
										// 1我加球友为关注好友
										MyToast.centerToast(context,
												"成功取消关注", Toast.LENGTH_SHORT);
										btn.setTag(false);
										
										changeMTO(ballFriend,0);
										if(isAttention){
											datas.remove(position);	
											refresh(datas);
											
										}
									} else {
										WrongResponse wrongResponse = ValidateUtil
												.wrongResponse(t);
										if (wrongResponse.show) {
											MyToast.centerToast(context,
													wrongResponse.msg,
													Toast.LENGTH_SHORT);
										} else {
											MyToast.centerToast(context,
													"取消关注失败", Toast.LENGTH_SHORT);
											MyLog.v("取消关注失败", wrongResponse.msg);
										}
									}

								}

								@Override
								public void onFailure(Throwable t, String strMsg) {
									// TODO Auto-generated method stub
									super.onFailure(t, strMsg);
									btn.setClickable(true);
									btn.setText("取消关注");
									NetUtil.requestError(context, null);
								}

								

							});
				}else{
					Api.getInstance().addFocus(
							((AppContext) context.getApplicationContext()).user,
							ballFriend.getMid(), new AjaxCallBack<String>() {

								@Override
								public void onSuccess(String t) {
									// TODO Auto-generated method stub
									super.onSuccess(t);
									btn.setClickable(true);
									WrongResponse response = null;

									try {
										response = JSON.parseObject(t,
												WrongResponse.class);
									} catch (Exception e) {
										// TODO: handle exception
									}
									if (response != null && response.code == 0) {
										btn.setText("取消关注");
										btn.setTag(true);
										// attentionTex.setText("取消关注");
										// attention.setTag(1);
										// -2不存在关系
										// -1 我把球友拉黑
										// 0 我加球友为普通好友，
										// 1我加球友为关注好友
										MyToast.centerToast(context,
												"关注成功", Toast.LENGTH_SHORT);
										
											changeMTO(ballFriend,0);
									} else {
										btn.setText("关注");
										WrongResponse wrongResponse = ValidateUtil
												.wrongResponse(t);
										if (wrongResponse.show) {
											MyToast.centerToast(context,
													wrongResponse.msg,
													Toast.LENGTH_SHORT);
										} else {
											MyToast.centerToast(context,
													"关注失败", Toast.LENGTH_SHORT);
											MyLog.v("关注失败", wrongResponse.msg+wrongResponse.data);
										}
									}

								}

								

								@Override
								public void onFailure(Throwable t, String strMsg) {
									// TODO Auto-generated method stub
									super.onFailure(t, strMsg);
									btn.setClickable(true);
									btn.setText("关注");
									NetUtil.requestError(context, null);
								}

								

							});
				}
				

			}
		});
		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				if (((AppContext) context.getApplicationContext()).isLogin) {

					Intent intent = new Intent(context,
							BallFriendDetailActivity.class);
					intent.putExtra("ballFriend", ballFriend);
					context.startActivity(intent);
				} else {
					Toast.makeText(context, "请先登录", Toast.LENGTH_SHORT).show();
				}

			}
		});
		return convertView;

	}
	/**
	 * 改变我相对别人的状态
	 */
	private void changeMTO(
			final BallFriend ballFriend,int code) {
		String str =ballFriend.getMemberRelation();
		String old = str.substring(0,str.indexOf(","));
		String mnew = str.replaceFirst(old,code+"");
		ballFriend.setMemberRelation(mnew);
	}

	class FriendView {
		public ImageView face, sex;
		public TextView nick, loveMsg, place, age;
		public Button btn;

		public FriendView(ImageView face, ImageView sex, TextView nick,
				TextView loveMsg, TextView place, TextView age, Button btn) {
			super();
			this.face = face;
			this.sex = sex;
			this.nick = nick;
			this.loveMsg = loveMsg;
			this.place = place;
			this.age = age;
			this.btn = btn;
		}

	}

}
