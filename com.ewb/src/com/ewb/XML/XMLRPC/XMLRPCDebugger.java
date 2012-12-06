package com.ewb.XML.XMLRPC;

import com.ewb.Collections.PrintUtils;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class XMLRPCDebugger extends PrintUtils {

    public static final int debug = 1;

    public static String execute(String SERVER_URL,String xml) throws Exception {
        String methodResponse="";
        System.out.println(">> xmlrpc transaction");
        System.out.println(">>>sending");
        if (debug > 0) System.out.println(xml);
        URL u = new URL(SERVER_URL);
        HttpURLConnection conn = (HttpURLConnection) u.openConnection();
        conn.setDoOutput(true);
        conn.setDoInput(true);
        conn.setRequestMethod("POST");
        OutputStream out = conn.getOutputStream();
        OutputStreamWriter wout = new OutputStreamWriter(out, "UTF-8");
        wout.write(xml);
        wout.flush();
        out.close();
        int rc = conn.getResponseCode();

        //print out headers
        Map<String, List<String>> m = conn.getHeaderFields();
        Iterator i = m.keySet().iterator();
        while (i.hasNext()) {
            Object o = i.next();
            System.out.println(o + "\t\t" + m.get(o));
        }

        if (rc == 200) {
            System.out.println(">>>>> xmlrpc success");
            System.out.println(">>>receiving");
            System.out.println(conn.getContent());
            InputStream in = conn.getInputStream();
            int c;
            while ((c = in.read()) != -1) methodResponse+=(char) c;
            System.out.println(methodResponse);
            in.close();
            out.close();
            conn.disconnect();
        } else {
            System.out.println(">>>>> xmlrpc error");
            System.out.println(conn.getResponseMessage());
            InputStream es = conn.getErrorStream();
            int t = es.read();
            while ((t = es.read()) != -1) methodResponse+=(char) t;
        }
        return methodResponse;
    }
}