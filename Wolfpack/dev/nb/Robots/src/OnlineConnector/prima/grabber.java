package OnlineConnector.prima;

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
    
    public static int x1,y1,x2,y2,x3,y3;
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
        System.out.println("Now hover over bottom right scroll button. (4 seconds ...)");
        r.delay(4000);
        pi=MouseInfo.getPointerInfo();
        x3=pi.getLocation().x;
        y3=pi.getLocation().y;
        System.out.println("done. starting ...");
    }
    
    public static String getNewLines() throws Exception {
        String newLines="",currString = getClip();
        if (prevString!=currString) newLines=extractNewLines(prevString, currString);
        prevString=currString;
        return newLines;
    }
    
    private static String extractNewLines(final String prevString, final String currString) {
        String newLines="";
        if (prevString.length()>200 && currString.length()>200) {
            String lastHundredChars=prevString.substring(prevString.length()-200);
            try {
                newLines=currString.substring(currString.indexOf(lastHundredChars)+200);
            } catch (Exception e) {
                System.out.println("prevString="+prevString);
                System.out.println("lastHundredChars="+lastHundredChars);
                System.out.println("currString="+currString);
                e.printStackTrace();
                System.exit(999);
            }
        } else {
            newLines="FAILURE";
        }
        return newLines;
    }
    
    private static void setClip(final Robot r) throws Exception {
        PointerInfo pi=null;
        pi=MouseInfo.getPointerInfo();
        r.mouseMove(x1,y1);
        Thread.sleep(40);
        r.mousePress(InputEvent.BUTTON1_MASK);
        Thread.sleep(40);
        r.mouseMove(x2,y2);
        Thread.sleep(40);
        r.mouseRelease(InputEvent.BUTTON1_MASK);
        Thread.sleep(40);
        r.keyPress(KeyEvent.VK_CONTROL);
        r.keyPress(KeyEvent.VK_C);
        r.keyRelease(KeyEvent.VK_C);
        r.keyRelease(KeyEvent.VK_CONTROL);
        Thread.sleep(40);
        r.mouseMove(x3,y3);
        r.mousePress(InputEvent.BUTTON1_MASK);
        r.mouseRelease(InputEvent.BUTTON1_MASK);
        r.mousePress(InputEvent.BUTTON1_MASK);
        r.mouseRelease(InputEvent.BUTTON1_MASK);
        r.mousePress(InputEvent.BUTTON1_MASK);
        r.mouseRelease(InputEvent.BUTTON1_MASK);
        Thread.sleep(40);
        r.mouseMove((x1+x2)/2,(y1+y2)/2);
        r.mousePress(InputEvent.BUTTON1_MASK);
        r.mouseRelease(InputEvent.BUTTON1_MASK);
        r.mouseMove(pi.getLocation().x,pi.getLocation().y);
    }
    
    private static String getClip() throws Exception {
        setClip(r);
        String str = "";
        Clipboard c = Toolkit.getDefaultToolkit().getSystemClipboard();
        try {
            str = ((String)c.getContents(null).getTransferData(DataFlavor.stringFlavor));
        } catch(Exception e) {
            e.printStackTrace();
            str = "empty";
        }
        return str;
    }
    
}
