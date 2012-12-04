package Bots.TG1.StaticBots.BasicBots;

import Bots.TG1.GenericBots.GenericBot;

public class RandomBot extends GenericBot  {
    
    public int makeDecision() throws Exception {
        int n=(int) (Math.random()*3);
        return n;
    }
            
}