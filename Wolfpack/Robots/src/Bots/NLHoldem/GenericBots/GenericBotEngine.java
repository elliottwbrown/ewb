package Bots.NLHoldem.GenericBots;

import java.util.PropertyResourceBundle;
import com.ewb.Utilities.XMLUtilities;
import PokerCommons.Client.PokerClient;
import PokerCommons.PokerObjects.Cards.Cards;
import PokerCommons.PokerObjects.GameProtocols.LimitHoldemCash.GameValues;

abstract public class GenericBotEngine implements GameValues {
    
    public static final int debugLevel=0,firstJoinDelay=2000,joinDelay=2000,playDelay=20;
    public int decision=CALL;
    /*
     * these variables store results from the games played with the current opponent
     */
    public int
            numOppFoldedBB=0,           // number of times opponent folded as big blind
            numOppFoldedSB=0,           // number of times opponent folded as small blind
            numOppFoldedPreFlop=0,      // number of times opponent folded pre flop
            numOppFolded=0,             // number of times opponent folded
            numOppChecked,              // number of times opponent checked this game
            numOppCalled=0,             // number of times opponent calles this game
            numOppRaised=0,             // number of times opponent raised this game
            numOppMoved=0,              // number of times opponent moved this game
            numOppHands=0;              // number of hands played against this opponent
    
    public int
            numOppLost,                 // number of times bot won
            numShowdowns=0,             // number of times opponent went to showdown
            numShowdownsWon=0;          // number of times opponent won showdown
    
    public int
            numBotFoldedBB=0,           // number of times bot folded as big blind
            numBotFoldedSB=0,           // number of times bot folded as small blind
            numBotFoldedPreFlop=0,      // number of times bot folded pre flop
            numBotFolded=0,             // number of times bot folded
            numBotChecked,              // number of times bot checked this game
            numBotCalled=0,             // number of times bot calles this game
            numBotRaised=0,             // number of times bot raised this game
            numBotMoved=0;              // number of times bot moved this game
    
    public int gameID,handStatus,roundNo,lastRoundNo,numPlayersInHand,
            numPlayersInGame,numChecks=0,numRaises=0,numGames=0;
    public int numBotBluffs,numBotSemiBluffs,numBotCheckRaises;
    public float winProb,adjWinProb,pot,callAmt,smallBlindAmt,
            raiseAmt,potOdds,potPlusRaiseOdds, impliedPotOdds;
    public volatile String response,lastResponse="";
    public boolean webPlay=true,beenCheckedTo,beenRaisedTo,oppFolded,
            oppFoldedPreFlop,oppFoldedBB,oppFoldedSB,showdown,won;
    public boolean botFolded,botFoldedPreFlop,botFoldedBB,botFoldedSB;
    public PropertyResourceBundle configuration;
    public PokerClient client;
    public Cards holeCards,communityCards;
    public String botID,botName,oppID,oppName,smallBlind,bigBlind;
        
    public GenericBotEngine() {
        botName=toString().substring(toString().indexOf("@")-10,toString().indexOf("@"));
        botID=toString();
        init();
    }
    
    public GenericBotEngine(boolean webPlay) {
        if (webPlay) init("web");
    }
    
