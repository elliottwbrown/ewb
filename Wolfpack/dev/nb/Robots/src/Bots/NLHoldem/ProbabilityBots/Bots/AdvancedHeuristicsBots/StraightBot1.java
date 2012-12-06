package Bots.NLHoldem.ProbabilityBots.Bots.AdvancedHeuristicsBots;

import Bots.NLHoldem.BasicBots.HeuristicsBots.BotParts.StraightAnalyzer;

public class StraightBot1 extends FlushBot1  {
    
    public StraightBot1() throws Exception {

    }  
    
    @Override
    public int makeDecision() throws Exception {
        decision=CALL;
        potOdds=callAmt/(pot+callAmt);
        potPlusRaiseOdds=(callAmt+raiseAmt)/(pot+callAmt+raiseAmt*2);
        if (roundNo==DEAL) {
            decision=CHECKFOLD;
            useAdvancedHoleCardMatrix();
        } else {
            useFlopHeuristics();
            useStraightAnalyzer();
        }
        return decision;
    }

    public void useStraightAnalyzer() throws Exception {
        float[] straightResults= StraightAnalyzer.getStraightProbabilities(holeCards,communityCards,numPlayersInHand);
        float pOfMeHavingStraight=straightResults[0];
        if (pOfMeHavingStraight>potPlusRaiseOdds) decision=RAISE;
    }
    
}

