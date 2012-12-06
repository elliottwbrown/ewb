package com.ewb.Time;

import java.util.Calendar;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class Dates {
    
    public Dates() {
    }
    
    public static Date currentDate() {
        Date d = new java.util.Date();
        return d;
    }
    
    public static String currentDateString(String format) {
        Date d = currentDate();
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(format);
        return sdf.format(d);
    }
    
    public static String currentTs() {
        return currentDateString("yyyy-MM-dd-HH-mm-ss");
    }
    
    public static Date round(Date d, int component, int interval) {
        
        // instantiate Calendar object w/ passed date
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        
        // round specified component to interval
        double interval_d = interval;
        double componentValue = cal.get(component);
        componentValue = Math.round(componentValue/interval_d) * interval_d;
        int i = (int) componentValue;
        
        // assemble new Calendar
        switch (component) {
            case Calendar.YEAR:
                cal.set(i,0,1,0,0,0);
                break;
            case Calendar.MONTH:
                cal.set(Calendar.MONTH,i);
                cal.set(Calendar.DATE,1);
                cal.set(Calendar.HOUR,0);
                cal.set(Calendar.MINUTE,0);
                cal.set(Calendar.SECOND,0);
                break;
            case Calendar.DATE:
                cal.set(Calendar.DATE,i);
                cal.set(Calendar.HOUR,0);
                cal.set(Calendar.MINUTE,0);
                cal.set(Calendar.SECOND,0);
                break;
            case Calendar.HOUR_OF_DAY:
                cal.set(Calendar.DATE,i);
                cal.set(Calendar.MINUTE,0);
                cal.set(Calendar.SECOND,0);
                break;
            case Calendar.HOUR:
                cal.set(Calendar.DATE,i);
                cal.set(Calendar.MINUTE,0);
                cal.set(Calendar.SECOND,0);
                break;
            case Calendar.MINUTE:
                cal.set(Calendar.MINUTE,i);
                cal.set(Calendar.SECOND,0);
                break;
            case Calendar.SECOND:
                cal.set(Calendar.SECOND,i);
                break;
        }
        
        // return date
        return cal.getTime();
    }
    
    public static Date add(Date d, int component, int interval) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        cal.add(component,interval);
        return cal.getTime();
    }
    
    public static String currentTsDate() {
        return currentDateString("yyyy-MM-dd")+"-00-00-00";
    }
    
    public static String formatDateString(String dateStrIn, String formatStrIn, String formatStrOut) {
        String dateStrOut = "";
        try {
            DateFormat inDf = new SimpleDateFormat(formatStrIn);
            DateFormat outDf = new SimpleDateFormat(formatStrOut);
            Date d = (Date)inDf.parse(dateStrIn);
            dateStrOut = outDf.format(d);
        }
        catch (Exception e) {
            dateStrOut = dateStrIn;
        }
        return dateStrOut;
    }
    
    public static String date2String(Date d, String format) {
        String outStr = "";
        try {
            DateFormat outDf = new SimpleDateFormat(format);
            outStr = outDf.format(d);
        }
        catch (Exception e) {
            outStr = d.toString();
        }
        return outStr;
    }
    
    public static Date string2Date(String s, String format) {
        java.util.Date d = new Date();
        if (s.trim().equals("")) {
            return d;
        }
        
        // parse string
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(format);
        try {
            d = sdf.parse(s);
        }
        catch (Exception e) {
            e.printStackTrace();
            return d;
        }
        
        return d;
    }
    
    public static Date ts2Date(String ts) {
        return string2Date(ts,"yyyy-MM-dd-HH-mm-ss");
    }
    
    public static String date2Ts(Date d) {
        return date2String(d,"yyyy-MM-dd-HH-mm-ss");
    }
    
    public static String string2Ts(String s, String format) {
        return date2Ts( string2Date(s,format) );
    }
    
    public static String ts2String(String ts, String format) {
        
        // blank
        if (ts.trim().equals("")) {
            return "";
        }
        
        java.util.Date d = new Date();
        try {
            d = ts2Date(ts);
        }
        catch (Exception e) {
            return "";
        }
        
        return date2String(d,format);
    }
    
    public static Date addDay(Date d, int delta) {
        
        // create Calendar
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        
        // add days
        cal.add(Calendar.DATE, delta);
        
        // return Date
        return cal.getTime();
    }
    
    public static Date addMonth(Date d, int delta) {
        
        // create Calendar
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        
        // add months
        cal.add(Calendar.MONTH, delta);
        
        // return Date
        return cal.getTime();
    }
    
}