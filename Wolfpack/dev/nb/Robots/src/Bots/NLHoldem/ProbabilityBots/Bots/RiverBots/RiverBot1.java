package Bots.NLHoldem.ProbabilityBots.Bots.RiverBots;

import Bots.NLHoldem.ProbabilityBots.BotParts.RiverAnalyzer.RiverAnalyzer;
import Bots.NLHoldem.ProbabilityBots.Bots.AdvancedHeuristicsBots.FlushBot1;

public class RiverBot1 extends FlushBot1  {
    
    public RiverBot1() throws Exception {
        super();
        RiverAnalyzer.init();
    }
    
    public int makeDecision() throws Exception {
        decision=CALL;
        potOdds=callAmt/(pot+callAmt);
        potPlusRaiseOdds=(callAmt+raiseAmt)/(pot+callAmt+raiseAmt*2);
        if (roundNo==DEAL) {
            decision=CHECKFOLD;
            useAdvancedHoleCardMatrix();
        } else if (roundNo==RIVER) {
            decision=CHECKFOLD;
            useRiverAnalyzer();
        } else {
            useFlopHeuristics();
            useFlushAnalzyer();
        }
        return decision;
    }
    
    public void useRiverAnalyzer() throws Exception {
        float winProb= RiverAnalyzer.getWinProbabilities(holeCards,communityCards,numPlayersInHand,hca);
        if (winProb>potOdds) decision=CALL;
        if (winProb>potPlusRaiseOdds) decision=RAISE;
    }
}

