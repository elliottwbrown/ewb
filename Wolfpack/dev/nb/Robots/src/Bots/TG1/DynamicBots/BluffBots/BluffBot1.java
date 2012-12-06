package Bots.TG1.DynamicBots.BluffBots;

import Bots.TG1.DynamicBots.MemoryBots.FourPointBuck39R50;
import PokerCommons.PokerObjects.Cards.face;

public class BluffBot1 extends FourPointBuck39R50 {
    
    @Override
    public int makeDecision() throws Exception {
        super.makeDecision();
        double r=Math.random();
        if (r<.1 & DecisionPoint==0 & myCardsFace>face.FOUR) decision=RAISE;
        return decision;
    }
    
}