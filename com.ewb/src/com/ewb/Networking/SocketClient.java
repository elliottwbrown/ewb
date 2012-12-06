package com.ewb.Networking;

import java.io.*;
import java.net.*;
import java.util.zip.*;

public class SocketClient {

    private int debugLevel = 0;

    public static void main(String[] args) throws Exception {
        testBFXP();
    }

    private static void testBFXP() throws Exception {
        System.out.println(">>> starting");
        long start, end;

        SocketClient client = new SocketClient();
        //String host="www.betfair.com";
        String host = "www.betfairgames.com";
        int port = 80;
        client.open(host, port);

        String url = ""
                + "GET /exchangepoker/LoadExchangePokerRefreshAction.do HTTP/1.1" + delim
                + "Accept: image/gif, image/x-xbitmap, image/jpeg, image/pjpeg, application/x-shockwave-flash, application/vnd.ms-excel, application/vnd.ms-powerpoint, application/msword, */*" + delim
                + "Accept-Language: en-us" + delim
                + "Accept-Encoding: gzip, deflate" + delim
                + "User-Agent: Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)" + delim
                + "Host: " + host + delim
                + "Connection: Keep-Alive" + delim;
        //"Cookie: userhistory=15606327391124997893950|17|Y; betexPtk=betexRegion%253DNA%257EbetexLocale%253Den_US%257EbetexCurrency%253DCAD%257EbetexTimeZone%253DEurope%252FLondon; NSC_fyibohfxfc=0a460a160050; UserPreferencesShowAvailableFunds_591289=true; Betex_591289=bmAR%3A%3Atrue%7C%7CbmAO%3A%3Afalse%7C%7CbmBI%3A%3Atrue%7C%7CbmB%3A%3Afalse%7C%7CbmNC%3A%3Afalse%7C%7CbmCo%3A%3Atrue%7C%7CbmD%3A%3Atrue%7C%7CbmESCB%3A%3Atrue%7C%7CbmFPL%3A%3Afalse%7C%7CbmLC%3A%3ARed%7C%7CbmMWMB%3A%3Atrue%7C%7CbmMB%3A%3Atrue%7C%7CbmP%3A%3Atrue%7C%7CbmPL%3A%3Atrue%7C%7CbmSPL%3A%3Afalse%7C%7CbmGSTA%3A%3Afalse%7C%7CbmGST%3A%3A60%7C%7CbmV%3A%3AtruemvM; betexPtkSess=betexRegionSessionCookie%253DNA%257EbetexLocaleSessionCookie%253Den_US%257EbetexSkin%253Dbetfair%257EbetexTimeZoneSessionCookie%253DEurope%252FLondon%257EbetexCurrencySessionCookie%253DCAD%257EbetexBrand%253Dbetfair; JSESSIONID=3q6i6huoq7hg6"+delim;

        int repetitions = 10;
        start = new java.util.Date().getTime();
        for (int c = 0; c < repetitions; c++) {
            client.send(url);
            String response[] = client.getResponse();
        }
        end = new java.util.Date().getTime();
        long seconds = (end - start) / 1000l;
        float per = (float) seconds / (float) repetitions;
        System.out.println(">>> sent:" + repetitions + " repetitions");
        System.out.println(">>> took:" + seconds + " seconds");
        System.out.println(">>> took:" + per + " per");
        System.out.println(">>> finished");
    }

    public SocketClient() throws Exception {
    }

    public SocketClient(String hostIPIn) throws Exception {
        init();
    }

    synchronized public void init() throws Exception {
        open(hostName, hostPort);
    }

    public String parseCookies(String headers) throws Exception {
        java.util.StringTokenizer st = new java.util.StringTokenizer(headers, "\n");
        cookies = "";
        while (st.hasMoreTokens()) {
            String line = st.nextToken();
            if (line.startsWith("Set-Cookie")) {
                line = line.replaceAll("Set-Cookie: ", "") + ";";
                line = line.substring(0, line.indexOf(";"));
                cookies += line + ";";
            }
        }
        return cookies;
    }

    synchronized public void sendAndResend(String url) throws Exception {
        try {
            send(url);
        } catch (java.net.SocketException e) {
            if (getDebugLevel() > 0) {
                System.out.println("Socket exception trapped - attempting reconnect ...");
            }
            init();
            send(url);
            Thread.sleep(1000);
        }
    }

    synchronized public void send(String url) throws Exception {
        if (getDebugLevel() > 0) {
            System.out.println(">>> sending at " + new java.util.Date());
            //System.out.println(url);
        }
        os.write(url.getBytes());
        os.write(delim.getBytes());
        os.flush();
        if (getDebugLevel() > 0) {
            System.out.println(">>> done at " + new java.util.Date());
        }
    }

