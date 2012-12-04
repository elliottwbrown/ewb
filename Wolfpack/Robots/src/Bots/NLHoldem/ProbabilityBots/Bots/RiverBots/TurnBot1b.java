package Bots.NLHoldem.ProbabilityBots.Bots.RiverBots;

import Bots.NLHoldem.ProbabilityBots.BotParts.TurnAnalyzer.TurnAnalyzer;

public class TurnBot1b extends RiverBot1  {
    
    public TurnBot1b() throws Exception {
        super();
        TurnAnalyzer.init();
    }
            
    public int makeDecision() throws Exception {
        decision=CALL;
        potOdds=callAmt/(pot+callAmt);
        potPlusRaiseOdds=(callAmt+raiseAmt)/(pot+callAmt+raiseAmt*2);
        if (roundNo==DEAL) {
            decision=CHECKFOLD;
            useAdvancedHoleCardMatrix();
        } else if (roundNo==TURN) {
            decision=CHECKFOLD;
            useTurnAnalyzer();
        } else {
            useFlopHeuristics();
            useFlushAnalzyer();
        }
        return decision;
    }
    
    public void useTurnAnalyzer() throws Exception {
        float winProb= TurnAnalyzer.getWinProbabilities2(holeCards,communityCards,hca);
        if (winProb>potOdds) decision=CALL;
        if (winProb>potPlusRaiseOdds) decision=RAISE;
    }
}

