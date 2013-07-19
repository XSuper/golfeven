package com.golfeven.weather;


import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;


public class WeatherJsonUtils {


	

	public WeatherInfo parseJson(String strResult, WeatherInfo info) {
		Log.v("ssss", strResult);
		try {
			JSONObject jsonObj = new JSONObject(strResult)
					.getJSONObject("weatherinfo");
			String date = jsonObj.getString("week");
			String weather_01 = jsonObj.getString("weather1");
			String weather_02 = jsonObj.getString("weather2");
			String weather_03 = jsonObj.getString("weather3");
			String weather_04 = jsonObj.getString("weather4");
			String weather_05 = jsonObj.getString("weather5");
			String temp_02 = jsonObj.getString("temp2");
			String temp_03 = jsonObj.getString("temp3");
			String temp_04 = jsonObj.getString("temp4");
			String temp_05 = jsonObj.getString("temp5");
			String adivise = jsonObj.getString("index_d");
			String city = jsonObj.getString("city");
			String temp = jsonObj.getString("temp1") ;
			String wind_direction = jsonObj.getString("wind1");
			info.setCity(city);
			info.setTemp(temp);
			info.setWind_direction(wind_direction);
			info.setWeather_01(weather_01);
			info.setWeather_02(weather_02);
			info.setWeather_03(weather_03);
			info.setWeather_04(weather_04);
			info.setWeather_05(weather_05);
			info.setTemp_02(temp_02);
			info.setTemp_03(temp_03);
			info.setTemp_04(temp_04);
			info.setTemp_05(temp_05);
			info.setAdivise(adivise);
			info.setDate(date);
		} catch (JSONException e) {
			System.out.println("Json parse error");
			e.printStackTrace();
		}
		return info;
	}
}
