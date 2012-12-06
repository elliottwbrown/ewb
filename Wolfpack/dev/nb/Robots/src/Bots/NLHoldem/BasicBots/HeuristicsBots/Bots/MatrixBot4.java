package Bots.NLHoldem.BasicBots.HeuristicsBots.Bots;

import Bots.NLHoldem.GenericBots.GenericBot;
import Bots.NLHoldem.BasicBots.HeuristicsBots.BotParts.BasicAnalyzer;

public class MatrixBot4 extends GenericBot  {
    
    public void gameOver() throws Exception {

    }    
    
    public int makeDecision() throws Exception {
        int decision=CALL;
        if (roundNo==DEAL) {
            if (BasicAnalyzer.containsPair(holeCards)) decision=RAISE;
        } else {
            if (BasicAnalyzer.containsTrip(holeCards,communityCards)) decision=RAISE;
            if (BasicAnalyzer.containsFlush(holeCards,communityCards)) decision=RAISE;
            if (BasicAnalyzer.containsStrait(holeCards,communityCards)) decision=RAISE;
        }
        return decision;
    }
}
