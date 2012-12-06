package Bots.NLHoldem.ProbabilityBots.Bots.AdvancedHeuristicsBots;

import Bots.NLHoldem.ProbabilityBots.Bots.PreFlopMatrixBots.MatrixBot10;
import Bots.NLHoldem.BasicBots.HeuristicsBots.BotParts.FlushAnalyzer;

public class FlushBot1 extends MatrixBot10  {
    
    public FlushBot1() throws Exception {
        
    }
    
    public int makeDecision() throws Exception {
        decision=CALL;
        potOdds=callAmt/(pot+callAmt);
        potPlusRaiseOdds=(callAmt+raiseAmt)/(pot+callAmt+raiseAmt*2);
        if (roundNo==DEAL) {
            decision=CHECKFOLD;
            useAdvancedHoleCardMatrix();
        } else {
            useFlopHeuristics();
            useFlushAnalzyer();
        }
        return decision;
    }
    
    public void useFlushAnalzyer() throws Exception {
        float[] results= FlushAnalyzer.getFlushProbabilities(holeCards,communityCards,numPlayersInHand);
        float pOfMeHavingFlush=results[0], pOfOthersHavingFlush=results[1];
        if (pOfMeHavingFlush>potPlusRaiseOdds && pOfMeHavingFlush>pOfOthersHavingFlush) decision=RAISE;
        if (pOfMeHavingFlush<potPlusRaiseOdds && pOfOthersHavingFlush>.5) decision=CHECKFOLD;
    }
}