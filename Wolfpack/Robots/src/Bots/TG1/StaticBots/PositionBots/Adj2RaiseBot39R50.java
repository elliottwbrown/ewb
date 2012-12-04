package Bots.TG1.StaticBots.PositionBots;

import Bots.TG1.GenericBots.GenericBot;

public class Adj2RaiseBot39R50 extends GenericBot {

    public float 
        oppOneBetThreshold=     0.50f,
        oneBetThreshold=        0.50f,
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