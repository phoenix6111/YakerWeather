package wanghaisheng.com.yakerweather.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import wanghaisheng.com.yakerweather.R;
import wanghaisheng.com.yakerweather.util.HttpCallbackListener;
import wanghaisheng.com.yakerweather.util.HttpUtil;
import wanghaisheng.com.yakerweather.util.MyApplication;
import wanghaisheng.com.yakerweather.util.Utility;

/**
 * Created by sheng on 2015/12/6.
 */
public class WeatherActivity extends Activity implements View.OnClickListener{
    private LinearLayout weatherInfoLayout;

    /**
     * 显示城市名
     */
    private TextView cityNameText;
    /**
     * 发布时间
     */
    private TextView publishTimeText;
    /**
     * 用于显示天气描述信息
     */
    private TextView weatherDespText;
    /**
     * 显示最低温度
     */
    private TextView temp1Text;
    /**
     * 显示最高温度
     */
    private TextView temp2Text;
    /**
     * 显示当前日期
     */
    private TextView currentDateText;
    /**
     * 切换城市按钮
     */
    private Button switchCityBtn;
    /**
     * 刷新天气按钮
     */
    private Button refreshWeatherBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.weather_layout);

        weatherInfoLayout = (LinearLayout) findViewById(R.id.weather_info_layout);
        cityNameText = (TextView) findViewById(R.id.city_name);
        publishTimeText = (TextView) findViewById(R.id.publish_text);
        weatherDespText = (TextView) findViewById(R.id.weather_desp);
        temp1Text = (TextView) findViewById(R.id.temp1);
        temp2Text = (TextView) findViewById(R.id.temp2);
        currentDateText = (TextView) findViewById(R.id.current_date);

        String countyCode = getIntent().getStringExtra("county_code");

        if(!TextUtils.isEmpty(countyCode)) {
            //有County Code时就查询天气
            publishTimeText.setText("同步中。。。");
            weatherInfoLayout.setVisibility(View.INVISIBLE);
            cityNameText.setVisibility(View.INVISIBLE);
            queryWeatherCode(countyCode);
        } else {
            //没有county code时就显示本地天气
            showWeather();
        }
    }

    @Override
    public void onClick(View v) {

    }

    /**
     *查询县级代号所对应的天气代号。
     * @param countyCode
     */
    private void queryWeatherCode(String countyCode) {
        String address = "http://www.weather.com.cn/data/list3/city" +
                countyCode + ".xml";

        queryFromServer(address,"countyCode");
    }

    /**
     * 查询天气代号所对应的天气
     * @param weatherCode
     */
    private void queryWeatherInfo(String weatherCode) {
        String address = "http://www.weather.com.cn/data/cityinfo/" +
                weatherCode + ".html";
        queryFromServer(address,"weatherCode");
    }

    /**
     * 根据传入的地址和类型去向服务器查询天气代号或者天气信息。
     * @param address
     * @param type
     */
    private void queryFromServer(final String address,final String type) {
        HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                if ("countyCode".equals(type)) {
                    String[] arrs = response.split("\\|");
                    if (null != arrs && arrs.length == 2) {
                        String weatherCode = arrs[1];
                        //获取到weatherCode 然后查询WeatherInfo
                        queryWeatherInfo(weatherCode);
                    }
                } else if ("weatherCode".equals(type)) {
                    Utility.handleWeatherResponse(MyApplication.getContext(), response);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showWeather();
                        }
                    });
                }
            }

            @Override
            public void onError(Exception e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        publishTimeText.setText("同步失败！");
                    }
                });
            }
        });
    }

    /**
     * 从SharedPreferences文件中读取存储的天气信息，并显示到界面上。
     */
    private void showWeather() {
        SharedPreferences refs = PreferenceManager.getDefaultSharedPreferences(MyApplication.getContext());
        cityNameText.setText(refs.getString("city_name",""));
        temp1Text.setText(refs.getString("temp1",""));
        temp2Text.setText(refs.getString("temp2",""));
        weatherDespText.setText(refs.getString("weather_desp",""));
        publishTimeText.setText("今天 "+refs.getString("publish_time","")+" 发布");
        currentDateText.setText(refs.getString("current_date",""));
        weatherInfoLayout.setVisibility(View.VISIBLE);
        cityNameText.setVisibility(View.VISIBLE);
    }
}
