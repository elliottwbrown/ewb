package Bots.TG1.StaticBots.BasicBots;

import Bots.TG1.GenericBots.GenericBot;

public class RaiseAllBot extends GenericBot  {
    
    public int  makeDecision() throws Exception {
        return RAISE;
    }

    public void parseEndOfGameState() throws Exception {
    };
            
}
