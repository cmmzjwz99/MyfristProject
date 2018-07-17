package com.lzhy.moneyhll.utils;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;

/****************************************************************************
 * Created by xu on 2017/1/14.
 * Function:
 ***************************************************************************/

public class UtilPrintHtml {
    public static String printHTML(String JsonUri){
        InputStreamReader inputStreamReader = null;
        StringBuffer html = new StringBuffer();
        InputStream inputStream = null;

        try
        {
            URL url = new URL(JsonUri); //根据Strng表现形式创建URL对象

            URLConnection urlConnection = url.openConnection();//返回一个 URLConnection 对象，它表示到 URL 所引用的远程对象的连接

            //urlConnection.setConnectTimeout(4000); //设置链接超时

            inputStream = urlConnection.getInputStream();//返回从打开的连接中读取到的输入流对象

            inputStreamReader = new InputStreamReader(inputStream, "utf-8");

            BufferedReader br = new BufferedReader(inputStreamReader);
            String line = "";
            while((line = br.readLine()) != null)
            {
                html.append(line+"\n");
            }
            Log.i("html", "ReadStreamOfJson: "+html);
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
                if (inputStream != null) {
                    inputStream.close();
                }
                if (inputStreamReader != null) {
                    inputStreamReader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
//        writeLog(html.toString()); //写文件到sd卡
        return html.toString();
    }
}
