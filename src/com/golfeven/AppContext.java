package com.golfeven;

import java.util.HashMap;
import java.util.Map;

import net.tsz.afinal.FinalBitmap;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Application;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.golfeven.firstGolf.api.Api;
import com.golfeven.firstGolf.bean.User;
import com.golfeven.firstGolf.common.Constant;
import com.golfeven.firstGolf.common.MyLog;
import com.golfeven.firstGolf.common.SharedPreferencesUtil;
import com.golfeven.firstGolf.common.StringUtils;
import com.golfeven.firstGolf.widget.MyToast;
import com.golfeven.weather.WeatherUtil;

public class AppContext extends Application {
	
	
	//****************初始化一些全局用到的变量
	private FinalBitmap fb; //用到的图片加载,设置统一的加载图片和  统一的失败图片
	
	
	
	public User user;
	public String uname;
	public String upass;

	public boolean isLogin;// 当前登录状态、
	public boolean isScroll;// 是否允许滑屏
	public boolean openVoice;// 是否打开声音

	public String weatherInfo;// 天气信息
	
	public String province;// 省份
	public String city;// 城市
	public String place;// 详细地址
	public String longitude;// 经度
	public String latitude;// 纬度

	// *********************************************
	// 百度地图定位
	public LocationClient mMapClient = null;
	public MyLocationListenner myListener = new MyLocationListenner();
	public static String TAG = "BaiDuMapGPS";

	// *********************************************

	/**
	 * 初始化文件设置  和  信息
	 */
	public void getInfo() {
		
		weatherInfo = SharedPreferencesUtil.read(
				Constant.FILE_COMMON, getApplicationContext(),
				Constant.FILE_COMMON_WEATHERINFO);
		city = SharedPreferencesUtil.read(Constant.FILE_MAP,
				getApplicationContext(), Constant.FILE_MAP_CITY);
		province = SharedPreferencesUtil.read(Constant.FILE_MAP,
				getApplicationContext(), Constant.FILE_MAP_PROVINCE);
		place = SharedPreferencesUtil.read(Constant.FILE_MAP,
				getApplicationContext(), Constant.FILE_MAP_PLACE);
		longitude = SharedPreferencesUtil.read(Constant.FILE_MAP,
				getApplicationContext(), Constant.FILE_MAP_LONGITUDE);
		latitude = SharedPreferencesUtil.read(Constant.FILE_MAP,
				getApplicationContext(), Constant.FILE_MAP_LATITUDE);
		String scrollStr = SharedPreferencesUtil.read(
				Constant.FILE_COMMON, getApplicationContext(),
				Constant.FILE_COMMON_ISSCROLL);
		isScroll = StringUtils.isEmpty(scrollStr)||"true".equals(scrollStr);
		
		String openVStr = SharedPreferencesUtil.read(
				Constant.FILE_COMMON, getApplicationContext(),
				Constant.FILE_COMMON_OPENVOICE);
		
		openVoice = StringUtils.isEmpty(openVStr)||"true".equals(openVStr);
		
		
		// 第一次进入程序，将天气的值设置为默认
		weatherInfo = StringUtils.isEmpty(weatherInfo)?Constant.HEAD_WEATHER_DEFAULT:weatherInfo;
		// 第一次进入程序，将城市的值设置为默认
		city = StringUtils.isEmpty(city)?Constant.HEAD_CITY_DEFAULT:city;
		
		uname = SharedPreferencesUtil.read(Constant.FILE_USER,
				getApplicationContext(), Constant.FILE_USER_USERID);
		upass = SharedPreferencesUtil.read(Constant.FILE_USER,
				getApplicationContext(), Constant.FILE_USER_UPASS);
		
	}

	/**
	 * 将信息保存到文件
	 */
	public void saveInfo() {
		Map<String, String> common_data = new HashMap<String, String>();
		common_data.put(Constant.FILE_COMMON_WEATHERINFO, weatherInfo);
		common_data.put(Constant.FILE_COMMON_ISSCROLL, isScroll+"");
		common_data.put(Constant.FILE_COMMON_OPENVOICE, openVoice+"");
		SharedPreferencesUtil.keep(Constant.FILE_COMMON,
				getApplicationContext(), common_data);
		
		Map<String, String> map_data = new HashMap<String, String>();
		map_data.put(Constant.FILE_MAP_CITY, city);
		map_data.put(Constant.FILE_MAP_LATITUDE, latitude);
		map_data.put(Constant.FILE_MAP_LONGITUDE, longitude);
		map_data.put(Constant.FILE_MAP_PLACE, place);
		map_data.put(Constant.FILE_MAP_PROVINCE, province);
		
		SharedPreferencesUtil.keep(Constant.FILE_MAP,
				getApplicationContext(), map_data);
	}

	private void loginAndUpdatePlace() {
		Api.getInstance().login(uname, upass, new AjaxCallBack<String>() {

			@Override
			public void onSuccess(String t) {
				// TODO Auto-generated method stub
				super.onSuccess(t);
				if(t.trim().startsWith("{")){
					user = JSON.parseObject(t, User.class);
					isLogin = true;
					Api.getInstance().updatePlace(user, longitude, latitude);
//					Map<String, String> user = new HashMap<String, String>();
//					user.put(Constant.FILE_USER_USERID, uname);
//					user.put(Constant.FILE_USER_UPASS,upass);
//					SharedPreferencesUtil.keep(Constant.FILE_USER,
//							getApplicationContext(), user);
				}
			}

			@Override
			public void onFailure(Throwable t, String strMsg) {
				// TODO Auto-generated method stub
				super.onFailure(t, strMsg);
				MyToast.centerToast(getApplicationContext(),"自动登陆失败",Toast.LENGTH_SHORT);
			}
			
		});

	}
	@Override
	public void onCreate() {
		getInfo();
		initBaiduMap();// 百度地图定位
		loginAndUpdatePlace();
		super.onCreate();
	}

