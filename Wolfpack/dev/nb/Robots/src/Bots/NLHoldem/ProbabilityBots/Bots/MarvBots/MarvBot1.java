package Bots.NLHoldem.ProbabilityBots.Bots.MarvBots;


import Bots.NLHoldem.ProbabilityBots.BotParts.MarvFlopAnalyzer.FlopAnalyzer;
import Bots.NLHoldem.ProbabilityBots.Bots.AdvancedHeuristicsBots.StraightBot1;

public class MarvBot1 extends StraightBot1  {
    
    public FlopAnalyzer fa=new FlopAnalyzer();
    
    public MarvBot1() throws Exception {
        super();
        fa.init();
    }
    
    public int makeDecision() throws Exception {
        decision=CALL;
        potOdds=callAmt/(pot+callAmt);
        potPlusRaiseOdds=(callAmt+raiseAmt)/(pot+callAmt+raiseAmt*2);
        if (roundNo==DEAL) {
            decision=CHECKFOLD;
            useAdvancedHoleCardMatrix();
        } else if (roundNo==FLOP) {
            decision=CHECKFOLD;
            useMarvFlopAnalyzer();
        } else {
            decision=CHECKFOLD;            
            useFlopHeuristics();
            useStraightAnalyzer();
            useFlushAnalzyer();
        }
        return decision;
    }
    
    public void useMarvFlopAnalyzer() throws Exception {
        float winProb= fa.getWinProbabilities(holeCards,communityCards,numPlayersInHand);
        if (winProb>potOdds) decision=CALL;
        float impliedPotOdds=potPlusRaiseOdds*1.8f;
        if (winProb>impliedPotOdds) decision=RAISE;
    }
}

