package wanghaisheng.com.yakerweather.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import wanghaisheng.com.yakerweather.db.YakerWeatherDB;
import wanghaisheng.com.yakerweather.model.City;
import wanghaisheng.com.yakerweather.model.County;
import wanghaisheng.com.yakerweather.model.Province;

/**
 * Created by sheng on 2015/12/5.
 */
public class Utility {

    /**
     * 解析和处理服务器返回的省级数据
     * @param yakerWeatherDB
     * @param response
     * @return
     */
    public synchronized static boolean handleProvincesResponse(YakerWeatherDB yakerWeatherDB, String response) {
        if(!TextUtils.isEmpty(response)){
            String[] allProvinces = response.split(",");
            if(null!=allProvinces && allProvinces.length>0) {
                for(String p:allProvinces) {
                    String[] strs = p.split("\\|");
                    Province province = new Province();
                    province.setProvinceCode(strs[0]);
                    province.setProvinceName(strs[1]);
                    // 将解析出来的数据存储到Province表
                    yakerWeatherDB.saveProvince(province);
                }

                return true;
            }
        }

        return false;
    }

    /**
     * 解析和处理服务器返回的市级数据
     * @param yakerWeatherDB
     * @param response
     * @param provinceId
     * @return
     */
    public synchronized static boolean handleCitiesResponse(YakerWeatherDB yakerWeatherDB, String response, int provinceId) {
        if(!TextUtils.isEmpty(response)) {
            String[] allCities = response.split(",");
            if(null!=allCities && allCities.length>0) {
                for(String p:allCities) {
                    String[] strs = p.split("\\|");
                    City city = new City();
                    city.setCityCode(strs[0]);
                    city.setCityName(strs[1]);
                    city.setProvinceId(provinceId);

                    yakerWeatherDB.saveCity(city);
                }

                return true;
            }
        }

        return false;
    }

    /**
     * 解析和处理服务器返回的县级数据
     * @param yakerWeatherDB
     * @param response
     * @param cityId
     * @return
     */
    public synchronized static boolean handleCountiesResponse(YakerWeatherDB yakerWeatherDB, String response, int cityId) {
        if(!TextUtils.isEmpty(response)) {
            String[] allCounties = response.split(",");
            if(null!=allCounties && allCounties.length>0) {
                for(String p:allCounties) {
                    String[] strs = p.split("\\|");
                    County county = new County();
                    county.setCountyCode(strs[0]);
                    county.setCountyName(strs[1]);
                    county.setCityId(cityId);

                    yakerWeatherDB.saveCounty(county);
                }

                return true;
            }
        }

        return false;
    }

    /**
     * 解析服务器返回的JSON数据，并将解析出的数据存储到本地
     * @param context
     * @param response
     */
    public static void handleWeatherResponse(Context context, String response) {

        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONObject weatherInfo = jsonObject.getJSONObject("weatherinfo");
            String cityName = weatherInfo.getString("city");
            String weatherCode = weatherInfo.getString("cityId");
            String temp1 = weatherInfo.getString("temp1");
            String temp2 = weatherInfo.getString("temp2");
            String weatherDesp = weatherInfo.getString("weather");
            String publishTime = weatherInfo.getString("ptime");

            saveWeatherInfo(context,cityName,weatherCode,temp1,temp2,weatherDesp,publishTime);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    /**
     * 将服务器返回的所有天气信息存储到SharedPreferences文件中。
     * @param context
     * @param cityName
     * @param weatherCode
     * @param temp1
     * @param temp2
     * @param weatherDesp
     * @param publishTime
     */
    public static void saveWeatherInfo(Context context,String cityName,String weatherCode,String temp1,String temp2,String weatherDesp,String publishTime) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年m月d日", Locale.CHINA);

        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putBoolean("city_selected",true);
        editor.putString("city_name", cityName);
        editor.putString("weather_code", weatherCode);
        editor.putString("temp1", temp1);
        editor.putString("temp2", temp2);
        editor.putString("weather_desp", weatherDesp);
        editor.putString("publish_time", publishTime);
        editor.putString("current_date",sdf.format(new Date()));

        editor.commit();

    }
}
