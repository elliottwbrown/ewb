package PokerCommons.PokerObjects.Utilities;

import PokerCommons.PokerObjects.Cards.Card;

public class HoleCardKeys {

    public static int getIntKeyFromCards(final Card card1, final Card card2) {
        boolean suited=false;
        if (card1.Suit==card2.Suit) suited=true;
        int face1=0,face2=0;
        if (card1.Face>card2.Face) {
            face1=card1.Face;
            face2=card2.Face;
        } else {
            face2=card1.Face;
            face1=card2.Face;
        }
        int key = getIntKeyFromInts(face1, face2, suited);
        return key;
    }
    
    public static int getIntKeyFromInts(final int face1, final int face2, final boolean suited) {
        int faceHigher=0,faceLower=0;
        if (face1>face2) {
            faceHigher=face1;
            faceLower=face2;
        } else {
            faceHigher=face2;
            faceLower=face1;
        }
        int key=faceHigher*100+faceLower;
        if (suited) key+=10000;
        return key;
    }
    
    public static HoleCards getHoleCards(int key) {
        boolean suited=key>=10000;
        if (suited) key-=10000;
        int faceLower=key % 100;
        int faceHigher=(key-faceLower)/100;
        HoleCards ck=new HoleCards(faceHigher,faceLower,suited);
        return ck;
    }
        
}
