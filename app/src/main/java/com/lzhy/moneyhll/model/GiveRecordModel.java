package com.lzhy.moneyhll.model;

/**
 * Created by Administrator on 2017/1/5 0005.
 */

public class GiveRecordModel extends BaseModel {
    public long createTime;

    //房车劵
    /**
     * 被赠送人头像
     */
    public String begiftedavatar ;
    /**
     * 赠送人头像
     */
    public String giftavatar ;
    /**
     * 赠送人id
     */
    public int curveMemberId ;
    /**
     * 被赠送人id
     */
    public int changeMemberId ;
    /**
     * 赠送人昵称
     */
    public String giftname ;
    /**
     * 被赠送人昵称
     */
    public String begiftedname ;
    /**
     * 被赠送人账户
     */
    public String begiftedTel;
    /**
     * 赠送人账户
     */
    public String giftTel;
    public int num;


    //龙珠
    public int userId;

    public int changeValue;

    public String nickName;

    public String avatar;

    public String account;
    /**
     * 1赠送者
     * 0被赠送者
     */
    public int rownum;
}
