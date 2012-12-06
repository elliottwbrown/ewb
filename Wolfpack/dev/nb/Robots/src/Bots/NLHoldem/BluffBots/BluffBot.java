package Bots.NLHoldem.BluffBots;

import Bots.NLHoldem.ProbabilityBots.Bots.ProbBots.ProbBot;

/*
 * extends ProbBot with bluffing
 * i.e. occaisonally RAISE instead of optimal expectation move - CALL
 *
 */
public class BluffBot extends ProbBot  {
    
    public double r=0;
    public float bluff1=.1f,bluff2=.25f,bluff3=.45f;
    public boolean checkRaiseOn=true;
    
    public BluffBot() {  }
    
    public int makeDecision() throws Exception {
        calculatePotOdds();
        calculateWinProb();
        getPureOddsDecision();
        considerBluffing();
        return decision;
    }
    
    public void considerBluffing() {
        if (roundNo==DEAL) r=Math.random();
        
        // RAISE instead of CHECK
        if (callAmt==0 && numChecks>0 && decision==CALL)
            if ((numChecks>0 && r<bluff1) || (numChecks>1 && r<bluff2) || (numChecks>2 && r<bluff3))
                decision=RAISE;
        
        // CHECK instead of RAISE
        if (checkRaiseOn)
            if (winProb>.8 && r<.1 && decision==RAISE) decision=CALL;
    }
    
}