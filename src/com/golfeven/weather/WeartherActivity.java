package com.golfeven.weather;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.golfeven.AppContext;
import com.golfeven.firstGolf.R;
import com.golfeven.firstGolf.base.BaseActivity;
import com.golfeven.firstGolf.common.Constant;
import com.golfeven.firstGolf.common.MyLog;
import com.golfeven.firstGolf.common.SharedPreferencesUtil;
import com.golfeven.firstGolf.common.StringUtils;
import com.golfeven.firstGolf.widget.MyToast;

/**
 * 
 * @author Administrator
 * 
 */

public class WeartherActivity extends BaseActivity {
	

	TextView textView_city, text_updateTime, text_currTemp_in,
			text_currWindCondition_in, text_currAdvice_in;
	TextView[] view_Weekdays, view_Temps, view_Weathers;
	ImageView[] imgs;
	int[] img_res_ids;
	boolean is_Saved = false;
	// 信息存储
	String city_Name_Main, city_Id_Main = "101020100", temp_Main,
			wind_direction_MAIN, humidity_Main, date_Main, weather_01_Main,
			weather_02_Main, weather_03_Main, weather_04_Main, weather_05_Main,
			temp_02_Main, temp_03_Main, temp_04_Main, temp_05_Main,
			adivise_Main, refersh_time, day_of_week_Main;

	String[] weekdays_Main, temps_Main;
	WeatherInfo info;
	private static final int MSG_SUCCESS = 0;// 获取信息的标识
	private static final int MSG_FAILURE = 1;// 获取信息的标识
	/** Called when the activity is first created. */
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_FAILURE:
				Toast.makeText(getApplication(), "更新失败", Toast.LENGTH_SHORT)
						.show();
				break;
			case MSG_SUCCESS:
				refersh_time = getDate();
				changeUI(info);
				Toast.makeText(getApplication(), "更新成功", Toast.LENGTH_SHORT)
						.show();
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.weather_main);
		textView_city = (TextView) findViewById(R.id.text_city);
		text_updateTime = (TextView) findViewById(R.id.text_updateTime);
		text_currTemp_in = (TextView) findViewById(R.id.text_currTemp_in);
		text_currWindCondition_in = (TextView) findViewById(R.id.text_currWindCondition_in);
		text_currAdvice_in = (TextView) findViewById(R.id.text_currAdvice_in);

		view_Weathers = findTweathers();
		imgs = findImageView();
		view_Weekdays = findWeekDaysView();
		view_Temps = findTemps();

		SharedPreferences preferences = getPreferences(MODE_PRIVATE);
		is_Saved = preferences.getBoolean("is_Saved", false);
		final Button refresh = (Button) findViewById(R.id.button_refresh);

