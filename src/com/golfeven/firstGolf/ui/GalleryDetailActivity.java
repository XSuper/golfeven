package com.golfeven.firstGolf.ui;

import java.util.ArrayList;
import java.util.List;

import net.tsz.afinal.FinalBitmap;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.annotation.view.ViewInject;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.golfeven.firstGolf.R;
import com.golfeven.firstGolf.base.BaseActivity;
import com.golfeven.firstGolf.bean.GalleryDetail;
import com.golfeven.firstGolf.bean.Image;
import com.golfeven.firstGolf.common.Constant;
import com.golfeven.firstGolf.common.MyLog;
import com.golfeven.firstGolf.common.SharedUtil;
import com.golfeven.firstGolf.widget.HeadBack;
import com.golfeven.firstGolf.widget.MyToast;
import com.golfeven.welcome.MyScrollLayout;
import com.golfeven.welcome.OnViewChangeListener;

public class GalleryDetailActivity extends BaseActivity implements
		OnViewChangeListener {
	@ViewInject(id = R.id.detail_gallery_headback)
	HeadBack headBack;
	@ViewInject(id = R.id.detail_gallery_indicators)
	LinearLayout indicators;
	@ViewInject(id = R.id.detail_gallery_scrolllayout)
	MyScrollLayout scrollLayout;
	@ViewInject(id = R.id.detail_gallery_text)
	TextView content;

	// toast信息常量
	public static final String TOAST_MSG_SUCCESS_TITLE = "成功";
	public static final String TOAST_MSG_ERROR_TITLE = "失败";
	public static final String TOAST_MSG_WARNING_TITLE = "提示";

	public static final String TOAST_MSG_LOADMORE_SUCCESS_CONTENT = "信息加载成功！";
	public static final String TOAST_MSG_LOADMORE_ERROR_CONTENT = "信息加载失败！";

	public static final String TOAST_MSG_NOMORE_CONTENT = "没有更多的内容！";

	private String id;
	private int size = 0;
	private ImageView[] imgs;
	private ImageView[] imgs_indicators;

	private FinalHttp fh = new FinalHttp();
	// private FinalBitmap fb;

	private int currentItem = 0;

	private GalleryDetail galleryDetail;
	private List<Image> iamgelist;

	private List<FinalBitmap> fbs;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		fbs = new ArrayList<FinalBitmap>();
		// fb = appContext.getFB(false);
		fb.isSquare = false;
		fb.configMemoryCacheSize(200);

		// fb = FinalBitmap.create(appContext, Constant.IMG_CACHEPATH);
		setContentView(R.layout.activity_gallery_detail);
		Intent intent = getIntent();
		id = intent.getStringExtra("galleryId");
		content.getBackground().setAlpha(100);
		scrollLayout.SetOnViewChangeListener(this);

		initData();

	}

	private void initData() {
		AjaxParams params = new AjaxParams();
		params.put("cmd", "GalleryDetail");
		params.put("id", id);
		headBack.setProgressVisible(true);
		fh.get(Constant.URL_BASE, params, new AjaxCallBack<String>() {
			@Override
			public void onSuccess(String t) {
				// TODO Auto-generated method stub
				super.onSuccess(t);
				headBack.setProgressVisible(false);
				String imgurls = "";
				try {
					JSONObject jo = new JSONObject(t);
					imgurls = jo.getString("imgurls");

				} catch (Exception e) {
					MyLog.e("jsonError", e.toString());
				}
				galleryDetail = (GalleryDetail) JSON.parseObject(t,
						GalleryDetail.class);
				iamgelist = (List<Image>) JSON.parseArray(imgurls, Image.class);
				galleryDetail.setImages(iamgelist);
				size = iamgelist.size();

				imgs = new ImageView[size];
				imgs_indicators = new ImageView[size];
				for (int i = 0; i < size; i++) {

					ImageView img = new ImageView(appContext);
					scrollLayout.addView(img);
					scrollLayout.measure(MeasureSpec.EXACTLY,
							MeasureSpec.EXACTLY);
					scrollLayout.layout(0, 0, 0, 0);
					img = (ImageView) scrollLayout.getChildAt(i);

//					FinalBitmap fbtemp = FinalBitmap.createNew(
//							GalleryDetailActivity.this, Constant.IMG_CACHEPATH);
//					fbtemp.configLoadfailImage(Constant.IMG_LOADING_FAIL);
//					fbtemp.configLoadingImage(Constant.IMG_LOADING);
//					fbtemp.configBitmapLoadThreadSize(20);
//					fbs.add(fbtemp);
					fb.display(img, Constant.URL_IMG_BASE
							+ iamgelist.get(i).getDdimg(),
							scrollLayout.getWidth(), scrollLayout.getHeight());
					MyLog.v("imagurl", Constant.URL_IMG_BASE
							+ iamgelist.get(i).getDdimg());
					imgs_indicators[i] = getIndicatorImage();
					indicators.addView(imgs_indicators[i]);
				}
				imgs_indicators[0].setEnabled(false);
				content.setText(iamgelist.get(0).getText());
				share();// 成功后才可以分享

			}

			@Override
			public void onFailure(Throwable t, String strMsg) {
				// TODO Auto-generated method stub
				super.onFailure(t, strMsg);
				headBack.setProgressVisible(false);
				MyToast.customToast(GalleryDetailActivity.this,
						Toast.LENGTH_SHORT, TOAST_MSG_ERROR_TITLE,
						TOAST_MSG_LOADMORE_ERROR_CONTENT,
						Constant.TOAST_IMG_ERROR);
			}

		});

	}

	public void share() {
		headBack.setRbtn2ClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				SharedUtil.share(GalleryDetailActivity.this, "图片分享", iamgelist
						.get(currentItem).getText(), "", Constant.URL_IMG_BASE
						+ iamgelist.get(currentItem).getDdimg());

			}
		});
	}

	public ImageView getIndicatorImage() {
		ImageView img = new ImageView(GalleryDetailActivity.this);
		LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		img.setImageResource(R.drawable.page_indicator_bg);
		params.gravity = Gravity.CENTER_VERTICAL;
		img.setLayoutParams(params);
		img.setClickable(true);
		img.setPadding(5, 5, 5, 5);
		return img;
	}

	@Override
	public void OnViewChange(int position) {
		setcurrentPoint(position);
	}

	private void setcurrentPoint(int position) {
		if (position < 0 || position > size - 1 || currentItem == position) {
			return;
		}
		content.setText(iamgelist.get(position).getText());
		// imgs[currentItem].setEnabled(true);
		// imgs[position].setEnabled(false);

		indicators.getChildAt(currentItem).setEnabled(true);
		indicators.getChildAt(position).setEnabled(false);

		currentItem = position;
		fb.display(imgs[position],
				Constant.URL_IMG_BASE + iamgelist.get(position).getDdimg());
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (fbs != null && fbs.size() > 0) {
			for (int i = 0;i<fbs.size();i++) {
				fbs.get(i).onDestroy();
			}
			fbs.removeAll(fbs);
			fbs = null;
			
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (fbs != null && fbs.size() > 0) {
			for (int i = 0;i<fbs.size();i++) {
				fbs.get(i).onResume();
			}
		}
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		if (fbs != null && fbs.size() > 0) {
			for (int i = 0;i<fbs.size();i++) {
				fbs.get(i).onPause();
			}
		}
	}

}