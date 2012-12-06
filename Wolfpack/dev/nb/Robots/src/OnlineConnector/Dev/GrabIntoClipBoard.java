package OnlineConnector.Dev;

import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

public class GrabIntoClipBoard {
    
    public static void main(String[] args) throws Exception {
        go();
    }
    
    
    public static void go() throws Exception {
        Robot r=new Robot();
        for (int i=0;i<60;i++) {
            setClip(r);
            r.delay(3000);
            System.out.println(getClip());
        }
    }
    
    private static void setClip(final Robot r) {
        r.mouseMove(650,500);
        r.mousePress(InputEvent.BUTTON1_MASK);
        r.mouseMove(750,500);
        r.mouseRelease(InputEvent.BUTTON1_MASK);
        r.keyPress(KeyEvent.VK_CONTROL);
        r.keyPress(KeyEvent.VK_C);
        r.keyRelease(KeyEvent.VK_CONTROL);
        r.keyRelease(KeyEvent.VK_C);
        r.mousePress(InputEvent.BUTTON1_MASK);
        r.mouseRelease(InputEvent.BUTTON1_MASK);
    }
    
    private static String getClip(){
        String str = "";
        Clipboard c = Toolkit.getDefaultToolkit().getSystemClipboard();
        try {
            str = ((String)c.getContents(null).getTransferData(DataFlavor.stringFlavor));
        } catch(Exception e){ str = "empty"; }
        return str;
    }
    
}
