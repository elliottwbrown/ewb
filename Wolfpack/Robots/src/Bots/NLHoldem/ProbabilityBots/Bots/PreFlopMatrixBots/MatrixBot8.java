package Bots.NLHoldem.ProbabilityBots.Bots.PreFlopMatrixBots;

import Bots.NLHoldem.GenericBots.GenericBot;
import Bots.NLHoldem.ProbabilityBots.BotParts.HoleCardsAnalyzer.HoleCardsAnalyzer;
import Bots.NLHoldem.BasicBots.HeuristicsBots.BotParts.BasicAnalyzer;

public class MatrixBot8 extends GenericBot  {
    
    public HoleCardsAnalyzer hca=new HoleCardsAnalyzer();
    
    public MatrixBot8() throws Exception {
        hca.init();
    }
    
    public void gameOver() throws Exception {

    }    
    
    public int makeDecision() throws Exception {
        int decision=CALL;
        float potOdds=callAmt/pot;
        if (roundNo==DEAL) useHoleCardMatrix(potOdds, potPlusRaiseOdds);
        else useFlopHeuristics();
        return decision;
    }
    
    public void useFlopHeuristics() throws Exception {
        if (BasicAnalyzer.containsPair(holeCards) ||
                BasicAnalyzer.containsPrivatePair(holeCards,communityCards) ||
                BasicAnalyzer.containsFlush(holeCards,communityCards) ||
                BasicAnalyzer.containsStrait(holeCards,communityCards)) decision=RAISE;
    }
    
    public void useHoleCardMatrix(final float potPlusCallOdds, final float potPlusRaiseOdds) {
        float winProb=hca.getWinProbabilty(holeCards);
        if (winProb>potPlusCallOdds) decision=CALL;
        if (winProb>potPlusRaiseOdds) decision=RAISE;
    }
    
    
}

