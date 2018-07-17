package com.lzhy.moneyhll.model;

import java.util.List;

/**
 * Created by Administrator on 2016/11/8 0008.
 */

public class HomeListModel extends BaseModel{
    public String projectname ;
    /**
     * 纬度
     */
    public double latitude ;
    /**
     * 经度
     */
    public double longitude ;
    public int projecttype ;
    public int pid ;
    public double adultprice  ;
    public double enjoytime ;
    public int authentication ;
    public String address ;
    public String imageurl ;
    public String picture2 ;
    public String salesunit ;
    public double distance ;
    public List<PlayWhereModel> nearProject;
    public int campType;
}
