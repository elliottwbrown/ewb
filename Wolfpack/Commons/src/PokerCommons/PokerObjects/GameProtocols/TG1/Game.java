package PokerCommons.PokerObjects.GameProtocols.TG1;

import java.util.TreeMap;
import ca.ualberta.cs.poker.Hand;
import ca.ualberta.cs.poker.HandEvaluator;
import PokerCommons.PokerObjects.Players.Player;
import PokerCommons.PokerObjects.Cards.Cards;
import PokerCommons.PokerObjects.Cards.Deck;
import PokerCommons.PokerObjects.Utilities.JudgeWinner;

public class Game extends GameImplementation implements GameValues {

    public static final int version = 1;
    public static int smallBlindAmt = 1;
    public static int bigBlindAmt = 2;
    public static int[] raiseAmt = {0, 2,2};
    public static final int turnLength = 60000;
    public static final int maxRaisesPerRound = 4;
    public static boolean keepPlayingHands = true, waitingForTurn = false;
    private static String balancesLoggerFileName = "c:\\temp\\Balances.log",
    gamesLoggerFileName="c:\\temp\\Games2.log";

    public static void startGame() throws Exception {
        handEval = new HandEvaluator();
        gameStatus = GAME_STARTED;
        gameStart = new java.util.Date();
        balancesLogger = new com.ewb.Logging.Logger(balancesLoggerFileName);
        gamesLogger=new com.ewb.Logging.Logger(gamesLoggerFileName);
        for (Player p : players.values()) {
            gamesLogger.log("Source Code Starts "+p.getName()+"\r\n");
            gamesLogger.log(p.getCode()+"\r\n");
            gamesLogger.log("Source Code Ends\r\n");
        }
        gamesLogger.log("Hand Histories Begin\r\n");
        setRandomDealer();
        startHand();
    }

    public static void resetGame() throws Exception {
        System.out.println("resetGame");
        players = new TreeMap();
        handStatus = HAND_NOT_STARTED;
        gameStatus = GAME_NOT_STARTED;
        handID = 0;
    }

    public static void setBlinds() throws Exception {
        smallBlindAmt = 1;
        bigBlindAmt = 2;
        raiseAmt[1] =2;

    }

    public static void startHand() throws Exception {
        //if (debugLevel>1) System.out.println("Starting Hand:"+gameID);
        deck = new Deck();
        deck.shuffle();

        handStatus = HAND_STARTED;
        roundNo = DEAL;
        pot = 0;
        handHistory = "Hand no. " + (++handID) + " started at " + new java.util.Date() + "\r\n";
        roundHistory = "";

        setBlinds();

        // choose small blind
        if (players.size() == 2) {
            smallBlind = dealer;
        } else {
            smallBlind = getPlayerToTheLeft(dealer);
        }

        smallBlind.setCallAmt(bigBlindAmt - smallBlindAmt);
        smallBlind.adjustBalance(-1 * smallBlindAmt);
        pot = smallBlindAmt;  // collect small blind

        bigBlind = getPlayerToTheLeft(smallBlind);  // collect bigBlind blind
        bigBlind.setCallAmt(0);
        bigBlind.adjustBalance(-1 * bigBlindAmt);
        pot += bigBlindAmt;

        handHistory += "  " + smallBlind.getName() + " posted SB $" + smallBlindAmt + "\r\n";
        handHistory += "  " + bigBlind.getName() + " posted BB $" + bigBlindAmt + "\r\n";

        for (Player p : players.values()) {   // all owe big blind
            p.setInCurrentHand(true);
            p.setCallAmt(bigBlindAmt);
            p.setWinner(false);
        }

        smallBlind.setCallAmt(bigBlindAmt - smallBlindAmt);  // except small blind - who owes only difference
        bigBlind.setCallAmt(0);  // and except big blind who owes nothing
        currentPlayer = getPlayerToTheLeft(bigBlind);

//        if (debugLevel>1) {
//            gamesLogger.log(">>>>>>>>> startHand:"+gameID+"\r\n");
//            gamesLogger.log(" players="+players.size()+"\r\n");
//            gamesLogger.log(" Dealer:"+dealer.getName()+"\r\n");
//            gamesLogger.log(" Small Blind:"+smallBlind.getName()+"\r\n");
//            gamesLogger.log(" Big Blind:"+bigBlind.getName()+"\r\n");
//            gamesLogger.log(" Next to play:"+currentPlayer.getName()+"\r\n");
//        }

        waitingForTurn = true;
        startRound();
    }

