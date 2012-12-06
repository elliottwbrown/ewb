package com.ewb.WebCrawler.TOs;

import java.io.Serializable;
import java.net.URL;

public class CrawlResult implements Serializable {
    
    public int STATUS_UP=1,STATUS_DOWN=2;
    public long elapsed=0;
    public int result=0;
    public int googlePR=0;
    public String page="";
    public URL url=null;

    public CrawlResult(URL url,long elapsed,int result,String page) {
        this.url=url;
        this.elapsed=elapsed;
        this.result=result;
        this.page=page;
    }

    public String toString() {
        return url.toExternalForm();
    }
}
