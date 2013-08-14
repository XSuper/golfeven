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
	
	public static float PROPORTION = 2;
	
	public boolean isFrist=true;//判断是否是刚刚进入该页面,刚进入不触发下拉框的刷新

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
		types = this.db.findAllByWhere(XunsaiType.class,"tag=2");
		sortAdapter = ArrayAdapter.createFromResource(appContext,
				R.array.spinner_sort, R.layout.spinner_text);
		xunsaiAdapter = new ArrayAdapter(appContext,R.layout.spinner_text,types);
		sortAdapter
				.setDropDownViewResource(R.layout.spinner_checkedtextview);
		xunsaiAdapter
				.setDropDownViewResource(R.layout.spinner_checkedtextview);

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
				List old = types;
				types.removeAll(types);
//				XunsaiType x = new XunsaiType();
//				x.setTypeid(0);
//				x.setTypeName("选择赛事");
//				types.add(x);
				try {
					
					types.addAll(JSON.parseArray(t, XunsaiType.class));
					
				} catch (Exception e) {
					// TODO: handle exception
					types = old;
				}
				old = null;
				
				xunsaiAdapter = new ArrayAdapter(appContext,R.layout.spinner_text,types);
				xunsai.setAdapter(xunsaiAdapter);
				xunsaiAdapter.setDropDownViewResource(R.layout.spinner_checkedtextview);
				xunsai.postInvalidate();
			}
		});
	}

	class SpinnerListener implements OnItemSelectedListener {

		@Override
		public void onItemSelected(AdapterView<?> parent, View view,
				int position, long id) {
			if(isFrist){
				isFrist = false;
				return;
			}
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
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		//appContext.getFB().proportion=0;
		db.deleteByWhere(XunsaiType.class, "tag=2");
		for (XunsaiType type : types) {
			type.setTag(2);
			db.save(type);
		}
	}
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		//appContext.getFB().proportion=0;
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		//appContext.getFB().proportion=PROPORTION;
	}

}
