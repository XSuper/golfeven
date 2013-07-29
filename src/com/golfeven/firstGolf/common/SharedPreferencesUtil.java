package com.golfeven.firstGolf.common;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * 用于读取sharedpreference
 * @author lenovo
 *
 */
public class SharedPreferencesUtil {

	/**
	 * 保存数据到SharedPreferences
	 * @param context Activity 上下文环境
	 * @param token Oauth2AccessToken
	 */
	public static void keep(String preferenceName,Context context, Map<String, String> data) {
		SharedPreferences pref = context.getSharedPreferences( preferenceName, Context.MODE_APPEND);
		Editor editor = pref.edit();
		Set<String> keys =data.keySet();
		Iterator<String> myi = keys.iterator();
		while (myi.hasNext()) {
			String key = (String) myi.next();
			editor.putString(key,data.get(key));
		}
		editor.commit();
	}
	/**
	 * 清空sharedpreference
	 * @param context
	 */
	public static void clear(String preferenceName,Context context){
	    SharedPreferences pref = context.getSharedPreferences(preferenceName, Context.MODE_APPEND);
	    Editor editor = pref.edit();
	    editor.clear();
	    editor.commit();
	}
	/**
	 * 从SharedPreferences读取数据
	 */
	public static String read(String preferenceName,Context context,String key){
		SharedPreferences pref = context.getSharedPreferences(preferenceName, Context.MODE_APPEND);
		return pref.getString(key, "");
	}
	/**
	 * 保存用户名和密码
	 * @param context
	 * @param uname
	 * @param upass
	 * @return
	 */
	public static void saveUser(Context context,String uname,String upass){
		Map<String, String> user = new HashMap<String, String>();
		user.put(Constant.FILE_USER_USERID, uname);
		user.put(Constant.FILE_USER_UPASS,upass);
		SharedPreferencesUtil.keep(Constant.FILE_USER,
				context, user);
	}
	/**
	 * 清除用户名和密码
	 * @param context
	 */
	public static void clearUser(Context context){
		clear(Constant.FILE_USER, context);
	}
	/**
	 * 判断请求是否能再次得到积分
	 * @param context
	 * @param type
	 * @return
	 */
	public static boolean judgeIntegral(Context context,int type){
		if(type ==0){
			String time =
					read(Constant.FILE_USER, context, Constant.FILE_INTEGRAL_TYPE+type);
			return !StringUtils.isOneDay(time);
		}
		String flag = read(Constant.FILE_USER, context, Constant.FILE_INTEGRAL_TYPE+type);
		if(StringUtils.isEmpty(flag)){
			return true;
		}else {
			return false;
		}
		
	}
	/**
	 * 统计积分
	 * @param context
	 * @param uname
	 * @param upass
	 */
	public static void StatisticsIntegral(Context context,int type){
		Map<String, String> integral = new HashMap<String, String>();
		if(type == 0){
			integral.put(Constant.FILE_INTEGRAL_TYPE+type, StringUtils.getDateStr(new Date()));
		}else{
			
			integral.put(Constant.FILE_INTEGRAL_TYPE+type, "true");
		}
		SharedPreferencesUtil.keep(Constant.FILE_USER,
				context, integral);
	}

}
