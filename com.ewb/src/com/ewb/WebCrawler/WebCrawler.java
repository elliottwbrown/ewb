package com.ewb.WebCrawler;

import com.ewb.Serialization.ObjectSerializer;
import com.ewb.WebCrawler.TOs.CrawlResult;
import com.ewb.WebCrawler.TOs.Link;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

public class WebCrawler {

    public static int debug = 0, numCrawls = 10;
    public static BufferedReader in;
    public static String domainFilter, sql;
    public static LinkedList<String> UnCrawledURLs = new LinkedList();
    public static LinkedList<String> CrawledURLs = new LinkedList();
    public static Map<String, CrawlResult> CrawlResults = new HashMap();

    public static CrawlResult CrawlURL(String u) throws MalformedURLException, Exception {
        URL url = new URL(u);
        CrawlResult cr;
        System.out.println(">>> crawling:" + url.toExternalForm());
        String inputLine="", onlinePage = "", onlinePageChar = "";
        Date startDate = new Date();
        try {
            URLConnection urlConn = url.openConnection();
            String ContentType = urlConn.getContentType().toUpperCase();
            InputStreamReader isr= null;
            new InputStreamReader(urlConn.getInputStream());
            if (ContentType.contains("UTF-8")) {
                in = new BufferedReader(new InputStreamReader(urlConn.getInputStream(), "UTF-8"));
            } else if (ContentType.contains("ISO-8859-2")) {
                in = new BufferedReader(new InputStreamReader(urlConn.getInputStream(), "ISO-8859-2"));
            } else {
                in = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
            }
            inputLine = in.readLine();
            while (inputLine != null) {
                onlinePage += inputLine+"\n";
                inputLine = in.readLine();
            }
            
            if (debug>3) System.out.println(ContentType);
            if (debug>3) System.out.println("pageLength: "+onlinePage.length());
            if (debug>3) System.out.println("page: "+onlinePage);
        } catch (java.io.FileNotFoundException fnfe) {
            System.err.println("URL not found.");
            /* what to do here?
            
            add the URL anyway?
             * 
             * otherwise the links wont add.
             * 
             */
        } catch (java.net.UnknownHostException uhe) {
            System.err.println("UnknownHost");
        } catch (java.io.IOException jio) {
            if (jio.getLocalizedMessage().contains("HTTP response code: 503 ")) {
                System.out.println("503");
            }
        } catch (Exception e) {
            if (e.getMessage().contains("MalformedURLException")) {
                System.err.println("MalformedURLException:" + url);
            } else {
                System.err.println("UNHANDLED:" + e.getCause());
                e.printStackTrace();
            }
        } finally {
            Date endDate = new Date();
            long elapsed = (endDate.getTime() - startDate.getTime());
            System.out.println(">>> crawl took " + elapsed + " milliseconds");
            return new CrawlResult(url, elapsed, 0, onlinePage);
        }
    }

    public static void ProcessLinks(HashSet hs, String urlString) throws Exception {
        URL url = new URL(urlString);;
        String host = url.getHost();
        if (!hs.isEmpty()) {
            Iterator i = hs.iterator();
            while (i.hasNext()) {
                Link l = (Link) i.next();
                String q = l.HREF;
                try {
                    if (q.startsWith("/")) {
                        q = "http://" + host + q;
                    } else if (!q.startsWith("http://")) {
                        q = "http://" + host + "/" + q;
                    }
                    if (q.endsWith("/")) q=q.substring(0,q.length()-1);
                    url = new URL(q);
                    if (!UnCrawledURLs.contains(q)
                            && !CrawledURLs.contains(q)
                            && q.contains(domainFilter)
                            && !q.contains("." + domainFilter)) {
                        UnCrawledURLs.push(q);
                    }
                } catch (MalformedURLException malformedURLException) {
                    malformedURLException.printStackTrace();
                    System.out.println("q=" + q);
                }
            }
        }
    }

    public static HashSet extractLinks(String onlinePage, String host) throws Exception {
        HashSet hs = new HashSet();
        try {
            int c = 0;
            while (onlinePage.contains("<a") & c++ < 1000) {
                String linkText = onlinePage.substring(onlinePage.indexOf("<a"));
                int s1 = onlinePage.indexOf("<a");
                int f1 = onlinePage.indexOf("/a>", s1) + 3;
                if (f1 > s1) {
                    linkText = onlinePage.substring(s1, f1);
                    onlinePage = onlinePage.substring(onlinePage.indexOf("<a"));
                    onlinePage = onlinePage.substring(onlinePage.indexOf("href"));
                    int s = onlinePage.indexOf("\"") + 1;
                    int f = onlinePage.indexOf("\"", s);
                    String href = onlinePage.substring(s, f);
                    Link link = new Link(linkText, href);

                    if (href.startsWith("/")) {
                        href = "http://" + host + href;
                    }
                    if (//!href.contains("?")
                             !href.contains("#")
                            && !href.contains("mailto")
                            && !href.contains(".jpg")
                            && !href.startsWith("https")) {
                        if (debug > 2) {
                            System.out.println(">>>>>> link:" + href);
                        }
                        hs.add(link);
                    } else {
                        if (debug > 2) {
                            System.out.println(">>>>>> rejected:" + href);
                        }
                    }
                    onlinePage = onlinePage.substring(s);
                }
                if (c > 997) {
                    System.err.println("Error 999:" + linkText);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hs;
    }
}