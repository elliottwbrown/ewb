package PokerClient.Application.Holdem;

import PokerClient.Application.Holdem.MainApp;

public class UpdateThread extends Thread {
    
    int debug=0;    
    private MainApp mainApp=null;
    private boolean keepUpdating=true;
    private int delayBetweenFetches=500;
    
    public UpdateThread(MainApp mainApp) {
        this.mainApp=mainApp;
    }
    
    public UpdateThread() {
    }
    
    public void run() {
        if (debug>0) System.out.println(">>>>>> UpdateThread "+getName()+" starting at "+new java.util.Date());
        try {
            while (true) {
                if (debug>0) System.out.println(">>>>>> UpdateThread "+getName()+" looping at "+new java.util.Date());
                SingleUpdateThread sut;
                if (keepUpdating) {
                    //mainApp.getGameStateButtonPressed();
                    if (debug>0) System.out.println(">>>>>>>>> SingleUpdateThread instantiating: "+new java.util.Date());
                    sut=new SingleUpdateThread(mainApp);
                    if (debug>0) System.out.println(">>>>>>>>> SingleUpdateThread starting: "+new java.util.Date());
                    sut.start();
                } else {
                    if (debug>0) System.out.println(">>>>>>>>> skipping update");
                }
                if (debug>0) System.out.println(">>>>>> UpdateThread finished :"+new java.util.Date());
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