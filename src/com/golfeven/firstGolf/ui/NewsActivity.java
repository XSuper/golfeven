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
import com.golfeven.firstGolf.adapter.NewsListAdapter;
import com.golfeven.firstGolf.base.BaseListActivity;
import com.golfeven.firstGolf.bean.News;
import com.golfeven.firstGolf.bean.XunsaiType;
import com.golfeven.firstGolf.common.Constant;
import com.golfeven.firstGolf.common.MyLog;
import com.golfeven.firstGolf.widget.HeadBack;
import com.golfeven.firstGolf.widget.PullToRefreshListView;


public class NewsActivity extends BaseListActivity {
	@ViewInject(id=R.id.activity_news_list_spinner_newstype )Spinner newstype;  
	@ViewInject(id=R.id.activity_news_list_spinner_sort)Spinner sort;  
	@ViewInject(id=R.id.activity_news_list_spinner_xunsai)Spinner xunsai;  

	private ArrayAdapter newstypeAdapter;
	private ArrayAdapter sortAdapter;
	private ArrayAdapter xunsaiAdapter;
	
	
	private List<XunsaiType> types;
	/**
	 * 必须按照这个顺序排列
	 */
	@Override
	public void mustInit() {
		this.layoutResid = R.layout.activity_news_list;
		setContentView(layoutResid);
		this.context = NewsActivity.this;
		this.entityClass = News.class ;
		this.adapter = new NewsListAdapter(context, datas);
		this.headBack = (HeadBack)findViewById(R.id.activity_news_list_headback);
		this.listView = (PullToRefreshListView)findViewById(R.id.activity_news_list);
		this.params.put("cmd","news");
		init();
	}
	
	private void init() {
		types = this.db.findAll(XunsaiType.class);
		// TODO Auto-generated method stub
		newstypeAdapter =ArrayAdapter.createFromResource(appContext, R.array.spinner_newsType,android.R.layout.simple_spinner_item);
		sortAdapter = ArrayAdapter.createFromResource(appContext, R.array.spinner_sort,android.R.layout.simple_spinner_item);
		xunsaiAdapter = new ArrayAdapter(appContext,android.R.layout.simple_spinner_item,types);
		newstypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sortAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		xunsaiAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
		intTypes();
		
		newstype.setAdapter(newstypeAdapter);
		sort.setAdapter(sortAdapter);
		xunsai.setAdapter(xunsaiAdapter);
		OnItemSelectedListener onItemSelectedListener = new SpinnerListener();
		newstype.setOnItemSelectedListener(onItemSelectedListener);
		sort.setOnItemSelectedListener(onItemSelectedListener);
		xunsai.setOnItemSelectedListener(onItemSelectedListener);
		
	}

	/**
	 * 初始化赛事类型数据
	 */
	private void intTypes() {
		AjaxParams params = new AjaxParams();
		params.put("cmd","Article.getXunsaiType");
		new FinalHttp().get(Constant.URL_BASE,params , new AjaxCallBack<String>() {

			@Override
			public void onSuccess(String t) {
				// TODO Auto-generated method stub
				super.onSuccess(t);
				types = JSON.parseArray(t, XunsaiType.class);
				MyLog.v("xunsaiAdapter",types.size()+"");
				xunsaiAdapter = new ArrayAdapter(appContext,android.R.layout.simple_spinner_item,types);
				xunsai.setAdapter(xunsaiAdapter);
				xunsaiAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				xunsai.postInvalidate();
			}
		});
	}
	
	/**
	 * 下拉框的按钮事件
	 * @author ISuper
	 *
	 */
	class SpinnerListener implements OnItemSelectedListener{

		@Override
		public void onItemSelected(AdapterView<?> parent, View view, int position,
				long id) {
			// TODO Auto-generated method stub
			if(parent == xunsai){
				XunsaiType o = (XunsaiType)xunsai.getItemAtPosition(position);
				params.put("typeid",o.getTypeid()+"");
				((NewsListAdapter)adapter).setTypeid(o.getTypeid()+"");
			}
			if(parent ==sort){
				String sortStr ="day";
				String value = sort.getItemAtPosition(position).toString();
				if("日排行".equals(value)){
					sortStr="1";
				}
				if("周排行".equals(value)){
					sortStr="2";
				}
				if("月排行".equals(value)){
					sortStr="3";
				}
				params.put("order",sortStr);
				((NewsListAdapter)adapter).setOrder(sortStr);
			}
			if(parent ==newstype){
				String value = newstype.getItemAtPosition(position).toString();
				String typeStr = "text";
				if("文字".equals(value)){
					typeStr = "text";
				}
				if("视频".equals(value)){
					typeStr = "video";
				}
				params.put("type",typeStr);
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
		db.deleteByWhere(XunsaiType.class, null);
		for (XunsaiType type : types) {
			db.save(type);
		}
	}

}
