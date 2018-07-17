package com.lzhy.moneyhll.utils;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.svprogresshud.SVProgressHUD;
import com.lzhy.moneyhll.R;

import static com.lzhy.moneyhll.utils.UtilCheckMix.networkIsAvailable;

/****************************************************************************
 * Created by xu on 2017/1/7.
 * Function:
 ***************************************************************************/

public final class UtilToast {
    private static SVProgressHUD mSVProgressHUD;


    /************************************************************
     *创建者;龙之游 @ xu 596928539@qq.com
     *修改时间:2017/1/20 17:17
     *注释: @param msg  使用该方法时，只需要传递一个控件对应的 名称就行了 如 "名字"
     ************************************************************/
    private static class SingletonHolder {
        private static final UtilToast INSTANCE = new UtilToast();
    }
    private UtilToast (){
        if (!networkIsAvailable(UtilActivityManager.getInstance().getCurrentActivity())) {
            return ;
        }
    }
    public  static  UtilToast getInstance() {

        return SingletonHolder.INSTANCE;
    }

    public void showDragonInfo( String code_dragon ){//提示信息
        mSVProgressHUD = new SVProgressHUD(UtilActivityManager.getInstance().getCurrentActivity());
        mSVProgressHUD.showInfoWithStatus( code_dragon, SVProgressHUD.SVProgressHUDMaskType.Black);
        mSVProgressHUD = null;
    }
    public void showDragonSuccess( String code_dragon ){//成功信息
        Log.i("showDragonSuccess", "showDragonSuccess: ");
        mSVProgressHUD = new SVProgressHUD(UtilActivityManager.getInstance().getCurrentActivity());
        mSVProgressHUD.showSuccessWithStatus(code_dragon);
        mSVProgressHUD = null;
    }
    public void showDragonError( String code_error ){//失败信息
        mSVProgressHUD = new SVProgressHUD(UtilActivityManager.getInstance().getCurrentActivity());
        mSVProgressHUD.showErrorWithStatus(code_error);
        mSVProgressHUD = null;
    }
    /**
     * @param activity 上下文
     * @param message  显示的信息
     */
    public void myToast(Activity activity, String message) {
        LayoutInflater inflater = LayoutInflater.from(activity);
        View layout = inflater.inflate(R.layout.mytoast,
                (ViewGroup) activity.findViewById(R.id.toast_layout_root));
        ImageView image = (ImageView) layout.findViewById(R.id.image);
        image.setImageResource(R.mipmap.ic_launcher);
        TextView text = (TextView) layout.findViewById(R.id.text);
        text.setText(message);
        Toast toast = new Toast(activity.getApplicationContext());
        //设置在屏幕中的位置
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        //设置持续时间
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }
    /**
     * 自定义显示Toast时间
     *
     * @param context
     * @param message
     * @param duration
     */
    public static void show(Context context, CharSequence message, int duration){
            Toast.makeText(context, message, duration).show();
    }

}
