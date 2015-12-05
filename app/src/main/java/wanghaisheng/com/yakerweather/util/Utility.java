package wanghaisheng.com.yakerweather.util;

import android.text.TextUtils;

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
}
