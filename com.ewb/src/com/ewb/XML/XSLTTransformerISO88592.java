package com.ewb.XML;

import com.ewb.FileSystem.FileAccess;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.OutputKeys;
import java.io.*;

public class XSLTTransformerISO88592 {

    final static int debugLevel = 1;

    public static void main(String[] args) throws Exception {
        if (args.length != 2) {
            System.err.println(
                    "Usage: java Transform [xmlfile] [xsltfile]");
            System.exit(1);
        }
        File xmlFile = new File(args[0]);
        File xsltFile = new File(args[1]);
        File outputFile = new File(args[3]);
//        go(xmlFile,xsltFile,outputFile);
    }

    public void go(String xmlFile, String xsltFile, String outputFileName) throws Exception {
        if (debugLevel > 1) {
            System.out.println("Transforming: " + xmlFile + " and " + xsltFile);
        }
        StreamSource xmlSource = new StreamSource(xmlFile);
        StreamSource xsltSource = new StreamSource(xsltFile);
        Transformer trans = TransformerFactory.newInstance().newTransformer(xsltSource);
        trans.setOutputProperty(OutputKeys.INDENT, "Yes");
        trans.setOutputProperty(OutputKeys.ENCODING, "ISO-8859-2");
        trans.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "Yes");
        StreamResult sr = new StreamResult(outputFileName);
        ByteArrayOutputStream actualOutputStream = new ByteArrayOutputStream();
        trans.transform(xmlSource, new StreamResult(actualOutputStream));
        String text = actualOutputStream.toString().trim();
        if (debugLevel > 2) System.out.println(text);
        FileAccess fa= new FileAccess();
        fa.writeToFileISO88592(text, "", outputFileName);
    }
}
