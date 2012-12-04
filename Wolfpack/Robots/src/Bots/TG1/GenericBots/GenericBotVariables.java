package Bots.TG1.GenericBots;

import PokerCommons.Client.PokerClient;
import PokerCommons.PokerObjects.Cards.Cards;
import PokerCommons.PokerObjects.GameProtocols.TG1.GameValues;
import java.util.PropertyResourceBundle;

public class GenericBotVariables implements GameValues {

    public int[] cardsIWasDealt={0,0,0,0,0,0,0,0,0,0,0,0,0};
    public int[] decisionPointsIReached={0,0,0,0,0,0,0,0,0,0};
    
    public final int 
            debugLevel=0,
            firstJoinDelay=2000,
            joinDelay=2000,
            playDelay=20;
    
    public int 
            gameID=0,
            handStatus=GameValues.HAND_NOT_STARTED,
            roundNo=GameValues.DEAL,
            lastRoundNo=GameValues.DEAL,
            numPlayersInHand=0,
            numPlayersInGame=0,
            decision=GameValues.CALL,
            balance=0,
            myCardsFace;    
    
    public float 
            winProb,
            adjWinProb,
            pot,
            callAmt,
            smallBlindAmt,
            raiseAmt,
            potOdds,
            potPlusRaiseOdds, 
            impliedPotOdds;
    
    public float
            
            // Bot rates
            rateHandsBotFolded=         .5f,
            rateHandsBotRaised=         .5f,
            rateHandsBotLimped=         .5f,
    
            // Opp rates
            rateHandsOppRaisedSB=       .5f,
            rateHandsOppFoldedSB=       .5f,
            rateHandsOppLimped=         .5f;
    
    public int

            // These variables record our play in the current hand.
            DecisionPoint=0,
            numBotRaisedThisHand=0,     // number of times bot raised this hand
            numBotCalledThisHand=0,     // number of times bot called this hand
            numBotCheckedThisHand=0,    // number of times bot checked this hand
            numBotMovedThisHand=0,      // number of times bot moved this hand     
            numBotMovedB4CThisHand=0,   // number of times bot moved this hand     

            // These variables record the opponent's play in the current hand.
            numOppRaisedThisHand=0,     // number of times opponent raised this hand
            numOppCalledThisHand=0,     // number of times opponent called this hand
            numOppCheckedThisHand=0,    // number of times opponent checked this hand
            numOppMovedThisHand=0,      // number of times opponent moved this hand
            numOppMovedB4CThisHand=0,   // number of times opponent moved 
            
            // These variables record our play in the current session.
            numBotFoldedBB=0,           // number of times bot folded as big blind
            numBotLimped=0,             // number of times bot limped
            numBotFoldedSB=0,           // number of times bot folded as small blind as first act
            numBotFoldedPreFlop=0,      // number of times bot folded pre flop
            numBotFolded=0,             // number of times bot folded
            numBotChecked,              // number of times bot checked 
            numBotCalled=0,             // number of times bot called
            numBotRaised=0,             // number of times bot raised
            numBotVPIP=0,               // number of times bot VPIP
            numBotMoved=0,              // number of times bot moved         
            numBotMovedB4C=0,           // number of times opponent moved 
            numShowdowns=0,             // number of times opponent went to showdown
            numShowdownsWon=0,          // number of times opponent won showdown
            numHandsBotRaised=0,        // number of hands opponent raised 
    
            // These variables record the opponent's play in the current session.
            numOppHands=0,              // number of hands played against this opponent
            numOppLimped=0,             // number of times opponent folded as small blind as first act
            numOppFoldedBB=0,           // number of times opponent folded as big blind
            numOppFoldedSB=0,           // number of times opponent folded as small blind
            numOppFoldedPreFlop=0,      // number of times opponent folded pre flop
            numOppFolded=0,             // number of times opponent folded
            numOppChecked,              // number of times opponent checked 
            numOppCalled=0,             // number of times opponent called 
            numOppRaised=0,             // number of times opponent raised
            numHandsOppRaisedSB=0,      // number of hands opponent raised 
            numHandsOppRaised2DP4=0,    // number of hands opponent raised to DP4
            numOppVPIP=0,               // number of times opponent VPIP
            numOppMoved=0,              // number of times opponent moved 
            numOppMovedB4C=0,           // number of times opponent moved 
            numOppLost=0,               // number of times opponent lost

            
            // These variables record the totals counts in the current hand
            numMovesThisHand=0,
            numChecksThisHand=0,
            numRaisesThisHand=0,

