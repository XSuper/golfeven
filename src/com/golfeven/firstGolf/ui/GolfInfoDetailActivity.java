package com.golfeven.firstGolf.ui;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.annotation.view.ViewInject;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.golfeven.firstGolf.R;
import com.golfeven.firstGolf.base.BaseActivity;
import com.golfeven.firstGolf.bean.GolfInfo;
import com.golfeven.firstGolf.bean.NewsDetail;
import com.golfeven.firstGolf.common.Constant;
import com.golfeven.firstGolf.common.HtmlUtil;
import com.golfeven.firstGolf.common.MyLog;
import com.golfeven.firstGolf.common.SharedUtil;
import com.golfeven.firstGolf.common.StringUtils;
import com.golfeven.firstGolf.widget.HeadBack;
import com.golfeven.firstGolf.widget.MyToast;

public class GolfInfoDetailActivity extends BaseActivity {
	@ViewInject(id = R.id.detail_news_title)
	TextView title;
	@ViewInject(id = R.id.detail_news_resourcs)
	TextView resourcs;
	@ViewInject(id = R.id.detail_news_time)
	TextView time;
	@ViewInject(id = R.id.detail_news_content)
	WebView content;
	@ViewInject(id = R.id.detail_news_headback)
	HeadBack headBack;

	@ViewInject(id = R.id.detail_news_btnLast, click = "lastNews")
	View btnLast;
	@ViewInject(id = R.id.detail_news_btnNext, click = "nextNews")
	View btnNext;

	private String id;
	private NewsDetail newsDetail;
	
	
	
	private String typeid = "137";
	private int index=0;//表示选择的第几个
	private List<GolfInfo> list;//传过来的列表
	private Map<String,NewsDetail> details;//用来缓存浏览过的
	
	private FinalHttp fh;
	

	// toast信息常量
	public static final String TOAST_MSG_SUCCESS_TITLE = "成功";
	public static final String TOAST_MSG_ERROR_TITLE = "失败";
	public static final String TOAST_MSG_WARNING_TITLE = "提示";

	// public static final String TOAST_MSG_LOAD_SUCCESS_CONTENT = "信息更新成功！";
	// public static final String TOAST_MSG_LOAD_ERROR_CONTENT = "信息更新失败！";

	public static final String TOAST_MSG_LOADMORE_SUCCESS_CONTENT = "信息加载成功！";
	public static final String TOAST_MSG_LOADMORE_ERROR_CONTENT = "信息加载失败！";

	// public static final String TOAST_MSG_REFRESH_SUCCESS_CONTENT = "刷新成功！";
	// public static final String TOAST_MSG_REFRESH_ERROR_CONTENT = "刷新失败！";

	public static final String TOAST_MSG_NOMORE_CONTENT = "没有更多的内容！";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_news_detail);
		headBack.setTitle("高尔夫知识");
		fh = new FinalHttp();
		details = new HashMap<String, NewsDetail>();
		Intent intent = getIntent();
		index = intent.getIntExtra("index",0);
		typeid = intent.getStringExtra("typeid");
		list = intent.getParcelableArrayListExtra("List");
		
		headBack.setProgressVisible(true);
		title.setText("数据正在加载...");
		load();

	}

	private void load() {
		id = list.get(index).getId();
		if(details.containsKey(id)){
			newsDetail = details.get(id);
			initData();
			return;
		}
		AjaxParams params = new AjaxParams();
		params.put("cmd", "getArticleDetail");
		params.put("id", list.get(index).getId());
		headBack.setProgressVisible(true);
		
		setbtnClickable(false);
		fh.get(Constant.URL_BASE, params,
				new AjaxCallBack<String>() {

					@Override
					public void onSuccess(String t) {
						// TODO Auto-generated method stub
						super.onSuccess(t);
						headBack.setProgressVisible(false);
						setbtnClickable(true);
						newsDetail = JSON.parseObject(t, NewsDetail.class);
						details.put(id,newsDetail);//做缓存
						initData();
						
					}

					@Override
					public void onFailure(Throwable t, String strMsg) {
						// TODO Auto-generated method stub
						super.onFailure(t, strMsg);
						headBack.setProgressVisible(false);
						setbtnClickable(true);
						title.setText("加载失败！");
						MyToast.customToast(GolfInfoDetailActivity.this,
								Toast.LENGTH_SHORT, TOAST_MSG_ERROR_TITLE,
								TOAST_MSG_LOADMORE_ERROR_CONTENT,
								Constant.TOAST_IMG_ERROR);
					}

				});
	}

	private void initData() {
		headBack.getRbtn2().setClickable(true);
		
headBack.setRbtn2ClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String url = HtmlUtil.getTureImageUrl(newsDetail.getBody());
				SharedUtil.share(GolfInfoDetailActivity.this,newsDetail.getTitle() , newsDetail.getDescription(), newsDetail.getPageUrl(), url);
				
			}
		});
		String body = newsDetail.getBody();
		MyLog.v("body", body);
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		body = HtmlUtil.setImageSize(body, (int)(320));
//		body = body.replaceAll("src", "width=200px height=200px src");
//		body = body.replaceAll("/uploads", Constant.URL_IMG_BASE + "/uploads");
//		// body = body.replaceAll("(<img[^>]*?)\\s+width\\s*=\\s*\\S+","$1");
//		// body = body.replaceAll("(<img[^>]*?)\\s+height\\s*=\\s*\\S+","$1");
		MyLog.v("body---",(dm.widthPixels)+"");
		MyLog.v("body", body);
		title.setText(newsDetail.getTitle());
		resourcs.setText(newsDetail.getTypename());
		String timeStr = StringUtils.friendly_time(StringUtils
				.strToDataStr(newsDetail.getPubdate()+"000"));
		time.setText(timeStr);
		content.loadDataWithBaseURL(null, body, "text/html", "utf-8", null);
	}

