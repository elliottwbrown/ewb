package PokerCommons.PokerObjects.GameProtocols.LimitHoldemCash;

import java.util.TreeMap;
import ca.ualberta.cs.poker.Hand;
import ca.ualberta.cs.poker.HandEvaluator;
import PokerCommons.PokerObjects.Players.Player;
import PokerCommons.PokerObjects.Cards.Cards;
import PokerCommons.PokerObjects.Cards.Deck;
import PokerCommons.PokerObjects.Utilities.JudgeWinner;

public class Game extends GameImplementation implements GameValues {

    public static final int version = 1;
    public static int smallBlindAmt = 10;
    public static int bigBlindAmt = 20;
    public static int[] raiseAmt = {0, 20, 20, 40, 40};
    public static final int turnLength = 60000;
    public static final int maxRaisesPerRound = 4;
    public static boolean keepPlayingHands = true, waitingForTurn = false;
    private static String balancesLoggerFileName = "c:\\temp\\Balances.log";//,
    //gamesLoggerFileName="c:\\temp\\Games.log";

    public static void startGame() throws Exception {
        handEval = new HandEvaluator();
        gameStatus = GAME_STARTED;
        gameStart = new java.util.Date();
        balancesLogger = new com.ewb.Logging.Logger(balancesLoggerFileName);
        //allCardsLogger=new com.ewb.Logging.Logger(allCardsLoggerFileName);
        //gamesLogger=new com.ewb.Logging.Logger(gamesLoggerFileName);
        setRandomDealer();
        startHand();
    }

    public static void resetGame() throws Exception {
        System.out.println("resetGame");
        players = new TreeMap();
        handStatus = HAND_NOT_STARTED;
        gameStatus = GAME_NOT_STARTED;
        gameID = 0;
    }

    public static void setBlinds() throws Exception {
        smallBlindAmt = 10;
        bigBlindAmt = 20;
        raiseAmt[1] = raiseAmt[2] = 20;
        raiseAmt[3] = raiseAmt[4] = 40;
    }

    public static void startHand() throws Exception {
        if (debugLevel>1) System.out.println("Starting Hand "+gameID);
        deck = new Deck();
        deck.shuffle();

        handStatus = HAND_STARTED;
        roundNo = DEAL;
        pot = 0;
        handHistory = "Hand no. " + (++gameID) + " started at " + new java.util.Date() + "\n";
        //handHistory += " " + (10 - (gameID % 10)) + " hands until next level.\n";
        roundHistory = "";
        communityCards = new Cards();

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

        handHistory += "  " + smallBlind.getName() + " posted SB $" + smallBlindAmt + "\n";
        handHistory += "  " + bigBlind.getName() + " posted BB $" + bigBlindAmt + "\n";

        for (Player p : players.values()) {   // all owe big blind
            p.setInCurrentHand(true);
            p.setCallAmt(bigBlindAmt);
        }

        smallBlind.setCallAmt(bigBlindAmt - smallBlindAmt);  // except small blind - who owes only difference
        bigBlind.setCallAmt(0);  // and except big blind who owes nothing
        currentPlayer = getPlayerToTheLeft(bigBlind);

//        if (debugLevel>1) {
//            gamesLogger.log(">>>>>>>>> startHand:"+gameID);
//            gamesLogger.log(" players="+players.size());
//            gamesLogger.log(" Dealer:"+dealer.getName());
//            gamesLogger.log(" Small Blind:"+smallBlind.getName());
//            gamesLogger.log(" Big Blind:"+bigBlind.getName());
//            gamesLogger.log(" Next to play:"+currentPlayer.getName());
//        }

        waitingForTurn = true;
        startRound();
    }

    public static void startRound() throws Exception {
//        if (debugLevel>1) gamesLogger.log(" startRound "+roundNames[roundNo]);
        numRaisesThisRound = 0;
        turnHistory = "";
        turnStart = new java.util.Date();
        turnEnd = new java.util.Date(turnStart.getTime() + turnLength);
        for (Player p : players.values()) {
            p.setBetThisRound(false);
        }
        for (Player p : players.values()) {
            p.setNumberRaises(0);
        }
        if (roundNo < END_OF_HAND) {
            if (roundNo > DEAL) {
                for (Player p : players.values()) {
                    p.setCallAmt(0);
                }
                currentPlayer = getPlayerInHandToTheLeft(dealer);
            }
            if (roundNo == DEAL) {
                dealHoleCards();
            }
            if (roundNo == FLOP) {
                dealFlop();
            }
            if (roundNo == TURN) {
                dealTurn();
            }
            if (roundNo == RIVER) {
                dealRiver();
            }
        }
        handHistory += " " + roundNames[roundNo] + "\n";
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
                }
            }
            handHistory += " Winner: " + winner.getName() + " wins " + pot + "\n";
            winningCardsInformation += winner.getHoleCards().print() + "\n";
            winner.setBalance(winner.getBalance() + pot);
        } else {
            handHistory += " Showdown." + "\n";
            boolean[] winners = null;
            int c = 0;
            for (Player p : players.values()) {
                if (p.isInCurrentHand()) {
                    c++;
                }
            }
            Cards[] hands = new Cards[c];
            c = 0;
            for (Player p : players.values()) {
                if (p.isInCurrentHand()) {
                    hands[c++] = p.getHoleCards();
                }
            }
            try {
                winners = JudgeWinner.go(communityCards, hands);
            } catch (Exception e) {
                e.printStackTrace();
            }
            c = 0;
            int numWinners = 0;
            for (Player p : players.values()) {
                if (p.isInCurrentHand()) {
                    Hand hand1 = new Hand(p.getHoleCards().print() + " " + communityCards.print());
                    handHistory += "  " + p.getName() + " had " + p.getHoleCards().print() + " (" + handEval.nameHand(hand1) + ") \n";
                    if (winners[c++]) {
                        numWinners++;
                    }
                }
            }
            if (numWinners == 1) {
                handHistory += "  There is one winner." + "\n";
            } else {
                handHistory += "  There are " + numWinners + " winners." + "\n";
            }
            c = 0;
            for (Player p : players.values()) {
                allCardsInformation += p.getHoleCards().print();
                if (p.isInCurrentHand() && winners[c++]) {
                    handHistory += " Winner: " + p.getName() + " wins " + (pot / numWinners) + "\n";
                    winningCardsInformation += p.getHoleCards().print() + ",";
                    allCardsInformation += "x";
                    p.setBalance(p.getBalance() + (pot / numWinners));
                }
                allCardsInformation += "\n";
            }
            winningCardsInformation += "\n";
        }
