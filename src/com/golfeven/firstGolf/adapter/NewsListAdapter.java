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
import com.golfeven.firstGolf.bean.News;
import com.golfeven.firstGolf.common.Constant;
import com.golfeven.firstGolf.ui.NewsDetailActivity;

public class NewsListAdapter extends MBaseAdapter{
	
	public FinalBitmap fb;
	private String typeid="44";
	private String order="1";
	
	
	public void setTypeid(String typeid) {
		this.typeid = typeid;
	}
	public void setOrder(String order) {
		this.order = order;
	}
	public void setOptions(String typeid,String order){
		this.typeid = typeid;
		this.order = order;
	}
	
	
	public NewsListAdapter(Context context,List<News> news) {
		this.context = context;
		this.datas = news;
		fb = getAppContext().getFB();
	}
	


	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.item_common, null);
		}
		// TODO Auto-generated method stub
		final News mNews= (News)datas.get(position);
		ImageView img = (ImageView)convertView.findViewById(R.id.item_common_img);
		TextView title =(TextView)convertView.findViewById(R.id.item_common_title);
		TextView digest =(TextView)convertView.findViewById(R.id.item_common_digest);
		
		
		title.setText(mNews.getTitle());
		digest.setText(mNews.getTypename());
//		fb.configLoadfailImage(Constant.IMG_LOADING_FAIL);
//		fb.configLoadingImage(Constant.IMG_LOADING);
		fb.display(img, Constant.URL_IMG_BASE+mNews.getLitpic());
		//fb.display(img, "http://www.a8.hk/uploads/allimg/130617/6-13061GJ959203-lp.jpg");
		convertView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context,NewsDetailActivity.class);
				intent.putExtra("index",position);
				intent.putExtra("typeid",typeid);
				intent.putExtra("order",order);
				intent.putParcelableArrayListExtra("List", (ArrayList<News>)datas);
				context.startActivity(intent);
				
				
			}
		});
		
		
		return convertView;
		
		
	}
	

}
