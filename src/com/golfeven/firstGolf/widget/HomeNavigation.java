package com.golfeven.firstGolf.widget;

import java.util.ArrayList;
import java.util.List;

import net.tsz.afinal.FinalBitmap;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.golfeven.AppContext;
import com.golfeven.firstGolf.R;
import com.golfeven.firstGolf.bean.BallFriend;
import com.golfeven.firstGolf.bean.BallPark;
import com.golfeven.firstGolf.bean.BallTeam;
import com.golfeven.firstGolf.bean.Gallery;
import com.golfeven.firstGolf.bean.GolfInfo;
import com.golfeven.firstGolf.bean.News;
import com.golfeven.firstGolf.common.Constant;
import com.golfeven.firstGolf.ui.BallFriendDetailActivity;
import com.golfeven.firstGolf.ui.BallParkDetailActivity;
import com.golfeven.firstGolf.ui.BallTeamDetailActivity;
import com.golfeven.firstGolf.ui.GalleryDetailActivity;
import com.golfeven.firstGolf.ui.GolfInfoDetailActivity;
import com.golfeven.firstGolf.ui.MyDetailActivity;
import com.golfeven.firstGolf.ui.NewsDetailActivity;

public class HomeNavigation extends LinearLayout {
	private LinearLayout mtop;
	private TextView title, more;
	private HorizontalScrollView hScroll;
	private MyImageView[] imgs;
	
	private LinearLayout imgLayout;
	
	private FinalBitmap fb;
	private Context context;
	
	private List datas;

	
	
