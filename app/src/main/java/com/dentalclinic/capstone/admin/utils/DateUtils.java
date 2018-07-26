package com.dentalclinic.capstone.admin.utils;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtils {
    /**
     * @param source
     * @param srcFormat
     * @param targetFormat
     * @return
     */
    public static String changeDateFormat(String source, DateTimeFormat srcFormat, DateTimeFormat targetFormat){
        try {
            if (source == null || source.trim().length() == 0) {
                Log.d("DEBUG_TAG", "DateUtils.changeDateFormat() source is null");
                return "";
            }
            SimpleDateFormat smpSrc = new SimpleDateFormat(srcFormat.toString(), Locale.US);
            Date srcDate = smpSrc.parse(source);
            SimpleDateFormat smpTarget = new SimpleDateFormat(targetFormat.toString(), Locale.US);
            String targetDate = smpTarget.format(srcDate);
            return targetDate;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }

    }



    /**
     * @param source
     * @param srcFormat
     * @return
     */
    public static Date getDate(String source, DateTimeFormat srcFormat){
        try {
            SimpleDateFormat smpSrc = new SimpleDateFormat(srcFormat.toString(), Locale.US);
            Date targetDate = smpSrc.parse(source);
            return targetDate;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getDate(Date source, DateTimeFormat targetFormat){
        SimpleDateFormat smpTarget = new SimpleDateFormat(targetFormat.toString(), Locale.US);
        String targetDateStr = smpTarget.format(source);
        return targetDateStr;
    }

    public static String getCurrentDate(){
        Calendar c = Calendar.getInstance();
        return getDate(c.getTime(),DateTimeFormat.DATE_APP);
    }
  public static String getCurrentDate(DateTimeFormat format){
        Calendar c = Calendar.getInstance();
        return getDate(c.getTime(),format );
    }

    public static String getCurrentDateFormat(){
        Calendar c = Calendar.getInstance();
        String result="";
        result+=getDayInWeek(c.get(Calendar.DAY_OF_WEEK));
        result+=" ngày "+c.get(Calendar.DAY_OF_MONTH) + " tháng " + (c.get(Calendar.MONTH)+1) + " năm "+c.get(Calendar.YEAR);
        return result;
    }

    private static String getDayInWeek(int day){
        switch (day){
            case Calendar.MONDAY:
                return "Thứ hai";
            case Calendar.TUESDAY:
                return "Thứ ba";
            case Calendar.WEDNESDAY:
                return "Thứ tư";
            case Calendar.THURSDAY:
                return "Thứ năm";
            case Calendar.FRIDAY:
                return "Thứ sáu";
            case Calendar.SATURDAY:
                return "Thứ bảy";
            case Calendar.SUNDAY:
                return "Chủ nhật";
        }
        return "";
    }



}
