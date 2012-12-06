package com.ewb.Utilities;

public class XMLUtilities {
    
    public static String getMessageBody(String xml) {
        return getTag(xml,"<messageBody>");
    }
    
    public static String getTag(String xml,String tag) {
        String msg = null;
        try {
            msg = "";
            if (tag.contains("/")) {
                int start = xml.indexOf(tag) + tag.length();
                int end = xml.indexOf("<", start);
                msg = xml.substring(start, end);
            } else {
                int start = xml.indexOf(tag) + tag.length();
                int end = xml.indexOf("</" + tag.substring(1), start);
                msg = xml.substring(start, end);
            }
        } catch (Exception e) {
            System.out.println("error:"+e.getLocalizedMessage());
            System.out.println("xml:"+xml);
            System.out.println("tag:"+tag);
        }
        return msg;
    }
    
    public static String getSecondTag(String xml,String tag,int n) {
        String msg="";
        xml=xml.substring(xml.indexOf(tag)+1);
        if (tag.contains("/")) {
            int start=xml.indexOf(tag)+tag.length();
            int end=xml.indexOf("<",start);
            msg=xml.substring(start,end);
        } else {
            int start=xml.indexOf(tag)+tag.length();
            int end=xml.indexOf("</"+tag.substring(1),start);
            msg=xml.substring(start,end);
        }
        return msg;
        
    }
    
    public static int countOccurences(String xml,String tag) {
        int count=0;
        boolean continueLooking=true;
        while (continueLooking) {
            if (xml.indexOf(tag)>=0) {
                count++;
                xml=xml.substring(xml.indexOf(tag)+tag.length());
            } else continueLooking=false;
        }
        return count;
    }
}
