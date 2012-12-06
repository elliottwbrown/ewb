package Bots.NLHoldem.BasicBots.AdvancedHeuristicsBots;

public class FlushAndStraightBot1 extends StraightBot1  {
    
    public FlushAndStraightBot1() throws Exception {

    }
    
    @Override
    public int makeDecision() throws Exception {
        decision=CALL;
        potOdds=callAmt/(pot+callAmt);
        potPlusRaiseOdds=(callAmt+raiseAmt)/(pot+callAmt+raiseAmt*2);
        if (roundNo==DEAL) {
            decision=CHECKFOLD;
            useAdvancedHoleCardMatrix();
        } else {
            decision=CHECKFOLD;            
            useFlopHeuristics();
            useStraightAnalyzer();
            useFlushAnalzyer();
        }            
        return decision;
    }
    
}

