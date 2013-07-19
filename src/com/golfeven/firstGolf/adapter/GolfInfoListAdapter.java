package com.golfeven.firstGolf.adapter;

import java.util.ArrayList;
import java.util.List;

import net.tsz.afinal.FinalBitmap;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.golfeven.firstGolf.R;
import com.golfeven.firstGolf.base.MBaseAdapter;
import com.golfeven.firstGolf.bean.GolfInfo;
import com.golfeven.firstGolf.bean.News;
import com.golfeven.firstGolf.common.Constant;
import com.golfeven.firstGolf.ui.GolfInfoDetailActivity;

public class GolfInfoListAdapter extends MBaseAdapter{
	
	public FinalBitmap fb;
	private String typeid="44";
	



	public void setTypeid(String typeid) {
		this.typeid = typeid;
	}



	public GolfInfoListAdapter(Context context,List<GolfInfo> golfInfos) {
		this.context = context;
		this.datas = golfInfos;
		fb = getAppContext().getFB();
	}
	


	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.item_common, null);
		}
		// TODO Auto-generated method stub
		GolfInfo golfInfo = (GolfInfo)datas.get(position);
		ImageView img = (ImageView)convertView.findViewById(R.id.item_common_img);
		TextView title =(TextView)convertView.findViewById(R.id.item_common_title);
		TextView digest =(TextView)convertView.findViewById(R.id.item_common_digest);
		
		
		title.setText(golfInfo.getTitle());
		digest.setText(golfInfo.getTypename());
		fb.display(img, Constant.URL_IMG_BASE+golfInfo.getLitpic());
//		fb.display(img, "http://www.a8.hk/uploads/allimg/130617/6-13061GJ959203-lp.jpg");
		convertView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context,GolfInfoDetailActivity.class);
				intent.putExtra("index",position);
				intent.putExtra("typeid",typeid);
				intent.putParcelableArrayListExtra("List", (ArrayList<GolfInfo>)datas);
				context.startActivity(intent);
				
				
			}
		});
		
		
		return convertView;
		
		
	}
	

}
