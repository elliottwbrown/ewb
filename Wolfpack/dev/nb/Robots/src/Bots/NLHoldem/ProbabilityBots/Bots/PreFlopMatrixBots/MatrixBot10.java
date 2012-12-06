package Bots.NLHoldem.ProbabilityBots.Bots.PreFlopMatrixBots;

public class MatrixBot10 extends MatrixBot9  {
    
    public MatrixBot10() throws Exception {
        
    }
    
    public int makeDecision() throws Exception {
        decision=CALL;
        potOdds=callAmt/(pot+callAmt);
        potPlusRaiseOdds=(callAmt+raiseAmt)/(pot+callAmt+raiseAmt*2);
        if (roundNo==DEAL) {
            decision=CHECKFOLD;
            useAdvancedHoleCardMatrix();
        } else useFlopHeuristics();
        return decision;
    }
    
    public void useAdvancedHoleCardMatrix() {
        float winProb=hca.getWinProbabilty(holeCards,numPlayersInHand);
        if (winProb>potOdds) decision=CALL;
        if (winProb>potPlusRaiseOdds) decision=RAISE;
    }
      
}

