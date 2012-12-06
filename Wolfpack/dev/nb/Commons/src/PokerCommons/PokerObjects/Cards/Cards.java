package PokerCommons.PokerObjects.Cards;

import java.util.*;

public class Cards {
    
    private ArrayList CardsAL;
    
    public Cards() {
        CardsAL=new ArrayList();
    }
    
    public Cards(Card[] cardsIn) {
        CardsAL=new ArrayList();
        for (int c=0; c<cardsIn.length; c++) CardsAL.add(cardsIn[c]);
    }
    
    public Cards(Cards cardsIn) {
        CardsAL=new ArrayList(cardsIn.getArrayList());
    }
    
    public Cards(String[] cardsIn) throws Exception {
        CardsAL=new ArrayList();
        for (int i=0; i<cardsIn.length; i++) CardsAL.add(new Card(cardsIn[i]));
    }
    
    public void add(Card cardIn) {
        CardsAL.add(cardIn);
    }
    
    public void add(Cards Cards) {
        for (int i=0; i<Cards.size(); i++) CardsAL.add((Card)Cards.get(i));
    }
    
    public Card removeTopCard() {
        return (Card) CardsAL.remove(CardsAL.size()-1);
    }
    
    public Card remove(int i) {
        return (Card) CardsAL.remove(i);
    }

    public void remove(Cards cardsIn) {
        for (int i=0; i<cardsIn.size(); i++) remove(cardsIn.get(i));
    }
    
    public void remove(Card cardIn) {
        for (int i=0; i<CardsAL.size(); i++) {
            if (cardIn.equals((Card)CardsAL.get(i))) {
                CardsAL.remove(i);
            }
        }
    }
    
    public void remove(String cardString) throws Exception {
        remove(new Card(cardString));
    }    

    public Card get(int i) {
        return (Card) CardsAL.get(i);
    }
    
    public int size() {
        return CardsAL.size();
    }
    
    public ArrayList getArrayList() {
        return CardsAL;
    }
    
    public String print() {
        String board="";
        for (int c=0; c<CardsAL.size(); c++) 
            board+=((Card)(CardsAL.get(c))).getCardShortestName()+" ";
        return board;
    }
    
    public void println() {
        System.out.println(print());
    }
    
    public void shuffle() {
        for (int i=0; i<100; i++) {
            for (int c=0; c<CardsAL.size(); c++) {
                int rand=(int) Math.round(Math.random()*51);
                Card temp =(Card) CardsAL.get(c);
                CardsAL.set(c, CardsAL.get(rand));
                CardsAL.set(rand, temp);
            }
        }
    }
    
}
