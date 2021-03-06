package com.golfeven.firstGolf.api;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.jivesoftware.smack.XMPPException;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;
import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.golfeven.AppContext;
import com.golfeven.firstGolf.bean.Score;
import com.golfeven.firstGolf.bean.User;
import com.golfeven.firstGolf.bean.WrongResponse;
import com.golfeven.firstGolf.common.Constant;
import com.golfeven.firstGolf.common.MyLog;
import com.golfeven.firstGolf.common.NetUtil;
import com.golfeven.firstGolf.common.SharedPreferencesUtil;
import com.golfeven.firstGolf.common.StringUtils;
import com.golfeven.firstGolf.common.ValidateUtil;
import com.golfeven.firstGolf.ui.MainActivity;
import com.golfeven.firstGolf.widget.MyToast;
import com.golfeven.xmpp.utils.XmppRunnable;
import com.golfeven.xmpp.xmppmanager.XmppUtils;

public class Api {
	private static Api api;
	FinalHttp fh;
	private Api(){
		fh = new FinalHttp();
	}
	/**
	 * 得到实体单例
	 * @return
	 */
	public static Api getInstance(){
		if(api == null){
			api = new Api();
		}
		return api;
	}
	/**
	 * 登陆
	 * @param uname
	 * @param upass
	 * @param mCallBack
	 */
	public void login(String uname,String upass,final AjaxCallBack<String> mCallBack) {
		// TODO Auto-generated method stub
		AjaxParams params = new AjaxParams();
		params.put("cmd", "Member.login");
		params.put("userid", uname);
		params.put("pwd", upass);
		fh.get(Constant.URL_BASE, params, new AjaxCallBack<String>() {

			@Override
			public void onSuccess(String t) {
				// TODO Auto-generated method stub
				super.onSuccess(t);
				mCallBack.onSuccess(t);
				MainActivity mainActivity = MainActivity.getMainActivity();
				AppContext appContext = AppContext.getAppContext();
				if(appContext.isLogin&&appContext.user != null){
//					try {
//						XmppUtils.getInstance().createConnection();
//					} catch (XMPPException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//						MyLog.e("XMPP", "登录前链接错误");
//					}
					new XmppRunnable(mainActivity.loginHandler, XmppRunnable.LOGIN, new String[]{appContext.user.getMid(),appContext.upass});
				}
			}
			
		});
		

	}
	/**
	 * 更新用户的地址信息
	 * @param user
	 * @param longitude
	 * @param latitude
	 */
	public void updatePlace(User user,String longitude,String latitude){
		
		updatePlace(user.getMid(), user.getToken(), longitude, latitude, null);
	}
	public void updatePlace(String mid,String token,String longitude,String latitude,AjaxCallBack<String> mCallBack){
		AjaxParams params = new AjaxParams();
		params.put("cmd", "Member.updateLocation");
		params.put("mid", mid);
		params.put("lng", longitude);
		params.put("token", token);
		params.put("lat", latitude);
		fh.get(Constant.URL_BASE, params, mCallBack);
	}
	
	/**
	 * 注册
	 * @param userid
	 * @param pass
	 * @param uname
	 * @param mCallBack
	 */
	public void register(String userid,String pass,String uname,AjaxCallBack<String> mCallBack){
		AjaxParams params = new AjaxParams();
		params.put("cmd", "Member.register");
		params.put("uname", uname);
		params.put("pwd", pass);
		if(StringUtils.isPhone(userid)){
			params.put("registby", "phone");
			params.put("mobile", userid);
		}else if(StringUtils.isEmail(userid)){
			params.put("registby", "email");
			params.put("email", userid);
		}
		fh.get(Constant.URL_BASE, params, mCallBack);
		
	}
	/**
	 * 拿球友详细信息
	 * @param uid 该球友id
	 * @param mid 当前用户id
	 * @param lat 
	 * @param lng
	 * @param mCallBack
	 */
	public void getBallFriendDetail(String uid,String mid,String lat,String lng,AjaxCallBack<String> mCallBack ){
		AjaxParams params = new AjaxParams();
		params.put("cmd", "Member.MemberInfo");
		if(!StringUtils.isEmail(mid)){
			params.put("mid", mid);
		}
		params.put("id",uid);
		params.put("lng", lng);
		params.put("lat", lat);
		fh.get(Constant.URL_BASE, params, mCallBack);
	}
	
