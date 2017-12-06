package com.example.mm.bean;
import java.io.Serializable;
/**
 * Created by mm on 2017/11/30.
 */

public class Forcast implements Serializable {
    private String date;
    private String low;
    private String high;
    private String fengli;
    private String fengxiang;
    private String type;

    public Forcast() {}


    public String getDate() {
        return date;
    }


    public void setDate(String date) {
        this.date = date;
    }

    public String getLow() {
        return low;
    }


    public void setLow(String low) {
        this.low = low;
    }

    public String getHigh() {
        return high;
    }


    public void setHigh(String high) {
        this.high = high;
    }


    public String getFengxiang() {
        return fengxiang;
    }


    public void setFengxiang(String fengxiang) {
        this.fengxiang = fengxiang;
    }


    public String getFengli() {
        return fengli;
    }


    public void setFengli(String fengli) {
        this.fengli = fengli;
    }


    public String getType() {
        return type;
    }


    public void setType(String type) {
        this.type = type;
    }

    public String getTempString() {
        return this.getLow() + "~" + this.getHigh();
    }

    @Override
    public String toString() {
        return "Forcast{" +
                "  date='" + date + '\'' +
                ", low='" + low + '\'' +
                ", high='" + high + '\'' +
                ", fengli='" + fengli + '\'' +
                ", fengxiang='" + fengxiang + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}