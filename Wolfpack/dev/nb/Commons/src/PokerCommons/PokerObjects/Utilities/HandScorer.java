package PokerCommons.PokerObjects.Utilities;

import PokerCommons.PokerObjects.Cards.Cards;
import PokerCommons.PokerObjects.Cards.face;
import java.util.*;

public class HandScorer {
    
    public static int[] go(Cards myCards) throws Exception {
        int[] fullScore=new int[3];
        if (myCards.size()==5) {
            //myCards.println();
            int size=myCards.size();
            int[] score={-1,0,0};
            ArrayList faces= new ArrayList();
            ArrayList suits= new ArrayList();
            for (int i=0; i<size; i++) faces.add(myCards.get(i).Face);
            for (int i=0; i<size; i++) suits.add(myCards.get(i).Suit);
            Collections.sort(faces);
            Collections.sort(suits);
            ArrayList facesCopy=(ArrayList) faces.clone();
            ArrayList suitsCopy=(ArrayList) suits.clone();
            
            // pairs, trips, quads
            score=checkForMatches(facesCopy);
            //System.out.println(score[0]+","+score[1]+","+score[2]);
            
            // two pair and full house
            facesCopy=(ArrayList) faces.clone();
            int[] tempScore={-1,0,0};
            if (score[0]==pokerHands.PAIR) tempScore=foundPairCheckTPFH(facesCopy);
            if (score[0]==pokerHands.THREE_OF_A_KIND) tempScore=foundTripCheckTPFH(facesCopy);
            if (tempScore[0]>score[0]) score=tempScore;
            //System.out.println(score[0]+","+score[1]+","+score[2]);
            
            // flushes straits, royal flushes and strait flushes
            if (score[0]==-1) {
                int flushScore=checkForFlushes(suitsCopy);
                int straitScore=checkForStraits(facesCopy);
                if (flushScore>0 && straitScore>0) {
                    if (checkForAceAndKing(faces)) {
                        score[0]=pokerHands.ROYAL_FLUSH;
                    } else {
                        score[0]=pokerHands.STRAIT_FLUSH;
                    }
                } else if (flushScore>0) { score[0]=flushScore;
                } else if (straitScore>0) { score[0]=straitScore; }
            }
            
            // high myCards
            score[2]=kickers(faces);
            
            // if strait with low ace - adjust kickers score to reflect low ace
            if ((score[0]==pokerHands.STRAIT || score[0]==pokerHands.STRAIT_FLUSH) && checkForAce(faces) && !checkForAceAndKing(faces)) {
                score[2]=1575418;
            }
            if (score[0]==-1) score[0]=0;
            
            fullScore[0]=score[0];
            fullScore[1]=score[1];
            fullScore[2]=score[2];
        } else {
            fullScore[0]=-1;
            fullScore[1]=-1;
            fullScore[2]=-1;
        }
        //System.out.println(fullScore[0]+","+fullScore[1]+","+fullScore[2]);
        return fullScore;
    }
    
    public static synchronized int[] checkForMatches(ArrayList faces) {
        int rawHandScore=-1,sizeOfMatchScore=0,lastScore=-1;
        ArrayList currList = new ArrayList();
        int size=faces.size();
        for (int x=0; x<size;x++) {
            int firstFace= (Integer) faces.remove(0);
            currList = new ArrayList();
            currList.add(firstFace);
            for (int i=0; i<faces.size(); i++) {
                if ((Integer)faces.get(i)==firstFace) {
                    currList.add(faces.remove(i));
                    i--;
                    x++;
                }
            }
            if (currList.size()==2) rawHandScore =pokerHands.PAIR;
            if (currList.size()==3) rawHandScore =pokerHands.THREE_OF_A_KIND;
            if (currList.size()==4) rawHandScore =pokerHands.FOUR_OF_A_KIND;
            if (rawHandScore>lastScore) {
                lastScore=rawHandScore;
                sizeOfMatchScore=(Integer)currList.get(0)+2;
            }
        }
        int[] score={rawHandScore,sizeOfMatchScore,0};
        return score;
    }
    
    public static int[] foundPairCheckTPFH(ArrayList faces) {
        int rawHandScore=-1,sizeOfMatchScore=0;
        int firstPairFace=getPairFace(faces);
        if (faces.size()>2) {
            ArrayList tempmyCards=removePair(faces);
            int tripFace=getTripFace(faces);
            int[] tempScore=checkForMatches(tempmyCards);
            if (tempScore[0]==pokerHands.THREE_OF_A_KIND) {
                rawHandScore=pokerHands.FULL_HOUSE;
                sizeOfMatchScore=(tripFace+2)*13+tempScore[1];
            } else if (tempScore[0]==pokerHands.PAIR) {
                rawHandScore=pokerHands.TWO_PAIR;
                if (tempScore[1]>firstPairFace) {
                    sizeOfMatchScore=tempScore[1]*13+firstPairFace;
                } else if (tempScore[1]<firstPairFace){
                    sizeOfMatchScore=(firstPairFace+1)*13+tempScore[1];
                } else if (tempScore[1]==firstPairFace){
                    System.out.println("tp exce");
                }
            }
        }
        int[] score={rawHandScore,sizeOfMatchScore,0};
        return score;
    }
    
