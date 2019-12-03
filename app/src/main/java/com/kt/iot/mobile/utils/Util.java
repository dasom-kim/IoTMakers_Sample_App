package com.kt.iot.mobile.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import static com.nostra13.universalimageloader.core.ImageLoader.TAG;

/**
 * Created by ceoko on 15. 4. 13..
 */

public class Util {
    public static int deviceOpenY = 0;
    public static int deviceUsedY = 0;
    public static int deviceCount = 0;

    public static int getDeviceOpenY() {
        return deviceOpenY;
    }

    public static void setDeviceOpenY(int deviceOpenY) {
        Util.deviceOpenY = deviceOpenY;
    }

    public static int getDeviceUsedY() {
        return deviceUsedY;
    }

    public static void setDeviceUsedY(int deviceUsedY) {
        Util.deviceUsedY = deviceUsedY;
    }

    public static int getDeviceCount() {
        return deviceCount;
    }

    public static void setDeviceCount(int deviceCount) {
        Util.deviceCount = deviceCount;
    }

    public static void showDialog(Context context, String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public static void setStatusBarColor(Activity activity, int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(color);
        }
    }

    /**
     * 위도,경도로 주소구하기
     * @param lat
     * @param lng
     * @return 주소
     */
    public static String getAddress(Context mContext,double lat, double lng) {
        String nowAddress = "위치 확인 불가";
        Geocoder geocoder = new Geocoder(mContext, Locale.KOREA);
        List <Address> address;

        try {
            if (geocoder != null) {
                //세번째 파라미터는 좌표에 대해 주소를 리턴 받는 갯수로
                //한좌표에 대해 두개이상의 이름이 존재할수있기에 주소배열을 리턴받기 위해 최대갯수 설정
                address = geocoder.getFromLocation(lat, lng, 1);

                if (address != null && address.size() > 0) {
                    // 주소 받아오기
                    //String currentLocationAddress = address.get(0).getAddressLine(0).toString();

                    String cut[] = address.get(0).toString().split(" ");
                    String afterCut[] = cut[3].split(",");
                    // cut[0] : Address[addressLines=[0:"대한민국
                    // cut[1] : 서울특별시  cut[2] : 송파구  cut[3] : 오금동
                    // cut[4] : cut[4] : 41-26"],feature=41-26,admin=null ~~~~
                    String currentLocationAddress = cut[1] + " " + cut[2] + " " + afterCut[0];

                    nowAddress  = currentLocationAddress;

                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return nowAddress;
    }

    /**
     * Crop된 이미지가 저장될 파일을 만든다.
     * @return Uri
     */
    public static File createSaveCropFile (Context context) throws IOException {
        String url = String.valueOf(System.currentTimeMillis()) + ".jpg";

        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            return new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), url);
        } else {
            File storageDir = new File(Environment.getExternalStorageDirectory() + "/Pictures", "iot");

            if (!storageDir.exists()) {
                Log.d(TAG, storageDir.toString());
                storageDir.mkdirs();
            }

            return new File(storageDir, url);
        }
    }

    /**
     * 선택된 uri의 사진 Path를 가져온다.
     * uri 가 null 경우 마지막에 저장된 사진을 가져온다.
     * @param uri
     * @return
     */
    public static File getImageFile(Context context, Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };

        if (uri == null) {
            uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        }

        Cursor cursor = context.getContentResolver().query(uri, projection, null, null,
                MediaStore.Images.Media.DATE_MODIFIED + " desc");

        if(cursor == null || cursor.getCount() < 1) {
            return null; // no cursor or no record
        }

        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();

        String path = cursor.getString(column_index);

        if (cursor != null ) {
            cursor.close();
            cursor = null;
        }

        return new File(path);
    }

    /**
     * 파일 복사
     * @param srcFile : 복사할 File
     * @param destFile : 복사될 File
     * @return
     */
    public static boolean copyFile(File srcFile, File destFile) {
        boolean result = false;
        try {
            InputStream in = new FileInputStream(srcFile);
            try {
                result = copyToFile(in, destFile);
            } finally  {
                in.close();
            }
        } catch (IOException e) {
            result = false;
        }
        return result;
    }

    /**
     * Copy data from a source stream to destFile.
     * Return true if succeed, return false if failed.
     */
    public static boolean copyToFile(InputStream inputStream, File destFile) {
        try {
            OutputStream out = new FileOutputStream(destFile);
            try {
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) >= 0) {
                    out.write(buffer, 0, bytesRead);
                }
            } finally {
                out.close();
            }
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public static String CHART_HOME_DIR = null;

    public static void setChartHomeDir(String dir) {
        CHART_HOME_DIR = dir + "/" + "ChartHome";
    }

    public static String getChartHomeDir() {
        return CHART_HOME_DIR;
    }

    public static String timestampToFormattedStr(long timemills){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA);
        return df.format(new Date(timemills));
    }

    public static long getTodayStartTimestamp(){

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);
        Date currentTime = new Date();
        String strCurrentTime = df.format(currentTime.getTime());

        Date todayStartTime = null;

        try {
            todayStartTime = df.parse(strCurrentTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if(todayStartTime != null)
            return todayStartTime.getTime();
        else
            return -1;

    }

    public static int getDayOfWeek(){

        Calendar cal = Calendar.getInstance();
        return cal.get(Calendar.DAY_OF_WEEK);
    }

    public static String calTimestampToHour(long watchTime){

        Log.i("time_test", "calTimestampToHour watchTime = " + watchTime);

        long minutes = TimeUnit.MILLISECONDS.toMinutes(watchTime);

        long remain = minutes%60;

        if(remain >= 6){

            DecimalFormat format = new DecimalFormat(".#");

            double dMinuite = minutes;
            return format.format(dMinuite/60);

        }else{
            return String.valueOf(minutes/60);
        }

    }

    public static String timeFormatToSimple(String time){

        //SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date = df.parse(time);

            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

            return sdf.format(date);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static long FormatedTimeToTimestamp(String time){

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date = df.parse(time);

            return date.getTime();

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return 0;
    }

}
