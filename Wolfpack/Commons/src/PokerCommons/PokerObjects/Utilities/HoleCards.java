package PokerCommons.PokerObjects.Utilities;

public class HoleCards {
    
    public HoleCards(int faceHigher, int faceLower, boolean suited) {
        this.faceHigher=faceHigher;
        this.faceLower=faceLower;
        this.suited=suited;
    }
    
    public boolean equals(HoleCards key) {
        boolean equal=true;
        if (suited!= key.suited) equal=false;
        if (faceHigher!= key.faceHigher) equal=false;
        if (faceLower!= key.faceLower) equal=false;
        return equal;
    }
    
    public int faceHigher;
    public int faceLower;
    public boolean suited;
}

