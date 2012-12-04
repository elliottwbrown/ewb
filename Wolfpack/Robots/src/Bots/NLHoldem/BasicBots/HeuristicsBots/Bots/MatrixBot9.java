package Bots.NLHoldem.BasicBots.HeuristicsBots.Bots;

import Bots.NLHoldem.BasicBots.HeuristicsBots.BotParts.BasicAnalyzer;

public class MatrixBot9 extends MatrixBot8  {
    
    @Override
    public void makePostFlopDecision() throws Exception {
        if (beenCheckedTo||beenRaisedTo) {
            if (beenCheckedTo) {
                decision=CHECKFOLD;        
                //if (BasicAnalyzer.containsPair(holeCards)) decision=RAISE;
                if (BasicAnalyzer.containsPrivatePair(holeCards,communityCards)) decision=RAISE;
                if (BasicAnalyzer.containsFlush(holeCards,communityCards)) decision=RAISE;
                if (BasicAnalyzer.containsStrait(holeCards,communityCards)) decision=RAISE;
            } else {
                decision=CHECKFOLD;        
                //if (BasicAnalyzer.containsPair(holeCards)) decision=RAISE;
                if (BasicAnalyzer.containsPrivatePair(holeCards,communityCards)) decision=CALL;
                if (BasicAnalyzer.containsFlush(holeCards,communityCards)) decision=RAISE;
                if (BasicAnalyzer.containsStrait(holeCards,communityCards)) decision=RAISE;
            }
        } else {
            decision=CHECKFOLD;        
            //if (BasicAnalyzer.containsPair(holeCards)) decision=RAISE;
            if (BasicAnalyzer.containsPrivatePair(holeCards,communityCards)) decision=RAISE;
            if (BasicAnalyzer.containsFlush(holeCards,communityCards)) decision=RAISE;
            if (BasicAnalyzer.containsStrait(holeCards,communityCards)) decision=RAISE;
        }        
    }    
}