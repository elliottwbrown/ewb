package PokerCommons.PokerObjects.Cards;

public class Card {
    
    public int Face;
    public int Suit;
    
    public Card(int FaceIn, int SuitIn) {
        Face=FaceIn;
        Suit=SuitIn;
    }
    
    public Card(String cardString) throws Exception {
        Face=-1;
        Suit=-1;
        if (cardString.length()!=2) throw new Exception();
        for (int currFace=0; currFace<face.faceShortNames.length; currFace++)
            if (face.faceShortNames[currFace].equals(cardString.substring(0,1))) Face=currFace;
        for (int currSuit=0; currSuit<suit.suitShortNames.length; currSuit++)
            if (suit.suitShortNames[currSuit].equals(cardString.substring(1,2))) Suit=currSuit;
        if ((Face==-1) || (Suit==-1)) throw new Exception();
    }
    
    public String getCardShortestName() {
        return face.faceShortNames[Face]+suit.suitShortNames[Suit];
    }
    
    public String getCardShortName() {
        return face.faceShortNames[Face]+" of "+suit.suitShortNames[Suit];
    }
    
    public boolean equals(Card cardIn) {
        return (cardIn.Face==Face && cardIn.Suit==Suit);
    }
    
    public String print() {
       return getCardShortestName();
    }
    
    public void println() {
        System.out.println(getCardShortestName());
    }
}
