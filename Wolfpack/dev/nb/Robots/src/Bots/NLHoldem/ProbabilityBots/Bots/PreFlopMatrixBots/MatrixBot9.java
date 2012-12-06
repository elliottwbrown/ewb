package Bots.NLHoldem.ProbabilityBots.Bots.PreFlopMatrixBots;

public class MatrixBot9 extends MatrixBot8 {
    
    public MatrixBot9() throws Exception {
        
    }
    
    public int makeDecision() throws Exception {
        decision=CALL;
        potOdds=callAmt/(pot+callAmt);
        potPlusRaiseOdds=(callAmt+raiseAmt)/(pot+callAmt+raiseAmt*2);
        if (roundNo==DEAL) useHoleCardMatrix(potOdds, potPlusRaiseOdds);
        else useFlopHeuristics();
        return decision;
    }
}

