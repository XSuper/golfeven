package com.golfeven.firstGolf.adapter;

import java.util.List;

import net.tsz.afinal.FinalBitmap;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.golfeven.firstGolf.R;
import com.golfeven.firstGolf.base.MBaseAdapter;
import com.golfeven.firstGolf.bean.BallPark;
import com.golfeven.firstGolf.common.Constant;
import com.golfeven.firstGolf.common.StringUtils;
import com.golfeven.firstGolf.common.Utils;
import com.golfeven.firstGolf.ui.BallParkDetailActivity;
import com.golfeven.firstGolf.ui.MainActivity;

public class BallParkListAdapter extends MBaseAdapter {

	public FinalBitmap fb;

	public boolean isChoice = false;//用来判断是列表展示 还是 选择球场

	public BallParkListAdapter(Context context, List<BallPark> datas) {
		this.context = context;
		this.datas = datas;
		fb = getAppContext().getFB();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.item_ballpark, null);
		}
		// TODO Auto-generated method stub
		final BallPark ballPark = (BallPark) datas.get(position);
		ImageView img = (ImageView) convertView
				.findViewById(R.id.item_ballpark_img);
		TextView title = (TextView) convertView
				.findViewById(R.id.item_ballpark_title);
		TextView type = (TextView) convertView
				.findViewById(R.id.item_ballpark_type);
		TextView distance = (TextView) convertView
				.findViewById(R.id.item_ballpark_distance);

		DisplayMetrics dm = new DisplayMetrics();
		 ((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(dm);
		 LayoutParams params = new LayoutParams(dm.widthPixels/4, dm.widthPixels/4);
		 params.setMargins(15, 15, 15, 15);
		 img.setLayoutParams(params);
		
		title.setText(ballPark.getTitle());
		type.setText(ballPark.getTypename());
		distance.setText(Utils.getDestance(ballPark.getDistance()));
		fb.display(img, Constant.URL_IMG_BASE + ballPark.getLitpic());
		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				if (!isChoice) {

					Intent intent = new Intent(context,
							BallParkDetailActivity.class);
					intent.putExtra("id", ballPark.getId());
					intent.putExtra("ballPark", ballPark);
					context.startActivity(intent);
				} else {

					Intent intent = new Intent(context, MainActivity.class);
					intent.putExtra("ballpark", ballPark);
					((Activity) context).setResult(
							MainActivity.PLAYBALLFRAMEGETBALLPARKCODE, intent);
					((Activity) context).finish();
					isChoice=false;
				}

			}
		});
		return convertView;

	}

}
