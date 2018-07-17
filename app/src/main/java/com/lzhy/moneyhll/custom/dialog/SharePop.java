package com.lzhy.moneyhll.custom.dialog;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.lzhy.moneyhll.R;
import com.lzhy.moneyhll.utils.PrintLog;
import com.lzhy.moneyhll.utils.UtilToast;
import com.ta.utdid2.android.utils.StringUtils;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

import static com.lzhy.moneyhll.utils.UtilCheckMix.isWeixinAvilible;

/**
 * Created by Administrator on 2016/11/30 0030.
 * 分享弹窗
 */

public class SharePop extends BasePopupWindow {
    /**
     * 获取一个新的实例对象
     *
     * @param _context
     * @return
     */
    public static SharePop newInstance(Context _context) {

        return new SharePop(_context);

    }

    /**
     * @param _context
     */
    private SharePop(Context _context) {

        super(_context, R.layout.pop_share);
        popupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);

        setAnimationStyle();
        findViews();

    }

    /**
     * 从定部进入的动画效果，需要继续完善
     */
    private void setAnimationStyle() {
        popupWindow.setAnimationStyle(R.anim.slide_button);
    }

    private LinearLayout share_wechat;
    private LinearLayout share_wxcircle;

    private void findViews() {

        share_wechat = (LinearLayout) view.findViewById(R.id.share_wechat);
        share_wxcircle = (LinearLayout) view.findViewById(R.id.share_wxcircle);
        share_wechat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isWeixinAvilible(mActivity)) {
                    Toast.makeText(mActivity, "请安装微信", Toast.LENGTH_SHORT).show();
                    return;
                }
                shareToWX();
                dismiss();
            }
        });
        share_wxcircle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isWeixinAvilible(mActivity)) {
                    Toast.makeText(mActivity, "请安装微信", Toast.LENGTH_SHORT).show();
                    return;
                }
                shareToWXCircle();
                dismiss();
            }
        });
        setOnClickListener(R.id.button, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onResult(SHARE_MEDIA platform) {
//            Log.d("plat", "platform" + platform);

            Toast.makeText(mActivity, "分享成功", Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            PrintLog.e(platform + " 分享失败啦" + t.toString());
            Toast.makeText(mActivity, "分享失败", Toast.LENGTH_SHORT).show();
            if (t != null) {
//                Log.d("throw", "throw:" + t.getMessage());
            }
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            UtilToast.getInstance().showDragonSuccess("取消分享");
        }
    };

    /**
     * 分享到微信
     */
    private void shareToWX() {
        PrintLog.e("2222222222222" + getTitle() + getShareContent() + getShareUrl() + getSharePic());

        ShareAction shareAction = new ShareAction(mActivity);
        shareAction.setPlatform(SHARE_MEDIA.WEIXIN);
        setShare(shareAction);
    }

    /**
     * 分享到微信朋友圈
     */
    private void shareToWXCircle() {

        ShareAction shareAction = new ShareAction(mActivity);
        shareAction.setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE);
        setShare(shareAction);
    }

    /**
     * 设置分享内容
     *
     * @param shareAction
     */
    private void setShare(ShareAction shareAction) {
        if (!StringUtils.isEmpty(getTitle())) {
            shareAction.withTitle(getTitle());
        } else {
            shareAction.withTitle("龙之游");
        }
        if (!StringUtils.isEmpty(getShareContent())) {
            shareAction.withText(getShareContent());
        } else {
            shareAction.withText("邀请体验龙之游");
        }

        if (!StringUtils.isEmpty(getSharePic())) {
            shareAction.withMedia(new UMImage(mActivity, getSharePic()));
        } else {
            shareAction.withMedia(new UMImage(mActivity, R.mipmap.share_lzy));
        }
        shareAction.withTargetUrl(getShareUrl());
        shareAction.setCallback(umShareListener);
        shareAction.share();
    }

    /************************
     * 设置分享的相关参数 *
     ************************/

    private String mSharePic = "";

    private String mShareContent = "";

    private String mShareUrl = "";

    private String mTitle = "";

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }


    public String getSharePic() {
        return mSharePic;
    }

    public void setSharePic(String sharePic) {
        mSharePic = sharePic;
    }

    public String getShareContent() {
        return mShareContent;
    }

    public void setShareContent(String shareContent) {
        mShareContent = shareContent;
    }

    public String getShareUrl() {
        return mShareUrl;
    }

    public void setShareUrl(String shareUrl) {
        mShareUrl = shareUrl;
    }
}
