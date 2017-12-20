package com.example.mm.myweather;

import android.app.Activity;
import android.app.FragmentManager;

import android.content.SharedPreferences;
import android.icu.text.LocaleDisplayNames;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.content.SharedPreferences.*;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.xmlpull.v1.XmlPullParserFactory;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.example.mm.bean.City;
import com.example.mm.myweather.ForecastFragment;
import com.example.mm.service.NotifyService;
import com.example.mm.util.LocationUtil;
import com.example.mm.bean.TodayWeather;
import com.example.mm.bean.Forcast;
import com.example.mm.util.NetUtil;
import com.example.mm.util.SetWeatherImage;
import com.example.mm.util.WeatherUtil;
/**
 * Created by mm on 2017/9/26.
 */
//主界面的Activity类
public class MainActivity extends Activity implements View.OnClickListener {
    private static final int UPDATE_TODAY_WEATHER = 1;
    private ImageView mUpdateBtn;
    private ImageView mLocationBtn;
    private ImageView mCitySelect;
    private TextView cityTv, timeTv, temperatureNowTv, humidityTv, weekTv, pmDataTv,
            pmQualityTv, temperatureTv, climateTv, windTv, city_name_Tv;
    private ImageView weatherImg, pmImg;

    private ForecastFragment forcastFragment;
    private static Handler mHandler = null;
    private LocationUtil locationUtil;
    //建立一个Handler，如果收到message，则调用函数天气更新信息
    private void initHandler() {
        mHandler = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case UPDATE_TODAY_WEATHER:
                        updateTodayWeather((TodayWeather) msg.obj);
                        break;
                    case LocationUtil.MESSAGE_GET_LOCATION_CODE:
                        City city = (City)msg.obj;
                        if(city != null){
                            //城市已定位
                            SharedPreferences preferences = getSharedPreferences("config", MODE_PRIVATE);
                            SharedPreferences.Editor editor=preferences.edit();
                            editor.putString("main_city_code", city.getNumber());
                            editor.putString("main_city_name", city.getCity());
                            editor.commit();
                            queryWeatherCode(city.getNumber());
                        }
                        break;
                    default:
                        break;
                }
            }
        };
    }
    public static Handler getHandler(){
        return mHandler;
    }

    @Override
    //重写MainActivity的onCreate函数
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_info);
        //为主界面更新按钮设置点击事件
        mUpdateBtn = (ImageView) findViewById(R.id.title_update_btn);
        mUpdateBtn.setOnClickListener(this);
        //为定位按钮设置点击事件
        mLocationBtn = (ImageView) findViewById(R.id.title_location);
        mLocationBtn.setOnClickListener(this);
        //检查网络状况
        if (NetUtil.getNetworkState(this) != NetUtil.NETWORN_NONE) {
            Log.d("myWeather", "网络OK");
            Toast.makeText(MainActivity.this, "网络OK！", Toast.LENGTH_LONG).show();
        } else {
            Log.d("myWeather", "网络挂了");
            Toast.makeText(MainActivity.this, "网络挂了！", Toast.LENGTH_LONG).show();
        }
        //为城市选择按钮设置点击事件
        mCitySelect = (ImageView) findViewById(R.id.title_city_manager);
        mCitySelect.setOnClickListener(this);
        //为当前城市建立一个SharedPreferences用于保存当前城市的代码和名称
        SharedPreferences preferences = getSharedPreferences("config", MODE_PRIVATE);
        Editor editor=preferences.edit();
        editor.putString("main_city_code", "101010100");
        editor.putString("main_city_name", "北京");
        editor.commit();
        //初始化主界面视图
        initView();
        initForcast();
        initHandler();
        this.locationUtil = new LocationUtil(this);
        locationUtil.getLocation();
        startService(new Intent(getBaseContext(), NotifyService.class));
        Log.d("服务","开启");
    }
    //初始化主界面视图函数
    void initView() {
        city_name_Tv = (TextView) findViewById(R.id.title_city_name);
        cityTv = (TextView) findViewById(R.id.city);
        timeTv = (TextView) findViewById(R.id.time);
        temperatureNowTv = (TextView) findViewById(R.id.temperature_now);
        humidityTv = (TextView) findViewById(R.id.humidity);
        weekTv = (TextView) findViewById(R.id.week_today);
        pmDataTv = (TextView) findViewById(R.id.pm_data);
        pmQualityTv = (TextView) findViewById(R.id.pm2_5_quality);
        pmImg = (ImageView) findViewById(R.id.pm2_5_img);
        temperatureTv = (TextView) findViewById(R.id.temperature);
        climateTv = (TextView) findViewById(R.id.climate);
        windTv = (TextView) findViewById(R.id.wind);
        weatherImg = (ImageView) findViewById(R.id.weather_img);
        city_name_Tv.setText("N/A");
        cityTv.setText("N/A");
        timeTv.setText("N/A");
        temperatureNowTv.setText("N/A");
        humidityTv.setText("N/A");
        pmDataTv.setText("N/A");
        pmQualityTv.setText("N/A");
        weekTv.setText("N/A");
        temperatureTv.setText("N/A");
        climateTv.setText("N/A");
        windTv.setText("N/A");
    }

    private void initForcast(){
        forcastFragment = new ForecastFragment();
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.forcast_container, forcastFragment).commit();
    }
    @Override
    //单击事件响应函数
    public void onClick(View view) {
        //如果单击事件来自于选择城市按钮，则向城市选择界面发出一个intent
        if(view.getId() == R.id.title_city_manager) {
            Intent i = new Intent(MainActivity.this, SelectCity.class);
            startActivityForResult(i, 1);
        }

        if(view.getId() == R.id.title_location) {
            locationUtil.getLocation();
        }
        //如果单击事件来自于城市更新按钮，则从SharedPreference中读取当前城市编码，并进行数据查询
        if (view.getId() == R.id.title_update_btn) {
            SharedPreferences sharedPreferences = getSharedPreferences("config", MODE_PRIVATE);
            String cityCode = sharedPreferences.getString("main_city_code", "101010100");
            Log.d("myWeather", cityCode);
            if (NetUtil.getNetworkState(this) != NetUtil.NETWORN_NONE) {
                Log.d("myWeather", "网络OK");
                //如果网络没问题，则调用函数从网络上获取当前编码城市的天气信息
                queryWeatherCode(cityCode);
            } else {
                Log.d("myWeather", "网络挂了");
                Toast.makeText(MainActivity.this, "网络挂了！", Toast.LENGTH_LONG).show();
            }
        }
    }
    //从城市选择界面返回的intent中取出城市编码信息
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK) {
            String newCityCode= data.getStringExtra("cityCode");
            Log.d("myWeather", "选择的城市代码为"+newCityCode);
            if (NetUtil.getNetworkState(this) != NetUtil.NETWORN_NONE) {
                Log.d("myWeather", "网络OK");
                //如果网络没问题，则调用函数从网络上获取当前编码城市的天气信息
                queryWeatherCode(newCityCode);
            } else {
                Log.d("myWeather", "网络挂了");
                Toast.makeText(MainActivity.this, "网络挂了！", Toast.LENGTH_LONG).show();
            }
        }
    }
    //天气信息请求函数，根据传入的城市编码，获取天气预报网络上的天气信息
    private void queryWeatherCode(String cityCode) {
        final String address = "http://wthrcdn.etouch.cn/WeatherApi?citykey=" + cityCode;
        Log.d("myWeather", address);
        //新建一个线程，用于查询和解析天气数据
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection con = null;
                TodayWeather todayWeather = null;
                try {
                    //设置网络访问的参数
                    URL url = new URL(address);
                    con = (HttpURLConnection) url.openConnection();
                    con.setRequestMethod("GET");
                    con.setConnectTimeout(8000);
                    con.setReadTimeout(8000);
                    //从网络访问返回的结果读取字符串信息
                    InputStream in = con.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder response = new StringBuilder();
                    String str;
                    while ((str = reader.readLine()) != null) {
                        response.append(str);
                        Log.d("myWeather", str);
                    }
                    String responseStr = response.toString();
                    Log.d("myWeather", responseStr);
                    //调用parseXML函数解析读取的XML源码，获取关于天气的数据类
                    todayWeather = WeatherUtil.parseXML(responseStr);
                    //如果读取并解析了天气信息，则向主线程发出一个更新天气的Message
                    if (todayWeather != null) {
                        Log.d("myWeather", todayWeather.toString());
                        Message msg =new Message();
                        msg.what = UPDATE_TODAY_WEATHER;
                        msg.obj=todayWeather;
                        mHandler.sendMessage(msg);
                        Log.d("myWeather", "sendMsg");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (con != null) {
                        con.disconnect();
                    }
                }
            }
        }).start();
    }

    //更新天气函数，主要用于更新主界面上的天气数据和图标
    void updateTodayWeather(TodayWeather todayWeather) {
        int pm25Num;
        String weatherType;
        Log.d("myWeather", "update00");
        //更新天气数据
        city_name_Tv.setText(todayWeather.getCity() + "天气");
        cityTv.setText(todayWeather.getCity());
        timeTv.setText(todayWeather.getUpdatetime() + "发布");
        temperatureNowTv.setText("温度：" + todayWeather.getWendu() + "℃");
        humidityTv.setText("湿度：" + todayWeather.getShidu());
        if(todayWeather.getPm25().equals("")) {
            pmDataTv.setText("无");
        } else { pmDataTv.setText(todayWeather.getPm25()); }
        if (todayWeather.getQuality().equals("")) {
            pmQualityTv.setText("无");
        } else { pmQualityTv.setText(todayWeather.getQuality()); }
        weekTv.setText(todayWeather.getDate());
        temperatureTv.setText(todayWeather.getLow() + "~" + todayWeather.getHigh());
        climateTv.setText(todayWeather.getType());
        windTv.setText("风力:" + todayWeather.getFengli());
        if (todayWeather.getPm25().equals("")) {
            pm25Num = 15;
        } else { pm25Num = Integer.parseInt(todayWeather.getPm25()); }
        pmImg.setImageResource(SetWeatherImage.setImageByPm25(pm25Num));

        weatherType = todayWeather.getType();
        weatherImg.setImageResource(SetWeatherImage.setImageByType(weatherType));
        forcastFragment.setWeather(todayWeather);
        forcastFragment.setForcastInfo();
        Toast.makeText(MainActivity.this, "更新成功！", Toast.LENGTH_SHORT).show();
    }
}
