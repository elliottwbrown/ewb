package com.ewb.XML;

import javax.xml.transform.*;
import javax.xml.transform.stream.*;

public class XSLTTransformerUTF8 {

    final static int debugLevel = 3;

    public static void go(String xml, String xsl, String out) throws Exception {
        if (debugLevel > 1) System.out.println("Transforming: " + xml + " and " + xsl);
        try {
            TransformerFactory tFactory = TransformerFactory.newInstance();
            Templates cachedXSLT = tFactory.newTemplates(new StreamSource(xsl));
            //Transformer transformer = tFactory.newTransformer(new StreamSource(xsl));
            Transformer transformer = cachedXSLT.newTransformer();
            transformer.transform(new StreamSource(xml), new StreamResult(out));
        } catch (TransformerException e) {
            System.err.println("The following error occured: " + e);
        }
    }
}
