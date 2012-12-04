package Bots.TG1.GenericBots;

import java.util.PropertyResourceBundle;
import com.ewb.Utilities.XMLUtilities;
import PokerCommons.Client.PokerClient;
import PokerCommons.PokerObjects.GameProtocols.TG1.GameValues;

abstract public class GenericBotEngine extends GenericBotParser implements GameValues {
    
    public PropertyResourceBundle configuration;
        
    public GenericBotEngine() {
        botName=toString().substring(toString().indexOf("@")-10,toString().indexOf("@"));
        botID=toString();
        init();
    }
    
    public GenericBotEngine(boolean webPlay) {
        if (webPlay) init("web");
    }
    
    public final void init() {
        try {
            configuration=(PropertyResourceBundle)PropertyResourceBundle.getBundle("Bots.TG1.GenericBots.Configuration.RobotProperties");
            client= new PokerClient(this.getClass().toString());
            lastResponse="";
        } catch (java.net.ConnectException e) {
            System.err.println(" Could not connect to game server.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public final void init(String server) {
        try {
            configuration=(PropertyResourceBundle)PropertyResourceBundle.getBundle("Bots.TG1.GenericBots.Configuration.RobotProperties");
            client= new PokerClient( configuration.getString("hostName"), configuration.getString("hostPort"));
            lastResponse="";
        } catch (java.net.ConnectException e) {
            System.err.println(" Could not connect to game server.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void run() {
    }
    
    public void connect() throws Exception {
            System.out.println(XMLUtilities.getMessageBody(client.getWelcomeAndCookie()));
    }
    
    public void join() {
        try {
            response=client.sendOperation("join","name="+botName);
            botID=client.getSessionID();
            System.out.println(XMLUtilities.getMessageBody(response)+" as "+botID);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void startGame() {
        try {
            boolean started=false;
            while (!started) {
                response=client.sendOperation("start");
                if (debugLevel>0) System.out.println(XMLUtilities.getMessageBody(response));
                if (response.contains("Started new game") || response.contains("Game already started.")) started=true;
                Thread.sleep(joinDelay);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void playLoop() {
        initVariablesForNewOpponent();
        while (true) {
            try {
                if (webPlay) startGame();
                playHand();
                Thread.sleep(joinDelay);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    public void playHand() {
        initVariablesForNewHand();
        while (true) {
            try {
                response=client.sendOperation("getGameState");
                handStatus=GameValues.HAND_NOT_STARTED;
                if (response.contains("<handStatus>")) 
                    handStatus=Integer.parseInt(XMLUtilities.getTag(response,"<handStatus>"));
                if (handStatus==GameValues.HAND_STARTED) {
                    if (debugLevel>0) System.out.println(">>> Hand started");
                    parseGameState();
                    if (response.contains("<turnInfo><player>"+botID)) {
                        if (debugLevel>0) System.out.println(">>> My turn");
                        sendDecision(makeDecision());
                    }
                } else if (handStatus==GameValues.HAND_FINISHED) {
                    parseGameState();
                    parseEndOfGameState();
                    break;
                }
                Thread.sleep(playDelay);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    public void sendDecision(int decision) throws Exception {
        if (decision==GameValues.CALL) response=client.sendOperation("call");
        if (decision==GameValues.RAISE) response=client.sendOperation("raise");
        if (decision==GameValues.CHECKFOLD) response=client.sendOperation("checkfold");
    }
    
    abstract public int makeDecision() throws Exception;
  
}