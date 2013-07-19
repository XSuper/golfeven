package com.golfeven.firstGolf.adapter;

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
import com.golfeven.firstGolf.bean.Gallery;
import com.golfeven.firstGolf.bean.GolfInfo;
import com.golfeven.firstGolf.common.Constant;
import com.golfeven.firstGolf.ui.GalleryDetailActivity;

public class GalleryListAdapter extends MBaseAdapter{
	
	public FinalBitmap fb;
	
	public GalleryListAdapter(Context context,List<GolfInfo> golfInfos) {
		this.context = context;
		this.datas = golfInfos;
		fb = getAppContext().getFB();
	}
	


	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.item_common, null);
		}
		// TODO Auto-generated method stub
		final Gallery gallery = (Gallery)datas.get(position);
		ImageView img = (ImageView)convertView.findViewById(R.id.item_common_img);
		TextView title =(TextView)convertView.findViewById(R.id.item_common_title);
		TextView digest =(TextView)convertView.findViewById(R.id.item_common_digest);
		
		
		title.setText(gallery.getTitle());
		digest.setText(gallery.getSource());
		fb.display(img, Constant.URL_IMG_BASE+gallery.getLitpic());
//		fb.display(img, "http://www.a8.hk/uploads/allimg/130617/6-13061GJ959203-lp.jpg");
		
		convertView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context, GalleryDetailActivity.class);
				intent.putExtra("galleryId",gallery.getId());
				context.startActivity(intent);
				
			}
		});
		
		return convertView;
		
		
	}
	

}
