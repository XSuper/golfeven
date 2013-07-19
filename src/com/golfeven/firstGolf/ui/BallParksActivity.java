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
import com.golfeven.firstGolf.adapter.BallParkListAdapter;
import com.golfeven.firstGolf.base.BaseListActivity;
import com.golfeven.firstGolf.bean.BallPark;
import com.golfeven.firstGolf.bean.PlaygroundType;
import com.golfeven.firstGolf.bean.XunsaiType;
import com.golfeven.firstGolf.common.Constant;
import com.golfeven.firstGolf.widget.HeadBack;
import com.golfeven.firstGolf.widget.PullToRefreshListView;


public class BallParksActivity extends BaseListActivity {
	@ViewInject(id=R.id.activity_ballpark_list_spinner_parktype )Spinner parktype;  
	@ViewInject(id=R.id.activity_ballpark_list_spinner_city)Spinner city;  
	@ViewInject(id=R.id.activity_ballpark_list_spinner_price)Spinner price;  

	private ArrayAdapter parktypeAdapter;
	private ArrayAdapter cityAdapter;
	private ArrayAdapter priceAdapter;

	private List<PlaygroundType> types;
	private boolean isChoice = false;//判断进来是 选择球场 还是 展示列表
	/**
	 * 必须按照这个顺序排列
	 */
	@Override
	public void mustInit() {
		isChoice = getIntent().getBooleanExtra("isChoice", false);
		this.layoutResid = R.layout.activity_ballpark_list;
		setContentView(layoutResid);
		this.context = BallParksActivity.this;
		this.entityClass = BallPark.class ;
		this.adapter = new BallParkListAdapter(context, datas);
		((BallParkListAdapter)adapter).isChoice=isChoice;
		this.headBack = (HeadBack)findViewById(R.id.activity_ballpark_list_headback);
		this.listView = (PullToRefreshListView)findViewById(R.id.activity_ballpark_list);
		this.params.put("cmd","getArticle");
		this.params.put("typeid","125");
		init();
	}
	private void init() {
		types = this.db.findAll(PlaygroundType.class);
		// TODO Auto-generated method stub
		parktypeAdapter =new ArrayAdapter(appContext,android.R.layout.simple_spinner_item,types);
		cityAdapter = ArrayAdapter.createFromResource(appContext, R.array.spinner_city, android.R.layout.simple_spinner_item);
		priceAdapter = ArrayAdapter.createFromResource(appContext, R.array.spinner_price, android.R.layout.simple_spinner_item);
		
		parktypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		priceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
		intTypes();
		parktype.setAdapter(parktypeAdapter);
		city.setAdapter(cityAdapter);
		price.setAdapter(priceAdapter);
		OnItemSelectedListener onItemSelectedListener = new SpinnerListener();
		parktype.setOnItemSelectedListener(onItemSelectedListener);
		city.setOnItemSelectedListener(onItemSelectedListener);
		price.setOnItemSelectedListener(onItemSelectedListener);
		
	}
	
	private void intTypes() {
		AjaxParams params = new AjaxParams();
		params.put("cmd","Article.getPlaygroundType");
		new FinalHttp().get(Constant.URL_BASE,params , new AjaxCallBack<String>() {

			@Override
			public void onSuccess(String t) {
				// TODO Auto-generated method stub
				super.onSuccess(t);
				types = JSON.parseArray(t, PlaygroundType.class);
				parktypeAdapter = new ArrayAdapter(appContext,android.R.layout.simple_spinner_item,types);
				parktype.setAdapter(parktypeAdapter);
				parktypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				parktype.postInvalidate();
			}
		});
	}
	class SpinnerListener implements OnItemSelectedListener{

		@Override
		public void onItemSelected(AdapterView<?> parent, View view, int position,
				long id) {
			// TODO Auto-generated method stub
			if(parent == parktype){
				PlaygroundType value = (PlaygroundType)parktype.getItemAtPosition(position);
				params.put("typeid",value.getTypeid()+"");
			}
			if(parent ==city){
				
			}
			if(parent ==price){
				String value = price.getItemAtPosition(position).toString();
				String priceOrder = "text";
				if("由高到低".equals(value)){
					priceOrder = "desc";
				}
				if("由低到高".equals(value)){
					priceOrder = "asc";
				}
				params.put("priceOrder",priceOrder);
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
		db.deleteByWhere(PlaygroundType.class, null);
		for (PlaygroundType type : types) {
			db.save(type);
		}
	}
	

}