//        if (debugLevel>0) {
//            gamesLogger.log(handHistory);
//            gamesLogger.log(" Cards");
//            gamesLogger.log("  community cards:"+communityCards.print()+"\n");
//            for (Player p : players.values())
//                gamesLogger.log("  "+p.getName()+" held "+p.getHoleCards().print()+" \n");
//        }

        String finishingBalances = "";
        for (Player p : players.values()) {
            finishingBalances += gameID + "," + p.getName() + "," + p.getBalance() + "\r\n";
        }
        balancesLogger.log(finishingBalances);
        //allCardsLogger.log(allCardsInformation);
        //Thread.sleep(10000);

//        if (smallBlind.getBalance()<=0) handHistory+="   Winner: "+bigBlind.getName();
//        if (bigBlind.getBalance()<=0) handHistory+="   Winner: "+smallBlind.getName();
//        if (bigBlind.getBalance()<=0 || smallBlind.getBalance()<=0) gameStatus=GAME_FINISHED;
    }

    public static void processTurn(int turn) throws Exception {
//        if (debugLevel>1) gamesLogger.log("  processTurn "+roundNames[roundNo]+" from "+currentPlayer.getName()+":"+actionLabels[turn]);
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
    }

    private static void processRaise() throws Exception {
        if (currentPlayer.getBalance() > 0) {
            currentPlayer.setNumberRaises(currentPlayer.getNumberRaises() + 1);
            if (currentPlayer.getCallAmt() == 0f) {
                handHistory += "  " + currentPlayer.getName() + " bet " + raiseAmt[roundNo] + "\n";
            } else {
                handHistory += "  " + currentPlayer.getName() + " raised " + raiseAmt[roundNo] + "\n";
            }
            betAllPlayers(currentPlayer.getSessionID(), raiseAmt[roundNo]);
            currentPlayer.adjustBalance(-1 * (currentPlayer.getCallAmt() + raiseAmt[roundNo]));
            pot += currentPlayer.getCallAmt() + raiseAmt[roundNo];
            currentPlayer.setCallAmt(0);
            turnHistory +=
                    "<bet>"
                    + "<player/>" + currentPlayer.getSessionID()
                    + "<amt/>" + (currentPlayer.getCallAmt() + raiseAmt[roundNo])
                    + "</bet>";
        } else {
            processCheckFold();
        }
    }

    private static void processCall() {
        if (currentPlayer.getCallAmt() == 0f) {
            handHistory += "  " + currentPlayer.getName() + " checked.\n";
            turnHistory +=
                    "<check>"
                    + "<player/>" + currentPlayer.getSessionID()
                    + "</check>";
        } else {
            if (currentPlayer.getBalance() > currentPlayer.getCallAmt()) {
                handHistory += "  " + currentPlayer.getName() + " called " + currentPlayer.getCallAmt() + "\n";
                turnHistory +=
                        "<call>"
                        + "<player/>" + currentPlayer.getSessionID()
                        + "<amt/>" + (currentPlayer.getCallAmt())
                        + "</call>";
                currentPlayer.adjustBalance(-1 * currentPlayer.getCallAmt());
                pot += currentPlayer.getCallAmt();
                currentPlayer.setCallAmt(0);
            } else {
                // all in
                int balance = currentPlayer.getBalance();
                handHistory += "  " + currentPlayer.getName() + " goes all-in for " + balance + "\n";
                turnHistory +=
                        "<call>"
                        + "<player/>" + currentPlayer.getSessionID()
                        + "<amt/>" + balance
                        + "</call>";
                currentPlayer.setBalance(0);
                pot += balance;
                int difference = currentPlayer.getCallAmt() - balance;
                pot -= balance;
                handHistory += "  " + difference + " returned to \n";
                currentPlayer.setCallAmt(0);
                if (roundNo == DEAL) {
                    dealFlop();
                    dealTurn();
                    dealRiver();
                }
                if (roundNo == FLOP) {
                    dealTurn();
                    dealRiver();
                }
                if (roundNo == TURN) {
                    dealRiver();
                }
                roundNo = RIVER;
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
            handHistory += "  " + currentPlayer.getName() + " checked.\n";
        } else {
            currentPlayer.setInCurrentHand(false);
            turnHistory +=
                    "<fold>"
                    + "<player>" + currentPlayer.getSessionID()+"</player>"
                    + "</fold>";
            handHistory += "  " + currentPlayer.getName() + " folded.\n";
        }
    }
}
