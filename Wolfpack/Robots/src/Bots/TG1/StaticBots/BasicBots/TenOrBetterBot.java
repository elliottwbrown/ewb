package Bots.TG1.StaticBots.BasicBots;

import Bots.TG1.GenericBots.GenericBot;
import PokerCommons.PokerObjects.Cards.face;

public class TenOrBetterBot extends GenericBot  {
    
    public int makeDecision() throws Exception {
        decision=CALL;
        if (myCardsFace>=face.TEN) decision=RAISE;
        return decision;
    }
}
