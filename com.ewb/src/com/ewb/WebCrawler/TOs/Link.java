package com.ewb.WebCrawler.TOs;

import java.net.URL;

public class Link {
    public int STATUS_UP=1,STATUS_DOWN=2;
    public String LINKTEXT;
    public String HREF;
    public int googlePR=0;
    public int FROM_URL_ID;
    public int TO_URL_ID;

    public Link(String LINKTEXTin,String HREFin) {
        this.LINKTEXT=LINKTEXTin;
        this.HREF=HREFin;
    }

    public String toString() {
        return LINKTEXT;
    }
}
