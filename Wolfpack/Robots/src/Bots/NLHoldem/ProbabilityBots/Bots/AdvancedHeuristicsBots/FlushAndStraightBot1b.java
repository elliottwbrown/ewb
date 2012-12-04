package Bots.NLHoldem.ProbabilityBots.Bots.AdvancedHeuristicsBots;

public class FlushAndStraightBot1b extends StraightBot1  {
    
    public FlushAndStraightBot1b() throws Exception {

    }
    
    @Override
    public int makeDecision() throws Exception {
        decision=CALL;
        potOdds=callAmt/(pot+callAmt);
        potPlusRaiseOdds=(callAmt+raiseAmt)/(pot+callAmt+raiseAmt*2);
        if (roundNo==DEAL) {
            decision=CHECKFOLD;
            useAdvancedHoleCardMatrix();
        } else if (roundNo==FLOP) {
            decision=CALL;            
            //useFlopHeuristics();
            //useStraightAnalyzer();
            useFlushAnalzyer();
        } else {
            decision=CHECKFOLD;
            //useFlopHeuristics();
            //useStraightAnalyzer();
            //useFlushAnalzyer();
        }            
        return decision;
    }
    
}

