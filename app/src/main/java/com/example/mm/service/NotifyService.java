package com.example.mm.service;

/**
 * Created by mm on 2017/12/13.
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

import com.example.mm.myweather.R;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import com.example.mm.myweather.MainActivity;
//import pku.ss.liudong.model.Weather;
//import pku.ss.liudong.util.PinYin;
//import pku.ss.liudong.util.WeatherUtil;

public class NotifyService extends Service {
    private Timer[] timer = new Timer[3];
    final private int[] times = {6, 12, 18};

    @Override
    public void onCreate() {
        super.onCreate();
        //获取当前时间
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);


        Calendar c1 = Calendar.getInstance();
        long[] trigMillis = new long[3];
        for (int i = 0; i < trigMillis.length; ++i) {
            //保证定时时间在当前时间之后
            c1.set(year, month, day, times[i], 0);
            if (c.getTimeInMillis() > c1.getTimeInMillis())
                c1.add(Calendar.DAY_OF_MONTH, 1);

            //计算闹钟触发延迟
            trigMillis[i] = c1.getTimeInMillis() - c.getTimeInMillis();

            //针对每个时间点设置定时器
            timer[i] = new Timer();
            timer[i].schedule(new TimerTask() {
                public void run() {
                    //拿到citycode
                    SharedPreferences sp = getSharedPreferences(MainActivity.SETTING_CONFIG, MODE_PRIVATE);
                    String cityCode = sp.getString(WeatherActivity.MAIN_CITY_CODE, "101010100");
                    WeatherUtil weatherUtil = new WeatherUtil(cityCode);
                    Weather weather = weatherUtil.getWeather();
                    String weatherInfo = weather.getWeatherTypeString() + " " + weather.getWendu() + "℃  空气质量:" + weather.getAqi();

                    NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(NotifyService.this);
                    mBuilder.setLights(Color.BLUE, 500, 500);
                    mBuilder.setContentTitle(weather.getCity() + "天气");
                    mBuilder.setContentText(weatherInfo);
                    //mBuilder.setSmallIcon(PinYin.getWeatherImageResourceId(weather.getTypeDay()));    //应该根据白天夜晚分别判断
                    mBuilder.setSmallIcon(R.drawable.weather_icon);
                    nm.notify(0, mBuilder.build());
                }
                //}, trigMillis[i], 24 * 60 * 60 * 1000);
            }, 0, 10000);//测试0, 1000);
        }
    }

    public NotifyService() {
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