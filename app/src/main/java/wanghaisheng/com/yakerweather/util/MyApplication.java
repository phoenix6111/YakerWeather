package wanghaisheng.com.yakerweather.util;

import android.app.Application;
import android.content.Context;

/**
 * Created by sheng on 2015/12/6.
 */
public class MyApplication extends Application {
    private static Context context;

    @Override
    public void onCreate() {
        context = getApplicationContext();
    }

    public static Context getContext() {
        return context;
    }
}
