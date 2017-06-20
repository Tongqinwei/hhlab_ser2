package com.beans;

/**
 * Created by hasee on 2017/5/4.
 */
public class data_out {

    private String id;
    private String mmac;
    private String rate;
    private String time;
    private String lon;
    private data_in[] data;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMmac() {
        return mmac;
    }

    public void setMmac(String mmac) {
        this.mmac = mmac;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public data_in[] getData() {
        return data;
    }

    public void setData(data_in[] data) {
        this.data = data;
    }
}
