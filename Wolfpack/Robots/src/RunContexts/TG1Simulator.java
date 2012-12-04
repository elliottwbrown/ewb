package RunContexts;

import Analysis.GameVisualizer;
import java.util.ArrayList;
import java.util.TreeMap;
import com.ewb.Utilities.XMLUtilities;
import PokerCommons.PokerObjects.GameProtocols.TG1.GameInterface;
import PokerCommons.PokerObjects.GameProtocols.TG1.GameValues;
import PokerCommons.PokerObjects.Players.Player;
import Bots.TG1.GenericBots.GenericBot;

public class TG1Simulator implements GameValues {

    // settings
    static int debugLevel       = 0;
    static int numHandsPerRun   = 10*1000;
    static int numRunsToPlay    = 10;
    static boolean graphResults = true; // true or false;
    
    // used to capture and store source code of bots with results in WolfpackDB 
    static String TG1BotFolder  = "C:/data/dev/svn_wolfpack/dev/WolfPack/nb/Robots/src/Bots/TG1/";

    // wolfpack simulation packages
    static GenericBot[] bots;
    static GameVisualizer gv;
    static GameInterface game   = null;

    public static void playHeadsUpAgainstAll(GenericBot Player1,  ArrayList AllOthers) throws Exception {
        System.out.println("Starting matches at "+new java.util.Date());
        for (int t=0; t<AllOthers.size(); t++) {
            System.out.println("Starting Match "+(t+1));
            bots=null;
            GenericBot nextPlayer=(GenericBot) AllOthers.get(t);
            playHeadsUpMatch(Player1,nextPlayer);
        }
        System.out.println("Matches finished at "+new java.util.Date());
    }

    public static void playHeadsUpTournament(ArrayList AllPlayers) throws Exception {
        System.out.println("Starting matches at "+new java.util.Date());
        int m=0;
        for (int t=0; t<AllPlayers.size(); t++) {
            GenericBot Player1=(GenericBot) AllPlayers.get(t);
            GenericBot Player2;
            for (int c=t+1; c<AllPlayers.size(); c++) {
                System.out.println("Starting Match "+ ++m);
                bots=null;
                Player2=(GenericBot) AllPlayers.get(c);
                playHeadsUpMatch(Player1, Player2);
            }
        }
        System.out.println("Matches finished at "+new java.util.Date());
    }
    
    public static void playMultipleHeadsUpMatches(GenericBot Player1, GenericBot Player2,int n) throws Exception {
        for (int i=0;i<n;i++) {
            playHeadsUpMatch(Player1, Player2);
        }
    }
    
    public static void playHeadsUpMatch(GenericBot Player1, GenericBot Player2) throws Exception {
        GameInterface.debugLevel=debugLevel;
        GameInterface.players=new TreeMap();
        GameInterface.handStatus=HAND_NOT_STARTED;
        GameInterface.handID=0;
        bots = new GenericBot[2];
        bots[0]=Player1;
        bots[1]=Player2;
        startPlaying(bots);
        if (debugLevel>0)
            for (Player p : GameInterface.players.values())
                System.out.println(p.getName()+" "+p.balance+" "+(p.balance>p.startingChips?"<< Winner":""));
    }
    
    private static void startPlaying(final GenericBot[] bots) throws Exception {
        String response;
        boolean bothPlayersHaveChips=true;
        int c = 0;

        for (int i = 0; i < bots.length; i++) {
            response = GameInterface.join(bots[i].toString(), bots[i].botName, bots[i].sourceCode);
            bots[i].initVariablesForNewOpponent();
        }            
        if (graphResults) {
            gv=new GameVisualizer();
            gv.initCharts(bots[0].botName, bots[1].botName);
        }
        while (c++ < numHandsPerRun && bothPlayersHaveChips) {
            try {
                if (debugLevel>2) System.out.println( "Playing hand "+c);
                playHand(bots);
                if (graphResults) gv.updateBalanceChart(GameInterface.handID, bots[0].balance,bots[1].balance);
                if (graphResults) gv.updateRatesChart(GameInterface.handID, bots[0].rateHandsOppRaisedSB,bots[1].rateHandsOppRaisedSB);
            } catch (Exception e) {
                if ("Player Broke".equals(e.getLocalizedMessage())) break;
                else throw e;
            }
        }
    }

    private static void playHand(final GenericBot[] bots) throws NumberFormatException, Exception {
        String response;
        int decision=0;
        response = GameInterface.start("");
        int handStatus = HAND_NOT_STARTED;
        for (int i = 0; i < bots.length; i++)
                    bots[i].initVariablesForNewHand();
        while (handStatus != HAND_FINISHED) {
            response = GameInterface.getGameState("");
            if (response.contains("<handStatus>")) 
                handStatus = Integer.parseInt(XMLUtilities.getTag(response, "<handStatus>"));
            if (handStatus == HAND_STARTED) {
                for (int i = 0; i < bots.length; i++) {
                    response = GameInterface.getGameState(bots[i].toString());
                    bots[i].response = response;
                    bots[i].parseGameState();
                    if (response.contains("<turnInfo><player>" + bots[i])) {
                        decision = bots[i].makeDecision();
                        GameInterface.turn(decision, bots[i].toString());
                        break;
                    }
                }
            }
        }
        for (int i = 0; i < bots.length; i++) {
            response = GameInterface.getGameState(bots[i].toString());
            bots[i].response = response;
            bots[i].parseGameState();
            bots[i].parseEndOfGameState();
        }
    }

}
