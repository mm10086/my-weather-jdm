package com.example.mm.util;

import android.os.Message;
import android.util.Log;

import com.example.mm.bean.Forcast;
import com.example.mm.bean.TodayWeather;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by mm on 2017/12/14.
 */

public class WeatherUtil {
    private String cityCode;
    public WeatherUtil(String code){
        this.cityCode = code;
    }
    public TodayWeather getWeather(){
        final String address = "http://wthrcdn.etouch.cn/WeatherApi?citykey=" + cityCode;
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
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (con != null) {
                con.disconnect();
            }
        }
        return todayWeather;
    }

    //XML解析函数，用于获取天气数据
    public static TodayWeather parseXML(String xmldata) {
        TodayWeather todayWeather = new TodayWeather();
        Log.d("myWeather", "parseXML00");
        Forcast[] forcastArr = todayWeather.getForcastArr();
        Log.d("myWeather", "parseXML01");
        int fengxiangCount = -1;
        int fengliCount = -1;
        int dateCount = -1;
        int highCount = -1;
        int lowCount = -1;
        int typeCount = 0;
        try {
            XmlPullParserFactory fac = XmlPullParserFactory.newInstance();
            XmlPullParser xmlPullParser = fac.newPullParser();
            xmlPullParser.setInput(new StringReader(xmldata));
            int eventType = xmlPullParser.getEventType();
            Log.d("myWeather", "parseXML");
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                        if (xmlPullParser.getName().equals("resp")) {
                            todayWeather = new TodayWeather();
                            Log.d("MyWeather", todayWeather.toString());
                        }
                        if (todayWeather != null) {
                            if (xmlPullParser.getName().equals("city")) {
                                eventType = xmlPullParser.next();
                                todayWeather.setCity(xmlPullParser.getText());
                            } else if (xmlPullParser.getName().equals("updatetime")) {
                                eventType = xmlPullParser.next();
                                todayWeather.setUpdatetime(xmlPullParser.getText());
                            } else if (xmlPullParser.getName().equals("shidu")) {
                                eventType = xmlPullParser.next();
                                todayWeather.setShidu(xmlPullParser.getText());
                            } else if (xmlPullParser.getName().equals("wendu")) {
                                eventType = xmlPullParser.next();
                                todayWeather.setWendu(xmlPullParser.getText());
                            } else if (xmlPullParser.getName().equals("pm25")) {
                                eventType = xmlPullParser.next();
                                todayWeather.setPm25(xmlPullParser.getText());
                            } else if (xmlPullParser.getName().equals("quality")) {
                                eventType = xmlPullParser.next();
                                todayWeather.setQuality(xmlPullParser.getText());
                            } else if(xmlPullParser.getName().equals("fengxiang")){
                                fengxiangCount++;
                                if(fengxiangCount == 0){
                                    eventType = xmlPullParser.next();
                                    todayWeather.setFengxiang(xmlPullParser.getText());
                                }else{
                                    if(fengxiangCount > 1 && fengxiangCount % 2 == 1){
                                        eventType = xmlPullParser.next();
                                        forcastArr[(fengxiangCount-1)/2 - 1].setFengxiang(xmlPullParser.getText());
                                    }
                                }
                            } else if (xmlPullParser.getName().equals("fengli")) {
                                fengliCount++;
                                if (fengliCount == 0) {
                                    eventType = xmlPullParser.next();
                                    todayWeather.setFengli(xmlPullParser.getText());
                                } else {
                                    if (fengliCount > 1 && fengliCount % 2 == 1) {
                                        eventType = xmlPullParser.next();
                                        forcastArr[(fengliCount - 1) / 2 - 1].setFengli(xmlPullParser.getText());
                                    }
                                }
                            } else if(xmlPullParser.getName().equals("date")){
                                dateCount++;
                                if(dateCount == 0){
                                    eventType = xmlPullParser.next();
                                    String dateStr = xmlPullParser.getText();
                                    todayWeather.setDate(dateStr);
                                }else{
                                    eventType = xmlPullParser.next();
                                    String dateStr2 = xmlPullParser.getText();
                                    forcastArr[dateCount-1].setDate(dateStr2.substring(dateStr2.length() - 3,dateStr2.length()));
                                }
                            } else if(xmlPullParser.getName().equals("high")){
                                highCount++;
                                if(highCount == 0){
                                    eventType = xmlPullParser.next();
                                    String highStr = xmlPullParser.getText();
                                    todayWeather.setHigh(highStr.split(" ")[1]);
                                }else{
                                    eventType = xmlPullParser.next();
                                    String highStr2 = xmlPullParser.getText();
                                    forcastArr[highCount-1].setHigh(highStr2.split(" ")[1]);
                                }
                            } else if(xmlPullParser.getName().equals("low")){
                                lowCount++;
                                if(lowCount == 0){
                                    eventType = xmlPullParser.next();
                                    String lowStr = xmlPullParser.getText();
                                    todayWeather.setLow(lowStr.split(" ")[1]);
                                }else{
                                    eventType = xmlPullParser.next();
                                    String lowStr2 = xmlPullParser.getText();
                                    forcastArr[lowCount-1].setLow(lowStr2.split(" ")[1]);
                                }
                            } else if(xmlPullParser.getName().equals("type")) {
                                typeCount++;
                                if (typeCount == 1) {
                                    eventType = xmlPullParser.next();
                                    todayWeather.setType(xmlPullParser.getText());
                                } else {
                                    if (typeCount % 2 == 1) {
                                        eventType = xmlPullParser.next();
                                        forcastArr[(typeCount - 1) / 2 - 1].setType(xmlPullParser.getText());
                                    }
                                }
                            }
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        break;
                }
                eventType = xmlPullParser.next();
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        todayWeather.setForcastArr(forcastArr);
        Log.d("myWeather", todayWeather.toString());
        return todayWeather;
    }
}
