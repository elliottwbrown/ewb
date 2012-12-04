package PokerClient.Application.Holdem;

import PokerClient.Application.Holdem.MainApp;

public class SingleUpdateThread extends Thread {
    
    int debug=0;
    private MainApp mainApp=null;
    private boolean keepUpdating=true;
    
    public SingleUpdateThread(MainApp mainApp) {
        this.mainApp=mainApp;
        if (debug>0) System.out.println(">>>>>>>>> SingleUpdateThread "+getName()+" instantiated: "+new java.util.Date());
    }
    
    @Override
    public void run() {
        if (debug>0) System.out.println(">>>>>>>>>>>> SingleUpdateThread "+getName()+" started: "+new java.util.Date());
        try {
            //mainApp.disablePlayButtons();
            mainApp.getGameStateButtonPressed();
            //mainApp.drawGameState();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (debug>0) System.out.println(">>>>>>>>>>>> SingleUpdateThread "+getName()+" ended.");
        }
    }
}