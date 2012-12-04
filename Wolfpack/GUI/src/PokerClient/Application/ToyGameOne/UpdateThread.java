package PokerClient.Application.ToyGameOne;

public class UpdateThread extends Thread {
    
    private MainApp mainApp=null;
    private boolean keepUpdating=true;
    private int delayBetweenFetches=500;
    
    public UpdateThread(MainApp mainApp) {
        this.mainApp=mainApp;
    }
    
    public UpdateThread() {
    }
    
    public void run() {
        System.out.println(">>>>>> UpdateThread "+getName()+" starting at "+new java.util.Date());
        try {
            while (true) {
                System.out.println(">>>>>> UpdateThread "+getName()+" looping at "+new java.util.Date());
                SingleUpdateThread sut;
                if (keepUpdating) {
                    //mainApp.getGameStateButtonPressed();
                    System.out.println(">>>>>>>>> SingleUpdateThread instantiating: "+new java.util.Date());
                    sut=new SingleUpdateThread(mainApp);
                    System.out.println(">>>>>>>>> SingleUpdateThread starting: "+new java.util.Date());
                    sut.start();
                } else {
                    System.out.println(">>>>>>>>> skipping update");
                }
                System.out.println(">>>>>> UpdateThread finished :"+new java.util.Date());
                Thread.sleep(delayBetweenFetches);
                sut=null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println(">>>>>> Thread ended.");
        }
    }
    
    public void setKeepUpdating(boolean value) {
        System.out.println(">>>>>> setKeepUpdating:"+value);
        keepUpdating=value;
    }
    
}