package PokerCommons.PokerObjects.Utilities;

import PokerCommons.PokerObjects.Cards.Card;
import PokerCommons.PokerObjects.Cards.Cards;

public class GetBest5 {
    
    public static void main(String args[]) throws Exception {
        System.out.println("starting");
       
        Cards hand1=new Cards();
        hand1.add(new Card("5H"));
        hand1.add(new Card("TC"));
        
        Cards hand2=new Cards();
        hand2.add(new Card("KC"));
        hand2.add(new Card("9D"));
        
        Cards community=new Cards();
        community.add(new Card("AC"));
        community.add(new Card("AS"));
        community.add(new Card("4C"));
        community.add(new Card("QC"));
        community.add(new Card("5C"));

        int[] score=null;
        score=go(community,hand1);
        System.out.println(score[0]+":"+score[1]+":"+score[2]);
        
        score=go(community,hand2);
        System.out.println(score[0]+":"+score[1]+":"+score[2]);
    }
    
    public static int[] go(Cards hand) throws not5CardsException,Exception {
        int[] score={-1,-1,-1};
        return score;        
    }
    
    public static int[] go(Cards communityCards,Cards hand) throws not5CardsException,Exception {
        int[] score={-1,-1,-1};
        if (communityCards.size()==4) score=getBest5From6(communityCards,hand);
        if (communityCards.size()==5) score=getBest5From7(communityCards,hand);
        return score;
    }
    
    public static int[] getBest5From7(Cards communityCards,Cards hand) throws not5CardsException,Exception {
        int[] highestRanking={-1,-1,-1};
        int[] ranking;
        
        Cards sevenCards = new Cards(communityCards);
        sevenCards.add(hand);
        Cards highestRankedHand = new Cards();
        
        for (int i=0 ; i<combinationsOf5from7.length ; i++) {
            String currentCombinationString=combinationsOf5from7[i];
            Cards testHand = new Cards();
            for (int c=0; c<currentCombinationString.length(); c++) {
                int cardNo=Integer.parseInt(currentCombinationString.substring(c,c+1))-1;
                testHand.add(sevenCards.get(cardNo));
            }
            //float compScore=score[0]*10000000+score[1]*1000+(score[2]/10000);
            
            ranking = HandScorer.go(testHand);
            
            if (ranking[0]>highestRanking[0]) {
                highestRanking=ranking;
            } else if (ranking[0]==highestRanking[0]) {
                if (ranking[1]>highestRanking[1]) {
                    highestRanking=ranking;
                } else if (ranking[1]==highestRanking[1]) {
                    if (ranking[2]>highestRanking[2]) highestRanking=ranking;
                }
            }
        }
        return highestRanking;
    }
    
    public static int[] getBest5From6(Cards communityCards,Cards hand) throws not5CardsException,Exception {
        int[] highestRanking={-1,-1,-1};
        int[] ranking;
        
        Cards sevenCards = new Cards(communityCards);
        sevenCards.add(hand);
        Cards highestRankedHand = new Cards();
        
        for (int i=0 ; i<combinationsOf5from6.length ; i++) {
            String currentCombinationString=combinationsOf5from6[i];
            Cards testHand = new Cards();
            for (int c=0; c<currentCombinationString.length(); c++) {
                int cardNo=Integer.parseInt(currentCombinationString.substring(c,c+1))-1;
                testHand.add(sevenCards.get(cardNo));
            }
            ranking = HandScorer.go(testHand);
            
            if (ranking[0]>highestRanking[0]) {
                highestRanking=ranking;
            } else if (ranking[0]==highestRanking[0]) {
                if (ranking[1]>highestRanking[1]) {
                    highestRanking=ranking;
                } else if (ranking[1]==highestRanking[1]) {
                    if (ranking[2]>highestRanking[2]) highestRanking=ranking;
                }
            }
        }
        return highestRanking;
    }
    
    private static Cards CARDS;
    private static String combinationsOf5from6[]={"12345","12346","12356","12456","13456","23456"};
    private static String combinationsOf5from7[]={
        "12345","12346","12356","12456","13456","23456","12347",
        "12357","12457","13457","23457","12367","12467","12567",
        "13467","13567","14567","23467","23567","24567","34567"};
}
