package com.golfeven.firstGolf.common;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.Toast;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.MKGeneralListener;
import com.baidu.mapapi.map.MKEvent;

public class BMapUtil {
    	
	
	
	private static BMapUtil mapUtil;
	    public boolean m_bKeyRight = true;
	    public BMapManager mBMapManager = null;

	    Context context;
	    public static final String strKey = "8AA992E674F38E7758BFA5803040AC897F6F3E23";
		
		
			private BMapUtil(Context context) {
			super();
			// TODO Auto-generated constructor stub
			this.context = context;
			initEngineManager(context);
		}
			public static BMapUtil getInstance(Context context){
				if(mapUtil == null){
					mapUtil = new BMapUtil(context);
				}
				return mapUtil;
			}
		
		
		public void initEngineManager(Context context) {
	        if (mBMapManager == null) {
	            mBMapManager = new BMapManager(context);
	        }

	        if (!mBMapManager.init(strKey,new MyGeneralListener(context))) {
	            Toast.makeText(context, 
	                    "BMapManager  初始化错误!", Toast.LENGTH_LONG).show();
	        }
		}
		
	
		
		// 常用事件监听，用来处理通常的网络错误，授权验证错误等
	    static class MyGeneralListener implements MKGeneralListener {
	    	
	        private Context context;
	        public MyGeneralListener(Context context) {
				super();
				this.context = context;
				// TODO Auto-generated constructor stub
			}

			@Override
	        public void onGetNetworkState(int iError) {
	            if (iError == MKEvent.ERROR_NETWORK_CONNECT) {
	                Toast.makeText(context, "您的网络出错啦！",
	                    Toast.LENGTH_LONG).show();
	            }
	            else if (iError == MKEvent.ERROR_NETWORK_DATA) {
	                Toast.makeText(context, "输入正确的检索条件！",
	                        Toast.LENGTH_LONG).show();
	            }
	            // ...
	        }

	        @Override
	        public void onGetPermissionState(int iError) {
	            if (iError ==  MKEvent.ERROR_PERMISSION_DENIED) {
	                //授权Key错误：
	                Toast.makeText(context, 
	                        "请在 DemoApplication.java文件输入正确的授权Key！", Toast.LENGTH_LONG).show();
	                BMapUtil.getInstance(context).m_bKeyRight = false;
	            }
	        }
	    }
	
	/**
	 * 从view 得到图片
	 * @param view
	 * @return
	 */
	public static Bitmap getBitmapFromView(View view) {
        view.destroyDrawingCache();
        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.setDrawingCacheEnabled(true);
        Bitmap bitmap = view.getDrawingCache(true);
        return bitmap;
	}
}
