package Bots.NLHoldem.BasicBots.HeuristicsBots.Bots;

import Bots.NLHoldem.GenericBots.GenericBot;
import Bots.NLHoldem.BasicBots.HeuristicsBots.BotParts.BasicAnalyzer;

public class MatrixBot6 extends GenericBot {
    
    public void gameOver() throws Exception {

    }    
    
    public int makeDecision() throws Exception {
        int decision=CHECKFOLD;
        if (roundNo==DEAL) {
            decision=CALL;
            if (BasicAnalyzer.containsPair(holeCards)) decision=RAISE;
            if (BasicAnalyzer.containsAce(holeCards)) decision=RAISE;
            if (BasicAnalyzer.containsKing(holeCards)) decision=RAISE;
        } else {
            if (BasicAnalyzer.containsPair(holeCards,communityCards)) decision=RAISE;
            if (BasicAnalyzer.containsFlush(holeCards,communityCards)) decision=RAISE;
            if (BasicAnalyzer.containsStrait(holeCards,communityCards)) decision=RAISE;
        }
        return decision;
    }
}
