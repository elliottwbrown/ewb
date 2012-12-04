package Bots.TG1.DynamicBots.MemoryBots;

import Bots.TG1.GenericBots.GenericBot;

public class dynAdjBot39R50 extends GenericBot {
    
    public float 
        oneBetThreshold=        0.50f,            
        twoBetThreshold=        0.75f,
        fourBetThreshold=       0.90f,  
        foldThreshold=          0.39f,
        limpRaiseThreshold=     0.49f;

    BotParts bp=new BotParts();
    public int myCard;
        
    @Override
    public int makeDecision() throws Exception {
        myCard=holeCards.get(0).Face;
        if (!actFirst() && !beenRaisedTo) decideFromDP1(); 
        else if (beenRaisedTo) decideFromDPs23468();
        else decideFromDP0();
        return decision;
    }
    
    public void decideFromDP0() throws Exception {
        decision=bp.adj4Card(myCard,oneBetThreshold,foldThreshold);
    }
    
    public void decideFromDP1() throws Exception {
        decision=bp.adj2Limp
                (rateHandsOppFoldedSB,rateHandsOppRaisedSB, myCard,
                limpRaiseThreshold);  
    }
    
    public void decideFromDPs23468() throws Exception {
        float oppOneBetThreshold=1f-rateHandsOppRaisedSB;
        decision=bp.adj2Raise
                (numOppRaisedThisHand,potOdds,myCard,oppOneBetThreshold, 
                twoBetThreshold, fourBetThreshold);  
    }    
    
    @Override
    public void parseEndOfGameState() throws Exception {
        super.parseEndOfGameState();
        if (debugLevel>2) {
            System.out.print("rateHandsBotFolded "+rateHandsBotFolded+"\t\t");
            System.out.print("rateHandsBotRaised "+rateHandsBotRaised+"\t\t");
            System.out.print("rateHandsOppRaisedSB "+rateHandsOppRaisedSB+"\t");
            System.out.print("rateHandsOppFoldedSB "+rateHandsOppFoldedSB+"\t");
            System.out.print("rateHandsOppLimped "+rateHandsOppLimped+"\t");
    //        System.out.print("numOppFoldedSB "+numOppFoldedSB+"\t");
            float total=rateHandsOppRaisedSB+rateHandsOppFoldedSB+rateHandsOppLimped;
            System.out.print("total "+total+"\t");
            System.out.println("");
        }

    }    
}