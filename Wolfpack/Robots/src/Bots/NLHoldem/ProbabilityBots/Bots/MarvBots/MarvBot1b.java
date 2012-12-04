package Bots.NLHoldem.ProbabilityBots.Bots.MarvBots;

import Bots.NLHoldem.ProbabilityBots.Bots.RiverBots.TurnBot1;
import Bots.NLHoldem.ProbabilityBots.BotParts.MarvFlopAnalyzer.FlopAnalyzer;

public class MarvBot1b extends MarvBot1  {
    
    public MarvBot1b() throws Exception {
        super();
        FlopAnalyzer.init();
    }
    
    public int makeDecision() throws Exception {
        decision=CALL;
        potOdds=callAmt/(pot+callAmt);
        potPlusRaiseOdds=(callAmt+raiseAmt)/(pot+callAmt+raiseAmt*2);
        if (roundNo==DEAL) {
            //decision=CHECKFOLD;
            //useAdvancedHoleCardMatrix();
        } else if (roundNo==FLOP) {
            decision=CHECKFOLD;
            useMarvFlopAnalyzer();
        }
        return decision;
    }
    
    public void useMarvFlopAnalyzer() throws Exception {
        float winProb= FlopAnalyzer.getWinProbabilities(holeCards,communityCards,numPlayersInHand);
        if (winProb>potOdds) decision=CALL;
        if (winProb>.7) decision=RAISE;
    }
}

