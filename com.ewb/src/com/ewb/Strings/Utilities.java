package com.ewb.Strings;

import java.util.StringTokenizer;

public class Utilities  {

     public static String getNthToken(String msg, int n) {
        StringTokenizer st = new StringTokenizer(msg);
        String token = "";
        for (int t = 0; t < n; t++) {
            try {
                token = st.nextToken();
            } catch (Exception e) {
                System.out.println(msg);
                System.out.println(n);
            }
        }
        return token;
    }

     public static String getNthToken(String msg, int n,String delim) {
        StringTokenizer st = new StringTokenizer(msg,delim);
        String token = "";
        for (int t = 0; t < n; t++) {
            token = st.nextToken();
        }
        return token;
    }

    public static String getTokenAfter(String msg, String tokenBefore) {
        msg=msg.substring(msg.indexOf(tokenBefore)+tokenBefore.length());
        StringTokenizer st = new StringTokenizer(msg);
        return st.nextToken();
    }
    
    
    public static String getStringFromXtoEOL(String msg,String X) {
        msg=msg.substring(msg.indexOf(X)+X.length());
        msg=msg.substring(0,msg.indexOf("\r\n"));
        return msg;
    }
    
    public static void PrintLines(String msg) {
        StringTokenizer st = new StringTokenizer(msg,"\r\n");
        int c=0;
        while (st.hasMoreTokens()) {
            System.out.println(++c+" "+st.nextToken());
        }
    }    
    
    public static void PrintTokens(String msg) {
        StringTokenizer st = new StringTokenizer(msg);
        int c=0;
        while (st.hasMoreTokens()) {
            System.out.println(++c+" "+st.nextToken());
        }
    }
}
