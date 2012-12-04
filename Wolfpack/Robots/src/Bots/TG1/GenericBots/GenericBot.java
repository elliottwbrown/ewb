package Bots.TG1.GenericBots;

import com.ewb.Strings.Utilities;

abstract public class GenericBot extends GenericBotEngine implements Runnable {
    
    public String sourceCode="";
    
    public GenericBot() {
        // full class name with packages
        //botName=toString();
        
        // just the class & Instance ID
        botName=toString();
        botName=Utilities.getNthToken(botName,5,".");
        
        // uncomment this next line to get only the class name
        botName=botName.substring(0,botName.indexOf("@"));
        
        botID=toString();
        init();
    }
    
    public GenericBot(boolean webPlay) {
        if (webPlay) init();
    }

    @Override
    public void run()  {
        System.out.println(">>> "+botName+" started");
        boolean finished=false;
        while (!finished) {
            try {
                connect();
                finished=true;
            } catch (Exception e) {
                System.out.println("Cannot connect to Poker Server:");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException interruptedException) {
                }
            }
        }
        join();
        try {
            Thread.sleep(firstJoinDelay);
        } catch (Exception e) {
            e.printStackTrace();
        }
        playLoop();
    }

}