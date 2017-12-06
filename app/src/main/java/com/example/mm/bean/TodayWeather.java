package com.example.mm.bean;
import java.util.Arrays;
/**
 * Created by mm on 2017/10/17.
 */
/*
包含今日天气信息的类
*/
public class TodayWeather {
    private String city;
    private String updatetime;
    private String wendu;
    private String shidu;
    private String pm25 = "";
    private String quality = "";
    private String fengxiang;
    private String fengli;
    private String date;
    private String high;
    private String low;
    private String type;
    private Forcast[] forcastArr;

    public TodayWeather() {
        forcastArr = new Forcast[4];
        for(int i = 0; i < 4; i++){
            forcastArr[i] = new Forcast();
        }
    }

    public String getCity() {
        return city;
    }
    public String getUpdatetime() {
        return updatetime;
    }
    public String getWendu() {
        return wendu;
    }
    public String getShidu() {
        return shidu;
    }
    public String getPm25() {
        return pm25;
    }
    public String getQuality() {
        return quality;
    }
    public String getFengxiang() {
        return fengxiang;
    }
    public String getFengli() {
        return fengli;
    }
    public String getDate() {
        return date;
    }
    public String getHigh() {
        return high;
    }
    public String getLow() {
        return low;
    }
    public String getType() {
        return type;
    }
    public Forcast[] getForcastArr() {
        return forcastArr;
    }

    public void setCity(String city) {
        this.city = city;
    }
    public void setUpdatetime(String updatetime) {
        this.updatetime = updatetime;
    }
    public void setWendu(String wendu) {
        this.wendu = wendu;
    }
    public void setShidu(String shidu) {
        this.shidu = shidu;
    }
    public void setPm25(String pm25) {
        this.pm25 = pm25;
    }
    public void setQuality(String quality) {
        this.quality = quality;
    }
    public void setFengxiang(String fengxiang) { this.fengxiang = fengxiang; }
    public void setFengli(String fengli) {
        this.fengli = fengli;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public void setHigh(String high) {
        this.high = high;
    }
    public void setLow(String low) {
        this.low = low;
    }
    public void setType(String type) {
        this.type = type;
    }
    public void setForcastArr(Forcast[] forcastArr) {
        this.forcastArr = forcastArr;
    }
    @Override
    public String toString() {
        return "Weather{" +
                "city='" + city + '\'' +
                ", updatetime='" + updatetime + '\'' +
                ", date='" + date + '\'' +
                ", wendu='" + wendu + '\'' +
                ", shidu='" + shidu + '\'' +
                ", fengli='" + fengli + '\'' +
                ", fengxiang='" + fengxiang + '\'' +
                ", pm25='" + pm25 + '\'' +
                ", quality='" + quality + '\'' +
                ", high='" + high + '\'' +
                ", low='" + low + '\'' +
                ", forcastArr=" + Arrays.toString(forcastArr) +
                '}';
    }
}