    public static void startRound() throws Exception {
        if (debugLevel>3) System.out.println("startRound:"+roundNo);
        numRaisesThisRound = 0;
        turnHistory = "";
        turnStart = new java.util.Date();
        turnEnd = new java.util.Date(turnStart.getTime() + turnLength);
        for (Player p : players.values()) p.setBetThisRound(false);
        for (Player p : players.values()) p.setNumberRaises(0);
        if (roundNo < END_OF_HAND) {
            if (roundNo > DEAL) {
                for (Player p : players.values()) {
                    p.setCallAmt(0);
                }
                currentPlayer = getPlayerInHandToTheLeft(dealer);
            }
            if (roundNo == DEAL) dealHoleCards();
        }
        handHistory += " " + roundNames[roundNo] + "\r\n";
    }

    public static void endRound() throws Exception {
        roundHistory += "<round><roundName>" + roundNames[roundNo] + "</roundName>" + turnHistory + "</round>";
        roundNo++;
        if (roundNo >= END_OF_HAND || countPlayersInGame() == 1) {
            endHand();
        } else {
            startRound();
        }
    }

    public static void endHand() throws Exception {
        handStatus = HAND_FINISHED;
        roundNo = END_OF_HAND;
        String allCardsInformation = "", winningCardsInformation = "";
        Player winner = null;

        // who won ?
        if (countPlayersInHand() == 1) {
            for (Player p : players.values()) {
                if (p.isInCurrentHand()) {
                    winner = p;
                    p.setWinner(true);
                }
            }
            handHistory += " Winner: " + winner.getName() + " wins " + pot + "\r\n";
            winningCardsInformation += winner.getHoleCards().print() + "\r\n";
            winner.setBalance(winner.getBalance() + pot);
        } else {
            handHistory += " Showdown." + "\r\n";
            boolean[] winners = null;
            int c = 0;
            for (Player p : players.values()) {
                if (p.isInCurrentHand()) {
                    c++;
                }
            }
            Cards[] hands = new Cards[c];
            c = 0;
            for (Player p : players.values()) 
                if (p.isInCurrentHand()) hands[c++] = p.getHoleCards();
            try {
                winners = JudgeWinner.go(hands);
            } catch (Exception e) {
                e.printStackTrace();
            }
            c = 0;
            int numWinners = 0;
            for (Player p : players.values()) {
                if (p.isInCurrentHand()) {
                    Hand hand1 = new Hand(p.getHoleCards().print());
                    handHistory += "  " + p.getName() + " had " + p.getHoleCards().print()+" \r\n";
                    if (winners[c++]) {
                        numWinners++;
                    }
                }
            }
            if (numWinners == 1) {
                handHistory += "  There is one winner." + "\r\n";
            } else {
                handHistory += "  There are " + numWinners + " winners." + "\r\n";
            }
            c = 0;
            for (Player p : players.values()) {
                allCardsInformation += p.getHoleCards().print();
                if (p.isInCurrentHand() && winners[c++]) {
                    handHistory += " Winner: " + p.getName() + " wins " + (pot / numWinners) + "\r\n";
                    winningCardsInformation += p.getHoleCards().print() + ",";
                    allCardsInformation += "x";
                    p.setBalance(p.getBalance() + (pot / numWinners));
                    p.setWinner(true);
                }
                allCardsInformation += "\r\n";
            }
            winningCardsInformation += "\r\n";
        }
        if (debugLevel>1) {
            gamesLogger.log(handHistory);
            for (Player p : players.values())
                if (!handHistory.contains("Showdown")) gamesLogger.log("  "+p.getName()+" had "+p.getHoleCards().print()+"\r\n");
        }

        String finishingBalances = "";
        for (Player p : players.values()) {
            String s=p.getName() + "," + p.getBalance() + "\r\n";
            finishingBalances += s;
            balancesLogger.log(handID + ","+ s);
            gamesLogger.log(handID+","+s);
        }
        gamesLogger.log("End of Hand"+"\r\n");
        //allCardsLogger.log(allCardsInformation);
        //Thread.sleep(10000);

        //if (debugLevel>1) System.out.println("-------------------------------------------------");
        if (debugLevel>1) System.out.println(handHistory);
        if (smallBlind.getBalance()<=0) handHistory+="   Winner: "+bigBlind.getName();
        if (bigBlind.getBalance()<=0) handHistory+="   Winner: "+smallBlind.getName();
//        if (bigBlind.getBalance()<=0 || smallBlind.getBalance()<=0) gameStatus=GAME_FINISHED;
    }

