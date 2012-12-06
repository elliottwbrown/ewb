package Bots.TG1.RangeBots.GearChangeBots;

import Bots.TG1.DynamicBots.MemoryBots.*;
import PokerCommons.PokerObjects.GameProtocols.TG1.GameValues;

public class RangeBot1 extends FourPointBuck39R50 {
    
    public int makeDecision() throws Exception {
        super.makeDecision();
        int repeatPeriod=2000;
        int gameInPeriod=gameID % repeatPeriod;
        if (gameInPeriod<1000) if (DecisionPoint==0 & decision==RAISE) decision=GameValues.CALL;
        if (gameInPeriod>1000&gameInPeriod<1500) 
            if (DecisionPoint==0 & decision==CALL) decision=GameValues.RAISE;
        return decision;
    }
}