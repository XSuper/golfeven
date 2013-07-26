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
			totle += integrals.get(i).getCredits();
		}
		return totle;
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
				m[0] = Integer.parseInt(num[0]);
				m[1] = Integer.parseInt(num[1]);
			} catch (Exception e) {
				// TODO: handle exception
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

}