//	/**
//	 * 上一页
//	 * @param view
//	 */
//	public void lastNews(View view) {
//		btnNext.setClickable(true);
//		if(index<=0){
//			btnLast.setClickable(false);
//			MyToast.customToast(GolfInfoDetailActivity.this,
//					Toast.LENGTH_SHORT, TOAST_MSG_WARNING_TITLE,
//					"已经是第一页了!",
//					Constant.TOAST_IMG_WARNING);
//			return;
//		}
//		index--;
//		load();
//		
//
//	}
//
//	/**
//	 * 下一页
//	 * @param view
//	 */
//	public void nextNews(View view) {
//
//		if(index == list.size()-1){
//			LoadList();
//			return;
//		}
//		index++;
//		load();
//	}
	/**
	 * 上一页
	 * @param view
	 */
	public void lastNews(View view) {
		headBack.getRbtn2().setClickable(false);
		btnNext.setVisibility(View.VISIBLE);
		if(index<=0){
			btnLast.setVisibility(View.INVISIBLE);
			headBack.getRbtn2().setClickable(true);
			MyToast.customToast(GolfInfoDetailActivity.this,
					Toast.LENGTH_SHORT, TOAST_MSG_WARNING_TITLE,
					"已经是第一页了!",
					Constant.TOAST_IMG_WARNING);
			return;
		}
		index--;
		if(index<=0){
			btnLast.setVisibility(View.INVISIBLE);
		}
		
		load();
		

	}

	/**
	 * 下一页
	 * @param view
	 */
	public void nextNews(View view) {
		headBack.getRbtn2().setClickable(false);
		btnLast.setVisibility(View.VISIBLE);
		if(index == list.size()-1){
			LoadList();
			return;
		}
		index++;
		load();
	}
	
	public void LoadList(){
		AjaxParams params = new AjaxParams();
		params.put("cmd", "golfInfo");
		params.put("typeid", typeid);
		params.put("rowIndex", index+"");
		params.put("row", "20");
		headBack.setProgressVisible(true);
		
		setbtnClickable(false);
		fh.get(Constant.URL_BASE, params,new AjaxCallBack<String>() {

			@Override
			public void onSuccess(String t) {
				// TODO Auto-generated method stub
				super.onSuccess(t);
				setbtnClickable(true);
				if(StringUtils.responseError(t)){
					btnNext.setVisibility(View.INVISIBLE);
					headBack.getRbtn2().setClickable(true);
					headBack.setProgressVisible(false);
					MyToast.customToast(GolfInfoDetailActivity.this, Toast.LENGTH_SHORT,
							TOAST_MSG_WARNING_TITLE, TOAST_MSG_NOMORE_CONTENT,
							Constant.TOAST_IMG_ERROR);
				}else{
					List<GolfInfo> mlList = JSONArray.parseArray(t, GolfInfo.class);
					list.addAll(mlList);
					index++;
					load();
				}
				
			}

			@Override
			public void onFailure(Throwable t, String strMsg) {
				// TODO Auto-generated method stub
				super.onFailure(t, strMsg);
				headBack.setProgressVisible(false);
				setbtnClickable(true);
				title.setText("加载失败！");
				MyToast.customToast(GolfInfoDetailActivity.this, Toast.LENGTH_SHORT,
						TOAST_MSG_ERROR_TITLE, TOAST_MSG_LOADMORE_ERROR_CONTENT,
						Constant.TOAST_IMG_ERROR);
			}
			
		});
	}
	
	public void setbtnClickable(boolean isclic){
		btnNext.setClickable(isclic);
		btnLast.setClickable(isclic);
	}

}
