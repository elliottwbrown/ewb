package Bots.TG1.RangeBots.GearChangeBots;

import Bots.TG1.DynamicBots.MemoryBots.*;

public class GearChangeBot2 extends FourPointBuck39R50 {
    
    @Override
    public int makeDecision() throws Exception {
        super.makeDecision();
        int loopSize=1000,
                gameIDmod=numHands % loopSize;
        if (DecisionPoint==0) {
            if (gameIDmod<80 & decision==RAISE) 
                decision=CALL;
//            if (gameIDmod<200 & decision==CALL) 
//                decision=CHECKFOLD;
            if (gameIDmod>200 & gameIDmod<580 & decision==CALL) 
                decision=RAISE;
        }
        return decision;
    }
}