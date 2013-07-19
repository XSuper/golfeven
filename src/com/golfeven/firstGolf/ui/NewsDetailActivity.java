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
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.golfeven.firstGolf.R;
import com.golfeven.firstGolf.base.BaseActivity;
import com.golfeven.firstGolf.bean.News;
import com.golfeven.firstGolf.bean.NewsDetail;
import com.golfeven.firstGolf.common.Constant;
import com.golfeven.firstGolf.common.HtmlUtil;
import com.golfeven.firstGolf.common.MyLog;
import com.golfeven.firstGolf.common.StringUtils;
import com.golfeven.firstGolf.widget.HeadBack;
import com.golfeven.firstGolf.widget.MyToast;

public class NewsDetailActivity extends BaseActivity {
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
	
	
	
	private String typeid = "44";
	private String order="1";
	private int index=0;//表示选择的第几个
	private List<News> list;//传过来的列表
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
		fh = new FinalHttp();
		details = new HashMap<String, NewsDetail>();
		Intent intent = getIntent();
		index = intent.getIntExtra("index",0);
		typeid = intent.getStringExtra("typeid");
		order = intent.getStringExtra("order");
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
						MyToast.customToast(NewsDetailActivity.this,
								Toast.LENGTH_SHORT, TOAST_MSG_ERROR_TITLE,
								TOAST_MSG_LOADMORE_ERROR_CONTENT,
								Constant.TOAST_IMG_ERROR);
					}

				});
	}

	private void initData() {

		// String body =
		// "<div class='img_wrapper' style='text-align: center; padding-bottom: 5px; widows: 2; text-transform: none; background-color: rgb(255,255,255); text-indent: 0px; letter-spacing: normal; font: 14px/23px 宋体; white-space: normal; orphans: 2; color: rgb(51,51,51); word-spacing: 0px; -webkit-text-size-adjust: auto; -webkit-text-stroke-width: 0px'> <img alt='麦克罗伊手握网球拍' src='/uploads/allimg/130627/1134114Q4-0.jpg' style='border-bottom: rgb(231,231,231) 1px solid; border-left: rgb(231,231,231) 1px solid; padding-bottom: 0px; margin: 0px auto; padding-left: 0px; padding-right: 0px; display: block; border-top: rgb(231,231,231) 1px solid; border-right: rgb(231,231,231) 1px solid; padding-top: 0px' /><span class='img_descr' style='text-align: left; padding-bottom: 6px; line-height: 20px; margin: 5px auto; padding-left: 0px; padding-right: 0px; zoom: 1; display: inline-block; color: rgb(102,102,102); font-size: 12px; padding-top: 6px'>麦克罗伊手握网球拍</span></div> <p style='padding-bottom: 0px; widows: 2; text-transform: none; background-color: rgb(255,255,255); text-indent: 0px; margin: 15px 0px; padding-left: 0px; letter-spacing: normal; padding-right: 0px; font: 14px/23px 宋体; white-space: normal; orphans: 2; color: rgb(51,51,51); word-spacing: 0px; padding-top: 0px; -webkit-text-size-adjust: auto; -webkit-text-stroke-width: 0px'> 　　第一高尔夫讯　北京时间6月27日消息，想使用温丹健身房的球员有个好消息：他们再也不用和罗里-麦克罗伊争用跑步机了。</p> <p style='padding-bottom: 0px; widows: 2; text-transform: none; background-color: rgb(255,255,255); text-indent: 0px; margin: 15px 0px; padding-left: 0px; letter-spacing: normal; padding-right: 0px; font: 14px/23px 宋体; white-space: normal; orphans: 2; color: rgb(51,51,51); word-spacing: 0px; padding-top: 0px; -webkit-text-size-adjust: auto; -webkit-text-stroke-width: 0px'> 　　在温丹网球锦标赛上陪伴女友沃兹尼亚奇时，麦克罗伊也一直使用者为球员们提供的健身房健身，不过这却引起了一些小争议。</p> <p style='padding-bottom: 0px; widows: 2; text-transform: none; background-color: rgb(255,255,255); text-indent: 0px; margin: 15px 0px; padding-left: 0px; letter-spacing: normal; padding-right: 0px; font: 14px/23px 宋体; white-space: normal; orphans: 2; color: rgb(51,51,51); word-spacing: 0px; padding-top: 0px; -webkit-text-size-adjust: auto; -webkit-text-stroke-width: 0px'> 　　在小麦使用了健身房以后，玛丽亚-基里连科的男友冰球明星亚利桑达-奥文金(Alexander Oveckin)也去健身房健身，但却被告知健身房的设施只为球员和教练提供。这引起了基里连科的不爽。</p> <p style='padding-bottom: 0px; widows: 2; text-transform: none; background-color: rgb(255,255,255); text-indent: 0px; margin: 15px 0px; padding-left: 0px; letter-spacing: normal; padding-right: 0px; font: 14px/23px 宋体; white-space: normal; orphans: 2; color: rgb(51,51,51); word-spacing: 0px; padding-top: 0px; -webkit-text-size-adjust: auto; -webkit-text-stroke-width: 0px'> 　　不过现在麦克罗伊已经离开了温丹小镇，准备周四开战斗爱尔兰公开赛了。而他的女友在不慎滑倒受伤的情况下，以两个2-6不敌捷克选手塞特科夫斯卡，爆冷出局。这也意味着，麦克罗伊将不用再牵挂这里，所有的争议也就自动消失了。</p> <div class='img_wrapper' style='text-align: center; padding-bottom: 5px; widows: 2; text-transform: none; background-color: rgb(255,255,255); text-indent: 0px; letter-spacing: normal; font: 14px/23px 宋体; white-space: normal; orphans: 2; color: rgb(51,51,51); word-spacing: 0px; -webkit-text-size-adjust: auto; -webkit-text-stroke-width: 0px'> <img alt='沃兹尼亚奇受伤了' src='/uploads/allimg/130627/1134114Z4-1.jpg' style='border-bottom: rgb(231,231,231) 1px solid; border-left: rgb(231,231,231) 1px solid; padding-bottom: 0px; margin: 0px auto; padding-left: 0px; padding-right: 0px; display: block; border-top: rgb(231,231,231) 1px solid; border-right: rgb(231,231,231) 1px solid; padding-top: 0px' title='沃兹尼亚奇受伤了' /><span class='img_descr' style='text-align: left; padding-bottom: 6px; line-height: 20px; margin: 5px auto; padding-left: 0px; padding-right: 0px; zoom: 1; display: inline-block; color: rgb(102,102,102); font-size: 12px; padding-top: 6px'>沃兹尼亚奇受伤了</span></div> <p style='padding-bottom: 0px; widows: 2; text-transform: none; background-color: rgb(255,255,255); text-indent: 0px; margin: 15px 0px; padding-left: 0px; letter-spacing: normal; padding-right: 0px; font: 14px/23px 宋体; white-space: normal; orphans: 2; color: rgb(51,51,51); word-spacing: 0px; padding-top: 0px; -webkit-text-size-adjust: auto; -webkit-text-stroke-width: 0px'> 　　麦克罗伊健身一开始是受到沃兹尼亚奇的激励，但现在随着程度再次进阶，变成了自己的爱好。</p> ";
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

	/**
	 * 上一页
	 * @param view
	 */
	public void lastNews(View view) {
		btnNext.setClickable(true);
		if(index<=0){
			btnLast.setClickable(false);
			MyToast.customToast(NewsDetailActivity.this,
					Toast.LENGTH_SHORT, TOAST_MSG_WARNING_TITLE,
					"已经是第一页了!",
					Constant.TOAST_IMG_WARNING);
			return;
		}
		index--;
		load();
		

	}

	/**
	 * 下一页
	 * @param view
	 */
	public void nextNews(View view) {

		if(index == list.size()-1){
			LoadList();
			return;
		}
		index++;
		load();
	}
	
	public void LoadList(){
		AjaxParams params = new AjaxParams();
		params.put("cmd", "news");
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
					btnNext.setClickable(false);
					MyToast.customToast(NewsDetailActivity.this, Toast.LENGTH_SHORT,
							TOAST_MSG_WARNING_TITLE, TOAST_MSG_NOMORE_CONTENT,
							Constant.TOAST_IMG_ERROR);
				}else{
					List<News> mlList = JSONArray.parseArray(t, News.class);
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
				MyToast.customToast(NewsDetailActivity.this, Toast.LENGTH_SHORT,
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
