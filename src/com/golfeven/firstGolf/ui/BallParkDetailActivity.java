package com.golfeven.firstGolf.ui;

import java.util.ArrayList;
import java.util.List;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.annotation.view.ViewInject;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.golfeven.firstGolf.R;
import com.golfeven.firstGolf.base.BaseActivity;
import com.golfeven.firstGolf.base.BaseListActivity;
import com.golfeven.firstGolf.bean.BallPark;
import com.golfeven.firstGolf.bean.BallParkDetail;
import com.golfeven.firstGolf.bean.Image;
import com.golfeven.firstGolf.common.Constant;
import com.golfeven.firstGolf.common.MyLog;
import com.golfeven.firstGolf.common.StringUtils;
import com.golfeven.firstGolf.widget.HeadBack;
import com.golfeven.firstGolf.widget.MainPageView;
import com.golfeven.firstGolf.widget.MyToast;

public class BallParkDetailActivity extends BaseActivity {
	private @ViewInject(id = R.id.activity_ballpark_detail_headback)
	HeadBack headBack;
	private @ViewInject(id = R.id.activity_ballpark_detail_PageView)
	MainPageView mainPageView;
	private @ViewInject(id = R.id.activity_ballpark_detail_layout_place, click = "toActivity")
	View placeLayout;
	private @ViewInject(id = R.id.activity_ballpark_detail_layout_money, click = "toActivity")
	View moneyLayout;
	private @ViewInject(id = R.id.activity_ballpark_detail_layout_tel, click = "toActivity")
	View telLayout;
	private @ViewInject(id = R.id.activity_ballpark_detail_place)
	TextView place;
	private @ViewInject(id = R.id.activity_ballpark_detail_money)
	TextView money;
	private @ViewInject(id = R.id.activity_ballpark_detail_tel)
	TextView tel;
	private @ViewInject(id = R.id.activity_ballpark_detail_description)
	TextView description;
	private @ViewInject(id = R.id.activity_ballpark_detail_body)
	WebView body;
	private @ViewInject(id = R.id.activity_ballpark_detail_detail, click = "slide")
	TextView detail;

	private String id;
	private BallPark ballPark;
	private BallParkDetail parkDetail;

	private FinalHttp fh;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ballpark_detail);
		Intent intent = getIntent();
		id = intent.getStringExtra("id");
		ballPark = intent.getParcelableExtra("ballPark");
		fh = new FinalHttp();
		initData();
		setValue();
	}

	private void initData() {
		// TODO Auto-generated method stub
		AjaxParams params = new AjaxParams();
		params.put("cmd", "Article.getPlaygroundDetail");
		params.put("id", id);
		headBack.setProgressVisible(true);
		headBack.setTitle("正在加载!");
		fh.get(Constant.URL_BASE, params, new AjaxCallBack<String>() {

			@Override
			public void onSuccess(String t) {
				// TODO Auto-generated method stub
				super.onSuccess(t);
				MyLog.v("tag", t + "");
				headBack.setProgressVisible(false);
				try {

					parkDetail = JSON.parseObject(t, BallParkDetail.class);
				} catch (Exception e) {
					// TODO: handle exception
				}
				setValue();
			}

			@Override
			public void onFailure(Throwable t, String strMsg) {
				// TODO Auto-generated method stub
				super.onFailure(t, strMsg);
				headBack.setProgressVisible(false);
				MyToast.customToast(BallParkDetailActivity.this,
						Toast.LENGTH_SHORT,
						BaseListActivity.TOAST_MSG_ERROR_TITLE, "信息加载失败!",
						Constant.TOAST_IMG_ERROR);
				headBack.setTitle("加载失败!");
			}

		});

	}

	private void setValue() {
		if (parkDetail == null) {
			headBack.setTitle(ballPark.getTitle());
			place.setText(ballPark.getShorttitle());
			List<Image> i = new ArrayList<Image>();
			Image im = new Image();
			im.setDdimg(ballPark.getLitpic());
			i.add(im);
			mainPageView.setImages(i);
			description.setText(ballPark.getDescription());
			return;
		}
		mainPageView.setImages(parkDetail.getImgurls());
		MyLog.v("tag", parkDetail.getBody() + "");
		headBack.setTitle(parkDetail.getTitle());
		place.setText(parkDetail.getShorttitle());
		money.setText("￥" + parkDetail.getMoney());
		tel.setText(parkDetail.getKeywords());
		description.setText(parkDetail.getDescription());
		body.loadDataWithBaseURL(null, parkDetail.getBody(), "text/html",
				"utf-8", null);
		WebSettings webSettings = body.getSettings();
		webSettings.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
	}

	public void toActivity(View view) {
		if (view == placeLayout) {
			if(!StringUtils.isEmpty(ballPark.getLat())&&!StringUtils.isEmpty(ballPark.getLng())){
				
				Intent intent = new Intent(BallParkDetailActivity.this,
						MapActivity.class);
				intent.putExtra("place",ballPark.getAreaName());
				intent.putExtra("Lat",ballPark.getLat());
				intent.putExtra("Lon",ballPark.getLng());
				startActivity(intent);
			}else{
				MyToast.customToast(BallParkDetailActivity.this, Toast.LENGTH_SHORT, MyToast.TOAST_MSG_WARNING_TITLE, "该球场坐标信息不全,无法提供定位导航!", Constant.TOAST_IMG_WARNING);
			}

		}
		if (view == moneyLayout) {

		}
		if (view == telLayout) {
			Uri telUri = Uri.parse("tel:" + parkDetail.getKeywords());
			Intent intent = new Intent(Intent.ACTION_DIAL, telUri);
			startActivity(intent);
		}
	}

	public void slide(View view) {
		if (View.VISIBLE == body.getVisibility()) {
			body.setVisibility(View.GONE);
		} else {
			body.setVisibility(View.VISIBLE);
//
//			Animation animation = AnimationUtils.loadAnimation(
//					BallParkDetailActivity.this, R.anim.slide_top_to_bottom);
//			body.startAnimation(animation);

		}

	}

}
