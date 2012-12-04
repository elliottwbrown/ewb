package Bots.NLHoldem.MemoryBots;

import java.util.Calendar;

import com.ewb.FileSystem.FileAccess;
import com.ewb.Utilities.XMLUtilities;
import Bots.NLHoldem.GenericBots.GenericBot;
import Bots.NLHoldem.ProbabilityBots.BotParts.HeldCardsAnalyzer.HeldCardAnalyzerDAO;
import Bots.NLHoldem.ProbabilityBots.BotParts.HoleCardsAnalyzer.HoleCardsAnalyzer;
import Bots.NLHoldem.ProbabilityBots.BotParts.MarvFlopAnalyzer.FlopAnalyzer;
import Bots.NLHoldem.ProbabilityBots.BotParts.RiverAnalyzer.RiverAnalyzer;
import Bots.NLHoldem.ProbabilityBots.BotParts.TurnAnalyzer.TurnAnalyzer;
import PokerCommons.Client.PokerClient;

public class MemoryBotMod extends GenericBot  {
    
    public float bluff1,bluff2,bluff3,checkProbAdjFactor,raiseProbAdjFactor;
    public static boolean checkRaiseOn,bluffingOn;
    //public com.ewb.Logging.Logger gameLog,playerLog;
    //public com.ewb.Logging.Logger playerLog;
    public HoleCardsAnalyzer hca=new HoleCardsAnalyzer();
    public HeldCardAnalyzerDAO heca=new HeldCardAnalyzerDAO();
    public int lastGameID=-1;
    public double r=0;
    public boolean checkRaised=false;
    public static Calendar now= Calendar.getInstance();
    public static String startTime=now.getTimeInMillis()+"";
    public static String logDir="c:\\temp\\";
    //public static String gameLogFile="c:\\temp\\MemoryBot1.log";
    public static String configFile="C:/data/dev/svn_wolfpack/dev/WolfPack/nb/Robots/src/Bots/MemoryBots/Configuration/botVars_aggressive.cfg";   
    
    public MemoryBotMod() throws Exception {
        initMemoryBot("localhost",configFile);
    }

    public MemoryBotMod(String SessionID) throws Exception {
        initMemoryBot("simulation",configFile);
    }  
        
    public int makeDecision() throws Exception {
        if (lastGameID != gameID) startNewHand();
        calculatePotOdds();
        calculateImpliedPotOdds();
        calculateWinProb();
        adjustWinProbForRespect();
        getPureOddsDecision();
        considerBluffing();
        //logInGameData();
        //if (winProb>1 || winProb<0) throw new Exception("winProb problem:"+winProb);
        return decision;
    }

