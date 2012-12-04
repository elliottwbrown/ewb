package PokerCommons.PokerObjects.GameProtocols.LimitHoldemTournament;

import PokerCommons.PokerObjects.Cards.Cards;

public class Player {
    
    private int PlayerID,numberRaises;
    private String SessionID;
    private String Name;
    private float balance;
    private boolean inCurrentHand;
    private boolean betThisRound;
    private float callAmt;
    private Cards HoleCards=new Cards();
    
    public Player(String name) {
        this.setBalance(1000f);
        this.setPlayerID(1);
        this.setSessionID(name);
        this.setNumberRaises(0);
    }
    
    public Player(String SessionID,String name) {
        this.setBalance(1000f);
        this.setPlayerID(1);
        this.setSessionID(SessionID);
        this.setName(name);
        this.setNumberRaises(0);
    }
    
    public Player(int PlayerID,String name,float balance) {
        this.setPlayerID(PlayerID);
        this.setName(getName());
        this.setBalance(getBalance());
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
    
    public float getBalance() {
        return balance;
    }
    
    public void setBalance(float Balance) {
        this.balance = Balance;
    }
    
    public void adjustBalance(float Balance) {
        this.setBalance(getBalance() + Balance);
    }
    
    public Cards getHoleCards() {
        return HoleCards;
    }
    
    public void setHoleCards(Cards HoleCards) {
        this.HoleCards = HoleCards;
    }
    
    public float getCallAmt() {
        return callAmt;
    }
    
    public void setCallAmt(float outstanding) {
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
