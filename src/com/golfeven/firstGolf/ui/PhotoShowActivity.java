package com.golfeven.firstGolf.ui;

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
import com.golfeven.firstGolf.bean.Photo;
import com.golfeven.firstGolf.common.Constant;
import com.golfeven.firstGolf.common.MyLog;
import com.golfeven.firstGolf.common.SharedUtil;
import com.golfeven.firstGolf.widget.HeadBack;
import com.golfeven.firstGolf.widget.MyToast;
import com.golfeven.welcome.MyScrollLayout;
import com.golfeven.welcome.OnViewChangeListener;

public class PhotoShowActivity extends BaseActivity implements
		OnViewChangeListener {
	@ViewInject(id = R.id.detail_showphoto_indicators)
	LinearLayout indicators;
	@ViewInject(id = R.id.detail_showphoto_scrolllayout)
	MyScrollLayout scrollLayout;

	
	private int size = 0;
	private ImageView[] imgs;
	private ImageView[] imgs_indicators;

	//private FinalBitmap fb;

	private int currentItem = 0;

	private List<Photo> photos;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//fb = appContext.getFB(false);
		fb.isSquare = false;
		// fb = FinalBitmap.create(appContext, Constant.IMG_CACHEPATH);
		setContentView(R.layout.activity_show_photo);
		Intent intent = getIntent();
		photos = intent.getParcelableArrayListExtra("photos");
		currentItem = intent.getIntExtra("index",0);
		scrollLayout.SetOnViewChangeListener(this);
		scrollLayout.mCurScreen = currentItem;
		initData();
	

	}

	private void initData() {
		

		size = photos.size();

		imgs = new ImageView[size];
		imgs_indicators = new ImageView[size];
		for (int i = 0; i < size; i++) {

			ImageView img = new ImageView(appContext);
			scrollLayout.addView(img);
			scrollLayout.measure(MeasureSpec.EXACTLY, MeasureSpec.EXACTLY);
			scrollLayout.layout(0, 0, 0, 0);
			img = (ImageView) scrollLayout.getChildAt(i);
			fb.display(img, Constant.URL_IMG_BASE + photos.get(i).getPic(),
					scrollLayout.getWidth(), scrollLayout.getHeight());

			imgs_indicators[i] = getIndicatorImage();
			indicators.addView(imgs_indicators[i]);
		}
		imgs_indicators[currentItem].setEnabled(false);
		
	}

	public ImageView getIndicatorImage() {
		ImageView img = new ImageView(PhotoShowActivity.this);
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
		// imgs[currentItem].setEnabled(true);
		// imgs[position].setEnabled(false);

		indicators.getChildAt(currentItem).setEnabled(true);
		indicators.getChildAt(position).setEnabled(false);

		currentItem = position;
		fb.display(imgs[position],
				Constant.URL_IMG_BASE + photos.get(position).getPic());
	}





}