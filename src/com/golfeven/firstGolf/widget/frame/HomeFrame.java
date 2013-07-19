package com.golfeven.firstGolf.widget.frame;

import java.util.ArrayList;
import java.util.List;

import net.tsz.afinal.FinalDb;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.alibaba.fastjson.JSON;
import com.golfeven.firstGolf.R;
import com.golfeven.firstGolf.bean.BallFriend;
import com.golfeven.firstGolf.bean.BallPark;
import com.golfeven.firstGolf.bean.BallTeam;
import com.golfeven.firstGolf.bean.Gallery;
import com.golfeven.firstGolf.bean.GolfInfo;
import com.golfeven.firstGolf.bean.News;
import com.golfeven.firstGolf.common.Constant;
import com.golfeven.firstGolf.ui.BallFriendsActivity;
import com.golfeven.firstGolf.ui.BallParksActivity;
import com.golfeven.firstGolf.ui.BallTeamsActivity;
import com.golfeven.firstGolf.ui.GallerysActivity;
import com.golfeven.firstGolf.ui.GolfInfosActivity;
import com.golfeven.firstGolf.ui.MainActivity;
import com.golfeven.firstGolf.ui.NewsActivity;
import com.golfeven.firstGolf.widget.HomeNavigation;
import com.golfeven.firstGolf.widget.MainPageView;

public class HomeFrame extends LinearLayout {
	public Context context;
	private HomeNavigation nav_news, nav_hotActivity, nav_gallery,
			nav_ballPark, nav_ballTeam, nav_ballFriends, nav_golfInfo;
	private FinalHttp fh;
	private boolean news_complete, hotActivity_complete, gallery_complete,
			ballPark_complete, ballTeam_complete, ballFriend_complete,
			golfInfo_complete;
	private MainPageView mpage;

	public static List listNews, listHotActivity, listGallery, listBallPark,
			listBallTeam, listBallFriends, listGolfInfo;

	public FinalDb fd;

	public HomeFrame(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		fd = FinalDb.create(context, Constant.SQLITE_NAME);

		initViews(context);
		addToMoreListener();
		LoadData();
	}

	private void initViews(Context context) {

		listNews = fd.findAllByWhere(News.class, null);
		listHotActivity = new ArrayList();
		listGallery = fd.findAll(Gallery.class);
		listBallPark = fd.findAllByWhere(BallPark.class, null);
		listBallTeam = fd.findAllByWhere(BallTeam.class, null);
		listBallFriends = fd.findAllByWhere(BallFriend.class, null);
		listGolfInfo = fd.findAll(GolfInfo.class);

		fh = new FinalHttp();

		View view = LayoutInflater.from(context).inflate(R.layout.frame_home,
				this);
		nav_news = (HomeNavigation) view.findViewById(R.id.frame_home_nav_news);
		nav_hotActivity = (HomeNavigation) view
				.findViewById(R.id.frame_home_nav_hotActivity);
		nav_gallery = (HomeNavigation) view
				.findViewById(R.id.frame_home_nav_gallery);
		nav_ballPark = (HomeNavigation) view
				.findViewById(R.id.frame_home_nav_ball_park);
		nav_ballTeam = (HomeNavigation) view
				.findViewById(R.id.frame_home_nav_ball_team);
		nav_ballFriends = (HomeNavigation) view
				.findViewById(R.id.frame_home_nav_ball_friends);
		nav_golfInfo = (HomeNavigation) view
				.findViewById(R.id.frame_home_nav_golfInfo);

		mpage = (MainPageView) view.findViewById(R.id.frame_home_PageView);
		mpage.setImages(null);
	}

	private void addToMoreListener() {
		nav_news.toMoreActivity(NewsActivity.class);
		nav_golfInfo.toMoreActivity(GolfInfosActivity.class);
		nav_gallery.toMoreActivity(GallerysActivity.class);
		nav_ballPark.toMoreActivity(BallParksActivity.class);
		nav_ballTeam.toMoreActivity(BallTeamsActivity.class);
		nav_ballFriends.toMoreActivity(BallFriendsActivity.class);
	}

