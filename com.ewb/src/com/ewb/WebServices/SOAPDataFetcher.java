package com.ewb.WebServices;

import com.ewb.Networking.StreamUtilities;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.ProtocolException;

public class SOAPDataFetcher extends DataFetcher {

    private static final int port = 80;
    private static final String HTTPProtocol = "HTTP/1.1";
    private static final String UserAgent = "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)";
    private static final int debug = 0;

    public static String getSOAPMessageViaHTTP(String SOAPUrl, String xmlFile2Send, String SOAPAction) throws Exception {
        HttpURLConnection httpConn = connectViaHTTP(SOAPUrl);
        byte[] b = OpenSOAPFile(xmlFile2Send);
        SendSOAPMsg(httpConn, b, SOAPAction);
        return ReceiveSOAPResponse(httpConn);
    }

    public static String getSOAPMessageViaHTTPS(String SOAPUrl, String xmlFile2Send, String SOAPAction) throws Exception {
        String response = "";
//        URL url = new URL(SOAPUrl);
//        if (debug > 1)  System.out.println(">>> Connecting to " + SOAPUrl);
//        URLConnection connection = url.openConnection();
//        HttpsURLConnection httpConn = (HttpsURLConnection) connection;
//        if (debug > 1) System.out.println(">>> Connected");
//
//        byte[] b = OpenSOAPFile(xmlFile2Send);
//        SendSOAPMsg(httpConn, b, SOAPAction);
//
//            InputStreamReader isr = new InputStreamReader(httpConn.getInputStream());
//            BufferedReader in = new BufferedReader(isr);
//            String inputLine;
//            while ((inputLine = in.readLine()) != null) {
//                if (debug > 1) System.out.println(">>> Read:"+inputLine);
//                response += inputLine;
//            }
//            in.close();
//            if (debug > 1) System.out.println(">>> Read");
        return response;
    }

    public static String ReceiveSOAPResponse(HttpURLConnection httpConn) throws Exception {
        String response = "";
        if (debug > 1) System.out.println(">>> Reading response");
        try {
            InputStreamReader isr = new InputStreamReader(httpConn.getInputStream());
            BufferedReader in = new BufferedReader(isr);
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                if (debug > 1) System.out.println(">>> Read:"+inputLine);
                response += inputLine;
            }
            in.close();
            if (debug > 1) System.out.println(">>> Read");
        } catch (IOException e1) {
            System.err.println(">>>>SOAPDataFetcher; Error reading response;");
            if (e1.getMessage().contains("500")) {
                System.err.println("SOAP Server answers HTTP500");
            } else  throw e1;
        }
        return response;
    }

    public static void SendSOAPMsg(HttpURLConnection httpConn, byte[] b, String SOAPAction) throws IOException, ProtocolException {
        httpConn.setRequestProperty("Content-Length", String.valueOf(b.length));
        httpConn.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
        httpConn.setRequestProperty("SOAPAction", SOAPAction);
        httpConn.setRequestMethod("POST");
        httpConn.setDoOutput(true);
        httpConn.setDoInput(true);
        if (debug > 1) {
            System.out.println(">>> Sending file");
        }
        OutputStream out = httpConn.getOutputStream();
        out.write(b);
        out.close();
        if (debug > 1)System.out.println(">>> Sent");
    }

    public static byte[] OpenSOAPFile(String xmlFile2Send) throws FileNotFoundException, IOException {
        if (debug > 1) System.out.println(">>> opening file to " + xmlFile2Send);
        FileInputStream fin = new FileInputStream(xmlFile2Send);
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        StreamUtilities.copy(fin, bout);
        fin.close();
        byte[] b = bout.toByteArray();
        if (debug > 1) System.out.println(">>> opened");
        return b;
    }

}
