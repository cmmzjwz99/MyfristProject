package com.lzhy.moneyhll.me.mine.bean;

/**
 * Created by cmm on 2016/11/3.
 */

public class WangDianInfo {
    public int icon;
    public String title;
    public String city;
    public int id;
    public int index;




//
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getIcon() {
        return icon;
    }
    public void setIcon(int icon) {
        this.icon = icon;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getCity() {
        return city;
    }
    public void setCity(String city) {
        this.city = city;
    }
    public WangDianInfo() {
        super();
    }
    public WangDianInfo(int icon,String city, String title) {
        super();
        this.icon = icon;
        this.city = city;
        this.title = title;
    }
    @Override
    public String toString() {
        return "MainInfo [icon=" + icon + ", city=" + city + ", title=" + title +  "]";
    }
}