	private void LoadData() {

		nav_news.initData(listNews);
		nav_hotActivity.initData(listHotActivity);
		nav_golfInfo.initData(listGolfInfo);
		nav_gallery.initData(listGallery);
		nav_ballPark.initData(listBallPark);
		nav_ballTeam.initData(listBallTeam);
		nav_ballFriends.initData(listBallFriends);

		AjaxParams News_params = new AjaxParams();
		News_params.put("cmd", "news");
		News_params.put("row", Constant.HOME_NAVIGATIN_IMG_SIZE + "");
		cLoadData(News_params, nav_news, News.class);

		AjaxParams golfInfo_params = new AjaxParams();
		golfInfo_params.put("cmd", "golfInfo");
		golfInfo_params.put("row", Constant.HOME_NAVIGATIN_IMG_SIZE + "");
		cLoadData(golfInfo_params, nav_golfInfo, GolfInfo.class);

		AjaxParams gallery_params = new AjaxParams();
		gallery_params.put("cmd", "Gallery");
		gallery_params.put("row", Constant.HOME_NAVIGATIN_IMG_SIZE + "");
		cLoadData(gallery_params, nav_gallery, Gallery.class);

		AjaxParams BallPark_params = new AjaxParams();
		BallPark_params.put("cmd", "Article.getPlayground");
		BallPark_params.put("typeid", "127");
		BallPark_params.put("row", Constant.HOME_NAVIGATIN_IMG_SIZE + "");
		cLoadData(BallPark_params, nav_ballPark, BallPark.class);

		AjaxParams ballTeam_params = new AjaxParams();
		ballTeam_params.put("cmd", "Clubs");
		ballTeam_params.put("row", Constant.HOME_NAVIGATIN_IMG_SIZE + "");
		cLoadData(ballTeam_params, nav_ballTeam, BallTeam.class);

		AjaxParams ballfriend_params = new AjaxParams();
		ballfriend_params.put("cmd", "Member.getMember");
		ballfriend_params.put("row", Constant.HOME_NAVIGATIN_IMG_SIZE + "");
		cLoadData(ballfriend_params, nav_ballFriends, BallFriend.class);

	}

	private void cLoadData(AjaxParams params, final HomeNavigation hn,
			final Class cls) {
		fh.get(Constant.URL_BASE, params, new AjaxCallBack<String>() {

			@Override
			public void onSuccess(String t) {
				// TODO Auto-generated method stub
				super.onSuccess(t);
				List datas = JSON.parseArray(t, cls);
				if (cls == News.class) {
					news_complete = true;
					listNews = datas;
				} else if (cls == GolfInfo.class) {
					golfInfo_complete = true;
					listGolfInfo = datas;
				} else if (cls == Gallery.class) {
					gallery_complete = true;
					listGallery = datas;

				} else if (cls == BallPark.class) {
					ballPark_complete = true;
					listBallPark = datas;

				} else if (cls == BallTeam.class) {
					ballTeam_complete = true;
					listBallTeam = datas;

				} else if (cls == BallFriend.class) {
					ballFriend_complete = true;
					listBallFriends = datas;

				}
				hn.initData(datas);
				loadComplete();
			}

			@Override
			public void onFailure(Throwable t, String strMsg) {
				// TODO Auto-generated method stub
				super.onFailure(t, strMsg);
				if (cls == News.class) {
					news_complete = true;
				} else if (cls == GolfInfo.class) {
					gallery_complete = true;
				} else if (cls == Gallery.class) {
					gallery_complete = true;
				} else if (cls == BallPark.class) {
					ballPark_complete = true;
				} else if (cls == BallTeam.class) {
					ballTeam_complete = true;
				} else if (cls == BallFriend.class) {
					ballFriend_complete = true;
				}
			}

		});
	}

	/**
	 * 判断加载是否完成
	 * 
	 * @return
	 */
	public boolean loadComplete() {
		if (news_complete && hotActivity_complete && gallery_complete
				&& ballPark_complete && ballFriend_complete
				&& golfInfo_complete) {

			((MainActivity) context).setProgressDisplay(false);
			return true;
		} else {
			return false;
		}
	}

	public void onDestroy() {
		// fd.deleteByWhere(News.class,null);
		// fd.deleteByWhere(Gallery.class,null);
		// fd.deleteByWhere(GolfInfo.class,null);
		// TODO 其他实体缓存
		// for (News news : (List<News>)listNews) {
		// fd.update(news);
		// }
		// for (Gallery gallery : (List<Gallery>)listGallery) {
		// fd.update(gallery);
		// }
		// for (GolfInfo golfinfo : (List<GolfInfo>)listGolfInfo) {
		// fd.update(golfinfo);
		// }

	}

	public void onStop() {

		mpage.onStop();
	}

	public void onStart() {
		mpage.onStart();
	}

}
