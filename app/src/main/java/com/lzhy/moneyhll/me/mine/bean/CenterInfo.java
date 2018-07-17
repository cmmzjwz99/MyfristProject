package com.lzhy.moneyhll.me.mine.bean;

/**
 * Created by cmm on 2016/10/31.
 */

public class CenterInfo {
    private int icon;
    private String title;

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
    public CenterInfo() {
        super();
    }
    public CenterInfo(int icon, String title) {
        super();
        this.icon = icon;
        this.title = title;
    }
    @Override
    public String toString() {
        return "MainInfo [icon=" + icon + ", title=" + title +  "]";
    }
}
