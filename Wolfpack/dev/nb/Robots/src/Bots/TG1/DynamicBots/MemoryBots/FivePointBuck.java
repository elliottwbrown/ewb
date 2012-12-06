package Bots.TG1.DynamicBots.MemoryBots;

import PokerCommons.PokerObjects.Cards.face;
import PokerCommons.PokerObjects.GameProtocols.TG1.GameValues;

public class FivePointBuck extends FourPointBuck39R50 {
    
    int oppDP1count=0;
    
    @Override
    public int makeDecision() throws Exception {
        decisionPointsIReached[DecisionPoint]++;
        if (DecisionPoint==0) decideFromDP0();                     
        else if (DecisionPoint==1) decideFromDP1();                     
        else if (DecisionPoint==3) decideFromDP3();
        else if (DecisionPoint==4) decideFromDP4();
        else decideFromDPs23468();
        return decision;
    }
    
    public void decideFromDP3() {
        if (myCardsFace>=face.EIGHT) decision=GameValues.CALL;
        else decision=GameValues.CHECKFOLD;
    }    
}