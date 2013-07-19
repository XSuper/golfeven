package com.golfeven.firstGolf.common;

import android.os.Environment;

import com.golfeven.firstGolf.R;

public class Constant {
	
	public static final String SQLITE_NAME="mgolfeven";//数据库名字  
	public static final String SQLITE_NAME_HOME="golfeven_home";//主页数据库名字 
	
	
	public static final String FILE_COMMON="file_common";//共用的属性文件  
	public static final String FILE_COMMON_FIRSTUSER="file_common_first";//共用的属性文件  
	public static final String FILE_COMMON_WEATHERINFO="common_weatherinfo";//共用的属性文件  保存天气
	public static final String FILE_COMMON_ISSCROLL="common_isscroll";//是否可以滑屏
	public static final String FILE_COMMON_OPENVOICE="common_openvoice";//是否打开声音
	
	
	public static final String FILE_USER="file_user";
	public static final String FILE_USER_USERID="file_user_userid";
	public static final String FILE_USER_UPASS="file_user_upass";
	
	
	public static final String FILE_MAP="file_map";//百度地图保存的信息
	public static final String FILE_MAP_PLACE="map_place";// 保存地址
	public static final String FILE_MAP_PROVINCE="map_place_province";// 省份
	public static final String FILE_MAP_CITY="map_place_city";//保存城市
	public static final String FILE_MAP_LONGITUDE="map_place_longitude";//经度
	public static final String FILE_MAP_LATITUDE="map_place_Latitude";//纬度
	
	
	
	
	public static final String HEAD_CITY_DEFAULT="无法获取城市信息";
	public static final String HEAD_WEATHER_DEFAULT="点击获取信息";//未获取天气时候 显示在头部的信息
	
	
	
	public static final int TOAST_IMG_ERROR=R.drawable.ic_launcher;//自定义toast的错误图片
	public static final int TOAST_IMG_SUCCESS=R.drawable.ic_launcher;//自定义toast的成功图片
	public static final int TOAST_IMG_WARNING=R.drawable.ic_launcher;//自定义toast的警告图片
	
	public static final int IMG_LOADING_FAIL=R.drawable.img_default;
	public static final int IMG_LOADING=R.drawable.ic_launcher;
	public static final String IMG_CACHEPATH=Environment.getExternalStorageDirectory()+"/golfeven_cache";
	
	
	
	public static final int HOME_NAVIGATIN_IMG_SIZE = 10;
	
	//--------------------url------
	public static final String URL_BASE="Http://www.a8.hk/jim/service.php";
	public static final String URL_IMG_BASE="Http://www.a8.hk/";

}
