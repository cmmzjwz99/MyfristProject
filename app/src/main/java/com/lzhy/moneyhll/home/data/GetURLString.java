package com.lzhy.moneyhll.home.data;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;

import static com.lzhy.moneyhll.utils.UtilFile.writeLog;

/************************************************************
 *@Author; 龙之游 @ xu 596928539@qq.com
 * 时间: 2016/11/17 19:13
 * 注释:  获取输入流 并转换为字符串  以json为例
************************************************************/

public final class GetURLString {
    public static String ReadStreamOfJson(String JsonUri){
        InputStreamReader isr = null;
        String result = "";
        StringBuffer html = new StringBuffer();
        InputStream is = null;

        try
        {
            URL url = new URL(JsonUri); //根据Strng表现形式创建URL对象

            URLConnection urlConnection = url.openConnection();//返回一个 URLConnection 对象，它表示到 URL 所引用的远程对象的连接

            //urlConnection.setConnectTimeout(4000); //设置链接超时

            is = urlConnection.getInputStream();//返回从打开的连接中读取到的输入流对象

            isr = new InputStreamReader(is, "utf-8");

            BufferedReader br = new BufferedReader(isr);
            String line = "";
            while((line = br.readLine()) != null)
            {
                html.append(line+"\n");
            }
//            Log.i("html", "ReadStreamOfJson: "+html);
        }
        catch(UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }finally {
            try {
                if (is != null) {
                    is.close();
                }
                if (isr != null) {
                    isr.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        writeLog(html.toString()); //写文件到sd卡
        return html.toString();
    }
}
