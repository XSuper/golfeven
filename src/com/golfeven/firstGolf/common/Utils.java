package com.golfeven.firstGolf.common;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.util.DisplayMetrics;

import com.golfeven.firstGolf.bean.Integral;
import com.golfeven.firstGolf.bean.Score;
import com.golfeven.firstGolf.ui.LoginActivity;

/**
 * 其让杂乱的帮助类
 * 
 * @author ISuper
 * 
 */
public class Utils {
	
	
	  //去重复方法
    public static  ArrayList RemoveSame(ArrayList list)
    {
        //上面写的那句是多余的，这个是最终的  
        for (int i = 0; i < list.size() - 1; i++)
        {
            for (int j = i + 1; j < list.size(); j++)
            {
                if (list.get(i).equals(list.get(j)))
                {
                    list.remove(j);
                    j--;
                }
            }
        }
        return list;
    }
	/**
	 * 弹出登录框
	 * @param activity
	 */
	public static void toLogin(Activity activity){
		Intent intent = new Intent(activity, LoginActivity.class);
		intent.putExtra("dialog",true);
		activity.startActivity(intent);
	}
	
	public static DisplayMetrics dm = null;
	/**
	 * 得到屏幕的宽
	 * @param activity
	 * @return
	 */
	public static int getScreenWith(Activity activity){
		if(dm == null){
			dm = new DisplayMetrics();
			activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		}
		return dm.widthPixels;
	}

	public static void ToActivity(Activity activity, Intent intent, boolean f) {
		// TODO Auto-generated method stub
		activity.startActivity(intent);
		if (f) {
			activity.finish();
		}

	}

	public static void ToActivity(Activity activity, Class c, boolean f) {
		Intent intent = new Intent(activity, c);
		activity.startActivity(intent);
		if (f) {
			activity.finish();
		}

	}

	/**
	 * 等待加载的进度条
	 * 
	 * @param context
	 * @param progressDialog
	 */
	public static ProgressDialog initWaitingDialog(Context context, String msg) {
		ProgressDialog progressDialog = new ProgressDialog(context);
		progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		if (StringUtils.isEmail(msg)) {
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
	 * 判断是否成绩都已经录入成功
	 * @param scores
	 * @return
	 */
	public static boolean scoreOk(List<Score> scores){
		boolean flag = true;
		for (Score score : scores) {
			if(StringUtils.isEmpty(score.getTotleCount())){
				flag = false;
				break;
			}
		}
		
		return flag;
	}

	/**
	 * 计算总积分
	 * 
	 * @param integrals
	 * @return
	 */
	public static int getToTleIntegral(List<Integral> integrals) {
		if (integrals == null || integrals.size() == 0) {
			return 0;
		}
		int totle = 0;
		for (int i = 0; i < integrals.size(); i++) {
			totle += Integer.parseInt(integrals.get(i).getCredits());
		}
		return totle;
	}

	/**
	 * 得到我是否关注对方
	 * @param str
	 * @return
	 */
	public static boolean getMState(String str){
		boolean flag = false;
		int s = getState(str)[0];
		if(s==0||s==1){
			flag = true;
		}
		return flag;
		
	}
	/**
	 * 得到球友间关系 
	 * 
	 * -2不存在关系 
	 * -1 我把球友拉黑 
	 * 0 我加球友为普通好友
	 * 1我加球友为关注好友
	 * 
	 * @param str
	 * @return
	 */
	public static int[] getState(String str) {
		int[] m = new int[2];
		m[0] = -2;
		m[1] = -2;

		if (StringUtils.isEmpty(str) || str.indexOf(",") == -1) {
		} else {
			String num[] = str.trim().split(",");

			try {
				m[0] = Integer.parseInt(num[0].trim());
				m[1] = Integer.parseInt(num[1].trim());
			} catch (Exception e) {
				MyLog.e("000",e.toString());
			}
		}
		return m;

	}

	/**
	 * 
	 * @param str
	 * @return
	 * 
	 *         mint[0] 关注按钮显示 1为关注,0为不显示,-1为取消关注
	 *         mint[1] 消息按钮显示 1为发消息,0为不显示,-1为查看消息 
	 *         mint[2] 拉黑按钮显示 1为拉黑,0为不显示,-1为洗白
	 */
	public static int[] judgeShow(String str) {
		int[] mint = new int[3];
		mint[0] = 0;
		mint[1] = 0;
		mint[2] = 0;
		int m = -2;
		int o = -2;
		int s[] = getState(str);
		m = s[0];
		o = s[1];

		// -2不存在关系
		// -1 我把球友拉黑
		// 0 我加球友为普通好友，
		// 1我加球友为关注好友

		switch (m) {
		case -2:
			mint[0] = 1;
			mint[1] = -1;
			break;
		case -1:
			mint[2] = -1;
			break;
		case 0:
			mint[0] = -1;
			mint[1] = 1;
			break;
		case 1:
			mint[0] = -1;
			mint[1] = 1;
			break;
		}
		switch (o) {
		case -2:
			mint[2] = 0;
			if (mint[1] == -1) {
				mint[1] = 0;
			}
			break;
		case -1:
			mint[1] = -1;
			break;
		case 0:
			if (mint[2] == 0) {
				mint[2] = 1;
			}
			break;
		case 1:
			if (mint[2] == 0) {
				mint[2] = 1;
			}
			break;
		}

		return mint;
	}
	
	
	/**
	 * 根据后台传出的距离数据 得到距离
	 * @param distance
	 * @return
	 */
	public static String getDestance(String distance){
		String s = "未知";
		if(!StringUtils.isEmpty(distance)&&distance.indexOf("9999")<=-1){
			float m = Float.parseFloat(distance);
			double km = m/1000;
			String dis = String.format("%.2f", km);
			s=dis+"km";
		}
		return s;
	}
	

}
