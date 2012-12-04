package PokerCommons.PokerObjects.Utilities;

import PokerCommons.PokerObjects.Cards.Cards;


public class JudgeWinner {
    
    public static boolean[] go(Cards[] hands) throws Exception {
            boolean winners[]={false,false};
            if (hands[0].get(0).Face>hands[1].get(0).Face) winners[0]=true;
            if (hands[0].get(0).Face<hands[1].get(0).Face) winners[1]=true;
            if (hands[0].get(0).Face==hands[1].get(0).Face) {
                winners[0]=true;
                winners[1]=true;
            }
    
        return winners;
    }

    public static boolean[] go(Cards community,Cards[] hands) throws Exception {
        int winner=-1;
        int second=-1;
        int[] highestHand={-1,-1,-1};
        int[][] ranks=new int[4][3];
        for (int i=0; i<hands.length; i++) {
            ranks[i]=GetBest5.go(community,hands[i]);
            if (ranks[i][0]>highestHand[0]) {
                highestHand=ranks[i];
            } else if (ranks[i][0]==highestHand[0]) {
                if (ranks[i][1]>highestHand[1]) {
                    highestHand=ranks[i];
                } else if (ranks[i][1]==highestHand[1]) {
                    if (ranks[i][2]>highestHand[2]) {
                        highestHand=ranks[i];
                        winner=i;
                    }
                }
            }
        }
        boolean winners[]={false,false,false,false};
        //System.out.println("winner"+highestHand[0]+":"+highestHand[1]+":"+highestHand[2]+" ");
        for (int i=0; i<4; i++) if (ranks[i][0]==highestHand[0] && ranks[i][1]==highestHand[1] && ranks[i][2]==highestHand[2]) winners[i]=true;
        
//        System.out.print(ranks[0][0]+":"+ranks[0][1]+":"+ranks[0][2]+" ");
//        System.out.print(ranks[1][0]+":"+ranks[1][1]+":"+ranks[1][2]+" ");
//        System.out.print(ranks[2][0]+":"+ranks[2][1]+":"+ranks[2][2]+" ");
//        System.out.print(ranks[3][0]+":"+ranks[3][1]+":"+ranks[3][2]+" ");
//        System.out.println();

        return winners;
    }
}
