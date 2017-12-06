package com.example.mm.util;
import com.example.mm.myweather.R;

/**
 * Created by mm on 2017/11/30.
 */

public class SetWeatherImage {
    public static int setImageByPm25(int pm25Num) {
        //更新天气图标
        int imageId = R.drawable.biz_plugin_weather_0_50;
        if (pm25Num >= 0 && pm25Num <= 50) {
            imageId = R.drawable.biz_plugin_weather_0_50;
        }
        else if (pm25Num > 50 && pm25Num <= 100) {
            imageId = R.drawable.biz_plugin_weather_51_100;
        }
        else if (pm25Num > 100 && pm25Num <= 150) {
            imageId = R.drawable.biz_plugin_weather_101_150;
        }
        else if (pm25Num > 150 && pm25Num <= 200) {
            imageId = R.drawable.biz_plugin_weather_151_200;
        }
        else if (pm25Num > 200 && pm25Num <= 300) {
            imageId = R.drawable.biz_plugin_weather_201_300;
        }
        else {imageId = R.drawable.biz_plugin_weather_greater_300;}
        return imageId;
    }

    public static int setImageByType(String weatherType) {
        int imageId = R.drawable.biz_plugin_weather_qing;
        switch (weatherType) {
            case "暴雪":
                imageId = R.drawable.biz_plugin_weather_baoxue;
            break;
            case "大雪":
                imageId = R.drawable.biz_plugin_weather_daxue;
            break;
            case "中雪":
                imageId = R.drawable.biz_plugin_weather_zhongxue;
            break;
            case "小雪":
                imageId = R.drawable.biz_plugin_weather_xiaoxue;
            break;
            case "阵雪":
                imageId = R.drawable.biz_plugin_weather_zhenxue;
            break;
            case "雨夹雪":
                imageId = R.drawable.biz_plugin_weather_yujiaxue;
            break;
            case "特大暴雨":
                imageId = R.drawable.biz_plugin_weather_tedabaoyu;
            break;
            case "大暴雨":
                imageId = R.drawable.biz_plugin_weather_dabaoyu;
            break;
            case "暴雨":
                imageId = R.drawable.biz_plugin_weather_baoyu;
            break;
            case "大雨":
                imageId = R.drawable.biz_plugin_weather_dayu;
            break;
            case "中雨":
                imageId = R.drawable.biz_plugin_weather_zhongyu;
            break;
            case "小雨":
                imageId = R.drawable.biz_plugin_weather_xiaoyu;
            break;
            case "阵雨":
                imageId = R.drawable.biz_plugin_weather_zhenyu;
            break;
            case "雷阵雨":
                imageId = R.drawable.biz_plugin_weather_leizhenyu;
            break;
            case "雷阵雨冰雹":
                imageId = R.drawable.biz_plugin_weather_leizhenyubingbao;
            break;
            case "晴":
                imageId = R.drawable.biz_plugin_weather_qing;
            break;
            case "多云":
                imageId = R.drawable.biz_plugin_weather_duoyun;
            break;
            case "阴":
                imageId = R.drawable.biz_plugin_weather_yin;
            break;
            case "沙尘暴":
                imageId = R.drawable.biz_plugin_weather_shachenbao;
            break;
            case "雾":
                imageId = R.drawable.biz_plugin_weather_wu;
            break;
        }
        return imageId;
    }
}
