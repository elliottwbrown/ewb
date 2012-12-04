package Bots.TG1.StaticBots.OrigamiBots;

import Bots.TG1.StaticBots.ProbaBots.ProbaBot76;

public class OrigamiBot39R50 extends ProbaBot76  {
    
    public float 
        foldThreshold=  .39f;
    
    public int makeDecision() throws Exception {
        super.makeDecision();
        if (winProb<foldThreshold) decision=CHECKFOLD;
        return decision;
    }
}