    synchronized public String[] getResponse() throws Exception {

        int serverByte = 0;
        String whole = "", header = "", message = "";
        String[] responseArray = new String[2];

        long endTime = new java.util.Date().getTime() + waitTimeInMillis;
        if (debugLevel > 0) {
            System.out.println(">>> endTime " + endTime);
            System.out.println(">>> getResponse");
            System.out.println(">>> getHeader");
        }

        // get header
        while (true) {
            long now = new java.util.Date().getTime();
            if (now > endTime) {
                System.out.println(">>> Timeout !");
                throw new Exception("network timeout Exception");
            } else {
                if (getDebugLevel() > 3) {
                    System.out.println("reading at " + now);
                    System.out.println("avail      " + is.available());
                }
                int prevByte = serverByte;
                if (is.available() > 0) {
                    serverByte = is.read();
                    header += (char) serverByte;
                    if (debugLevel > 3) {
                        if (serverByte > 0) {
                            System.out.println((char) serverByte + " (" + serverByte + ")");
                        }
                        if (serverByte <= 0) {
                            System.out.println("NPC (" + serverByte + ")");
                        }
                    }
                    if (serverByte == 10) {
                        if (header.length() > 4 && header.substring(header.length() - 4).equals("\r\n\r\n")) {
                            header = header.substring(0, header.length() - 4);
                            break;
                        }
                    }
                } else {
                    //throw new Exception("Network Not Available Exception");
                }
            }
        }
        if (debugLevel > 0) {
            System.out.println("HEADER >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
            System.out.println(header);
            System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
            System.out.println(">>> getMessageBody");
        }

        // get message
        if (header.indexOf("Connection: close") > 1) {
            reset();
        } else {
            if (header.indexOf("Content-Encoding: gzip") > 1) {
                if (debugLevel > 0) {
                    System.out.println(">>> getMessageBody:gzip");
                }
                int start = header.indexOf("Content-Length:") + 16;
                int end = start + 5;
                int contentLength = Integer.parseInt(header.substring(start, end).trim());
                serverByte = 0;
                byte[] content = new byte[1000000];
                for (int c = 0; c < contentLength; c++) {
                    int prev = serverByte;
                    serverByte = is.read();
                    content[c] = (byte) serverByte;
                }
                message = unzipper(content);
            } else if (header.indexOf("Content-Length") > 1) {
                if (getDebugLevel() > 0) {
                    System.out.println(">>> getMessageBody:Content-Length");
                }
                int start = header.indexOf("Content-Length:") + 16;
                int end = header.indexOf("\r\n", start);

                int length = Integer.parseInt(header.substring(start, end));
                for (int x = 0; x < length; x++) {
                    serverByte = is.read();
                    if (debugLevel == 3) {
                        System.out.print((char) serverByte);
                    }
                    //int prevByte=serverByte;
                    if (debugLevel == 4) {
                        System.out.print(" reading ...");
                        System.out.print((char) serverByte + " (" + serverByte + ")");
                        System.out.print(" ... read");
                        System.out.println();
                    }
                    if (serverByte < 0) {
                        System.out.println("breaking" + serverByte);
                        break;
                    }
                    message += (char) serverByte;
                }
            } else {
                if (debugLevel > 0) {
                    System.out.println(">>> getMessageBody:other");
                }
                while (true) {
                    endTime = new java.util.Date().getTime() + waitTimeInMillis;
                    serverByte = is.read();
                    if (debugLevel == 3) {
                        System.out.print((char) serverByte);
                    }
                    //int prevByte=serverByte;
                    if (debugLevel == 4) {
                        System.out.print(" reading ...");
                        System.out.print((char) serverByte + " (" + serverByte + ")");
                        System.out.print(" ... read");
                        System.out.println();
                    }
                    if (serverByte < 0) {
                        System.out.println(">>> breaking" + serverByte);
                        break;
                    }
                    if (new java.util.Date().getTime() > endTime) {
                        System.out.println(">>> Timeout !");
                        throw new Exception("network timeout Exception");
                    }
                    message += (char) serverByte;
                }
            }
        }
        if (getDebugLevel() > 0) {
            System.out.println("MESSAGE >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
            System.out.println(message);
            System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        }
        responseArray[0] = header;
        responseArray[1] = message;
        return responseArray;
    }

    synchronized public void open(String hostName, String portIn) throws Exception {
        open(hostName, Integer.parseInt(portIn));
    }

    synchronized public void open(String hostName, int portIn) throws Exception {
        if (getDebugLevel() > 0) {
            System.out.println(">>> opening " + hostName + " on port " + portIn + " at " + new java.util.Date());
        }
        this.hostName = hostName;
        hostPort = portIn;
        currentSocket = new Socket(this.hostName, hostPort);
        currentSocket.setKeepAlive(true);
        //currentSocket.setReceiveBufferSize(1024000);
        //currentSocket.setSendBufferSize(1024000);
        System.out.println(currentSocket);
        System.out.println(currentSocket.getInetAddress());
        System.out.println(currentSocket.getKeepAlive());
        System.out.println(currentSocket.getReceiveBufferSize());
        System.out.println(currentSocket.getSoTimeout());
        System.out.println(currentSocket.getSendBufferSize());
        os = currentSocket.getOutputStream();
        is = currentSocket.getInputStream();
        if (getDebugLevel() > 0) {
            System.out.println(">>> opened " + hostName + " at " + new java.util.Date());
        }
    }

    synchronized public void close() throws Exception {
        currentSocket.close();
        os.close();
        is.close();
    }

    synchronized public void reset() throws Exception {
        close();
        open(this.hostName, hostPort);
    }

    public String unzipper(byte[] zipped) throws Exception {
        ByteArrayInputStream baris = new ByteArrayInputStream(zipped);
        GZIPInputStream in = new GZIPInputStream(baris);
        byte[] buf = new byte[1024];
        int len;
        ByteArrayOutputStream baros = new ByteArrayOutputStream();
        OutputStream outf = baros;
        while ((len = in.read(buf)) > 0) {
            outf.write(buf, 0, len);
        }
        in.close();
        outf.close();
        String content = content = baros.toString();
        baros.close();
        return content;
    }

    public int getDebugLevel() {
        return debugLevel;
    }

    public void setDebugLevel(int debugLevel) {
        this.debugLevel = debugLevel;
    }

    public static final String delim = "\n";
    public String cookies = "", startUrl = "", hostName;
    public Socket currentSocket;
    public InputStream is;
    public OutputStream os;
    public int hostPort = 80;
    private long waitTimeInMillis = 1000l;
}