	/**
	 * 点击更多后跳转的页面
	 * @param cls
	 */
	public void toMoreActivity(final Class cls){
		more.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context, cls);
				context.startActivity(intent);
				
			}
		});
	}

	public LinearLayout getMtop() {
		return mtop;
	}

	public void setMtop(LinearLayout mtop) {
		this.mtop = mtop;
	}

	public TextView getTitle() {
		return title;
	}

	public void setTitle(TextView title) {
		this.title = title;
	}

	public TextView getMore() {
		return more;
	}

	public void setMore(TextView more) {
		this.more = more;
	}

	public HorizontalScrollView gethScroll() {
		return hScroll;
	}

	public void sethScroll(HorizontalScrollView hScroll) {
		this.hScroll = hScroll;
	}



	public HomeNavigation(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		initViews(context);
	}

	public HomeNavigation(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		initViews(context);
		init(attrs);

		// TODO Auto-generated constructor stub
	}

	private void initViews(Context context) {
		this.context = context;
		View view = LayoutInflater.from(context).inflate(
				R.layout.widget_home_navigation, this);
		mtop = (LinearLayout) view
				.findViewById(R.id.widget_home_navigation_top);
		title = (TextView) view.findViewById(R.id.widget_home_navigation_title);
		more = (TextView) view.findViewById(R.id.widget_home_navigation_more);
		hScroll = (HorizontalScrollView) view
				.findViewById(R.id.widget_home_navigation_hscroll);
		imgLayout =(LinearLayout)view.findViewById(R.id.widget_home_navigation_imgs);
		imgs = new MyImageView[Constant.HOME_NAVIGATIN_IMG_SIZE];
		for(int i = 0;i<Constant.HOME_NAVIGATIN_IMG_SIZE;i++){
			MyImageView img = MyImageView.CreateHomeImageView(context);
			imgLayout.addView(img);
			imgs[i] = img;
		}
		
		
		fb = ((AppContext)context.getApplicationContext()).getFB();
	}

	private void init(AttributeSet attrs) {
		TypedArray a = getContext().obtainStyledAttributes(attrs,
				R.styleable.widget_home_navigation);
		try {
			String title = a
					.getString(R.styleable.widget_home_navigation_title);

			if (title != null) {
				this.title.setText(title);
			}
		} finally {
			a.recycle();
		}

	}

	public void initData(List datas) {
		if(datas==null||datas.size()<=0){
			return;
		}
		this.datas = datas;
		Class cls = datas.get(0).getClass();
		if (cls == GolfInfo.class) {
			
			for (int i = 0; i < Constant.HOME_NAVIGATIN_IMG_SIZE && i < datas.size(); i++) {
				GolfInfo golfInfo = (GolfInfo) datas.get(i);
				GolfClick click = new GolfClick();
				imgs[i].setDescribe(golfInfo.getTitle());
				imgs[i].setTag(i);
				imgs[i].setOnClickListener(click);
				fb.display(imgs[i],
						Constant.URL_IMG_BASE + golfInfo.getLitpic());
			}

		} else if (cls == News.class) {
			NewsClick click = new NewsClick();
			for (int i = 0; i < Constant.HOME_NAVIGATIN_IMG_SIZE  && i < datas.size(); i++) {
				News news = (News) datas.get(i);
				imgs[i].setDescribe(news.getTitle());
				imgs[i].setOnClickListener(click);
				imgs[i].setTag(i);
				fb.display(imgs[i], Constant.URL_IMG_BASE + news.getLitpic());
			}
		}else if (cls == Gallery.class) {
			GalleryClick click = new GalleryClick();
			for (int i = 0; i < Constant.HOME_NAVIGATIN_IMG_SIZE  && i < datas.size(); i++) {
				Gallery gallery = (Gallery) datas.get(i);
				imgs[i].setTag(gallery.getId());
				imgs[i].setOnClickListener(click);
				imgs[i].setDescribe(gallery.getTitle());
				fb.display(imgs[i], Constant.URL_IMG_BASE + gallery.getLitpic());
			}
		}else if (cls == BallPark.class) {
			BallParkClick click = new BallParkClick();
			for (int i = 0; i < Constant.HOME_NAVIGATIN_IMG_SIZE  && i < datas.size(); i++) {
				BallPark ballPark = (BallPark) datas.get(i);
				imgs[i].setTag(ballPark);
				imgs[i].setOnClickListener(click);
				imgs[i].setDescribe(ballPark.getTitle());
				fb.display(imgs[i], Constant.URL_IMG_BASE + ballPark.getLitpic());
			}
		}else if (cls == Gallery.class) {
			GalleryClick click = new GalleryClick();
			for (int i = 0; i < Constant.HOME_NAVIGATIN_IMG_SIZE  && i < datas.size(); i++) {
				Gallery gallery = (Gallery) datas.get(i);
				imgs[i].setTag(gallery.getId());
				imgs[i].setOnClickListener(click);
				imgs[i].setDescribe(gallery.getTitle());
				fb.display(imgs[i], Constant.URL_IMG_BASE + gallery.getLitpic());
			}
		}else if (cls == BallTeam.class) {
			BallTeamClick click = new BallTeamClick();
			for (int i = 0; i < Constant.HOME_NAVIGATIN_IMG_SIZE  && i < datas.size(); i++) {
				BallTeam ballTeam = (BallTeam) datas.get(i);
				imgs[i].setTag(ballTeam);
				imgs[i].setOnClickListener(click);
				imgs[i].setDescribe(ballTeam.getIntroduce());
				fb.display(imgs[i], Constant.URL_IMG_BASE + ballTeam.getLogo());
			}
		}else if (cls == BallFriend.class) {
			BallFriendClick click = new BallFriendClick();
			for (int i = 0; i < Constant.HOME_NAVIGATIN_IMG_SIZE  && i < datas.size(); i++) {
				BallFriend ballFriend = (BallFriend) datas.get(i);
				imgs[i].setTag(ballFriend);
				imgs[i].setDescribe(ballFriend.getUname());
				imgs[i].setOnClickListener(click);
				fb.display(imgs[i], Constant.URL_IMG_BASE + ballFriend.getFace());
			}
		}
	}
	
	
	/**
	 * 新闻导航的点击事件
	 * @author ISuper
	 *
	 */
	class NewsClick implements OnClickListener{

		@Override
		public void onClick(View v) {
			int index = (Integer)v.getTag();
			
			Intent intent = new Intent(context,NewsDetailActivity.class);
			intent.putExtra("index",index);
			intent.putExtra("typeid","44");
			intent.putExtra("order",1);
			intent.putParcelableArrayListExtra("List", (ArrayList<News>)datas);
			context.startActivity(intent);
			
		}
		
	}
	class GolfClick implements OnClickListener{
		
		@Override
		public void onClick(View v) {
			int index = (Integer)v.getTag();
			
			Intent intent = new Intent(context,GolfInfoDetailActivity.class);
			intent.putExtra("index",index);
			intent.putExtra("typeid","137");
			intent.putParcelableArrayListExtra("List", (ArrayList<GolfInfo>)datas);
			context.startActivity(intent);
			
		}
		
	}
	class GalleryClick implements OnClickListener{

		@Override
		public void onClick(View v) {
			String id=(String)v.getTag();
			Intent intent = new Intent(context, GalleryDetailActivity.class);
			intent.putExtra("galleryId",id);
			context.startActivity(intent);
			
		}
		
	}
	class BallParkClick implements OnClickListener{
		
		@Override
		public void onClick(View v) {
			BallPark ballPark=(BallPark)v.getTag();
			Intent intent = new Intent(context, BallParkDetailActivity.class);
			intent.putExtra("id",ballPark.getId());
			intent.putExtra("ballPark",ballPark);
			context.startActivity(intent);
			
		}
		
	}
	class BallTeamClick implements OnClickListener{
		
		@Override
		public void onClick(View v) {
			BallTeam ballTeam=(BallTeam)v.getTag();
			Intent intent = new Intent(context, BallTeamDetailActivity.class);
			intent.putExtra("ballTeam",ballTeam);
			context.startActivity(intent);
			
		}
		
	}
	class BallFriendClick implements OnClickListener{
		
		@Override
		public void onClick(View v) {
			if (((AppContext) getContext().getApplicationContext()).isLogin) {
				
				BallFriend ballFriend=(BallFriend)v.getTag();
				Intent intent = new Intent(context, BallFriendDetailActivity.class);
				intent.putExtra("ballFriend",ballFriend);
				context.startActivity(intent);
			}
			
		}
		
	}

}
