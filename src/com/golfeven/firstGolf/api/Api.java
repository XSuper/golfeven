package com.golfeven.firstGolf.api;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Set;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;
import android.app.Activity;
import android.widget.Toast;

import com.golfeven.firstGolf.bean.User;
import com.golfeven.firstGolf.common.Constant;
import com.golfeven.firstGolf.common.SharedPreferencesUtil;
import com.golfeven.firstGolf.common.StringUtils;
import com.golfeven.firstGolf.widget.MyToast;

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
	public void login(String uname,String upass,AjaxCallBack<String> mCallBack) {
		// TODO Auto-generated method stub
		AjaxParams params = new AjaxParams();
		params.put("cmd", "Member.login");
		params.put("userid", uname);
		params.put("pwd", upass);
		fh.get(Constant.URL_BASE, params, mCallBack);
		

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
	public void addCredits(final Activity activity,User user,final int type,final String msg){
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
		params.put("uid",user.getMid());
		params.put("token",user.getToken());
		params.put("type",type+"");
		params.put("credits",integral+"");
		fh.get(Constant.URL_BASE, params, new AjaxCallBack<String>() {

			@Override
			public void onSuccess(String t) {
				// TODO Auto-generated method stub
				super.onSuccess(t);
				SharedPreferencesUtil.StatisticsIntegral(activity.getApplicationContext(), type);
				MyToast.customToast(activity, Toast.LENGTH_SHORT,MyToast.TOAST_MSG_SUCCESS_TITLE, msg+"获得"+integral+"积分", Constant.TOAST_IMG_SUCCESS);
			}

			@Override
			public void onFailure(Throwable t, String strMsg) {
				// TODO Auto-generated method stub
				super.onFailure(t, strMsg);
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
		params.put("uid",user.getMid());
		
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
					addCredits(activity, user, type, null);
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
}
