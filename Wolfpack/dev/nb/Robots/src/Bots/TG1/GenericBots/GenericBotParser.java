package Bots.TG1.GenericBots;

import com.ewb.Utilities.XMLUtilities;
import PokerCommons.PokerObjects.Cards.Cards;
import PokerCommons.PokerObjects.GameProtocols.TG1.GameValues;

public class GenericBotParser extends GenericBotVariables {

    public int getDecisionPoint() throws Exception {
        DecisionPoint=0;
        if (bigBlind.equals(botID)) {                               
            if (!beenRaisedTo) DecisionPoint=1;                     
            else {
                if (numMovesThisHand==1 && numRaisesThisHand==1) DecisionPoint=2;
                if (numMovesThisHand==3 && numRaisesThisHand==2) DecisionPoint=5;                            
                if (numMovesThisHand==3 && numRaisesThisHand==3) DecisionPoint=6;                          
                if (numMovesThisHand==5 && numRaisesThisHand==4) DecisionPoint=9;                         
            }
        } else {                                                    
            if(!beenRaisedTo) DecisionPoint=0;                      
            else {
                if (numMovesThisHand==2 && numRaisesThisHand==1) DecisionPoint=3;                           
                if (numMovesThisHand==2 && numRaisesThisHand==2) DecisionPoint=4;                            
                if (numMovesThisHand==4 && numRaisesThisHand==3) DecisionPoint=7;                        
                if (numMovesThisHand==4 && numRaisesThisHand==4) DecisionPoint=8;                
            }                                        
        }  
        return DecisionPoint;
    }
    
    public void parseGameState() throws Exception {
        response=XMLUtilities.getTag(response,"<gameState>");
        if (!lastResponse.equals(response)) {
            lastResponse=response;
            parsePlayers();
            parseGameInfo();
            parseCards();
            parseMoves();  
            DecisionPoint=getDecisionPoint();
        }
    }

    public void parseEndOfGameState() throws Exception {
        
        // game counters
        countCards();
        numHands++;
        
        if (response.contains("Showdown")) showdown=true;
        if (response.contains("Winner: "+botName)) won=true;
        if (showdown) numShowdowns++;
        if (showdown && won) numShowdownsWon++;
        if (won) numOppLost++;

        // self modelling counters
        oppFolded=oppFoldedPreFlop=oppFoldedBB=oppFoldedSB=false;
        if (response.contains("<fold><player>"+botID)) {
            botFolded=true;
                botFoldedPreFlop=true;
                if (bigBlind.equals(botID)) botFoldedBB=true;
                else botFoldedSB=true;
        }        
        if (botFoldedPreFlop) numBotFoldedPreFlop++;
        if (botFoldedSB && numOppMovedThisHand==0) numBotFoldedSB++;
        if (botFoldedBB) numBotFoldedBB++;
        if (botFolded) numBotFolded++;
        numBotRaised+=numBotRaisedThisHand;
        numBotCalled+=numBotCalledThisHand;
        numBotChecked+=numBotCheckedThisHand;
        numBotMoved+=numBotMovedThisHand;
        if (numBotRaisedThisHand>0) numHandsBotRaised++;
        //if (smallBlind.equals(botID) && numBotCalledThisHand>0) numBotLimped++;
        
        // opponent modelling counters
        botFolded=botFoldedPreFlop=botFoldedBB=botFoldedSB=false;
        if (response.contains("<fold><player>"+oppID)) {
            oppFolded=true;
            oppFoldedPreFlop=true;
            if (bigBlind.equals(botID)) oppFoldedSB=true;
            else oppFoldedBB=true;
        }
        if (oppFoldedPreFlop) numOppFoldedPreFlop++;
        if (oppFoldedSB && numBotMovedThisHand==0) numOppFoldedSB++;
        if (oppFoldedBB) numOppFoldedBB++;
        if (oppFolded) numOppFolded++;
        numOppRaised+=numOppRaisedThisHand;
        numOppCalled+=numOppCalledThisHand;
        numOppChecked+=numOppCheckedThisHand;
        numOppMoved+=numOppMovedThisHand;
        numOppMovedB4C+=numOppMovedB4CThisHand;
        if (numOppRaisedThisHand>0 && smallBlind.equals(oppID) && numOppCalledThisHand==0) 
            numHandsOppRaisedSB++;
        if (numOppRaisedThisHand==1 && bigBlind.equals(oppID) && numBotRaisedThisHand==0)
            numHandsOppRaised2DP4++;
        if (smallBlind.equals(oppID) && !beenRaisedTo && beenCheckedTo) numOppLimped++;
        
        // rates for Bot
        if (numHands>1) {
            int eligibleHands=numHands-numOppFoldedSB;
            rateHandsBotFolded = (float)numBotFolded/eligibleHands;
            rateHandsBotRaised = (float)numHandsBotRaised/eligibleHands;        
            rateHandsBotLimped = (float)numBotLimped/(numHands/2);        
        }        
        
        // rates for Opp
        if (numHands>1) {
            rateHandsOppRaisedSB = (float)numHandsOppRaisedSB/(numHands/2);
            rateHandsOppFoldedSB = (float)numOppFoldedSB /(numHands/2);  
            rateHandsOppLimped = (float)numOppLimped /(numHands/2);        
        }
        
        //balance=1;

    }
    
