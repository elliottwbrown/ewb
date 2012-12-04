package Bots.NLHoldem.BasicBots.HeuristicsBots.Bots;

import Bots.NLHoldem.BasicBots.HeuristicsBots.BotParts.BasicAnalyzer;

public class MatrixBot10 extends MatrixBot9  {
    
    public void gameOver() throws Exception {

    }    
    
    public void makePreFlopDecision() throws Exception {
        if (beenCheckedTo||beenRaisedTo) {
            if (beenCheckedTo) {
                decision=CHECKFOLD;
                if (BasicAnalyzer.containsPair(holeCards)) decision=RAISE;
                if (BasicAnalyzer.containsAceAndHigh(holeCards)) decision=RAISE;
                if (BasicAnalyzer.containsKingAndHigh(holeCards)) decision=RAISE;
            } else {
                decision=CHECKFOLD;
                if (BasicAnalyzer.containsPair(holeCards)) decision=RAISE;
                if (BasicAnalyzer.containsAceAndHigh(holeCards)) decision=CALL;
                //if (BasicAnalyzer.containsKingAndHigh(holeCards)) decision=CALL;
            }
        } else {
            decision=CALL;
            if (BasicAnalyzer.containsPair(holeCards)) decision=RAISE;
            if (BasicAnalyzer.containsAceAndHigh(holeCards)) decision=RAISE;
            //if (BasicAnalyzer.containsKingAndHigh(holeCards)) decision=RAISE;
        }
    }

}