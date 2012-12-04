package PokerClient.Application.ToyGameOne;

public class SingleUpdateThread extends Thread {
    
    private MainApp mainApp=null;
    private boolean keepUpdating=true;
    
    public SingleUpdateThread(MainApp mainApp) {
        this.mainApp=mainApp;
        System.out.println(">>>>>>>>> SingleUpdateThread "+getName()+" instantiated: "+new java.util.Date());
    }
    
    public void run() {
        System.out.println(">>>>>>>>>>>> SingleUpdateThread "+getName()+" started: "+new java.util.Date());
        try {
            //mainApp.disablePlayButtons();
            mainApp.getGameStateButtonPressed();
            //mainApp.drawGameState();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println(">>>>>>>>>>>> SingleUpdateThread "+getName()+" ended.");
        }
    }
}