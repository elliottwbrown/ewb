package Bots.TG1.StaticBots.BasicBots;

import Bots.TG1.GenericBots.GenericBot;
import PokerCommons.PokerObjects.Cards.face;

public class JackOrBetterBot extends GenericBot  {
    
    public void parseEndOfGameState() throws Exception {
    }    
    
    public int makeDecision() throws Exception {
        int decision=CALL;
        if (myCardsFace>=face.JACK) decision=RAISE;
        return decision;
    }
}
