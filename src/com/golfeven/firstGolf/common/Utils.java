package com.golfeven.firstGolf.common;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;

import com.golfeven.firstGolf.bean.Integral;
import com.golfeven.firstGolf.bean.Score;

/**
 * 其让杂乱的帮助类
 * 
 * @author ISuper
 * 
 */
public class Utils {
	
	

	public static void ToActivity(Activity activity,Intent intent,boolean f) {
		// TODO Auto-generated method stub
		activity.startActivity(intent);
		if(f){
			activity.finish();
		}

	}
	public static void ToActivity(Activity activity,Class c,boolean f){
		Intent intent = new Intent(activity,c);
		activity.startActivity(intent);
		if(f){
			activity.finish();
		}
		
	}
	/**
	 * 等待加载的进度条
	 * @param context
	 * @param progressDialog
	 */
	public static ProgressDialog initWaitingDialog(Context context,String msg){
		ProgressDialog progressDialog
			 = new ProgressDialog(context);
			progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			if(StringUtils.isEmail(msg)){
				msg = "请稍等...";
			}
			progressDialog.setMessage(msg);
			progressDialog.setCanceledOnTouchOutside(false);
			progressDialog.setCancelable(false);
			
		progressDialog.show();
		return progressDialog;
	}
	/**
	 * 根据得到洞数生成
	 * 
	 * @param str
	 *            ”XX洞“
	 * @return
	 */
	public static List<Score> getScores(String str) {
		List<Score> scores = new ArrayList<Score>();
		str = str.replaceAll("洞", "").trim();
		int size = 9;
		try {

			size = Integer.parseInt(str);
		} catch (Exception e) {
			// TODO: handle exception
		}
		for (int i = 1; i <= size; i++) {
			Score score = new Score(i);
			scores.add(score);
		}

		return scores;

	}
	
	/**
	 * 计算总积分
	 * @param integrals
	 * @return
	 */
	public static int getToTleIntegral(List<Integral> integrals){
		int totle = 0;
		for (int i = 0; i < integrals.size(); i++) {
			totle += integrals.get(i).getCredits();
		}
		return totle;
	}

}
