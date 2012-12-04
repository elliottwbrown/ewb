package OnlineConnector.Dev;

import java.awt.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;

public class MakeScreenshot {
    
    public static void main(String[] args) throws Exception {
        MakeScreenshot ms= new MakeScreenshot();
        ms.go();
    }
    
    public void go() throws Exception {
        String filename="c:/temp/";
        for (int t=0;t<60;t++) {
            long time=new java.util.Date().getTime();
            screenScrape(filename+time+".png");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }
    
    public void screenScrape(String outFileName) throws Exception {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();
        Rectangle screenRect = new Rectangle(1050,217,201,511) ;
        Robot robot = new Robot();
        BufferedImage image = robot.createScreenCapture(screenRect);
        ImageIO.write(image, "png", new File(outFileName));
    }
}
