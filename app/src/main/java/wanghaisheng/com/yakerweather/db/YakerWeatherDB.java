package wanghaisheng.com.yakerweather.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import wanghaisheng.com.yakerweather.model.City;
import wanghaisheng.com.yakerweather.model.County;
import wanghaisheng.com.yakerweather.model.Province;

/**
 * Created by sheng on 2015/12/5.
 */
public class YakerWeatherDB {

    /**
     * 数据库名
     */
    public static final String DB_NAME = "yaker_weather";
    /**
     * 数据库版本
     */
    public static final int VERSION = 1;

    private static YakerWeatherDB yakerWeatherDB;

    private SQLiteDatabase db;

    /**
     * 构造函数私有化，单例
     * @param context
     */
    private YakerWeatherDB(Context context) {
        YakerWeatherOpenHelper helper = new YakerWeatherOpenHelper(context,DB_NAME,null,VERSION);
        this.db = helper.getWritableDatabase();
    }

    /**
     * 获取 YakerWeatherDB实例
     * @param context
     * @return
     */
    public synchronized static YakerWeatherDB getInstance(Context context) {
        if(yakerWeatherDB == null) {
            yakerWeatherDB = new YakerWeatherDB(context);
        }

        return yakerWeatherDB;
    }

    /**
     * 保存Province到数据库
     * @param province
     */
    public void saveProvince(Province province) {
        if(null != province) {
            ContentValues contentValues = new ContentValues();
            contentValues.put("province_name",province.getProvinceName());
            contentValues.put("province_code",province.getProvinceCode());

            db.insert("Province",null,contentValues);
        }
    }

    /**
     * 从数据库读取全国所有的省份信息。
     * @return
     */
    public List<Province> loadProvinces(){
        List<Province> provinceList = new ArrayList<Province>();
        Cursor cursor = db.query("Province",null,null,null,null,null,null);
        if(cursor.moveToFirst()) {
            do{
                Province province = new Province();
                province.setId(cursor.getInt(cursor.getColumnIndex("id")));
                province.setProvinceName(cursor.getString(cursor.getColumnIndex("province_name")));
                province.setProvinceCode(cursor.getString(cursor.getColumnIndex("province_code")));
                provinceList.add(province);
            }while(cursor.moveToNext());
        }

        return provinceList;
    }

    /**
     * 保存City数据
     * @param city
     */
    public void saveCity(City city){
        if(null != city){
            ContentValues values = new ContentValues();
            values.put("city_name",city.getCityName());
            values.put("city_code",city.getCityCode());
            values.put("province_id",city.getProvinceId());

            db.insert("City",null,values);
        }
    }

    /**
     *根据provinceId 加载City数据
     * @return
     */
    public List<City> loadCities(int provinceId) {
        List<City> cityList = new ArrayList<City>();
        Cursor cursor = db.query("City",null,"province_id=?",new String[]{String.valueOf(provinceId)},null,null,null);
        if(cursor.moveToFirst()){
            do {
                City city = new City();
                city.setId(cursor.getInt(cursor.getColumnIndex("id")));
                city.setCityName(cursor.getString(cursor.getColumnIndex("city_name")));
                city.setCityCode(cursor.getString(cursor.getColumnIndex("city_code")));
                city.setProvinceId(cursor.getInt(cursor.getColumnIndex("province_id")));

                cityList.add(city);
            }while(cursor.moveToNext());
        }

        return cityList;
    }

    /**
     * 保存County数据
     * @param county
     */
    public void saveCounty(County county) {
        if(null!=county){
            ContentValues values = new ContentValues();
            values.put("county_name",county.getCountyName());
            values.put("county_code",county.getCountyCode());
            values.put("city_id",county.getCityId());

            db.insert("County",null,values);
        }
    }

    /**
     * 加载county数据
     * @param cityId
     * @return
     */
    public List<County> loadCounties(int cityId){
        List<County> countyList = new ArrayList<County>();
        Cursor cursor = db.query("County",null,"city_id=?",new String[]{String.valueOf(cityId)},null,null,null);

        if(cursor.moveToFirst()){
            do{
                County county = new County();
                county.setId(cursor.getInt(cursor.getColumnIndex("id")));
                county.setCountyName(cursor.getString(cursor.getColumnIndex("county_name")));
                county.setCountyCode(cursor.getString(cursor.getColumnIndex("county_code")));
                county.setCityId(cursor.getInt(cursor.getColumnIndex("city_id")));

                countyList.add(county);
            }while(cursor.moveToNext());
        }

        return countyList;
    }


}
