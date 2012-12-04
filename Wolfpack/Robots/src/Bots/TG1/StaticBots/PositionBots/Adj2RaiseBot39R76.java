package Bots.TG1.StaticBots.PositionBots;

import Bots.TG1.GenericBots.GenericBot;

public class Adj2RaiseBot39R76 extends GenericBot {

    public float 
        oppOneBetThreshold=     0.76f,
        oneBetThreshold=        0.76f,
        twoBetThreshold=        0.75f,
        fourBetThreshold=       0.90f,  
        foldThreshold=          0.39f;
    
    @Override
    public int makeDecision() throws Exception {
        BotParts bp=new BotParts();
        if (beenRaisedTo) decision=bp.adj2Raise
                (numOppRaisedThisHand,potOdds,myCardsFace,oppOneBetThreshold, twoBetThreshold, fourBetThreshold);
        else decision=bp.adj4Card(myCardsFace,oneBetThreshold,foldThreshold);
        return decision;
    }
}