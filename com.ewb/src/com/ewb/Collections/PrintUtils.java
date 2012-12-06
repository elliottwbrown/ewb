package com.ewb.Collections;

import java.util.HashMap;
import java.util.Iterator;


public class PrintUtils {
        
    public static void printObjectArray(Object[] o) {
        for (int c=0; c<o.length; c++) {
            Object o2=o[c];
            System.out.print(o2.getClass()+":");
            System.out.println(o2);
        }
    }

    public static void printHashMap(HashMap hm) {
        Iterator i=hm.keySet().iterator();
        while (i.hasNext()) {
            Object key=i.next();
            Object value=hm.get(key);
            System.out.print(key.toString()+":");
            System.out.println(value.toString());
        }
    }

}
