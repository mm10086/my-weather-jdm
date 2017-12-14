package com.example.mm.util;


import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

import java.util.Iterator;
import java.util.List;

import com.example.mm.app.MyApplication;
import com.example.mm.myweather.MainActivity;
import com.example.mm.bean.City;

/**
 * Created by mm on 2017/12/13.
 */
public class LocationUtil {
    public static final int MESSAGE_GET_LOCATION_CODE = 11;
    public LocationClient mLocationClient = null;
    public BDLocationListener myListener;
    private String GPScity=null;

    private Activity activity;

    public LocationUtil (Activity activity) {
        this.activity = activity;
    }

    class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            // Receive Location
            StringBuffer sb = new StringBuffer(256);
            sb.append(location.getCity());
            sb.append(location.getDistrict());
            GPScity = location.getDistrict();
            GPScity = GPScity.substring(0, GPScity.length()-1);

            Log.v("Location Thread>>>", sb.toString());
            Log.v("Location Thread>>>", GPScity.toString());

            if(GPScity!=null){
                mLocationClient.stop();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        MyApplication app = MyApplication.getInstance();
                        Handler mHandler = MainActivity.getHandler();
                        List<City> list = app.getCityList();
                        Iterator<City> i = list.iterator();
                        City city = null;
                        while(i.hasNext()){
                            city = i.next();
                            if(city.getCity().equals(GPScity)){
                                break;
                            }
                        }
                        Message msg = new Message();
                        msg.obj = city;
                        msg.what = MESSAGE_GET_LOCATION_CODE;
                        mHandler.sendMessage(msg);
                    }
                }).start();
                //do something with city...
            }
        }
    }

    public void getLocation(){
        //context = this;
        mLocationClient = new LocationClient(activity.getApplicationContext()); // 声明LocationClient类
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);// 设置定位模式
        option.setCoorType("bd09ll");// 返回的定位结果是百度经纬度,默认值gcj02
        option.setScanSpan(1000);// 设置发起定位请求的间隔时间为5000ms
        option.setIsNeedAddress(true);// 返回的定位结果包含地址信息
        mLocationClient.setLocOption(option);
        myListener = new MyLocationListener();
        mLocationClient.registerLocationListener(myListener); // 注册监听函数
        mLocationClient.start();
        mLocationClient.requestLocation();
    }
}