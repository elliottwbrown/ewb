package Bots.TG1.DynamicBots.LimpRaiseBots;

import Bots.TG1.DynamicBots.MemoryBots.FourPointBuck;
import PokerCommons.PokerObjects.GameProtocols.TG1.GameValues;

public class LimpRaiseBot1 extends FourPointBuck {
    
    @Override
    public int makeDecision() throws Exception {
        int myCard=holeCards.get(0).Face;
        int DecisionPoint=getDecisionPoint();
        decisionPointsIReached[DecisionPoint]++;
        if (DecisionPoint==0) decideFromDP0();                     
        else if (DecisionPoint==1) decideFromDP1();                     
        else if (DecisionPoint==4) decideFromDP4();
        else decideFromDPs23468();
        double r=Math.random();
        System.out.println(""+r);
        if (DecisionPoint==0 && myCard>10 && r<.00001) {
            System.out.println("here"+r);
            decision=GameValues.CALL;
        }
        return decision;
    }

}