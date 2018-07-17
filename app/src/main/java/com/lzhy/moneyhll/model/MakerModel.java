package com.lzhy.moneyhll.model;

/**
 * Created by Administrator on 2016/11/25 0025.
 */

public class MakerModel extends BaseModel {
    /**
     * 103表示创客项目，104表示创客商品
     */

    public int projecttype;
    public String projectname;
    public double latitude;
    public double longitude;
    public double adultprice ;
    public double enjoytime  ;

    public String consumerHotline  ;
    public String address ;
    public String imageurl;
    public String picture2;
    public String salesunit;
    public long endTime;
    public long startTime;
}
