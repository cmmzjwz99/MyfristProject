package com.lzhy.moneyhll.model;

/**
 * Created by Administrator on 2016/11/12 0012.
 */

public class ProjectOrderModel extends BaseModel {
    /**
     * 实付现金
     */
    public double payAmount;
    /**
     * 数量
     */
    public int quantity;
    /**
     * 图片
     */
    public String imageUrl ;
    /**
     * 项目名称
     */
    public String title;
    /**
     * 有效期开始时间
     */
    public String startTime;
    /**
     * 有效期结束时间
     */
    public String endTime;
    /**
     * 验证码
     */
    public String checkCode;
    /**
     * 游玩时间
     */
    public double enjoytime;
    /**
     * 单价
     */
    public double adultPrice;
    /**
     * 计量单位
     */
    public String salesUnit;
    /**
     * 是否有评论 0没有评论
     */
    public int projectStatus;

    public String content;

    public int star;
    /**
     * 项目状态（status=2 表示未使用  status =3表示已使用）
     */
    public int status;
}
