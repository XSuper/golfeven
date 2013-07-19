package com.golfeven.firstGolf.widget;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import net.tsz.afinal.FinalBitmap;
import android.content.Context;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.golfeven.AppContext;
import com.golfeven.firstGolf.R;
import com.golfeven.firstGolf.bean.Image;
import com.golfeven.firstGolf.common.Constant;

public class MainPageView extends LinearLayout {
	private List<Image> images;
	private int size=0;
	
	private Context context;
	private FinalBitmap fb;
	
	
	public LinearLayout parent;
	private LinearLayout Indicators;
	private ViewPager viewPager; // android-support-v4中的滑动组件
	private List<ImageView> imageViews; // 滑动的图片集合

	private List<View> dots; // 图片标题正文的那些点
	private TextView tv_title;
	private int currentItem = 0; // 当前图片的索引号
	
	private View view;
	// 切换当前显示的图片 该handler
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			viewPager.setCurrentItem(currentItem);// 切换当前显示的图片
		};
	};
	// An ExecutorService that can schedule commands to run after a given delay,
	// or to execute periodically.
	private ScheduledExecutorService scheduledExecutorService;

	public MainPageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		this.context = context;
		fb = ((AppContext)context.getApplicationContext()).getFB(true);
		view = LayoutInflater.from(context).inflate(
				R.layout.main_pageview, this);
		parent = (LinearLayout)view.findViewById(R.id.main_pageview);
		
		
	}
	public void setImages(List<Image> images){
		this.images = images;
		if(images==null||images.size()==0){
			
			String json ="[" +
					"{ddimg:'/uploads/allimg/130706/7-130F6142P1.jpg',text: '球0000实景',width: '300',height: '225'}," +
					"{ddimg: '/uploads/allimg/130706/7-130F6142P2.jpg',text: '球111实景',width: '600',height: '450'}," +
					"{ddimg: '/uploads/allimg/130706/7-130F6142P5.jpg',text: '00场实景',width: '600',height: '450'}," +
					"{ddimg: '/uploads/allimg/130706/7-130F6142P6.jpg',text: '球场实景',width: '600',height: '450'}" +
					"{ddimg:'/uploads/allimg/130706/7-130F6142P1.jpg',text: '球场收到萨顶顶实景',width: '300',height: '225'}," +
					"{ddimg: '/uploads/allimg/130706/7-130F6142P2.jpg',text: '球场实景',width: '600',height: '450'}," +
					"{ddimg: '/uploads/allimg/130706/7-130F6142P5.jpg',text: '球场实sdadsa 景',width: '600',height: '450'}," +
					"{ddimg: '/uploads/allimg/130706/7-130F6142P6.jpg',text: '球场实景',width: '600',height: '450'}" +
					"]";
			
			this.images = JSONArray.parseArray(json, Image.class);
		}
		
		
		init();
	}

	private void init() {
		size = images.size();
		
		Indicators = (LinearLayout)view.findViewById(R.id.tv_Indicators);
		imageViews = new ArrayList<ImageView>();
		dots = new ArrayList<View>();
		// 初始化图片资源
		tv_title = (TextView) view.findViewById(R.id.tv_title);
		tv_title.setText(images.get(0).getText());//
		viewPager = (ViewPager) view.findViewById(R.id.vp);
		for (int i = 0; i < size; i++) {
			ImageView imageView = new ImageView(getContext());
			//imageView.setImageResource(imageResId[i]);
			fb.display(imageView, Constant.URL_IMG_BASE
					+ images.get(i).getDdimg(),
					viewPager.getWidth(), viewPager.getHeight());
			imageView.setScaleType(ScaleType.CENTER_CROP);
			imageViews.add(imageView);
			ImageView image = getIndicatorImage();
			Indicators.measure(MeasureSpec.EXACTLY,
					MeasureSpec.EXACTLY);
			Indicators.layout(0, 0, 0, 0);
			Indicators.addView(image);
			dots.add(image);
		}
		viewPager.setAdapter(new MyPagerAdapter());// 设置填充ViewPager页面的适配器
		// 设置一个监听器，当ViewPager中的页面改变时调用
		viewPager.setOnPageChangeListener(new MyPageChangeListener());
		onStart();
	}
	
	public ImageView getIndicatorImage() {
		ImageView img = new ImageView(context);
		LayoutParams params = new LayoutParams(10,
				10);
		img.setImageResource(R.drawable.dot_focused);
		params.gravity = Gravity.CENTER_VERTICAL;
		img.setLayoutParams(params);
		params.setMargins(5, 5, 5, 5);
		img.setClickable(true);
		//img.setPadding(150,150, 150, 150);
		return img;
	}

	public void onStop() {
		// 当Activity不可见的时候停止切换
		scheduledExecutorService.shutdown();
		//scheduledExecutorService.shutdownNow();
		// ****************头部滚动
	}
	public void onStart() {
		scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
		// 当Activity显示出来后，每两秒钟切换一次图片显示
		scheduledExecutorService.scheduleAtFixedRate(new ScrollTask(), 0, 3,
				TimeUnit.SECONDS);
	}

	/**
	 * 换行切换任务
	 * 
	 * @author Administrator
	 * 
	 */
	private class ScrollTask implements Runnable {

		public void run() {
			synchronized (viewPager) {
				 try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}  
				System.out.println("currentItem: " + currentItem);
				currentItem = ++currentItem  % imageViews.size();
				handler.obtainMessage().sendToTarget(); // 通过Handler切换图片
			}
		}

	}

	/**
	 * 当ViewPager中页面的状态发生改变时调用
	 * 
	 * @author Administrator
	 * 
	 */
	private class MyPageChangeListener implements OnPageChangeListener {
		private int oldPosition = 0;

		/**
		 * This method will be invoked when a new page becomes selected.
		 * position: Position index of the new selected page.
		 */
		public void onPageSelected(int position) {
			currentItem = position;
			tv_title.setText(images.get(position).getText());
			dots.get(oldPosition).setBackgroundResource(R.drawable.dot_normal);
			dots.get(position).setBackgroundResource(R.drawable.dot_focused);
			oldPosition = position;
		}

		public void onPageScrollStateChanged(int arg0) {

		}

		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}
	}

	/**
	 * 填充ViewPager页面的适配器
	 * 
	 * @author Administrator
	 * 
	 */
	private class MyPagerAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return size;
		}

		@Override
		public Object instantiateItem(View arg0, int arg1) {
			((ViewPager) arg0).addView(imageViews.get(arg1));
			return imageViews.get(arg1);
		}

		@Override
		public void destroyItem(View arg0, int arg1, Object arg2) {
			((ViewPager) arg0).removeView((View) arg2);
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {

		}

		@Override
		public Parcelable saveState() {
			return null;
		}

		@Override
		public void startUpdate(View arg0) {

		}

		@Override
		public void finishUpdate(View arg0) {

		}
	}

}