    public void init() {
        try {
            configuration=(PropertyResourceBundle)PropertyResourceBundle.getBundle("Bots.GenericBots.Configuration.RobotProperties");
            client= new PokerClient(this.getClass().toString());
            lastResponse="";
        } catch (java.net.ConnectException e) {
            System.err.println(" Could not connect to game server.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void init(String server) {
        try {
            configuration=(PropertyResourceBundle)PropertyResourceBundle.getBundle("Bots.GenericBots.Configuration.RobotProperties");
            client= new PokerClient( configuration.getString("hostName"), configuration.getString("hostPort"));
            lastResponse="";
        } catch (java.net.ConnectException e) {
            System.err.println(" Could not connect to game server.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void run() {
//        System.out.println(">>> "+botName+" started");
//        
//        boolean finished=false;
//        while (!finished) {
//            try {
//                connect();
//                finished=true;
//            } catch (Exception e) {
//                System.out.println("Cannot connect to Poker Server:");
//            }
//        }
//        
//        join();
//        try {
//            Thread.sleep(firstJoinDelay);
//        } catch (Exception e) {}
//        playLoop();
    }
    
    public void connect() throws Exception {
            System.out.println(XMLUtilities.getMessageBody(client.getWelcomeAndCookie()));
    }
    
    public void join() {
        try {
            String response=client.sendOperation("join","name="+botName);
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
        while (true) {
            try {
                if (debugLevel>0) System.out.println("PlayLoop");
                if (webPlay) startGame();
                numChecks=0;
                numRaises=0;
                playHand();
                Thread.sleep(joinDelay);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    public void playHand() {
        while (true) {
            try {
                if (debugLevel>0) System.out.println("playHand");
                response=client.sendOperation("getGameState");
                int handStatus=HAND_NOT_STARTED;
                if (response.contains("<handStatus>")) 
                    handStatus=Integer.parseInt(XMLUtilities.getTag(response,"<handStatus>"));
                if (handStatus==HAND_STARTED) {
                    if (debugLevel>0) System.out.println(">>> Hand started");
                    parseGameState();
                    if (response.contains("<turnInfo><player>"+botID)) {
                        if (debugLevel>0) System.out.println(">>> My turn");
                        sendDecision(makeDecision());
                    }
                } else if (handStatus==HAND_FINISHED) {
                    String msg=">>> Hand "+XMLUtilities.getTag(response,"<gameID>")+" finished at "+new java.util.Date()+" "+
                            botName+"'s balance:"+XMLUtilities.getTag(response,"<sessionID>"+botID+"</sessionID><name>"+botName+"<name/><balance>");
                    parseGameState();
                    gameOver();
                    System.out.println(msg);
                    //logger.log(msg);
                    break;
                }
                Thread.sleep(playDelay);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    public void sendDecision(int decision) throws Exception {
        if (decision==CALL) response=client.sendOperation("call");
        if (decision==RAISE) response=client.sendOperation("raise");
        if (decision==CHECKFOLD) response=client.sendOperation("checkfold");
    }
    
    public void parseGameState() throws Exception {
        //response=XMLUtilities.getTag(response,"<messageBody>");
        response=XMLUtilities.getTag(response,"<gameState>");
        
        if (!lastResponse.equals(response)) {
            if (debugLevel>1) System.out.println("response:"+response);
            lastResponse=response;
            
            // parse players
            oppID=XMLUtilities.getTag(response,"<sessionID>");
            oppName=XMLUtilities.getTag(response,"<name>");
            if (oppID.equals(botID)) {
                oppID=XMLUtilities.getSecondTag(response,"<sessionID>",2);
                oppName=XMLUtilities.getSecondTag(response,"<name>",2);
            }
            
            // parse game state
            gameID=Integer.parseInt(XMLUtilities.getTag(response,"<gameID>"));
            handStatus=Integer.parseInt(XMLUtilities.getTag(response,"<handStatus>"));
            lastRoundNo=roundNo;
            roundNo=Integer.parseInt(XMLUtilities.getTag(response,"<roundNo>"));
            smallBlindAmt=Float.parseFloat(XMLUtilities.getTag(response,"<smallBlindAmt>"));
            smallBlind=XMLUtilities.getTag(response,"<smallBlind>");
            bigBlind=XMLUtilities.getTag(response,"<bigBlind>");
            
            // parse and populate cards
            if (response.contains("<cardInfo>")) {
                pot=Float.parseFloat(XMLUtilities.getTag(response,"<pot>"));
                callAmt=Float.parseFloat(XMLUtilities.getTag(response,"<callAmount>"));
                holeCards=new Cards();
                communityCards=new Cards();
                if (roundNo>=DEAL) {
                    holeCards.add(new PokerCommons.PokerObjects.Cards.Card(XMLUtilities.getTag(response,"<card1>")));
                    holeCards.add(new PokerCommons.PokerObjects.Cards.Card(XMLUtilities.getTag(response,"<card2>")));
                    raiseAmt=smallBlindAmt*2;
                }
                if (roundNo>=FLOP) {
                    communityCards.add(new PokerCommons.PokerObjects.Cards.Card(XMLUtilities.getTag(response,"<card3>")));
                    communityCards.add(new PokerCommons.PokerObjects.Cards.Card(XMLUtilities.getTag(response,"<card4>")));
                    communityCards.add(new PokerCommons.PokerObjects.Cards.Card(XMLUtilities.getTag(response,"<card5>")));
                }
                if (roundNo>=TURN) {
                    raiseAmt=smallBlindAmt*4;
                    communityCards.add(new PokerCommons.PokerObjects.Cards.Card(XMLUtilities.getTag(response,"<card6>")));
                }
                if (roundNo>=RIVER) communityCards.add(new PokerCommons.PokerObjects.Cards.Card(XMLUtilities.getTag(response,"<card7>")));
             //   numPlayersInHand =Integer.parseInt(XMLUtilities.getTag(response,"<numPlayersInHand>"));
            }
            
            // parse last bet
            String bets=response.substring(response.indexOf("<turnHistory>"),response.indexOf("</turnHistory>"));
            beenCheckedTo=false;
            if (bets.contains("<check>") && !bets.contains("<bet>")) {
                beenCheckedTo=true;
                numRaises=0;
                numChecks++;
            }
            beenRaisedTo=false;
            if (bets.contains("<bet>") && callAmt!=0) {
                beenRaisedTo=true;
                numChecks=0;
                numRaises++;
            }
            
            // parse end of game statistics
            if (handStatus==HAND_FINISHED) {
                oppFolded=oppFoldedPreFlop=oppFoldedBB=oppFoldedSB=false;
                if (response.contains("<fold><player>"+oppID)) {
                    oppFolded=true;
                    if (lastRoundNo==DEAL) {
                        oppFoldedPreFlop=true;
                        if (bigBlind.equals(client.getSessionID())) oppFoldedSB=true;
                        else oppFoldedBB=true;
                    }
                }
                botFolded=botFoldedPreFlop=botFoldedBB=botFoldedSB=false;
                if (response.contains("<fold><player>"+botID)) {
                    botFolded=true;
                    if (lastRoundNo==DEAL) {
                        botFoldedPreFlop=true;
                        if (bigBlind.equals(client.getSessionID())) botFoldedSB=true;
                        else botFoldedBB=true;
                    }
                }
                showdown=won=false;
                if (response.contains("Showdown")) showdown=true;
                if (response.contains("Winner: "+botName)) won=true;
            }
        }
    }
    
    abstract public int makeDecision() throws Exception;
    
    abstract public void gameOver() throws Exception;
}