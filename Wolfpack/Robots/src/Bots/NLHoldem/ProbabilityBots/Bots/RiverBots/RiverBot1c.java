package Bots.NLHoldem.ProbabilityBots.Bots.RiverBots;

import Bots.NLHoldem.ProbabilityBots.BotParts.HoleCardsAnalyzer.HoleCardsAnalyzer;
import Bots.NLHoldem.ProbabilityBots.BotParts.RiverAnalyzer.RiverAnalyzer;
import Bots.NLHoldem.ProbabilityBots.Bots.AdvancedHeuristicsBots.FlushBot1;

public class RiverBot1c extends FlushBot1  {
    
    private int c=0;
    HoleCardsAnalyzer hca=new HoleCardsAnalyzer();
    
    public RiverBot1c() throws Exception {
        super();
        try {
            hca.init();
            RiverAnalyzer.init();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
    
    public int makeDecision() throws Exception {
        decision=CALL;
        potOdds=callAmt/(pot+callAmt);
        potPlusRaiseOdds=(callAmt+raiseAmt)/(pot+callAmt+raiseAmt*2);
        if (roundNo==DEAL) {
            //decision=CHECKFOLD;
            //useAdvancedHoleCardMatrix();
        } else if (roundNo==RIVER) {
            //decision=CHECKFOLD;            
            useRiverAnalyzer();
        } else {
            //useFlopHeuristics();
            //useFlushAnalzyer();
        }
        return decision;
    }
    
    public void useRiverAnalyzer() throws Exception {
        float winProb= RiverAnalyzer.getWinProbabilities(holeCards,communityCards,numPlayersInHand,hca);
        if (winProb>=.6) decision=RAISE;
    }    
}

