package Bots.NLHoldem.BasicBots.AlwaysBots;

import Bots.NLHoldem.GenericBots.GenericBot;
import com.ewb.Utilities.XMLUtilities;

public class CheckFoldAllBot extends GenericBot  {
    
    public int makeDecision() throws Exception {
        return CHECKFOLD;
    }
    
    
    public void gameOver() throws Exception {
        numGames++;
        String roundHistory=XMLUtilities.getTag(response,"<roundHistory>");
        if (showdown) numShowdowns++;
        if (showdown && won) numShowdownsWon++;
        if (won) numOppLost++;
        if (oppFoldedPreFlop) numOppFoldedPreFlop++;
        if (botFoldedPreFlop) numBotFoldedPreFlop++;
        if (botFoldedSB) numBotFoldedSB++;
        if (oppFoldedSB) numOppFoldedSB++;
        if (oppFoldedBB) numOppFoldedBB++;
        if (botFoldedBB) numBotFoldedBB++;
        int numOppRaisedThisGame=XMLUtilities.countOccurences(roundHistory,"<bet><player/>"+oppID);
        int numOppCalledThisGame=XMLUtilities.countOccurences(roundHistory,"<call><player/>"+oppID);
        int numOppFoldedThisGame=XMLUtilities.countOccurences(roundHistory,"<fold><player/>"+oppID);
        int numOppCheckedThisGame=XMLUtilities.countOccurences(roundHistory,"<check><player/>"+oppID);
        int numBotRaisedThisGame=XMLUtilities.countOccurences(roundHistory,"<bet><player/>"+botID);
        int numBotCalledThisGame=XMLUtilities.countOccurences(roundHistory,"<call><player/>"+botID);
        int numBotFoldedThisGame=XMLUtilities.countOccurences(roundHistory,"<fold><player/>"+botID);
        int numBotCheckedThisGame=XMLUtilities.countOccurences(roundHistory,"<check><player/>"+botID);
        int numOppMovedThisGame=numOppRaisedThisGame+numOppCalledThisGame+numOppFoldedThisGame+numOppCheckedThisGame;
        int numBotMovedThisGame=numBotRaisedThisGame+numBotCalledThisGame+numBotFoldedThisGame+numBotCheckedThisGame;
        numBotRaised+=numBotRaisedThisGame;
        numBotCalled+=numBotCalledThisGame;
        numBotFolded+=numBotFoldedThisGame;
        numBotChecked+=numBotCheckedThisGame;
        numBotMoved+=numBotMovedThisGame;
        numOppRaised+=numOppRaisedThisGame;
        numOppCalled+=numOppCalledThisGame;
        numOppFolded+=numOppFoldedThisGame;
        numOppChecked+=numOppCheckedThisGame;
        numOppMoved+=numOppMovedThisGame;
     
    };
            
}
