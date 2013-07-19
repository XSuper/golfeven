package com.golfeven.firstGolf.adapter;

import java.util.List;

import net.tsz.afinal.FinalBitmap;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.golfeven.AppContext;
import com.golfeven.firstGolf.R;
import com.golfeven.firstGolf.base.MBaseAdapter;
import com.golfeven.firstGolf.bean.BallFriend;
import com.golfeven.firstGolf.bean.BallPark;
import com.golfeven.firstGolf.common.Constant;
import com.golfeven.firstGolf.ui.BallFriendDetailActivity;
import com.golfeven.firstGolf.ui.BallParkDetailActivity;
import com.golfeven.firstGolf.ui.MainActivity;

public class BallFriendListAdapter extends MBaseAdapter {

	public FinalBitmap fb;


	public BallFriendListAdapter(Context context, List<BallFriend> datas) {
		this.context = context;
		this.datas = datas;
		fb = getAppContext().getFB();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.item_common, null);
		}
		// TODO Auto-generated method stub
		final BallFriend ballFriend = (BallFriend) datas.get(position);
		ImageView img = (ImageView) convertView
				.findViewById(R.id.item_common_img);
		TextView title = (TextView) convertView
				.findViewById(R.id.item_common_title);
		TextView digest = (TextView) convertView
				.findViewById(R.id.item_common_digest);

		title.setText(ballFriend.getUname());
		digest.setText(ballFriend.getDistance());
		fb.display(img, Constant.URL_IMG_BASE + ballFriend.getFace());
		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				if(((AppContext)context.getApplicationContext()).isLogin){
					
					Intent intent = new Intent(context,
							BallFriendDetailActivity.class);
					intent.putExtra("ballFriend", ballFriend);
					context.startActivity(intent);
				}else{
					Toast.makeText(context, "请先登录",Toast.LENGTH_SHORT).show();
				}

			}
		});
		return convertView;

	}

}
