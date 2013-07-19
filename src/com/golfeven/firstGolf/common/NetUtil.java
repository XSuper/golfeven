package com.golfeven.firstGolf.common;

import com.golfeven.firstGolf.widget.MyToast;

import android.content.Context;
import android.widget.Toast;

public class NetUtil {
	public static void requestError(Context context,String msg){
		MyToast.centerToast(context,( StringUtils.isEmpty(msg)?"网络连接失败":msg), Toast.LENGTH_SHORT);
	}

}
