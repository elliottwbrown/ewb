package com.ewb.XML;

import java.io.StringReader;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

public class MarshallerUnmarshaller {

    private static void unmarshalIBM(String msg) throws JAXBException {
        JAXBContext jc = JAXBContext.newInstance("dev.DataFetcher");
        Unmarshaller u = jc.createUnmarshaller();
        StringBuffer xmlStr = new StringBuffer(msg);
        Object o = u.unmarshal(new StreamSource(new StringReader(xmlStr.toString())));
        System.out.println(o);
    }
}