    public static void processTurn(int turn) throws Exception {
        //if (debugLevel>1) gamesLogger.log("  processTurn "+roundNames[roundNo]+" from "+currentPlayer.getName()+":"+actionLabels[turn]+"\r\n");
        currentPlayer.setBetThisRound(true);
        if (turn == CHECKFOLD) {
            processCheckFold();
        } else if (turn == CALL) {
            processCall();
        } else if (turn == RAISE) {
            numRaisesThisRound++;
            //if ((currentPlayer.getNumberRaises()+1)<=maxRaisesPerRound) processRaise();
            if (numRaisesThisRound <= maxRaisesPerRound) {
                processRaise();
            } else {
                processCall();
            }
        }
        if (countPlayersInGame() == 1) {
            endRound();
        } else if (isRemainingCalls()) {
            currentPlayer = getPlayerInHandToTheLeft(currentPlayer);
        } else {
            endRound();
        }
        if (currentPlayer.balance<=0) throw new Exception("Player Broke");
    }

    private static void processRaise() throws Exception {
        if (currentPlayer.getBalance() > 0) {
            currentPlayer.setNumberRaises(currentPlayer.getNumberRaises() + 1);
            if (currentPlayer.getCallAmt() == 0f) {
                handHistory += "  " + currentPlayer.getName() + " bet " + raiseAmt[roundNo] + "\r\n";
            } else {
                handHistory += "  " + currentPlayer.getName() + " raised " + raiseAmt[roundNo] + "\r\n";
            }
            betAllPlayers(currentPlayer.getSessionID(), raiseAmt[roundNo]);
            currentPlayer.adjustBalance(-1 * (currentPlayer.getCallAmt() + raiseAmt[roundNo]));
            pot += currentPlayer.getCallAmt() + raiseAmt[roundNo];
            currentPlayer.setCallAmt(0);
            turnHistory +=
                    "<bet>"
                    + "<player>" + currentPlayer.getSessionID()+"</player>"
                    + "<amt>" + (currentPlayer.getCallAmt() + raiseAmt[roundNo])+"</amt>"
                    + "</bet>";
        } else {
            processCheckFold();
        }
    }

    private static void processCall() {
        if (currentPlayer.getCallAmt() == 0f) {
            handHistory += "  " + currentPlayer.getName() + " checked.\r\n";
            turnHistory +=
                    "<check>"
                     + "<player>" + currentPlayer.getSessionID()+"</player>"
                    + "</check>";
        } else {
            if (currentPlayer.getBalance() > currentPlayer.getCallAmt()) {
                handHistory += "  " + currentPlayer.getName() + " called " + currentPlayer.getCallAmt() + "\r\n";
                turnHistory +=
                        "<call>"
                         + "<player>" + currentPlayer.getSessionID()+"</player>"
                        + "<amt>" + (currentPlayer.getCallAmt())+"</amt>"
                        + "</call>";
                currentPlayer.adjustBalance(-1 * currentPlayer.getCallAmt());
                pot += currentPlayer.getCallAmt();
                currentPlayer.setCallAmt(0);
            } else {
                // all in
                int balance = currentPlayer.getBalance();
                handHistory += "  " + currentPlayer.getName() + " goes all-in for " + balance + "\r\n";
                turnHistory +=
                        "<call>"
                        + "<player>" + currentPlayer.getSessionID()+"</player>"
                        + "<amt>" + balance+"</amt>"
                        + "</call>";
                currentPlayer.setBalance(0);
                pot += balance;
                int difference = currentPlayer.getCallAmt() - balance;
                pot -= balance;
                handHistory += "  " + difference + " returned to \n";
                currentPlayer.setCallAmt(0);
                roundNo = END_OF_HAND;
                //for (Player p : players.values()) p.setInCurrentHand(false);
            }
        }
    }

    private static void processCheckFold() {
        if (currentPlayer.getCallAmt() == 0f) {
            turnHistory +=
                    "<check>"
                    + "<player>" + currentPlayer.getSessionID()+"</player>"
                    + "</check>";
            handHistory += "  " + currentPlayer.getName() + " checked.\r\n";
        } else {
            currentPlayer.setInCurrentHand(false);
            turnHistory +=
                    "<fold>"
                    + "<player>" + currentPlayer.getSessionID()+"</player>"
                    + "</fold>";
            handHistory += "  " + currentPlayer.getName() + " folded.\r\n";
        }
    }
}