    public void initMemoryBot(final String server, final String configFile) throws Exception {
        System.out.println("initMemoryBot "+client);
        try {
            client = new PokerClient(server, "8080");
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
//            gameLog=new com.ewb.Logging.Logger(gameLogFile);
            hca.init();
            heca.init();
            FlopAnalyzer.init();
            TurnAnalyzer.init();
            RiverAnalyzer.init();
            
            // read values from file
            FileAccess fa=new FileAccess();
            fa.openForRead(configFile);
            bluff1=Float.parseFloat(fa.readLine());
            bluff2=Float.parseFloat(fa.readLine());
            bluff3=Float.parseFloat(fa.readLine());
            checkProbAdjFactor=Float.parseFloat(fa.readLine());
            raiseProbAdjFactor=Float.parseFloat(fa.readLine());
            checkRaiseOn=fa.readLine().startsWith("true");
            bluffingOn=fa.readLine().startsWith("true");
            
            if (debugLevel>0) {
//                gameLog.log("instantiating MemoryBot1"+"\n");
//                gameLog.log("  bluff1="+bluff1+"\n");
//                gameLog.log("  bluff2="+bluff2+"\n");
//                gameLog.log("  bluff3="+bluff3+"\n");
//                gameLog.log("  checkProbAdjFactor="+checkProbAdjFactor+"\n");
//                gameLog.log("  raiseProbAdjFactor="+raiseProbAdjFactor+"\n");
//                gameLog.log("  checkRaiseOn="+checkRaiseOn+"\n");
//                gameLog.log("  bluffingOn="+bluffingOn+"\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
        System.out.println(" finished instantiating MemoryBot1");
    }
    
    public void logInGameData() {
        if (debugLevel>0) {
//            gameLog.log("Hand no. "+gameID+"\n");
//            gameLog.log(" Round no. "+roundNo+"\n");
//            gameLog.log("  Cards "+holeCards.print()+" "+communityCards.print()+"\n");
//            gameLog.log("  rawWinProb= "+winProb+"\n");
//            if (adjWinProb!=winProb) gameLog.log("  adjWinProb= "+adjWinProb+"\n");
//            gameLog.log("  potPlusCallOdds= "+potOdds+"\n");
//            gameLog.log("  potPlusRaiseOdds= "+potPlusRaiseOdds+"\n");
//            gameLog.log("  impliedPotOdds= "+impliedPotOdds+"\n");
//            gameLog.log("  Pot "+pot+"\n");
//            gameLog.log("  callAmt="+callAmt+"\n");
//            gameLog.log("  numChecks="+numChecks+"\n");
//            gameLog.log("  numRaises="+numRaises+"\n");
//            if (beenCheckedTo) gameLog.log("  beenCheckedTo\n");
//            if (beenRaisedTo) gameLog.log("  beenRaisedTo\n");
//            gameLog.log("  finalDecision = "+actionLabels[decision]+"\n");
        }
    }
    
    public void startNewHand() {
        lastGameID=gameID;
        r=Math.random();
        checkRaised=false;
        numChecks=0;
        numRaises=0;
    }
    
    public void getPureOddsDecision() {
        decision=CHECKFOLD;
        if (adjWinProb>potOdds) decision=CALL;
        if (adjWinProb>impliedPotOdds) decision=RAISE;
    }    
//    
    public void calculatePotOdds() {
        potOdds=callAmt/pot;
    }
    
    public void calculateImpliedPotOdds() {
        impliedPotOdds=.5f;
    }    
    
    public void calculateWinProb() throws Exception {
        if (roundNo==DEAL) winProb=hca.getWinProbabilty(holeCards,numPlayersInHand);
        else if (roundNo==FLOP) winProb= FlopAnalyzer.getWinProbabilities(holeCards,communityCards,numPlayersInHand);
        else if (roundNo==TURN) winProb= TurnAnalyzer.getWinProbabilities(holeCards,communityCards,heca);
        else if (roundNo==RIVER) winProb= RiverAnalyzer.getWinProbabilities(holeCards,communityCards,heca);
        adjWinProb=winProb;
    }

    public void adjustWinProbForRespect() {
        adjWinProb=winProb;
        if (beenRaisedTo) {
            adjWinProb=Math.max(0,winProb*raiseProbAdjFactor);
            for (int i=0; i<numRaises-1;i++) adjWinProb=Math.max(0,adjWinProb*raiseProbAdjFactor);
        }
        if (beenCheckedTo) {
            adjWinProb=Math.min(1,winProb*checkProbAdjFactor);
            for (int i=0; i<numChecks-1;i++) adjWinProb=Math.min(1,adjWinProb*checkProbAdjFactor);
        }
    }    
    
    public void considerBluffing() {
        
        /*
         * note: these bluffs occur only after we've been checked to
         * is this useful numChecks - or do we need numChecksCurrentRound too ?
         * should we consider bluffing out of position ?
         * should we consider bluffing after a bet aka going over the top ?
         */
        
        // RAISE instead of FOLD
        if (bluffingOn) {
            if (callAmt==0 && numChecks>0 && decision==CHECKFOLD) {
                if ((numChecks>0 && r<bluff1) || (numChecks>1 && r<bluff2) || (numChecks>2 && r<bluff3)) {
                    decision=RAISE;
//                    if (debugLevel>0) gameLog.log("  BLUFF"+"\n");
                    numBotBluffs++;
                }
            }
        }
        // RAISE instead of CALL
        if (callAmt==0 && numChecks>0 && decision==CALL) {
            if ((numChecks>0 && r<bluff1) || (numChecks>1 && r<bluff2) || (numChecks>2 && r<bluff3)) {
                decision=RAISE;
//                if (debugLevel>0) gameLog.log("  SEMIBLUFF"+"\n");
                numBotSemiBluffs++;
            }
        }
        // CHECK instead of RAISE
        if (checkRaiseOn) {
            if (adjWinProb>.8 && r<.1 && decision==RAISE) {
                if (!checkRaised) {
                    decision=CALL;
                    checkRaised=true;
//                    if (debugLevel>0) gameLog.log("  CHECKRAISE"+"\n");
                    numBotCheckRaises++;
                }
            }
        }
    }
    
    public void gameOver() throws Exception {
        numGames++;
        String roundHistory=XMLUtilities.getTag(response,"<roundHistory>");
        if (showdown) numShowdowns++;
        if (showdown && won) numShowdownsWon++;
        if (won) numOppLost++;
        if (oppFoldedPreFlop) numOppFoldedPreFlop++;
        if (botFoldedPreFlop) numBotFoldedPreFlop++;
        if (botFoldedSB) numBotFoldedSB++;
        if (oppFoldedSB) numOppFoldedSB++;
        if (oppFoldedBB) numOppFoldedBB++;
        if (botFoldedBB) numBotFoldedBB++;
        int numOppRaisedThisGame=XMLUtilities.countOccurences(roundHistory,"<bet><player/>"+oppID);
        int numOppCalledThisGame=XMLUtilities.countOccurences(roundHistory,"<call><player/>"+oppID);
        int numOppFoldedThisGame=XMLUtilities.countOccurences(roundHistory,"<fold><player/>"+oppID);
        int numOppCheckedThisGame=XMLUtilities.countOccurences(roundHistory,"<check><player/>"+oppID);
        int numBotRaisedThisGame=XMLUtilities.countOccurences(roundHistory,"<bet><player/>"+botID);
        int numBotCalledThisGame=XMLUtilities.countOccurences(roundHistory,"<call><player/>"+botID);
        int numBotFoldedThisGame=XMLUtilities.countOccurences(roundHistory,"<fold><player/>"+botID);
        int numBotCheckedThisGame=XMLUtilities.countOccurences(roundHistory,"<check><player/>"+botID);
        int numOppMovedThisGame=numOppRaisedThisGame+numOppCalledThisGame+numOppFoldedThisGame+numOppCheckedThisGame;
        int numBotMovedThisGame=numBotRaisedThisGame+numBotCalledThisGame+numBotFoldedThisGame+numBotCheckedThisGame;
        numBotRaised+=numBotRaisedThisGame;
        numBotCalled+=numBotCalledThisGame;
        numBotFolded+=numBotFoldedThisGame;
        numBotChecked+=numBotCheckedThisGame;
        numBotMoved+=numBotMovedThisGame;
        numOppRaised+=numOppRaisedThisGame;
        numOppCalled+=numOppCalledThisGame;
        numOppFolded+=numOppFoldedThisGame;
        numOppChecked+=numOppCheckedThisGame;
        numOppMoved+=numOppMovedThisGame;
        
//        gameLog.log("Hand no. "+gameID+" is over.\n");
//        if (oppFolded) gameLog.log(" Opponent folded\n");
//        gameLog.log(" r= "+r+"\n");
//        gameLog.log(" showdown="+showdown+"\n");
//        gameLog.log(" won="+won+"\n");
        
        // before writing to file - is there one there already ?
        // is it from this session or a previous session ?
        // if from a previous session - incoporate the data
        
//        playerLog=new com.ewb.Logging.Logger(logDir+"\\"+oppName+".log");
//        playerLog.log(" numGames="+numGames+"\n");
//        playerLog.log(" numShowdowns="+numShowdowns+"\n");
//        playerLog.log(" numOppLost="+numOppLost+"\n");
//        playerLog.log(" numOppShowdownsLost="+numShowdownsWon+"\n");
//        playerLog.log(" numOppMoved="+numOppMoved+"\n");
//        playerLog.log(" numOppChecked="+numOppChecked+"\n");
//        playerLog.log(" numOppCalled="+numOppCalled+"\n");
//        playerLog.log(" numOppRaised="+numOppRaised+"\n");
//        playerLog.log(" numOppFolded="+numOppFolded+"\n");
//        playerLog.log(" numOppFoldedPreFlop="+numOppFoldedPreFlop+"\n");
//        playerLog.log(" numOppFoldedBB="+numOppFoldedBB+"\n");
//        playerLog.log(" numOppFoldedSB="+numOppFoldedSB+"\n");
//        playerLog.log(" numBotMoved="+numBotMoved+"\n");
//        playerLog.log(" numBotChecked="+numBotChecked+"\n");
//        playerLog.log(" numBotCalled="+numBotCalled+"\n");
//        playerLog.log(" numBotRaised="+numBotRaised+"\n");
//        playerLog.log(" numBotFolded="+numBotFolded+"\n");
//        playerLog.log(" numBotFoldedPreFlop="+numBotFoldedPreFlop+"\n");
//        playerLog.log(" numBotFoldedBB="+numBotFoldedBB+"\n");
//        playerLog.log(" numBotFoldedSB="+numBotFoldedSB+"\n");
//        playerLog.log(" numBotBluffs="+numBotBluffs+"\n");
//        playerLog.log(" numBotSemiBluffs="+numBotSemiBluffs+"\n");
//        playerLog.log(" numBotCheckRaises="+numBotCheckRaises+"\n");
    };

}