package Bots.NLHoldem.GenericBots;

abstract public class GenericBot extends GenericBotEngine implements Runnable {
    
    public GenericBot() {
        botName=toString().substring(toString().indexOf("@")-10,toString().indexOf("@"));
        botID=toString();
        init();
    }
    
    public GenericBot(boolean webPlay) {
        if (webPlay) init();
    }
    
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