            // These variables record the totals counts in the current session
            numHands=0;
    
    public boolean 
            webPlay=true,
            beenCheckedTo,
            beenRaisedTo,
            oppFolded,
            oppFoldedPreFlop,
            oppFoldedBB,
            oppFoldedSB,
            showdown,
            won,
            botFolded,
            botFoldedPreFlop,
            botFoldedBB,
            botFoldedSB;    
    
    public volatile String response,lastResponse="";
    public PropertyResourceBundle configuration;
    public PokerClient client;
    public Cards holeCards;
    public String botID,botName,oppID,oppName,smallBlind,bigBlind;    
    
    public void initVariablesForNewOpponent() {
        
         // These variables record our play in the current hand.
            numBotRaisedThisHand=0;     // number of times bot raised this hand
            numBotCalledThisHand=0;     // number of times bot called this hand
            numBotCheckedThisHand=0;    // number of times bot checked this hand
            numBotMovedThisHand=0;      // number of times bot moved this hand     
            numBotMovedB4CThisHand=0;   // number of times bot moved this hand     

            // These variables record the opponent's play in the current hand.
            numOppRaisedThisHand=0;     // number of times opponent raised this hand
            numOppCalledThisHand=0;     // number of times opponent called this hand
            numOppCheckedThisHand=0;    // number of times opponent checked this hand
            numOppMovedThisHand=0;      // number of times opponent moved this hand
            numOppMovedB4CThisHand=0;   // number of times opponent moved 
            
            // These variables record our play in the current session.
            numBotFoldedBB=0;           // number of times bot folded as big blind
            numBotLimped=0;             // number of times bot limped
            numBotFoldedSB=0;           // number of times bot folded as small blind as first act
            numBotFoldedPreFlop=0;      // number of times bot folded pre flop
            numBotFolded=0;             // number of times bot folded
            numBotChecked=0;            // number of times bot checked 
            numBotCalled=0;             // number of times bot called
            numBotRaised=0;             // number of times bot raised
            numBotVPIP=0;               // number of times bot VPIP
            numBotMoved=0;              // number of times bot moved         
            numBotMovedB4C=0;           // number of times opponent moved 
            numShowdowns=0;             // number of times opponent went to showdown
            numShowdownsWon=0;          // number of times opponent won showdown
            numHandsBotRaised=0;        // number of hands opponent raised 
    
            // These variables record the opponent's play in the current session.
            numOppHands=0;              // number of hands played against this opponent
            numOppLimped=0;             // number of times opponent folded as small blind as first act
            numOppFoldedBB=0;           // number of times opponent folded as big blind
            numOppFoldedSB=0;           // number of times opponent folded as small blind
            numOppFoldedPreFlop=0;      // number of times opponent folded pre flop
            numOppFolded=0;             // number of times opponent folded
            numOppChecked=0;           // number of times opponent checked 
            numOppCalled=0;             // number of times opponent called 
            numOppRaised=0;             // number of times opponent raised
            numHandsOppRaised2DP4=0;    // number of hands opponent raised to DP4
            numOppVPIP=0;               // number of times opponent VPIP
            numOppMoved=0;              // number of times opponent moved 
            numOppMovedB4C=0;           // number of times opponent moved 
            numOppLost=0;               // number of times opponent lost
            numHandsOppRaisedSB=0;      // number of hands opponent raised 
            

            // These variables record the totals counts in the current hand
            numMovesThisHand=0;
            numChecksThisHand=0;
            numRaisesThisHand=0;

            // These variables record the totals counts in the current session
            numHands=0;
            balance=20000;
    }

    public void initVariablesForNewHand() {
        // These variables record our play in the current hand.
        numBotRaisedThisHand=0;     // number of times bot raised this hand
        numBotCalledThisHand=0;     // number of times bot called this hand
        numBotCheckedThisHand=0;    // number of times bot checked this hand
        numBotMovedThisHand=0;      // number of times bot moved this hand     
        numBotMovedB4CThisHand=0;   // number of times bot moved this hand     

        // These variables record the opponent's play in the current hand.
        numOppRaisedThisHand=0;     // number of times opponent raised this hand
        numOppCalledThisHand=0;     // number of times opponent called this hand
        numOppCheckedThisHand=0;    // number of times opponent checked this hand
        numOppMovedThisHand=0;      // number of times opponent moved this hand
        numOppMovedB4CThisHand=0;   // number of times opponent moved 
        showdown=won=false;
    }        
}