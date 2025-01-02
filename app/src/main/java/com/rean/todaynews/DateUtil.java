package com.rean.todaynews;

import android.annotation.SuppressLint;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
    @SuppressLint({"SimpleDateFormat", "DefaultLocale"})
    public static String getDurationToNow(String dateStr) {
        Date date;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dateStr);
            assert date != null;
            long duration = System.currentTimeMillis() - date.getTime();
            long days = duration / (24 * 60 * 60 * 1000);
            duration = duration % (24 * 60 * 60 * 1000);
            long hours = duration / (60 * 60 * 1000);
            duration = duration % (60 * 60 * 1000);
            long minute = duration / (60 * 1000);

            if (0 == days && 0 == hours && 0 == minute) { // 小于1分钟
                return "刚刚";
            }
            else if(0 == days && 0 == hours) { // 小于1小时
                return String.format("%d分钟前", minute);
            }
            else if(0 == days) { // 小于1天
                return String.format("%d小时前", hours);
            }
            else if(days < 7) { // 小于7天
                return String.format("%d天前", days);
            }
            else if(days < 30) { // 小于1个月
                return String.format("%d周前", days / 7);
            }
            else if(days < 365) { // 小于1年
                return String.format("%d个月前", days / 30);
            }
            else { // 大于1年
                return String.format("%d年前", days / 365);
            }
        } catch (ParseException e) {
            Log.e("DateUtil", "getDurationToNow: " + e.getMessage());
        }
        return "未知时间";
    }
}