    public static int[] foundTripCheckTPFH(ArrayList faces) {
        int rawHandScore=-1,sizeOfMatchScore=0;
        if (faces.size()>2) {
            sizeOfMatchScore=(getTripFace(faces)+2)*13;
            ArrayList tempmyCards=removeTrip(faces);
            int[] tempScore=checkForMatches(tempmyCards);
            if (tempScore[0]==pokerHands.PAIR) {
                rawHandScore=pokerHands.FULL_HOUSE;
                sizeOfMatchScore+=tempScore[1];
            }
        }
        int[] score={rawHandScore,sizeOfMatchScore,0};
        return score;
    }
    
    public static int checkForFlushes(ArrayList suits) {
        for (int i=1; i<suits.size(); i++){
            if ((Integer)suits.get(i)!=(Integer)suits.get(i-1)) return 0;
        }
        return pokerHands.FLUSH;
    }
    
    public static int checkForStraits(ArrayList faces) {
        boolean strait=true;
        for (int i=1; i<faces.size(); i++) if ((Integer)faces.get(i)!=(Integer)faces.get(i-1)+1) strait=false;
        if (!strait && checkForAce(faces))
            if ((Integer) faces.get(0)==0 && (Integer) faces.get(1)==1
                && (Integer) faces.get(2)==2 && (Integer) faces.get(3)==3) strait=true;
        if (strait) return pokerHands.STRAIT;
        else return 0;
    }
    
    public static boolean checkForAceAndKing(ArrayList faces) {
        boolean king=false;
        boolean ace=false;
        for (int i=1; i<faces.size(); i++) {
            if ((Integer)faces.get(i)==face.ACE) ace=true;
            if ((Integer)faces.get(i)==face.KING) king=true;
        }
        return (ace && king);
    }
    
    public static boolean checkForAce(ArrayList faces) {
        boolean ace=false;
        for (int i=1; i<faces.size(); i++) {
            if ((Integer)faces.get(i)==face.ACE) ace=true;
        }
        return (ace);
    }
    
    public static ArrayList removePair(ArrayList faces) {
        
        for (int c=0; c<faces.size(); c++) {
            int currFace= (Integer) faces.get(c);
            for (int i=c+1; i<faces.size(); i++) {
                if (((Integer) faces.get(i))==currFace) {
                    if (getTripFace(faces)!=faces.get(c)) {
                        faces.remove(c);
                        faces.remove(i-1);
                        return faces;
                    }
                }
            }
        }
        
        return null;
    }
    
    public static int getPairFace(ArrayList faces) {
        for (int c=0; c<faces.size(); c++) {
            int currFace= (Integer) faces.get(c);
            for (int i=c+1; i<faces.size(); i++) {
                if (((Integer) faces.get(i))==currFace) {
                    if (getTripFace(faces)!=faces.get(c)) {
                        return (Integer) faces.get(i);
                    }
                }
            }
        }
        return -1;
    }
    
    public static ArrayList removeTrip(ArrayList faces) {
        
        for (int c=0; c<faces.size(); c++) {
            int currFace= (Integer) faces.get(c);
            for (int i=c+1; i<faces.size(); i++) {
                if (((Integer) faces.get(i))==currFace) {
                    for (int z=i+1; z<faces.size(); z++) {
                        if (((Integer) faces.get(z))==currFace) {
                            faces.remove(c);
                            faces.remove(i-1);
                            faces.remove(z-2);
                            return faces;
                        }
                    }
                }
            }
        }
        
        return null;
    }
    
    public static int getTripFace(ArrayList faces) {
        for (int c=0; c<faces.size(); c++) {
            int currFace= (Integer) faces.get(c);
            for (int i=c+1; i<faces.size(); i++) {
                if (((Integer) faces.get(i))==currFace) {
                    for (int z=i+1; z<faces.size(); z++) if (((Integer) faces.get(z))==currFace) return currFace;
                }
            }
        }
        return -1;
    }
    
    public static int kickers(ArrayList faces) {
        int score=0;
        for (int i=0; i<faces.size(); i++) {
            int face=(Integer)faces.get(i);
            score+=(int) Math.pow(13,i+1)*((Integer) face+1);
            //System.out.println(face);
        }
        return score;
    }
    
}
