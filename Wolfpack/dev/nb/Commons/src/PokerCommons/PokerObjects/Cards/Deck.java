package PokerCommons.PokerObjects.Cards;

public class Deck extends Cards {
    
    public Deck() {
        super();
        for (int currFace=face.TWO; currFace<=face.ACE; currFace++) {
            for (int currSuit=suit.CLUBS; currSuit<=suit.SPADES; currSuit++) {
                add(new Card(currFace,currSuit));
            }
        }
    }
    
    public Deck(Deck deck) {
        for (int c=0;c<deck.size();c++) {
            add(deck.get(c));
        }
    }
}
