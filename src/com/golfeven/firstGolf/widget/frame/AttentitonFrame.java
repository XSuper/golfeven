package com.golfeven.firstGolf.widget.frame;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.tsz.afinal.FinalDb;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.golfeven.AppContext;
import com.golfeven.firstGolf.R;
import com.golfeven.firstGolf.adapter.BallFriendAttentionListAdapter;
import com.golfeven.firstGolf.adapter.BallFriendListAdapter;
import com.golfeven.firstGolf.base.MBaseAdapter;
import com.golfeven.firstGolf.bean.BallFriend;
import com.golfeven.firstGolf.common.Constant;
import com.golfeven.firstGolf.common.MyLog;
import com.golfeven.firstGolf.common.StringUtils;
import com.golfeven.firstGolf.ui.MainActivity;
import com.golfeven.firstGolf.widget.MyToast;
import com.golfeven.firstGolf.widget.PullToRefreshListView;
import com.golfeven.firstGolf.widget.PullToRefreshListView.OnRefreshListener;

public class AttentitonFrame extends LinearLayout{
	AppContext appContext;
	View mto;//关注
	View otm;//粉丝
	
	boolean ismto = true;//是不是我关注别人
	public final static String CMDMTO ="Member.focusMember"; 
	public final static String CMDOTM ="Member_friend.fansList";
	
	
	public AttentitonFrame(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context; 
		appContext = (AppContext)context.getApplicationContext();
		if(appContext.isLogin){
			
			onCreate();
		}
	}

	public Context context;
	public Class entityClass;
	public MBaseAdapter adapter;
	public PullToRefreshListView listView;
	public MainActivity mainActivity;
	public int layoutResid;
	public List datas = new ArrayList();
	public AjaxParams params = new AjaxParams();
	SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	// public String key_cmd = "golfInfo";
	public int key_row = 20;
	public int key_rowindex = 0;

	public FinalHttp fh = new FinalHttp();
	public FinalDb db;
	public View footer;
	public TextView foot_more;
	public ProgressBar foot_progress;

	private boolean isEnd = false;// 是否加载到最后

	// listView 状态
	public static final int LOAD_SUCCESS = 0x1;
	public static final int LOAD_FAIL = 0x2;
	public static final int REFRESH_SUCCESS = 0x3;
	public static final int REFRESH_FAIL = 0x4;
	public static final int LOADMORE_SUCCESS = 0x5;
	public static final int LOADMORE_FAIL = 0x6;
	public static final int NOMORE = 0x10;

	public static final int LOAD = 0x7;
	public static final int LOADMORE = 0x8;
	public static final int REFRESH = 0x9;

	// public static final int TAG_KEY_TIME = R.string.list_lastUpdate;

	// toast信息常量
	public static final String TOAST_MSG_SUCCESS_TITLE = "成功";
	public static final String TOAST_MSG_ERROR_TITLE = "失败";
	public static final String TOAST_MSG_WARNING_TITLE = "提示";

	public static final String TOAST_MSG_LOAD_SUCCESS_CONTENT = "信息更新成功！";
	public static final String TOAST_MSG_LOAD_ERROR_CONTENT = "信息更新失败！";

	public static final String TOAST_MSG_LOADMORE_SUCCESS_CONTENT = "信息加载成功！";
	public static final String TOAST_MSG_LOADMORE_ERROR_CONTENT = "信息加载失败！";

	public static final String TOAST_MSG_REFRESH_SUCCESS_CONTENT = "刷新成功！";
	public static final String TOAST_MSG_REFRESH_ERROR_CONTENT = "刷新失败！";

	public static final String TOAST_MSG_NOMORE_CONTENT = "没有更多的内容！";

	public Handler mHandler;

