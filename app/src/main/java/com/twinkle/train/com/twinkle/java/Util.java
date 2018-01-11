package com.twinkle.train.com.twinkle.java;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Environment;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.BinaryHttpResponseHandler;
import com.twinkle.train.MainNActivity;

import org.apache.http.Header;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Util {


    static String getpic = "http://119.29.98.60:8080/trainAndroid/pic/";



    public static Bitmap makeRoundCorner(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int left = 0, top = 0, right = width, bottom = height;
        float roundPx = height / 2;
        if (width > height) {
            left = (width - height) / 2;
            top = 0;
            right = left + height;
            bottom = height;
        } else if (height > width) {
            left = 0;
            top = (height - width) / 2;
            right = width;
            bottom = top + width;
            roundPx = width / 2;
        }

        Bitmap output = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        int color = 0xff424242;
        Paint paint = new Paint();
        Rect rect = new Rect(left, top, right, bottom);
        RectF rectF = new RectF(rect);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;


    }


    //创建admin文件夹
    public void createSDCardDir(String admin) {

        File sdcardDir = Environment.getExternalStorageDirectory();
        //得到一个路径，内容是sdcard的文件夹路径和名字
        String path = sdcardDir.getPath() + "/" + admin;
        File path1 = new File(path);
        if (!path1.exists()) {
            //若不存在，创建目录，可以在应用启动的时候创建
            path1.mkdirs();

        }
    }



    public void downloadFile(final String admin, final int flag, final Context context) throws Exception {

        final AsyncHttpClient client = new AsyncHttpClient();
// 指定文件类型
        String[] allowedContentTypes = new String[]{"image/png", "image/jpeg"};
// 获取二进制数据如图片和其他文件
        client.get(getpic + admin + ".jpg", new BinaryHttpResponseHandler(allowedContentTypes) {

            @Override
            public void onSuccess(int statusCode, Header[] headers,
                                  byte[] binaryData) {

                String How = "How";
                new Util().createSDCardDir(How);
                String tempPath = Environment.getExternalStorageDirectory()
                        .getPath() + "/" + How + "/" + admin + ".jpg";
                // TODO Auto-generated method stub
                // 下载成功后需要做的工作
                //
                // Log.e("binaryData:", "共下载了：" + binaryData.length);
                //
                Bitmap bmp = BitmapFactory.decodeByteArray(binaryData, 0,
                        binaryData.length);

                File file = new File(tempPath);
                // 压缩格式
                Bitmap.CompressFormat format = Bitmap.CompressFormat.JPEG;
                // 压缩比例
                int quality = 100;
                try {
                    // 若存在则删除
                    if (file.exists())
                        file.delete();
                    // 创建文件
                    file.createNewFile();
                    //
                    OutputStream stream = new FileOutputStream(file);
                    // 压缩输出
                    bmp.compress(format, quality, stream);
                    // 关闭
                    stream.close();


                    if(flag ==1){
                        context.startActivity(new Intent(context, MainNActivity.class));
                    }
                    //
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers,
                                  byte[] binaryData, Throwable error) {
                // TODO Auto-generated method stub

                if(flag ==1){
                    context.startActivity(new Intent(context, MainNActivity.class));
                }
            }
        });
    }


    public void copy_file(File fromFile, File toFile, Boolean rewrite)

    {
        if (!fromFile.exists() || !fromFile.isFile() || !fromFile.canRead()) {
            return;
        }
        if (!toFile.getParentFile().exists()) {
            toFile.getParentFile().mkdirs();
        }
        if (toFile.exists() && rewrite) {
            toFile.delete();
        }
        try {
            FileInputStream fosfrom = new java.io.FileInputStream(fromFile);
            FileOutputStream fosto = new FileOutputStream(toFile);
            byte bt[] = new byte[1024];
            int c;
            while ((c = fosfrom.read(bt)) > 0) {
                fosto.write(bt, 0, c); //将内容写到新文件当中
            }
            fosfrom.close();
            fosto.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public static void compressBmpToFile(String old_file, File file) {


        Bitmap bmp = BitmapFactory.decodeFile(old_file);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int options = 30;//个人喜欢从30开始,
        bmp.compress(Bitmap.CompressFormat.JPEG, options, baos);
        while (baos.toByteArray().length / 1024 > 100) {
            baos.reset();
            options -= 10;
            bmp.compress(Bitmap.CompressFormat.JPEG, options, baos);
        }
        try {

            FileOutputStream fos = new FileOutputStream(file);
            fos.write(baos.toByteArray());
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getHtml(InputStream is, String encoding)
            throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = is.read(buffer)) != -1) {
            bos.write(buffer, 0, len);
        }
        is.close();
        return new String(bos.toByteArray(), encoding);
    }

    //信息写入本地
    public static void writeFileSdcard(String fileName, byte[] message) {

        try {
            File fp = new File(Environment.getExternalStorageDirectory() + "/How", fileName);
            FileOutputStream fos = new FileOutputStream(fp, true);
            byte[] buffer = message;
            fos.write(buffer);

            fos.close();


        } catch (Exception e) {

            e.printStackTrace();

        }

    }

    //本地信息的删除
    public static void delFile(String fileName) {
        try {
            File file = new File(Environment.getExternalStorageDirectory() + "/How", fileName);
            if (file.isFile()) {
                file.delete();
            }
            file.exists();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static final String vf(String paramString) {
        Object localObject;
        try {

            byte[] arrayOfByte = MessageDigest.getInstance("MD5").digest(paramString.getBytes());
            int j = arrayOfByte.length;
            int i = 0;
            paramString = "";
            // Object localObject;
            for (; ; ) {
                localObject = paramString;
                if (i >= j) {
                    break;
                }
                String str = Integer.toHexString(arrayOfByte[i] & 0xFF);
                localObject = str;
                if (str.length() == 1) {
                    localObject = "0" + str;
                }
                paramString = paramString + (String) localObject;
                i += 1;
            }
            return (String) localObject;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            localObject = "";
        }

        return (String) localObject;
    }



}
