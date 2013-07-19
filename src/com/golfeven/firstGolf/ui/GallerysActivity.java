package com.golfeven.firstGolf.ui;

import java.util.List;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.annotation.view.ViewInject;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.alibaba.fastjson.JSON;
import com.golfeven.firstGolf.R;
import com.golfeven.firstGolf.adapter.GalleryListAdapter;
import com.golfeven.firstGolf.base.BaseListActivity;
import com.golfeven.firstGolf.bean.Gallery;
import com.golfeven.firstGolf.bean.XunsaiType;
import com.golfeven.firstGolf.common.Constant;
import com.golfeven.firstGolf.widget.HeadBack;
import com.golfeven.firstGolf.widget.PullToRefreshListView;

public class GallerysActivity extends BaseListActivity {
	@ViewInject(id = R.id.activity_Gallery_list_spinner_sort)
	Spinner sort;
	@ViewInject(id = R.id.activity_Gallery_list_spinner_xunsai)
	Spinner xunsai;
	private List<XunsaiType> types;

	private ArrayAdapter sortAdapter;
	private ArrayAdapter xunsaiAdapter;

	/**
	 * 必须按照这个顺序排列
	 */
	@Override
	public void mustInit() {
		this.layoutResid = R.layout.activity_gallery_list;
		setContentView(layoutResid);
		this.context = GallerysActivity.this;
		this.entityClass = Gallery.class;
		this.adapter = new GalleryListAdapter(context, datas);
		this.headBack = (HeadBack) findViewById(R.id.activity_Gallery_list_headback);
		this.listView = (PullToRefreshListView) findViewById(R.id.activity_Gallery_list);
		this.params.put("cmd", "Gallery");
		init();
	}

	private void init() {
		// TODO Auto-generated method stub
		types = this.db.findAll(XunsaiType.class);
		sortAdapter = ArrayAdapter.createFromResource(appContext,
				R.array.spinner_sort, android.R.layout.simple_spinner_item);
		xunsaiAdapter = new ArrayAdapter(appContext,android.R.layout.simple_spinner_item,types);
		sortAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		xunsaiAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		intTypes();
		sort.setAdapter(sortAdapter);
		xunsai.setAdapter(xunsaiAdapter);
		OnItemSelectedListener onItemSelectedListener = new SpinnerListener();
		sort.setOnItemSelectedListener(onItemSelectedListener);
		xunsai.setOnItemSelectedListener(onItemSelectedListener);
	}
	/**
	 * 初始化赛事类型数据
	 */
	private void intTypes() {
		AjaxParams params = new AjaxParams();
		params.put("cmd","Article.getGalleryType");
		new FinalHttp().get(Constant.URL_BASE,params , new AjaxCallBack<String>() {

			@Override
			public void onSuccess(String t) {
				// TODO Auto-generated method stub
				super.onSuccess(t);
				types = JSON.parseArray(t, XunsaiType.class);
				xunsaiAdapter = new ArrayAdapter(appContext,android.R.layout.simple_spinner_item,types);
				xunsai.setAdapter(xunsaiAdapter);
				xunsaiAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				xunsai.postInvalidate();
			}
		});
	}

	class SpinnerListener implements OnItemSelectedListener {

		@Override
		public void onItemSelected(AdapterView<?> parent, View view,
				int position, long id) {
			// TODO Auto-generated method stub
			if (parent == xunsai) {
				XunsaiType o = (XunsaiType)xunsai.getItemAtPosition(position);
				params.put("typeid",o.getTypeid()+"");
			}
			if (parent == sort) {
				String sortStr = "day";
				String value = sort.getItemAtPosition(position).toString();
				if ("日排行".equals(value)) {
					sortStr = "1";
				}
				if ("周排行".equals(value)) {
					sortStr = "2";
				}
				if ("月排行".equals(value)) {
					sortStr = "3";
				}
				params.put("order", sortStr);
			}
			reset();
			listView.setTag(LOAD);
			requestData();

		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub

		}

	}

}