	public void onCreate() {
		// TODO Auto-generated method stub
		mustInit();
		mHandler = new Handler() {

			@Override
			public void dispatchMessage(Message msg) {
				// TODO Auto-generated method stub
				super.dispatchMessage(msg);
				switch (msg.what) {
				case LOAD_SUCCESS:// 加载成功
					// MyToast.customToast(context, Toast.LENGTH_SHORT,
					// TOAST_MSG_SUCCESS_TITLE,
					// TOAST_MSG_LOAD_SUCCESS_CONTENT,
					// Constant.TOAST_IMG_SUCCESS);
					break;
				case LOAD_FAIL:// 加载失败

					break;
				case REFRESH_SUCCESS:
					MyToast.customToast(context, Toast.LENGTH_SHORT,
							TOAST_MSG_SUCCESS_TITLE,
							TOAST_MSG_REFRESH_SUCCESS_CONTENT,
							Constant.TOAST_IMG_SUCCESS);
					break;
				case REFRESH_FAIL:
					MyToast.customToast(context, Toast.LENGTH_SHORT,
							TOAST_MSG_ERROR_TITLE,
							TOAST_MSG_REFRESH_ERROR_CONTENT,
							Constant.TOAST_IMG_ERROR);

					break;
				case LOADMORE_SUCCESS:
					footer.setVisibility(View.INVISIBLE);

					break;
				case LOADMORE_FAIL:
					footer.setVisibility(View.INVISIBLE);
					MyToast.customToast(context, Toast.LENGTH_SHORT,
							TOAST_MSG_ERROR_TITLE,
							TOAST_MSG_LOADMORE_ERROR_CONTENT,
							Constant.TOAST_IMG_ERROR);

					break;
				case NOMORE:
					footer.setVisibility(View.VISIBLE);
					foot_more.setText("已加载全部");
					foot_progress.setVisibility(View.INVISIBLE);
					MyToast.customToast(context, Toast.LENGTH_SHORT,
							TOAST_MSG_WARNING_TITLE, TOAST_MSG_NOMORE_CONTENT,
							Constant.TOAST_IMG_WARNING);

					break;

				}

			}

		};

		// 得到上拉加载的布局
		footer = ((Activity)context).getLayoutInflater().inflate(R.layout.listview_footer, null);
		foot_more = (TextView) footer.findViewById(R.id.listview_foot_more);
		foot_progress = (ProgressBar) footer
				.findViewById(R.id.listview_foot_progress);
		// adapter = new GolfInfoListAdapter(context, datas);

		listView.addFooterView(footer); // 将上拉加载的视图添加到listView中
		footer.setVisibility(View.INVISIBLE);
		// 添加滚动事件
		listView.setOnScrollListener(new OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				listView.onScrollStateChanged(view, scrollState);
				if (datas.isEmpty())
					return;

				// 判断是否滚动到底部
				boolean scrollEnd = false;
				try {
					if (view.getPositionForView(footer) == view
							.getLastVisiblePosition())
						scrollEnd = true;
				} catch (Exception e) {
					scrollEnd = false;
				}
				if (scrollEnd && !isEnd
						&& mainActivity.mprogressBar.getVisibility() == View.INVISIBLE) {
					footer.setVisibility(View.VISIBLE);
					foot_more.setText(R.string.loading);
					foot_progress.setVisibility(View.VISIBLE);
					listView.setTag(LOADMORE);
					mainActivity.setProgressDisplay(true);

					key_rowindex += key_row;

					requestData();
				}

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				listView.onScroll(view, firstVisibleItem, visibleItemCount,
						totalItemCount);

			}
		});
		// 下拉刷新的操作
		listView.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				if (mainActivity.mprogressBar.getVisibility() == View.VISIBLE) {
					return;
				}
				// 数据重置
				reset();

				listView.setTag(REFRESH);
				// Object oTime = listView.getTag(TAG_KEY_TIME);
				// if(oTime!= null&&oTime instanceof String){
				// listView.setLastUpdated(StringUtils.friendly_time(oTime.toString()));
				// }
				// headBack.setProgressVisible(true);
				requestData();

			}
		});
		listView.setAdapter(adapter);
		adapter.refresh(datas);
		listView.setTag(LOAD);
		requestData();
	}

	/**
	 * 请求数据
	 */
	public synchronized void requestData() {
		params.put("rowIndex", key_rowindex + "");
		params.put("row", key_row + "");
		// if(headBack.getProgressVisibleState()==View.VISIBLE){
		// return;
		// }


		mainActivity.setProgressDisplay(true);
		fh.get(Constant.URL_BASE, params, new AjaxCallBack<String>() {
			@Override
			public void onSuccess(String t) {
				super.onSuccess(t);
				mainActivity.setProgressDisplay(false);
				footer.setVisibility(View.INVISIBLE);
				Integer tag = ((Integer) listView.getTag());
				Date date = new Date();
				switch (tag) {
				case LOAD:
					try {
						datas = JSON.parseArray(t, entityClass);
						
					} catch (Exception e) {
						// TODO: handle exception
					}
					if(datas!=null&&datas.size()<key_row){
						isEnd = true;
					}
					// listView.setTag(TAG_KEY_TIME,sf.format(date));
					listView.onRefreshComplete(sf.format(date));
					mHandler.sendEmptyMessage(LOAD_SUCCESS);
					break;
				case REFRESH:
					datas.clear();
					// listView.setTag(TAG_KEY_TIME,sf.format(date));
					try {
						
						datas = JSON.parseArray(t, entityClass);
					} catch (Exception e) {
						// TODO: handle exception
					}
					if(datas!=null&&datas.size()<key_row){
						isEnd = true;
					}
					listView.onRefreshComplete(sf.format(date));
					mHandler.sendEmptyMessage(REFRESH_SUCCESS);
					break;
				case LOADMORE:
					if (StringUtils.responseError(t)) {
						isEnd = true;
						mHandler.sendEmptyMessage(NOMORE);

						return;
					}
					List mdata = JSON.parseArray(t, entityClass);
					if(mdata!=null&&mdata.size()<key_row){
						isEnd = true;
					}
					datas.addAll(mdata);
					mHandler.sendEmptyMessage(LOADMORE_SUCCESS);
					break;

				}
				adapter.refresh(datas);
			}

			@Override
			public void onFailure(Throwable t, String strMsg) {
				super.onFailure(t, strMsg);
				MyLog.v("fail", t.toString());
				mainActivity.setProgressDisplay(true);

				Integer tag = ((Integer) listView.getTag());
				switch (tag) {
				case LOAD:
					mHandler.sendEmptyMessage(LOAD_FAIL);
					listView.onRefreshComplete();
					break;
				case REFRESH:
					listView.onRefreshComplete();
					mHandler.sendEmptyMessage(REFRESH_FAIL);
					break;
				case LOADMORE:
					mHandler.sendEmptyMessage(LOADMORE_FAIL);
					key_rowindex -= key_row;
					isEnd = true;

					break;

				}
			}
		});
	}

	public void reset() {
		isEnd = false;
		key_rowindex = 0;
	}
	

	/**
	 * <b>初始化数据</b> 必须按照下面顺序
	 * 
	 * this.layoutResid = R.layout.activity_golfinfo_list;
	 * setContentView(layoutResid); this.context = GolfInfosActivity.this;
	 * this.entityClass = GolfInfo.class ; this.adapter = new
	 * GolfInfoListAdapter(context, datas); this.headBack
	 * =(HeadBack)findViewById(R.id.activity_golfinfo_list_headback);
	 * this.listView
	 * =(PullToRefreshListView)findViewById(R.id.activity_golfinfo_list);
	 * this.params.put("cmd","golfInfo");
	 * 
	 */
	public  void mustInit(){
		
		
		View view = LayoutInflater.from(context).inflate(
				R.layout.frame_attention, this);
		mto = view.findViewById(R.id.frame_attention_mto);
		otm = view.findViewById(R.id.frame_attention_otm);
		
		this.entityClass = BallFriend.class ;
		this.adapter = new BallFriendAttentionListAdapter(context, datas);
		this.mainActivity = ((MainActivity)context);
		this.listView = (PullToRefreshListView)view.findViewById(R.id.frame_attention_list);
		this.params.put("cmd","Member.focusMember");
		this.params.put("mid",appContext.user.getMid());
		this.params.put("token",appContext.user.getToken());
		
		MyOnClickListener clickListener = new MyOnClickListener();
		mto.setOnClickListener(clickListener);
		otm.setOnClickListener(clickListener);
	}
	
	public void onResume() {
		
		if(appContext.isLogin){
			if(adapter==null){
				onCreate();
			}else{
				datas= new ArrayList();
				adapter.refresh(datas);
				if(ismto){
					
					this.params.put("cmd",CMDMTO);
					((BallFriendAttentionListAdapter)adapter).isAttention = true;
				}else{
					this.params.put("cmd",CMDOTM);
					((BallFriendAttentionListAdapter)adapter).isAttention = false;
				}
				this.params.put("mid",appContext.user.getMid());
				this.params.put("token",appContext.user.getToken());
				reset();
				listView.setTag(LOAD);
				requestData();
			}
		}else{
			if(adapter!=null){
				
				this.params.remove("mid");
				this.params.remove("token");
				datas= new ArrayList();
				adapter.refresh(datas);
			}
		}
	}
	View selectView = mto;//设置初始显示按钮
	class MyOnClickListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			if(v==selectView){
				return;
			}
			selectView = v;
			// TODO Auto-generated method stub
			mto.setBackgroundResource(R.drawable.g_tab_bg01);
			otm.setBackgroundResource(R.drawable.g_tab_bg01);
			if(v==mto){
				params.put("cmd",CMDMTO );
				((BallFriendAttentionListAdapter)adapter).isAttention = true;
				ismto=true;
			}
			if(v==otm){
				params.put("cmd",CMDOTM);
				ismto = false;
				((BallFriendAttentionListAdapter)adapter).isAttention = false;
			}
			datas = new ArrayList();
			adapter.refresh(datas);
			v.setBackgroundResource(R.drawable.g_tab_bg02);
			reset();
			listView.setTag(LOAD);
			requestData();
			
		}
		
	}

}
