package Bots.NLHoldem.BasicBots.HeuristicsBots.Bots;

import Bots.NLHoldem.BasicBots.HeuristicsBots.BotParts.BasicAnalyzer;

public class MatrixBot8 extends MatrixBot7  {
    
    public void makePreFlopDecision() throws Exception {
        decision=CALL;
        if (BasicAnalyzer.containsPair(holeCards)) decision=RAISE;
        if (BasicAnalyzer.containsAceAndHigh(holeCards)) decision=RAISE;
        if (beenCheckedTo||beenRaisedTo) {
            if (beenCheckedTo) {
                if (BasicAnalyzer.containsKing(holeCards)) decision=RAISE;
                if (BasicAnalyzer.containsFace(holeCards,10)) decision=RAISE;
            } else {
                decision=CHECKFOLD;
                if (BasicAnalyzer.containsPair(holeCards)) decision=RAISE;
                if (BasicAnalyzer.containsKing(holeCards)) decision=CALL;
            }
        } else {
            if (BasicAnalyzer.containsPair(holeCards)) decision=RAISE;
            if (BasicAnalyzer.containsKing(holeCards)) decision=RAISE;
            if (BasicAnalyzer.containsFace(holeCards,10)) decision=RAISE;
        }
    }
    
}