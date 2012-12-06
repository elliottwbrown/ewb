package PokerCommons.PokerObjects.Players;

import PokerCommons.PokerObjects.Cards.Cards;

public class Player extends PlayerChips {
    
    private int PlayerID,numberRaises;
    private String SessionID;
    private String Name;
    private boolean inCurrentHand;
    private boolean betThisRound,winner;
    private int callAmt;
    private Cards HoleCards=new Cards();
    private String code="";
    
    public Player(String name) {
        this.setBalance(startingChips);
        this.setPlayerID(1);
        this.setSessionID(name);
        this.setNumberRaises(0);
        this.setWinner(false);
    }
        
    public void setWinner(boolean winner) {
        this.winner=winner;
    }

    public boolean getWinner() {
         return this.winner;
    }
        
    public Player(String SessionID,String name) {
        this.setBalance(startingChips);
        this.setPlayerID(1);
        this.setSessionID(SessionID);
        this.setName(name);
        this.setNumberRaises(0);
    }
    
    public Player(String SessionID,String name,String code) {
        this.setBalance(startingChips);
        this.setPlayerID(1);
        this.setSessionID(SessionID);
        this.setName(name);
        this.setNumberRaises(0);
        this.setCode(code);
    }
    
    public Player(int PlayerID,String name,int balance) {
        this.setPlayerID(PlayerID);
        this.setName(getName());
        this.setBalance(balance);
    }
    
    public int getPlayerID() {
        return PlayerID;
    }
    
    public void setPlayerID(int PlayerID) {
        this.PlayerID = PlayerID;
    }
    
    public String getName() {
        return Name;
    }
    
    public void setName(String Name) {
        this.Name = Name;
    }
    
    public Cards getHoleCards() {
        return HoleCards;
    }
    
    public void setHoleCards(Cards HoleCards) {
        this.HoleCards = HoleCards;
    }
    
    public int getCallAmt() {
        return callAmt;
    }
    
    public void setCallAmt(int outstanding) {
        this.callAmt = outstanding;
    }
    
    public boolean isBetThisRound() {
        return betThisRound;
    }
    
    public void setBetThisRound(boolean betThisRound) {
        this.betThisRound=betThisRound;
    }
    
    public boolean getBetThisRound() {
        return betThisRound;
    }
    
    public boolean isInCurrentHand() {
        return inCurrentHand;
    }
    
    public void setInCurrentHand(boolean inCurrentHand) {
        this.inCurrentHand = inCurrentHand;
    }

    public String getCode() {
        return code;
    }
    
    public void setCode(String code) {
        this.code = code;
    }
    
    public String getSessionID() {
        return SessionID;
    }
    
    public void setSessionID(String SessionID) {
        this.SessionID = SessionID;
    }
    
    public void setNumberRaises(int numberRaises) {
        this.numberRaises = numberRaises;
    }
    
    public int getNumberRaises() {
        return numberRaises;
    }
}