//		String pro = SharedPreferencesUtil.read(Constant.FILE_MAP,
//				getApplicationContext(), Constant.FILE_MAP_PROVINCE);
//		String city = SharedPreferencesUtil.read(Constant.FILE_MAP,
//				getApplicationContext(), Constant.FILE_MAP_CITY);
		city_Id_Main = WeatherUtil.getCityId(appContext.province, appContext.city);

		if (is_Saved) {
			changeUI(getInfo());

		}

		updateWeather(refresh);

		/**
		 * 刷新操作
		 */

		OnClickListener listener = new OnClickListener() {

			public void onClick(View v) {
				updateWeather(refresh);
			}
		};
		refresh.setOnClickListener(listener);

	}

	/**
	 * 获取图片
	 * 
	 * @return
	 */
	public ImageView[] findImageView() {
		int[] image = { R.id.img_currImage, R.id.img_01_imageView,
				R.id.img_02_imageView, R.id.img_03_imageView,
				R.id.img_04_imageView };

		ImageView[] imageView = new ImageView[image.length];

		for (int i = 0; i < imageView.length; i++) {
			imageView[i] = (ImageView) findViewById(image[i]);
		}
		return imageView;

	}

	public TextView[] findWeekDaysView() {
		int[] textView = { R.id.text_01_day_of_week, R.id.text_02_day_of_week,
				R.id.text_03_day_of_week, R.id.text_04_day_of_week };

		TextView[] textView_weekdays = new TextView[textView.length];

		for (int i = 0; i < textView.length; i++) {
			textView_weekdays[i] = (TextView) findViewById(textView[i]);
		}
		return textView_weekdays;

	}

	public TextView[] findTemps() {
		int[] textView = { R.id.text_01_temp_in, R.id.text_02_temp_in,
				R.id.text_03_temp_in, R.id.text_04_temp_in };

		TextView[] textView_weekdays = new TextView[textView.length];

		for (int i = 0; i < textView.length; i++) {
			textView_weekdays[i] = (TextView) findViewById(textView[i]);
		}
		return textView_weekdays;

	}

	public TextView[] findTweathers() {
		int[] textView = { R.id.text_currWeather, R.id.text_01_weather_in,
				R.id.text_02_weather_in, R.id.text_03_weather_in,
				R.id.text_04_weather_in };

		TextView[] textView_weathers = new TextView[textView.length];

		for (int i = 0; i < textView.length; i++) {
			textView_weathers[i] = (TextView) findViewById(textView[i]);
		}
		return textView_weathers;

	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// BACK键
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (refersh_time != null) {
				saveInfo();
			}
			WeartherActivity.this.finish();

			return false;
		}
		return false;
	}

	public String getDate() {
		SimpleDateFormat sDateFormat = new SimpleDateFormat("MM-dd hh:mm");
		String date = "更新时间:" + sDateFormat.format(new java.util.Date());
		System.out.println(date);
		return date;
	}

	public void changeUI(WeatherInfo infos) {
		ChangerUtils changerUtils = new ChangerUtils();
		city_Name_Main = infos.getCity();
		adivise_Main = infos.getAdivise();
		day_of_week_Main = infos.getDate();
		humidity_Main = infos.getHumidity();
		wind_direction_MAIN = infos.getWind_direction();
		temp_Main = infos.getTemp();
		String[] temps = { infos.getTemp_02(), infos.getTemp_03(),
				infos.getTemp_04(), infos.getTemp_05() };
		temps_Main = temps;
		weather_01_Main = infos.getWeather_01();
		weather_02_Main = infos.getWeather_02();
		weather_03_Main = infos.getWeather_03();
		weather_04_Main = infos.getWeather_04();
		weather_05_Main = infos.getWeather_05();
		weekdays_Main = changerUtils.weekDays(changerUtils.matchDayofWeek(infos
				.getDate()));
		String[] infos_weathers = { infos.getWeather_01(),
				infos.getWeather_02(), infos.getWeather_03(),
				infos.getWeather_04(), infos.getWeather_05() };
		img_res_ids = changerUtils.serchImage(infos_weathers);

		for (int i = 0; i < imgs.length; i++) {
			imgs[i].setImageResource(img_res_ids[i]);
			view_Weathers[i].setText(infos_weathers[i]);
		}
		for (int i = 0; i < weekdays_Main.length; i++) {
			view_Weekdays[i].setText(weekdays_Main[i]);
			view_Temps[i].setText(temps[i]);

		}
		textView_city.setText(city_Name_Main);
		text_updateTime.setText(refersh_time);
		text_currTemp_in.setText(temp_Main);
		text_currAdvice_in.setText(adivise_Main);
		text_currWindCondition_in.setText(wind_direction_MAIN);

	}

	/**
	 * 保存数据
	 */
	public void saveInfo() {
		SharedPreferences uiState = getPreferences(0);
		SharedPreferences.Editor editor = uiState.edit();
		editor.putBoolean("is_Saved", true);
		editor.putString("adivise_Main", adivise_Main);
		editor.putString("city_Name_Main", city_Name_Main);
		editor.putString("humidity_Main", humidity_Main);
		editor.putString("wind_direction_MAIN", wind_direction_MAIN);
		editor.putString("temp_Main", temp_Main);
		editor.putString("temps_Main_0", temps_Main[0]);
		editor.putString("temps_Main_1", temps_Main[1]);
		editor.putString("temps_Main_2", temps_Main[2]);
		editor.putString("temps_Main_3", temps_Main[3]);
		editor.putString("weather_01_Main", weather_01_Main);
		editor.putString("weather_02_Main", weather_02_Main);
		editor.putString("weather_03_Main", weather_03_Main);
		editor.putString("weather_04_Main", weather_04_Main);
		editor.putString("weather_05_Main", weather_05_Main);
		editor.putString("city_id", city_Id_Main);
		editor.putString("refersh_time", refersh_time);
		editor.putString("day_of_week_Main", day_of_week_Main);
		editor.commit();

	}

	/**
	 * 获得保存数据
	 * 
	 * @return info
	 */
	public WeatherInfo getInfo() {
		WeatherInfo weatherInfo = new WeatherInfo();
		SharedPreferences sharedPreference = getPreferences(MODE_PRIVATE);
		weatherInfo.setCity(sharedPreference.getString("city_Name_Main", ""));
		weatherInfo
				.setHumidity(sharedPreference.getString("humidity_Main", ""));
		weatherInfo.setWind_direction(sharedPreference.getString(
				"wind_direction_MAIN", ""));
		weatherInfo.setTemp(sharedPreference.getString("temp_Main", ""));
		weatherInfo.setTemp_02(sharedPreference.getString("temps_Main_0", ""));
		weatherInfo.setTemp_03(sharedPreference.getString("temps_Main_1", ""));
		weatherInfo.setTemp_04(sharedPreference.getString("temps_Main_2", ""));
		weatherInfo.setTemp_05(sharedPreference.getString("temps_Main_3", ""));
		weatherInfo.setWeather_01(sharedPreference.getString("weather_01_Main",
				""));
		weatherInfo.setWeather_02(sharedPreference.getString("weather_02_Main",
				""));
		weatherInfo.setWeather_03(sharedPreference.getString("weather_03_Main",
				""));
		weatherInfo.setWeather_04(sharedPreference.getString("weather_04_Main",
				""));
		weatherInfo.setWeather_05(sharedPreference.getString("weather_05_Main",
				""));
		weatherInfo.setAdivise(sharedPreference.getString("adivise_Main", ""));
		refersh_time = sharedPreference.getString("refersh_time", "");
		city_Id_Main = sharedPreference.getString("city_id", "");
		weatherInfo.setDate(sharedPreference.getString("day_of_week_Main", ""));
		return weatherInfo;
	}

	/**
	 * 更新天气
	 * 
	 * @param refresh
	 */
	private void updateWeather(final Button refresh) {

		if (appContext.city == Constant.HEAD_CITY_DEFAULT) {
			MyToast.customToast(WeartherActivity.this, Toast.LENGTH_SHORT,
					"错误", "无法获取地址，请检查网络连接状态！", Constant.TOAST_IMG_ERROR);
			return;
		}

			final RotateAnimation ra = new RotateAnimation(0, 720 * 20,
					Animation.RELATIVE_TO_SELF, 0.5f,
					Animation.RELATIVE_TO_SELF, 0.5f);
			ra.setDuration(20000);// 旋转时间
			refresh.startAnimation(ra);
			city_Id_Main = StringUtils.isEmpty(city_Id_Main) ? WeatherUtil.DEFAULT_CITYID
					: city_Id_Main;
			String strUrl = "http://m.weather.com.cn/data/" + city_Id_Main
					+ ".html";
			MyLog.v("url00", strUrl);
			FinalHttp finalHttp = new FinalHttp();
			finalHttp.get(strUrl, new AjaxCallBack<String>() {
				@Override
				public void onSuccess(String t) {
					// TODO Auto-generated method stub
					super.onSuccess(t);
		MyLog.v("天气信息获取成功", t);
					info = new WeatherInfo();
					info = new WeatherJsonUtils().parseJson(t, info);
					mHandler.obtainMessage(MSG_SUCCESS, info).sendToTarget();
//					Map<String, String> data = new HashMap<String, String>();
//					data.put(Constant.FILE_COMMON_WEATHERINFO, info.weather_01);
//					SharedPreferencesUtil.keep(Constant.FILE_COMMON,
//							getApplicationContext(), data);// 保存天气信息
					appContext.weatherInfo = info.weather_01;
					ra.setDuration(0);
				}

				@Override
				public void onFailure(Throwable t, String strMsg) {
					// TODO Auto-generated method stub
					super.onFailure(t, strMsg);
					mHandler.obtainMessage(MSG_FAILURE).sendToTarget();
					ra.setDuration(0);
				MyLog.v("天气信息获取失败", strMsg);
				}

			});
	}

}