    public void parseMoves() {
        String bets=response.substring(response.indexOf("<turnHistory>"),response.indexOf("</turnHistory>"));
        beenCheckedTo=false;
        if (bets.contains("<check>") && !bets.contains("<bet>")) beenCheckedTo=true;
        beenRaisedTo=false;
        if (bets.contains("<bet>") && callAmt!=0) beenRaisedTo=true;
        String turnHistory=XMLUtilities.getTag(response,"<turnHistory>");
        
        // opponent modelling variables
        numOppRaisedThisHand=XMLUtilities.countOccurences(turnHistory,"<bet><player>"+oppID);
        numOppCalledThisHand=XMLUtilities.countOccurences(turnHistory,"<call><player>"+oppID);
        numOppCheckedThisHand=XMLUtilities.countOccurences(turnHistory,"<check><player>"+oppID);
        numOppMovedThisHand=numOppRaisedThisHand+numOppCalledThisHand+numOppCheckedThisHand;
        numOppMovedB4CThisHand=(numRaisesThisHand<4)?numOppMovedThisHand:2;            
        
        // self modelling variables
        numBotRaisedThisHand=XMLUtilities.countOccurences(turnHistory,"<bet><player>"+botID);
        numBotCalledThisHand=XMLUtilities.countOccurences(turnHistory,"<call><player>"+botID);
        numBotCheckedThisHand=XMLUtilities.countOccurences(turnHistory,"<check><player>"+botID);
        numBotMovedThisHand=numBotRaisedThisHand+numBotCalledThisHand+numBotCheckedThisHand;
        
        // move totals
        numRaisesThisHand=numOppRaisedThisHand+numBotRaisedThisHand;
        numChecksThisHand=numOppCheckedThisHand+numBotCheckedThisHand;
        numMovesThisHand=numBotMovedThisHand+numOppMovedThisHand;
    }

    public void parseCards() throws Exception {
        if (response.contains("<cardInfo>")) {
            pot=Float.parseFloat(XMLUtilities.getTag(response,"<pot>"));
            callAmt=Float.parseFloat(XMLUtilities.getTag(response,"<callAmount>"));
            potOdds = callAmt / pot;                  
            holeCards=new Cards();
            if (roundNo>=GameValues.DEAL) 
                holeCards.add(new PokerCommons.PokerObjects.Cards.Card(XMLUtilities.getTag(response,"<card1>")));
            myCardsFace=holeCards.get(0).Face; 
        }
    }

    public void parseGameInfo() throws NumberFormatException {
        gameID=Integer.parseInt(XMLUtilities.getTag(response,"<gameID>"));
        handStatus=Integer.parseInt(XMLUtilities.getTag(response,"<handStatus>"));
        lastRoundNo=roundNo;
        roundNo=Integer.parseInt(XMLUtilities.getTag(response,"<roundNo>"));
        smallBlindAmt=Float.parseFloat(XMLUtilities.getTag(response,"<smallBlindAmt>"));
        smallBlind=XMLUtilities.getTag(response,"<smallBlind>");
        bigBlind=XMLUtilities.getTag(response,"<bigBlind>");
        raiseAmt=smallBlindAmt*2;      
    }

    public void parsePlayers() {
        oppID=XMLUtilities.getTag(response,"<sessionID>");
        oppName=XMLUtilities.getTag(response,"<name>");
        balance=Integer.parseInt(XMLUtilities.getTag(response,"<balance>"));
        if (oppID.equals(botID)) {
            oppID=XMLUtilities.getSecondTag(response,"<sessionID>",2);
            oppName=XMLUtilities.getSecondTag(response,"<name>",2);
            balance=Integer.parseInt(XMLUtilities.getSecondTag(response,"<balance>",2));
        }
    }
    
    public boolean actFirst() {
        return response.contains("<dealer>" + botID);
    }       
    
    public void countCards() {
        try {
            cardsIWasDealt[holeCards.get(0).Face]++;
        } catch (Exception e) {
            //e.printStackTrace();
        }
    }    
}