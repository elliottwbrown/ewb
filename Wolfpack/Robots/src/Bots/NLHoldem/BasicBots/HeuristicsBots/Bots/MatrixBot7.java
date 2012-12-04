package Bots.NLHoldem.BasicBots.HeuristicsBots.Bots;

import Bots.NLHoldem.BasicBots.HeuristicsBots.BotParts.BasicAnalyzer;
import Bots.NLHoldem.GenericBots.GenericBot;

public class MatrixBot7 extends GenericBot  {
    
    public void gameOver() throws Exception {

    }    
    
    public int makeDecision() throws Exception {
        if (roundNo==DEAL) makePreFlopDecision();
        else makePostFlopDecision();
        return decision;
    }

    public void makePreFlopDecision() throws Exception {
        decision=CALL;
        if (BasicAnalyzer.containsPair(holeCards)) decision=RAISE;
        if (BasicAnalyzer.containsAceAndHigh(holeCards)) decision=RAISE;
        if (BasicAnalyzer.containsKingAndHigh(holeCards)) decision=RAISE;
    }
    
    public void makePostFlopDecision() throws Exception {
        decision=CHECKFOLD;        
        if (BasicAnalyzer.containsPair(holeCards)) decision=RAISE;
        if (BasicAnalyzer.containsPrivatePair(holeCards,communityCards)) decision=RAISE;
        if (BasicAnalyzer.containsFlush(holeCards,communityCards)) decision=RAISE;
        if (BasicAnalyzer.containsStrait(holeCards,communityCards)) decision=RAISE;
    }

}