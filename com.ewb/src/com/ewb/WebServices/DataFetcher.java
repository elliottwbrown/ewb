package com.ewb.WebServices;

import com.ewb.Networking.StreamUtilities;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class DataFetcher {

    private static final int port = 80;
    private static final String HTTPProtocol = "HTTP/1.1";
    private static final String UserAgent = "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)";
    private static final int debug = 0;

    public static byte[] openFile(String requestFileLocation) throws Exception {
        if (debug > 2) System.out.println(">>> opening file to " + requestFileLocation);
        FileInputStream fin = new FileInputStream(requestFileLocation);
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        StreamUtilities.copy(fin, bout);
        fin.close();
        return bout.toByteArray();
    }

    public static String getURLviaHTTP(String urlString, String encoding) throws MalformedURLException, IOException {
        URL url = new URL(urlString);
        if (debug > 1) System.out.println(">>> opening connection " + urlString);
        URLConnection uc = url.openConnection();
        if (debug > 1)System.out.println(">>> opened");
        if (debug > 1) System.out.println(">>> sending request");
        uc.setRequestProperty("Authorization", "Basic " + encoding);
        if (debug > 1) System.out.println(">>> reading response");
        InputStream is = uc.getInputStream();
        final int MAX_LENGTH = 128000;
        byte[] buf = new byte[MAX_LENGTH];
        int total = 0;
        while (total < MAX_LENGTH) {
            int count = is.read(buf, total, MAX_LENGTH - total);
            if (count <0) break;
            if (debug > 3) System.out.println(count);
            total += count;
        }
        if (debug > 1) System.out.println(">>> read");
        is.close();
        String reply = new String(buf, 0, total);
        return reply;
    }

    public static HttpURLConnection connectViaHTTP(String SOAPUrl) throws IOException, MalformedURLException {
        URL url = new URL(SOAPUrl);
        if (debug > 1) System.out.println(">>> Connecting to " + SOAPUrl);
        URLConnection connection = url.openConnection();
        HttpURLConnection httpConn = (HttpURLConnection) connection;
        if (debug > 1) System.out.println(">>> Connected");
        return httpConn;
    }

}