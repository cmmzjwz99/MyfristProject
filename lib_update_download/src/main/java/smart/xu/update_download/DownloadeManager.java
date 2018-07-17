package smart.xu.update_download;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import static smart.xu.update_download.Constant.Constant.DownloadCachePath;

/***********************************************************************
* @author: 596928539@qq.com
* 用途: 下载管理类
************************************************************************/

public class DownloadeManager implements View.OnClickListener{

    private Context mContext;
    private String apkNames;
    private String apkUrl;
    //提示语
//   private String updateMsg = "有最新的软件包哦，快下载吧~";      
    //返回的安装包url
    // private String apkUrl = "http://hiao.com/android/bus/QingDaoBus.apk";
    private Dialog noticeDialog;
    private Dialog downloadDialog;
    /* 下载包安装路径 */
    private String savePath = "";

    private String saveFileName = "";
    /* 进度条与通知ui刷新的handler和msg常量 */
    private ProgressBar mProgress;

    private static final int DOWN_UPDATE = 1;
    private static final int DOWN_OVER = 2;
    private int progress;
    private Thread downLoadThread;
    private boolean interceptFlag = false;
//    public boolean isdown = false;
    private TextView updatePercentTextView;
    private TextView updateCurrentTextView;
    private TextView updateTotalTextView;
    private int apkLength;
    private int apkCurrentDownload;

    public DownloadeManager(Context mContext, String apkNames, String apkUrl) {
        this.mContext = mContext;
        this.apkNames = apkNames;
        this.apkUrl = apkUrl;
/*
        if (Build.VERSION.SDK_INT > 21) {
            this.savePath = DownloadCachePath21;
        }else {
            this.savePath = DownloadCachePath;
        }*/
        this.savePath = DownloadCachePath;
        this.saveFileName = savePath + apkNames;
        showDownloadDialog();
    }


    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case DOWN_UPDATE:
                    mProgress.setProgress(progress);
                    updatePercentTextView.setText("已完成 "+progress + "" + "%");
                    try {
                        int currentM, currentK, totalM, totalK;
                        currentM = apkCurrentDownload / 1024 / 1024;
                        currentK = apkCurrentDownload / 1024 - currentM * 1024;

                        totalM = apkLength / 1024 / 1024;
                        totalK = apkLength / 1024 - totalM * 1024;

                        updateCurrentTextView.setText(  String.format("%.2f", Double.valueOf(currentM+"."+currentK))  + "MB/");
                        updateTotalTextView.setText(  String.format("%.2f", Double.valueOf(totalM+"."+totalK)) + "MB");

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    break;
                case DOWN_OVER:
                    installApk();
                    break;
                default:
                    break;
            }
        }

        ;
    };

    public void showDownloadDialog() {


        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("正在下载");

        final LayoutInflater inflater = LayoutInflater.from(mContext);

        View v = inflater.inflate(R.layout.progress_down, null);
        mProgress = (ProgressBar) v.findViewById(R.id.progress);

        updatePercentTextView = (TextView) v.findViewById(R.id.updatePercentTextView);
        updateCurrentTextView = (TextView) v.findViewById(R.id.updateCurrentTextView);
        updateTotalTextView = (TextView) v.findViewById(R.id.updateTotalTextView);

        updatePercentTextView.setTextColor(mContext.getResources().getColor(R.color.black));
        updateCurrentTextView.setTextColor(mContext.getResources().getColor(R.color.black));
        updateTotalTextView.setTextColor(mContext.getResources().getColor(R.color.black));

        builder.setView(v);
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                interceptFlag = true;
            }
        });
        downloadDialog = builder.create();
        downloadDialog.setCanceledOnTouchOutside(false);//调用这个方法时，按对话框以外的地方不起作用。按返回键还起作用
//        downloadDialog.setCancelable(false);//调用这个方法时，按对话框以外的地方不起作用。按返回键也不起作用
        downloadDialog.show();
        downloadApk();
    }
    private Runnable mdownApkRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                File ApkFile = new File(saveFileName);
                boolean exists = ApkFile.exists();
                if (exists) {
                    ApkFile.delete();
                }
                FileOutputStream fos = new FileOutputStream(ApkFile,false);
                
                URL url = new URL(apkUrl);
                Log.i("url", "onResponse: "+apkUrl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.connect();
                apkLength = conn.getContentLength();
                System.out.println();
                InputStream is = conn.getInputStream();
                apkCurrentDownload = 0;
                byte buf[] = new byte[1024];
                int length = -1 ;
                while((length = is.read(buf))!=-1){
                    apkCurrentDownload += length;
                     progress = (int) (((float) apkCurrentDownload / apkLength) * 100);

                    Log.i("downloadApk", "downloadApk: ");
                     //更新进度   
                     mHandler.sendEmptyMessage(DOWN_UPDATE);
                     fos.write(buf, 0, length);
                     if (apkCurrentDownload == apkLength) {
                         //下载完成通知安装   
                         mHandler.sendEmptyMessage(DOWN_OVER);
                         downloadDialog.dismiss();
                         break;
                     }
                     if(interceptFlag ){
                    	 ApkFile.delete();
                    	 break;
                     }
                }
                fos.close();
                is.close();
            }catch (IOException e) {
                e.printStackTrace();
                try {
                    Thread.sleep(2000);
                    downloadDialog.dismiss();
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
                Looper.prepare();
                Toast.makeText(mContext, "更新失败，请联系服务商", Toast.LENGTH_LONG).show();
                Looper.loop();

            }

        }
    };

    /**
     * 下载apk
     *
     * @param
     */

    private void downloadApk() {
        Log.i("downloadApk", " private void downloadApk(): ");


        downLoadThread = new Thread(mdownApkRunnable);
        downLoadThread.start();
    }

    /**
     * 安装apk
     *
     * @param
     */
    private void installApk() {
        File apkfile = new File(saveFileName);
        if (!apkfile.exists()) {
            return;
        }
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive");
        mContext.startActivity(i);
        try {
            if (downloadDialog!=null&&downloadDialog.isShowing())
            downloadDialog.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {

    }
}
