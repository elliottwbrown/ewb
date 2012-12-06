package Bots.TG1.StaticBots.ProbaBots;

import Bots.TG1.GenericBots.GenericBot;

public class ProbaBot76 extends GenericBot  {
    
    public static final float oneBetThreshold= .76f;
               
    public int makeDecision() throws Exception {
        winProb=(myCardsFace+1f)/13f;
        decision=CALL;
        if (winProb>oneBetThreshold) decision=RAISE;
        return decision;
    }
}
