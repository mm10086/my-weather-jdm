package com.example.mm.service;

/**
 * Created by mm on 2017/12/20.
 */
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RemoteViews;

import com.example.mm.myweather.R;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import com.example.mm.myweather.MainActivity;
import com.example.mm.bean.TodayWeather;
import com.example.mm.util.SetWeatherImage;
import com.example.mm.util.WeatherUtil;

public class WidgetService extends Service {


    @Override
    public void onCreate() {
        super.onCreate();
        initWidget();
    }

    private void initWidget() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d("ssdfdf", "1");
                SharedPreferences preferences = getSharedPreferences("config", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("main_city_code", "101010100");
                editor.putString("main_city_name", "北京");
                editor.commit();
                String cityCode = preferences.getString("main_city_code", "101010100");
                WeatherUtil weatherUtil = new WeatherUtil(cityCode);
                TodayWeather weather = weatherUtil.getWeather();
                Log.d("ssdfdf", weather.toString());


                RemoteViews rv = new RemoteViews(getPackageName(), R.layout.mini_weather_widget);
                //rv.setImageViewIcon(R.id.widget_weather_img, SetWeatherImage.setImageByType(weather.getType()));
                rv.setTextViewText(R.id.widget_climate, weather.getType());
                rv.setTextViewText(R.id.widget_week_today, weather.getDate());
                rv.setTextViewText(R.id.widget_wind, weather.getFengli());
                rv.setTextViewText(R.id.widget_temperature, weather.getLow() + "~" + weather.getHigh());
            }
        }).start();
    }

    public WidgetService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startID) {
        return super.onStartCommand(intent, flags, startID);
    }

    public void onDestroy() {
        super.onDestroy();
    }
}