	/**
	 * 拿个人相集
	 * @param uid
	 * @param mCallBack
	 */
	public void getPhoto(String uid,AjaxCallBack<String> mCallBack ){
		AjaxParams params = new AjaxParams();
		params.put("cmd", "Member.getPicList");
		params.put("mid",uid);
		fh.get(Constant.URL_BASE, params, mCallBack);
	}
	/**
	 * 上传头像
	 * @param user
	 * @param in
	 * @param mCallBack
	 */
	public void uploadFace(User user,InputStream in,AjaxCallBack<String> mCallBack){
		AjaxParams params = new AjaxParams();
		params.put("cmd", "Member.changeFace");
		params.put("mid",user.getMid());
		params.put("token",user.getToken());
		params.put("img",in, "face.jpg", "image/jpeg");
		fh.post(Constant.URL_BASE, params, mCallBack);
		
	}
	/**
	 * 上传照片
	 * @param user
	 * @param in
	 * @param mCallBack
	 */
	public void uploadPhoto(User user,InputStream in,AjaxCallBack<String> mCallBack){
		AjaxParams params = new AjaxParams();
		params.put("cmd", "Member.uploadPic");
		params.put("mid",user.getMid());
		params.put("token",user.getToken());
		params.put("img",in,"temp.jpg", "image/jpeg");
		fh.post(Constant.URL_BASE, params, mCallBack);
		
	}
	/**
	 * 删除个人照片
	 * @param user
	 * @param id
	 * @param mCallBack
	 */
	public void deletPhoto(User user,String id,AjaxCallBack<String> mCallBack){
		AjaxParams params = new AjaxParams();
		params.put("cmd", "Member.deleteFile");
		params.put("mid",user.getMid());
		params.put("token",user.getToken());
		params.put("id",id);
		fh.get(Constant.URL_BASE, params, mCallBack);
		
	}
	/**
	 * 增加积分
	 * @param user
	 * @param integral
	 * @param mCallBack
	 */
	int integral;
	public void addCredits(final Context activity,User user,final int type,final String msg){
		integral = 0;
		if(!SharedPreferencesUtil.judgeIntegral(activity.getApplicationContext(), type)){
			return;
		}
		
		switch (type) {
		case 0://登陆
			
			integral = 50;
			break;
		case 1://完善个人资料
			integral = 100;
			break;
		case 2://上传真实图片
			integral = 50;
			break;
		case 3://上传成绩
			integral = 150;
			break;
		case 4://绑定微博(第一期没有)
			integral = 80;
			break;
		case 5://常出没地
			integral = 100;
			break;
		case 6://完成签名
			integral = 50;
			break;
		case 7://完成标签
			integral = 50;
			break;
		case 8://客户端好评
			integral = 100;
			break;

		
		}
		if(user==null){
			return;
		}
		AjaxParams params = new AjaxParams();
		params.put("cmd", "Member.setcredits");
		params.put("mid",user.getMid());
		params.put("token",user.getToken());
		params.put("type",type+"");
		params.put("credits",integral+"");
		fh.get(Constant.URL_BASE, params, new AjaxCallBack<String>() {

			
			@Override
			public void onSuccess(String t) {
				// TODO Auto-generated method stub
				super.onSuccess(t);
				SharedPreferencesUtil.StatisticsIntegral(activity.getApplicationContext(), type);
				
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
					if(activity instanceof Activity){
						MyToast.customToast(activity, Toast.LENGTH_SHORT,MyToast.TOAST_MSG_SUCCESS_TITLE, msg+"您获得"+integral+"积分", Constant.TOAST_IMG_SUCCESS);
					}
					
					
				} else {
					WrongResponse wrongResponse = ValidateUtil
							.wrongResponse(t);
					if (wrongResponse.show) {
						MyToast.centerToast(activity,
								wrongResponse.msg,
								Toast.LENGTH_SHORT);
					} else {
//						MyToast.centerToast(activity,
//								"积分获取失败", Toast.LENGTH_SHORT);
						MyLog.v("积分获取失败", wrongResponse.msg);
					}
				}

			}

			@Override
			public void onFailure(Throwable t, String strMsg) {
				// TODO Auto-generated method stub
				super.onFailure(t, strMsg);
				
				NetUtil.requestError(activity, null);
			}
			
		});
	}
	/**
	 * 读取积分信息
	 * @param user
	 * @param mCallBack
	 */
	public void readCredits(User user,AjaxCallBack<String> mCallBack){
		if(user==null){
			mCallBack.onFailure(null, "当前用户未登陆");
			return;
		}
		AjaxParams params = new AjaxParams();
		params.put("cmd", "Member.getcredits");
		params.put("mid",user.getMid());
		
		params.put("token",user.getToken());
		fh.get(Constant.URL_BASE, params, mCallBack);
	}
	/**
	 *更新用户信息
	 * @param user
	 * @param data
	 * @param mCallBack
	 */
	public void updateInfo(final Activity activity,final User user,HashMap<String,String> data ,final Set<Integer> typeSet,final AjaxCallBack<String> mCallBack){
		if(user==null){
			mCallBack.onFailure(null, "当前用户未登陆");
			return;
		}
		AjaxParams params = new AjaxParams();
		params.put("cmd", "Member.updateinfo");
		params.put("mid",user.getMid());
		params.put("token",user.getToken());
		Set<String> keys = data.keySet();
		for (String string : keys) {
			params.put(string,data.get(string));
		}
		fh.get(Constant.URL_BASE, params, new AjaxCallBack<String>() {
			@Override
			public void onSuccess(String t) {
				// TODO Auto-generated method stub
				super.onSuccess(t);
				for (Integer type : typeSet) {
					addCredits(activity, user, type, user.getUname());
				}
				mCallBack.onSuccess(t);
			}
			@Override
			public void onFailure(Throwable t, String strMsg) {
				// TODO Auto-generated method stub
				super.onFailure(t, strMsg);
				mCallBack.onFailure(t, strMsg);
			}
		});
	}

	/**
	 * 添加关注
	 * @param user
	 * @param fid
	 * @param mCallBack
	 */
	public void addFocus(User user,String fid,AjaxCallBack<String> mCallBack){
		AjaxParams params = new AjaxParams();
		params.put("cmd", "Member_friend.addToFocus");
		params.put("mid",user.getMid());
		params.put("token",user.getToken());
		params.put("fid",fid);
		fh.get(Constant.URL_BASE, params, mCallBack);
		
	}
	/**
	 * 删除关注
	 * @param user
	 * @param fid
	 * @param mCallBack
	 */
	public void removeFocus(User user,String fid,AjaxCallBack<String> mCallBack){
		AjaxParams params = new AjaxParams();
		params.put("cmd", "Member_friend.removeFocus");
		params.put("mid",user.getMid());
		params.put("token",user.getToken());
		params.put("fid",fid);
		fh.get(Constant.URL_BASE, params, mCallBack);
		
	}
	
	/**
	 * 成绩上传
	 * @param user
	 * @param date
	 * @param objid
	 * @param scores
	 * @param mCallBack
	 */
	public void uploadScore(User user,String date,String objid,List<Score> scores,AjaxCallBack<String> mCallBack){
		AjaxParams params = new AjaxParams();
		params.put("cmd", "Member.insertGofMark");
		params.put("mid",user.getMid());
		params.put("token",user.getToken());
		
		StringBuffer data = new StringBuffer();
		data.append("{\"markdate\":\"");
		data.append(date);
		data.append("\",\"objid\":");
		data.append(objid);
		data.append(",\"grade\":[");
		
		for (int i = 0;i< scores.size();i++) {
			data.append("{\"hole\":"+scores.get(i).getId()+",\"putter\":"+scores.get(i).getTuiCount()+",\"pole\":"+scores.get(i).getTotleCount()+"}");
			if((i+1)!=scores.size()){
				data.append(",");
			}
		}
		data.append("]}");
		
		MyLog.v("data",data.toString());
		params.put("data",data.toString());
		fh.get(Constant.URL_BASE, params, mCallBack);
		
	}
	
	/**
	 * 拿成绩接口
	 * @param user
	 * @param mCallBack
	 */
	public void getGrade(User user,AjaxCallBack<String> mCallBack){
		AjaxParams params = new AjaxParams();
		params.put("cmd", "Member.getmark");
		params.put("mid",user.getMid());
		params.put("token",user.getToken());
		fh.get(Constant.URL_BASE, params, mCallBack);
		
	}
	/**
	 * 更改拉黑状态
	 * @param user
	 * @param fid
	 * @param mCallBack
	 * @param pullblack 是否拉黑 true 为拉黑 false 为洗白
	 */
	public void updateStatus(User user,String fid,boolean pullblack,AjaxCallBack<String> mCallBack){
		AjaxParams params = new AjaxParams();
		params.put("cmd", "Member_friend.updateStatus");
		params.put("mid",user.getMid());
		params.put("token",user.getToken());
		params.put("fid",fid);
		if(pullblack){
			params.put("ftype","bad");
		}else{
			params.put("ftype","clear");
			
		}
		
		fh.get(Constant.URL_BASE, params, mCallBack);
		
	}
	
}
