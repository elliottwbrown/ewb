package Bots.TG1.RangeBots.RandomBots;

import Bots.TG1.DynamicBots.MemoryBots.*;
import PokerCommons.PokerObjects.Cards.face;

public class GearChangeBot3 extends FourPointBuck39R50 {
    
    public int makeDecision() throws Exception {
        super.makeDecision();
        int loopSize=1000,
            gameIDmod=numHands % loopSize;
        double r=Math.random();
        if (DecisionPoint==0 & r<1) {
            if (gameIDmod<100 & myCardsFace>face.FOUR) decision=CALL;
            //if (gameIDmod>100 & gameIDmod<200) decision=RAISE;
        }
        return decision;
    }
}