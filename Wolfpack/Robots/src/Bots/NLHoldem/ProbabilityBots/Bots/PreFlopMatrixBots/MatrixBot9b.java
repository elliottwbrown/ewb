package Bots.NLHoldem.ProbabilityBots.Bots.PreFlopMatrixBots;

import Bots.NLHoldem.GenericBots.GenericBot;
import Bots.NLHoldem.BasicBots.HeuristicsBots.BotParts.BasicAnalyzer;
import Bots.NLHoldem.ProbabilityBots.BotParts.HoleCardsAnalyzer.HoleCardsAnalyzer;

public class MatrixBot9b extends MatrixBot9  {
    
    public MatrixBot9b() throws Exception {
        
    }  
    
    public int makeDecision() throws Exception {
        decision=CALL;
        potOdds=callAmt/(pot+callAmt);
        potPlusRaiseOdds=(callAmt+raiseAmt)/(pot+callAmt+raiseAmt*2);
        if (roundNo==DEAL) {
            decision=CHECKFOLD;
            useHoleCardMatrix(potOdds, potPlusRaiseOdds);
        } else useFlopHeuristics();
        return decision;
    }
}

