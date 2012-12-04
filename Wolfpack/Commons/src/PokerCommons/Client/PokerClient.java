package PokerCommons.Client;

import java.util.*;
import java.util.StringTokenizer.*;
import java.util.Date.*;
import com.ewb.Networking.SocketClient;

public class PokerClient extends SocketClient {

    private int debugLevel = 0;
    private static final String pathPrefix = "/Wolfpack/FC?op=";
    private PropertyResourceBundle robotProperties;
    private String hostPort="8080";
    private String standardHeaders, SessionID;
    
    public PokerClient(String SessionID) throws Exception {
        this.SessionID = SessionID;
    }

    public PokerClient(String hostName, String hostPort) throws Exception {
        init(hostName, hostPort);
    }

    private void init(String hostName, String hostPort) throws Exception {
        standardHeaders =
                "Accept: */*" + delim
                + "Accept-Language: en-us" + delim
                + "Accept-Encoding: gzip, deflate" + delim
                + "User-Agent: Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)" + delim
                + "Host: " + hostName + delim
                + "Cache-Control: no-cache" + delim
                + "Connection: Keep-Alive" + delim;
        try {
            open(hostName, hostPort);
        } catch (java.net.ConnectException jnce) {
            System.out.println("Cannot Connect To Poker Server");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void init(final PropertyResourceBundle robotProperties) throws Exception {
        this.robotProperties = robotProperties;
        hostName = robotProperties.getString("hostName");
        hostPort = robotProperties.getString("hostPort");
        standardHeaders =
                "Accept: */*" + delim
                + "Accept-Language: en-us" + delim
                + "Accept-Encoding: gzip, deflate" + delim
                + "User-Agent: Wolfpack 1.0" + delim
                + "Host: " + hostName + delim
                + "Cache-Control: no-cache" + delim
                + "Connection: Keep-Alive" + delim;
        open(hostName, hostPort);
    }

    public String getWelcomeAndCookie() throws Exception {
        String Packet = "GET " + pathPrefix + "getWelcome" + " HTTP/1.1" + delim + standardHeaders + "Cookie: " + cookies + delim + delim;
        sendAndResend(Packet);
        String[] response = getResponse();
        String header = response[0];
        String message = response[1];
        if (debugLevel > 2) {
            System.out.println(">>> header:\r\n" + header);
        }
        if (debugLevel > 2) {
            System.out.println(">>> message:\r\n" + message);
        }
        cookies = parseCookies(header);
        int start = cookies.indexOf("JSESSIONID=") + "JSESSIONID=".length();
        //SessionID = cookies.substring(start, start + 32);
        SessionID = cookies.substring(start, start + 28);
        return message;
    }

    public String getSessionID() throws Exception {
        return SessionID;
    }

    public String sendOperation(String Operation, String nvPair) throws Exception {
        return sendOperation(Operation + "&" + nvPair);
    }

    public String sendOperation(String Operation) throws Exception {
        String Packet = "GET " + pathPrefix + Operation + " HTTP/1.1" + delim + standardHeaders + "Cookie: " + cookies + delim + delim;
        boolean succeeded = false;
        String[] response = new String[2];
        while (!succeeded) {
            try {
                sendAndResend(Packet);
                response = getResponse();
                succeeded = true;
            } catch (Exception e) {
                //e.printStackTrace();
                Thread.sleep(1000);
            }
        }
        String header = response[0];
        String message = response[1];
        if (debugLevel > 2) {
            System.out.println(">>> header:\r\n" + header);
        }
        if (debugLevel > 2) {
            System.out.println(">>> message:\r\n" + message);
        }
        return message;
    }

}
