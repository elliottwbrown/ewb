package Bots.NLHoldem.RespectBots;

import Bots.NLHoldem.GenericBots.GenericBot;
import Bots.NLHoldem.ProbabilityBots.BotParts.HeldCardsAnalyzer.HeldCardAnalyzerDAO;
import Bots.NLHoldem.ProbabilityBots.BotParts.HoleCardsAnalyzer.HoleCardsAnalyzer;
import Bots.NLHoldem.ProbabilityBots.BotParts.MarvFlopAnalyzer.FlopAnalyzer;
import Bots.NLHoldem.ProbabilityBots.BotParts.RiverAnalyzer.RiverAnalyzer;
import Bots.NLHoldem.ProbabilityBots.BotParts.TurnAnalyzer.TurnAnalyzer;

public class RespectBotEngine extends GenericBot  {
    
    public static final boolean checkRaiseOn=true;
    public static final String gameLogFile="c:\\temp\\RespectBot.log";
    public int lastGameID=-1;
    public double r=0;
    public float bluff1=.1f,bluff2=.25f,bluff3=.45f,
            checkProbAdjFactor=1.2f,raiseProbAdjFactor=.9f;
    public boolean checkRaised=false;
    public com.ewb.Logging.Logger gameLog;
    public HoleCardsAnalyzer hca=new HoleCardsAnalyzer();
    public HeldCardAnalyzerDAO heca=new HeldCardAnalyzerDAO();
    
    public RespectBotEngine() {
    }
    
    public void init() {
        System.out.println(" instantiating RespectBt");
        try {
            gameLog = new com.ewb.Logging.Logger(gameLogFile);
            hca.init();
            heca.init();
            FlopAnalyzer.init();
            TurnAnalyzer.init();
            RiverAnalyzer.init();
            if (debugLevel > 0) {
                gameLog.log("instantiating RespectBot" + "\n");
                gameLog.log("  checkRaiseOn=" + checkRaiseOn + "\n");
                gameLog.log("  bluff1=" + bluff1 + "\n");
                gameLog.log("  bluff2=" + bluff2 + "\n");
                gameLog.log("  bluff3=" + bluff3 + "\n");
                gameLog.log("  checkProbAdjFactor=" + checkProbAdjFactor + "\n");
                gameLog.log("  raiseProbAdjFactor=" + raiseProbAdjFactor + "\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
        System.out.println(" finished instantiating RespectBt");
    }
    
    public int makeDecision() throws Exception {
        calculatePotOdds();
        calculateWinProb();
        adjustWinProbForRespect();
        getPureOddsDecision();
        considerBluffing();
        if (lastGameID!=gameID) startNewHand();
        logInGameData();
        if (winProb>1 || winProb<0) throw new Exception("winProb problem:"+winProb);
        return decision;
    }

    public void logInGameData() {
        if (debugLevel>0) {
            gameLog.log(" Round no. "+roundNo+"\n");
            gameLog.log("  Cards "+holeCards.print()+" "+communityCards.print()+"\n");
            gameLog.log("  Pot "+pot+"\n");
            gameLog.log("  potPlusCallOdds= "+potOdds+"\n");
            gameLog.log("  potPlusRaiseOdds= "+potPlusRaiseOdds+"\n");
            gameLog.log("  impliedPotOdds= "+impliedPotOdds+"\n");
            gameLog.log("  rawWinProb= "+winProb+"\n");
            gameLog.log("  adjWinProb= "+adjWinProb+"\n");
            gameLog.log("  callAmt="+callAmt+"\n");
            gameLog.log("  numChecks="+numChecks+"\n");
            gameLog.log("  numRaises="+numRaises+"\n");
            gameLog.log("  beenCheckedTo="+beenCheckedTo+"\n");
            gameLog.log("  beenRaisedTo="+beenRaisedTo+"\n");
            gameLog.log("  decision= "+decision+"\n");
        }
    }
    
    public void startNewHand() {
        lastGameID=gameID;
        r=Math.random();
        checkRaised=false;
        if (debugLevel>0) gameLog.log("Hand no. "+gameID+"\n");
        if (debugLevel>0) gameLog.log(" r= "+r+"\n");
    }
    
    public void getPureOddsDecision() {
        decision=CHECKFOLD;
        if (adjWinProb>potOdds) decision=CALL;
        if (adjWinProb>impliedPotOdds) decision=RAISE;
    }
    
    public void calculatePotOdds() {
        potOdds=callAmt/(pot+callAmt);
        impliedPotOdds=.5f;
    }
    
    public void calculateWinProb() throws Exception {
        if (roundNo==DEAL) winProb=hca.getWinProbabilty(holeCards,numPlayersInHand);
        else if (roundNo==FLOP) winProb= FlopAnalyzer.getWinProbabilities(holeCards,communityCards,numPlayersInHand);
        else if (roundNo==TURN) winProb= TurnAnalyzer.getWinProbabilities(holeCards,communityCards,heca);
        else if (roundNo==RIVER) winProb= RiverAnalyzer.getWinProbabilities(holeCards,communityCards,heca);
    }
    
    public void adjustWinProbForRespect() {
        adjWinProb=winProb;
        if (beenRaisedTo) for (int i=0; i<numRaises;i++) adjWinProb=Math.max(0,winProb*raiseProbAdjFactor);
        if (beenCheckedTo) for (int i=0; i<numChecks;i++) adjWinProb=Math.min(1,winProb*checkProbAdjFactor);
    }
    
    public void considerBluffing() {
//        if (callAmt==0 && numChecks>0 && decision==CHECKFOLD) {  // RAISE instead of FOLD
//            if ((numChecks>0 && r<bluff1) || (numChecks>1 && r<bluff2) || (numChecks>2 && r<bluff3)) {
//                decision=RAISE;
//                if (debugLevel>0) gameLog.log("  BLUFF"+"\n");
//            }
//        }
        if (callAmt==0 && numChecks>0 && decision==CALL) {       // RAISE instead of CALL
            if ((numChecks>0 && r<bluff1) || (numChecks>1 && r<bluff2) || (numChecks>2 && r<bluff3)) {
                decision=RAISE;
                if (debugLevel>0) gameLog.log("  BLUFF"+"\n");
            }
        }
        if (checkRaiseOn) {                                      // CHECK instead of RAISE
            if (winProb>.8 && r<.1 && decision==RAISE) {
                if (checkRaised=false) {
                    decision=CALL;
                    checkRaised=true;
                    if (debugLevel>0) gameLog.log("  CHECKRAISE"+"\n");
                }
            }
        }
    }
    
    public void gameOver() throws Exception {};   
    
}