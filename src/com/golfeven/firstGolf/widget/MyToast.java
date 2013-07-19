package com.golfeven.firstGolf.widget;


import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.golfeven.firstGolf.R;

public class MyToast {
	// toast信息常量
		public static final String TOAST_MSG_SUCCESS_TITLE = "成功";
		public static final String TOAST_MSG_ERROR_TITLE = "失败";
		public static final String TOAST_MSG_WARNING_TITLE = "提示";

		public static final String TOAST_MSG_LOAD_SUCCESS_CONTENT = "信息更新成功！";
		public static final String TOAST_MSG_LOAD_ERROR_CONTENT = "信息更新失败！";

		public static final String TOAST_MSG_LOADMORE_SUCCESS_CONTENT = "信息加载成功！";
		public static final String TOAST_MSG_LOADMORE_ERROR_CONTENT = "信息加载失败！";

		public static final String TOAST_MSG_REFRESH_SUCCESS_CONTENT = "刷新成功！";
		public static final String TOAST_MSG_REFRESH_ERROR_CONTENT = "刷新失败！";

		public static final String TOAST_MSG_NOMORE_CONTENT = "没有更多的内容！";
	static Toast toast;

	/**
	 * 居中的Toast
	 * 
	 * @param context
	 * @param content
	 * @param duration
	 */
	public static void centerToast(Context context, String content, int duration) {
		toast = Toast.makeText(context, content, duration);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}

	/**
	 * 自定义样式的Toast
	 * @param context
	 * @param duration
	 * @param title
	 * @param content
	 * @param img
	 */
	public static void customToast(Context context, int duration,String title,String content, int img) {
		LayoutInflater inflater = ((Activity)context).getLayoutInflater();
		View view = inflater.inflate(R.layout.userdefinedtoast,
				(ViewGroup) ((Activity)context).findViewById(R.id.toast_layout));
		TextView txtView_Title = (TextView) view.findViewById(R.id.txt_Title);
		TextView txtView_Context = (TextView) view
				.findViewById(R.id.txt_context);
		ImageView imageView = (ImageView) view.findViewById(R.id.image_toast);
		txtView_Title.setText(title);
		txtView_Context.setText(content);
		imageView.setImageResource(img);
		toast = new Toast(context);
		//toast.setGravity(Gravity.CENTER, 0, 0);
		toast.setDuration(duration);
		toast.setView(view);
		toast.show();
	}

}
