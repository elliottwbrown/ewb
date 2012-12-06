package PokerCommons.PokerObjects.Players;

public class PlayerChips {
    
    public int balance;
    public int startingChips=20000;
    
    public PlayerChips() {    
        
    }
   
    public int getBalance() {
        return balance;
    }
    
    public void setBalance(int Balance) {
        this.balance = Balance;
    }
    
    public void adjustBalance(int Balance) {
        this.setBalance(getBalance() + Balance);
    }
    
}
