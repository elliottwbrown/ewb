package Bots.TG1.StaticBots.ProbaBots;

import Bots.TG1.GenericBots.GenericBot;

public class ProbaBot78 extends GenericBot  {
    
    public void parseEndOfGameState() throws Exception {
    }    
    
    public int makeDecision() throws Exception {
        calculateWinProb();
        int decision=CALL;
        if (winProb>.78) decision=RAISE;
        return decision;
    }
    
    public void calculateWinProb() throws Exception {
       winProb=(myCardsFace+1f)/13f;
    }
}
