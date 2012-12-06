package Bots.TG1.RangeBots.GearChangeBots;

import Bots.TG1.DynamicBots.MemoryBots.*;

public class GearChangeBot1 extends FourPointBuck39R50 {
    
    @Override
    public int makeDecision() throws Exception {
        super.makeDecision();
        if (numHands<20000 & getDecisionPoint()==0) {
            int loopSize=400,
                gameIDmod=numHands % loopSize;
            if (gameIDmod<100 & decision==RAISE) decision=CALL;
            else decision=RAISE;
        }
        return decision;
    }
}