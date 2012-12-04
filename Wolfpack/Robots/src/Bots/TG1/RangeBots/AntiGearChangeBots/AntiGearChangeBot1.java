package Bots.TG1.RangeBots.AntiGearChangeBots;

import Bots.TG1.DynamicBots.MemoryBots.*;
import Bots.TG1.StaticBots.ProbaBots.ProbaBot50;

public class AntiGearChangeBot1 extends FourPointBuck39R50 {
    
    @Override
    public int makeDecision() throws Exception {
        super.makeDecision();
//        int loopSize=2000,
//            gameIDmod=numHands % loopSize;
//        if ((DecisionPoint==1 || DecisionPoint==2) & gameIDmod<1000) 
//            decision=new ProbaBot50().makeDecision();
        return decision;
    }
}