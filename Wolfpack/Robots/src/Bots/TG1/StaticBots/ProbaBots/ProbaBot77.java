package Bots.TG1.StaticBots.ProbaBots;

import Bots.TG1.GenericBots.GenericBot;

public class ProbaBot77 extends GenericBot  {
    
    public void parseEndOfGameState() throws Exception {
    }    
    
    public int makeDecision() throws Exception {
        calculateWinProb();
        int decision=CALL;
        if (winProb>.77) decision=RAISE;
        return decision;
    }
    
    public void calculatePotOdds() {
        potOdds=callAmt/(pot+callAmt);
        potPlusRaiseOdds=(callAmt+raiseAmt)/(pot+callAmt+raiseAmt*2);
    }
    
    public void calculateWinProb() throws Exception {
       winProb=(myCardsFace+1f)/13f;
    }
}