	// 更新天气
	public void updataSimpleWeatherInfo(String id) {
		String url = "http://www.weather.com.cn/data/cityinfo/" + id + ".html";
		new FinalHttp().get(url, new AjaxCallBack<String>() {

			@Override
			public void onSuccess(String t) {
				// TODO Auto-generated method stub

				super.onSuccess(t);
				JSONObject obj = null;
				try {
					obj = new JSONObject(t).getJSONObject("weatherinfo");
					weatherInfo = obj.getString("weather");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		});
	}

	// ******************************************************************************************************
	// *百度地图相关**************************************************************************************
	private void initBaiduMap() {

		mMapClient = new LocationClient(this);
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true); // 打开gps
		option.setCoorType("bd09ll"); // 设置坐标类型
		// option.setServiceName("com.baidu.location.service_v2.9");
		option.setProdName("FirstGolf");
		option.setPoiExtraInfo(true);
		option.setAddrType("all");
		mMapClient.registerLocationListener(myListener);
		option.setPoiNumber(5);
		// option.setScanSpan(10000);//设置发起定位请求的间隔时间为10000ms
		option.disableCache(true);
		mMapClient.setLocOption(option);
		mMapClient.start();
		mMapClient.requestLocation();// 请求定位
	}

	/**
	 * 
	 * 百度gps定位
	 * 
	 * 监听函数，又新位置的时候，格式化成字符串，输出到屏幕中
	 */
	public class MyLocationListenner implements BDLocationListener {
		@Override
		public void onReceiveLocation(BDLocation location) {
			if (location == null)
				return;
			StringBuffer sb = new StringBuffer(256);
			sb.append("time : ");
			sb.append(location.getTime());
			sb.append("\nerror code : ");
			sb.append(location.getLocType());
			sb.append("\nlatitude : ");
			sb.append(location.getLatitude());// 纬度
			
			latitude = location.getLatitude()+"";
			
			sb.append("\nlontitude : ");
			sb.append(location.getLongitude());// 经度
			
			longitude = location.getLongitude()+"";
			
			sb.append("\nradius : ");
			sb.append(location.getRadius());
//			if (StringUtils.isEmpty(SharedPreferencesUtil
//					.read(Constant.FILE_MAP, getBaseContext(),
//							Constant.FILE_MAP_CITY))) {
//				// 城市为空 没有网络的情况下 显示默认值
//				data.put(Constant.FILE_MAP_CITY, Constant.HEAD_CITY_DEFAULT);// 没有网络的情况下显示默认值
//			}
			if (location.getLocType() == BDLocation.TypeGpsLocation) {
				sb.append("\nspeed : ");
				sb.append(location.getSpeed());
				sb.append("\nsatellite : ");
				sb.append(location.getSatelliteNumber());
			} else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {
				sb.append("\n省：");
				sb.append(location.getProvince());

			province = location.getProvince();
				sb.append("\n市：");
				sb.append(location.getCity());
			city = location.getCity();

				String cityid = WeatherUtil.getCityId(
						StringUtils.placeToString(location.getProvince()),
						StringUtils.placeToString(location.getCity()));
				// 城市定位后 更新天气信息；
				updataSimpleWeatherInfo(cityid);

				sb.append("\n区/县：");
				sb.append(location.getDistrict());
				sb.append("\naddr : ");
				sb.append(location.getAddrStr());

			}
			sb.append("\nsdk version : ");
			sb.append(mMapClient.getVersion());
			sb.append("\nisCellChangeFlag : ");
			sb.append(location.isCellChangeFlag());
			MyLog.i(TAG, sb.toString());
		}

		public void onReceivePoi(BDLocation poiLocation) {
			if (poiLocation == null) {
				return;
			}
			StringBuffer sb = new StringBuffer(256);
			sb.append("Poi time : ");
			sb.append(poiLocation.getTime());
			sb.append("\nerror code : ");
			sb.append(poiLocation.getLocType());
			sb.append("\nlatitude : ");
			sb.append(poiLocation.getLatitude());
			sb.append("\nlontitude : ");
			sb.append(poiLocation.getLongitude());
			sb.append("\nradius : ");
			sb.append(poiLocation.getRadius());
			if (poiLocation.getLocType() == BDLocation.TypeNetWorkLocation) {
				sb.append("\naddr : ");
				sb.append(poiLocation.getAddrStr());
			}
			if (poiLocation.hasPoi()) {
				sb.append("\nPoi:");
				sb.append(poiLocation.getPoi());
			} else {
				sb.append("noPoi information");
			}
			MyLog.v(TAG, sb.toString());
		}
	}
	// *********百度地图相关end***********************************************************************************************
	
	
	//  工具----------------------
	
	/**
	 * 得到统一的finalbitmap
	 * @return
	 */
	public FinalBitmap getFB(boolean isSquare){
		
		FinalBitmap mfb = FinalBitmap.create(this, Constant.IMG_CACHEPATH,100);
			mfb.configLoadfailImage(Constant.IMG_LOADING_FAIL);
			mfb.configLoadingImage(Constant.IMG_LOADING);
			mfb.setSquare(isSquare);
		
		return mfb;
	}
	public FinalBitmap getFB(){
		if(fb==null){
			fb = FinalBitmap.create(this, Constant.IMG_CACHEPATH,100);
			fb.configLoadfailImage(Constant.IMG_LOADING_FAIL);
			fb.configLoadingImage(Constant.IMG_LOADING);
			fb.setSquare(true);
		}
		return fb;
	}
	
}
