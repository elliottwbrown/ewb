package OnlineConnector.Titan;

import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.MouseInfo;
import java.awt.PointerInfo;
import java.util.StringTokenizer;

public class grabber {
    
    public static int x1,y1,x2,y2;
    public static Robot r;
    public static String prevString="";
    
    public static void calibrate() throws Exception {
        r=new Robot();
        System.out.println("Starting - hover over top left corner. (4 seconds ...)");
        r.delay(4000);
        PointerInfo pi=MouseInfo.getPointerInfo();
        x1=pi.getLocation().x;
        y1=pi.getLocation().y;
        System.out.println("Now hover over bottom right corner. (4 seconds ...)");
        r.delay(4000);
        pi=MouseInfo.getPointerInfo();
        x2=pi.getLocation().x;
        y2=pi.getLocation().y;
        System.out.println("done.");
    }
    
    public static String getNewLines() {
        String newLines="",currString = getClip();
        if (prevString!=currString) newLines=extractNewLines(prevString, currString);
        prevString=currString;
        return newLines;
    }
    
    private static String extractNewLines(final String prevString, final String currString) {
        StringTokenizer curr=new StringTokenizer(currString,"\n");
        StringTokenizer prev=new StringTokenizer(prevString,"\n");
        String prevLine="";
        while (prev.hasMoreTokens()) prevLine=prev.nextToken();
        boolean matched=false;
        String newLines="";
        while (curr.hasMoreTokens()) {
            String currLine=curr.nextToken();
            if (matched) newLines+=currLine+"\n";
            if (currLine.equals(prevLine)) matched=true;
        }
        return newLines;
    }
    
    private static void setClip(final Robot r) {
        PointerInfo pi=null;
        pi=MouseInfo.getPointerInfo();
        r.mouseMove(x1,y1);
        r.mousePress(InputEvent.BUTTON1_MASK);
        r.mouseMove(x2,y2);
        r.mouseRelease(InputEvent.BUTTON1_MASK);
        r.keyPress(KeyEvent.VK_CONTROL);
        r.keyPress(KeyEvent.VK_C);
        r.keyRelease(KeyEvent.VK_C);
        r.keyRelease(KeyEvent.VK_CONTROL);
        r.mouseMove((x1+x2)/2,(y1+y2)/2);
        r.mousePress(InputEvent.BUTTON1_MASK);
        r.mouseRelease(InputEvent.BUTTON1_MASK);
        r.mouseMove(pi.getLocation().x,pi.getLocation().y);
    }
    
    private static String getClip(){
        setClip(r);
        String str = "";
        Clipboard c = Toolkit.getDefaultToolkit().getSystemClipboard();
        try {
            str = ((String)c.getContents(null).getTransferData(DataFlavor.stringFlavor));
        } catch(Exception e){ str = "empty"; }
        return str;
    }
    
}
