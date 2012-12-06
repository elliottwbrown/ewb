package poker.ai.player;



import poker.Card;
import poker.Deck;
import poker.GameInfo;
import poker.Hand;
import poker.HandEvaluator;
import poker.Holdem;
import poker.Player;
import poker.ai.ProbTriple;
import poker.ai.model.WeightTable;
import poker.util.Preferences;
import poker.util.Randomizer;
import poker.util.Reporter;

/** 
 * ArodBot008 assigns a weight table to each player.  Uses
 * their initial preflop action and our preflop rules to
 * come up with probability of playing each 2-card combo.
 * The weight tables are used to make decisions later in the
 * game, but they are not updated based on later actions.
 * 
 * @author brian@chrissyandbrian.com
 */
public class ArodBot008 extends Player {

	// DONE: create own gi and fill it as we go so info is correct
	// DONE: change preflop to use local gi
	// DONE: change preflop to return a prob triple
	// DONE: create weight table for each player (including us)
	// DONE: use opponent-specific wt for decisions
	// DONE: fix how we play big-blind if there is a raise

	private static final String ALWAYS_CALL_MODE = "ALWAYS_CALL_MODE";

	private int ID; // our position ID for the current hand
	private Card c1, c2; // our hole cards
	private GameInfo gi; // general game information
	private Preferences prefs; // the configuration options for this bot

	private HandEvaluator he = new HandEvaluator();
	private Randomizer rand = new Randomizer();

	private int myPreflopGroup; //preflop group
	private WeightTable stdWT;
	private WeightTable[] wt;
	private GameInfo gi2; // this one is not sync'd with PPA
	private int orbit; // 0 is first orbit
	private volatile boolean flag;

	public ArodBot008() {
		stdWT = new WeightTable();
		stdWT.makeStdWeightTable();
		wt = new WeightTable[10];
		for (int i = 0; i < 10; i++) {
			wt[i] = new WeightTable();
		}
		gi2 = new GameInfo();
		flag = false;
	}
	
	
//** added	
	
	public void update(int action, int code) {}
/**	

	/**
	 * Get the current settings for this bot.
	 */
	public Preferences getPreferences() {
		return prefs;
	}

	/**
	 * Load the current settings for this bot.
	 */
	public void init(Preferences playerPrefs) {
		this.prefs = playerPrefs;
		//rand.setSeed(prefs.getIntPreference("RANDOM_SEED", 0));
	}

	/**
	 * An example setting for this bot. It can be turned into
	 * an always-call mode, or to a simple strategy.
	 * @return true if always-call mode is active.
	 */
//	public boolean getAlwaysCallMode() {
//		return prefs.getBooleanPreference(ALWAYS_CALL_MODE, false);
//	}

	/**
	 * @return true if debug mode is on.
	 */
	public boolean getDebug() {
		//return prefs.getBooleanPreference("DEBUG", false);
		return false;
	}

	/**
	 * print a debug statement.
	 */
	public void debug(String str) {
		if (getDebug()) {
			System.out.println(str);
		}
	}

	/**
	 * print a debug statement with no end of line character
	 */
	public void debugb(String str) {
		if (getDebug()) {
			System.out.print(str);
		}
	}

	/**
	 * Start playing a new game
	 * 
	 * @param gInfo the current game information
	 * @param c1 the first hole card
	 * @param c2 the second hole card
	 * @param ID the player's position
	 */
	public synchronized void newGame(
		GameInfo gInfo,
		Card c1,
		Card c2,
		int ID) {
		this.c1 = c1;
		this.c2 = c2;
		this.ID = ID;
		this.gi = gInfo;
		myPreflopGroup = PreflopGroup.preflopGroup(c1, c2);
		orbit = 0;



		if (gi2.getNumPlayers() == 0) {
			for (int i = 0; i < gi.getNumPlayers(); i++) {
				gi2.addPlayer(gi.getPlayerInfo(i).toString(), "");
			}
		}

		int lastButton = gi.getButton() - 1;
		if (lastButton == -1) {
			lastButton = gi.getNumPlayers() - 1;
		}
		gi2.setButton(lastButton);
		// startNewGame advances the button
		gi2.startNewGame();
		// startNewGame assigns button as current player
		gi2.advanceCurrentPlayer();
		debug("gi2 button is " + gi2.getPlayerName(gi2.getButton()));
	}

	/**
	 * An action has been observed. 
	 */
	public synchronized void actionEvent(int pos, int action, int amount) {
		flag = true;
		// if we are still active
		if (gi.activePlayer(ID)) {
			// note: gi2 has info prior to this action (this is a GOOD thing)
			// if ( preflop && first orbit )
			if ((gi.getStage() == Holdem.PREFLOP) && (orbit == 0)) {
				// if action is check, then all 1's weight table
				if (action == gi.U_CHECK) {
					wt[pos] = new WeightTable(1);
					// pray that Java's garbage collector works :-)
					// else if action is call or raise,
				} else if (
					(action == gi.U_CALL) || (action == gi.U_RAISE)) {
					// for each set of possible 2-card hands
					Card c1Local = new Card();
					Card c2Local = new Card();
					ProbTriple pt3 = new ProbTriple();
					int oppPreflopGroup = 9;
					for (int h1 = 0; h1 < 51; h1++) {
						c1Local.setIndex(h1);
						for (int h2 = h1 + 1; h2 < 52; h2++) {
							c2Local.setIndex(h2);
							// get probtriple and assign appropriate prob
							// raise or call action
							oppPreflopGroup =
								PreflopGroup.preflopGroup(c1Local, c2Local);
							pt3 = preFlopAction(gi2, pos, oppPreflopGroup);
							debug(pt3.toString());
							if (((c1 == c1Local) && (c2 == c2Local))
								|| ((c1 == c2Local) && (c2 == c1Local))) {
								wt[pos].setHandWeight(c1Local, c2Local, 0);
							} else if (action == gi.U_CALL) {
								wt[pos].setHandWeight(
									c1Local,
									c2Local,
									pt3.getCall());
							} else {
								wt[pos].setHandWeight(
									c1Local,
									c2Local,
									pt3.getRaise());
							}
						}
					}
				}
				if ((action == gi.U_CHECK)
					|| (action == gi.U_CALL)
					|| (action == gi.U_RAISE)) {
					debug(wt[pos].toStringSmall());
					wt[pos].normalize();
					debug("NORMALIZE...");
					debug(wt[pos].toStringSmall());
					wt[pos].scale();
					debug("SCALE");
					debug(wt[pos].toStringSmall());
					debug(
						gi.getPlayerName(pos)
							+ " most likely holds "
							+ wt[pos].chooseMaxHand());
				}
			}
			//	update gi2 with info from this action
//			switch (action) {
//				case gi.U_BBLIND :
//					{
//						gi2.bigBlind();
//						break;
//					}
//				case gi.U_SBLIND  :
//					{
//						gi2.smallBlind();
//						break;
//					}
//				case gi.U_CHECK :
//					{
//						gi2.call();
//						break;
//					}
//				case gi.U_CALL :
//					{
//						gi2.call();
//						break;
//					}
//				case gi.U_RAISE :
//					{
//						gi2.raise();
//						break;
//					}
//				default :
//					{
//						break;
//					}
//			}
			
			
			
			if (action==gi.U_BBLIND) {
			
						gi2.bigBlind();
						
					}
				else if (action==gi.U_SBLIND)
					{
						gi2.smallBlind();
					
					}
					else if (action==gi.U_CHECK)
					{
						gi2.call();
						
					}
				else if (action==gi.U_CALL)
					{
						gi2.call();
					
					}
				else if (action==gi.U_RAISE)
					{
						gi2.raise();
					
					}
			

			
			
			int BB = gi.getButton() + 2;
			if (BB > (gi.getNumPlayers() - 1)) {
				BB = BB - gi.getNumPlayers();
			}
			if ((pos == BB) && (action != gi.U_BBLIND)) {
				orbit++;
			}
			gi2.advanceCurrentPlayer();
		}
		flag = false;
		debug("flag set to false. finished processing actionEvent");
	}

	/**
	 * The game is over
	 */
	public void gameOverEvent() {
		gi2.gameOver();
	}

	/**
	* A new betting round has started.
	*/
	public void stageEvent(int stage) {
	}

	/**
	 * A showdown has occurred.
	 * @param pos the position of the player showing
	 * @param c1 the first hole card shown
	 * @param c2 the second hole card shown
	 */
	public void showdownEvent(int pos, Card c1, Card c2) {
	}

	/**
	 * A new game has been started.
	 * @param gi the game stat information
	 */
	public void gameStartEvent(GameInfo gi) {
	}

	/**
	 * A player has won money.
	 * @param pos the player position
	 * @param amount the amount won
	 * @param handName the name of the winning hand
	 */
	public void winEvent(int pos, int amount, String handName) {
	}

	/**
	 * Get the players next action. Uses simple probability triple generator
	 * to decide
	 * 
	 * @return the player's action
	 */
	public synchronized int action() {

		while (flag) {
			try {
				debug("flag set, waiting 200ms on action");
				Thread.sleep(200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

//		if (getAlwaysCallMode()) {
//			return Holdem.CALL;
//		}

		int a = Holdem.CALL;
		ProbTriple pt = new ProbTriple(0, 1, 0);
		double r = 0;
		if (gi.getStage() == Holdem.PREFLOP) {
			debug("Hand: [" + c1.toString() + "-" + c2.toString() + "] ");

			// if we have already acted once and someone raises behind us we will always call
			if (gi.playerCommitted(ID)) {
				pt.set(0, 1, 0);
			} else {
				pt = preFlopAction(gi, ID, myPreflopGroup);
			}
			r = rand.nextDouble();
			if (r < pt.getFold()) {
				a = Holdem.FOLD;
			} else if (r < (pt.getFold() + pt.getCall())) {
				a = Holdem.CALL;
			} else {
				a = Holdem.RAISE;
			}
		} else {
			a = postFlopAction();
		}

		if (a == Holdem.FOLD && gi.getAmountToCall(ID) == 0) {
			// don't ever fold if we don't have to
			a = Holdem.CALL;
		} else if (a == Holdem.RAISE && gi.getNumRaises()>2) {
			// if we can't raise, just call
			a = Holdem.CALL;
		}

		return a;
	}

	/**
	 * Decide what to do for a pre-flop action
	 */
	public synchronized ProbTriple preFlopAction(
		GameInfo giLocal,
		int IDlocal,
		int preflopGroupLocal) {

		ProbTriple pt1 = new ProbTriple(.75, .15, .10);

		if (giLocal.getNumPlayers() > 7) {
			// NOT SHORTHANDED
			//debug("Not shorthanded (8 or more)");

			// IDoffset: 0 = button,  1 = small blind, 2 = big blind, etc
			int IDoffset = 0;
			if (IDlocal > giLocal.getButton()) {
				IDoffset = IDlocal - giLocal.getButton();
			} else if (IDlocal < giLocal.getButton()) {
				IDoffset =
					IDlocal + giLocal.getNumPlayers() - giLocal.getButton();
			}

			//			debug(
			//				"IDlocal is "
			//					+ IDlocal
			//					+ ", Button is "
			//					+ giLocal.getButton()
			//					+ ", IDoffset is "
			//					+ IDoffset);

			if (((giLocal.getNumPlayers() > 8)
				&& (IDoffset < 6)
				&& (IDoffset > 2))
				|| ((giLocal.getNumPlayers() == 8)
					&& (IDoffset < 5)
					&& (IDoffset > 2))) {
				// EARLY POSITION
				//debug("Early position (first 3 after big blind, 1st 2 if only 8 players)");
				if (giLocal.getBetsToCall(IDoffset) > 2) {
					if (preflopGroupLocal == 1) {
						//Call any
						pt1.set(.02, .95, .03);
						return pt1;
					} else {
						pt1.set(.95, .04, .01);
						return pt1;
					}
				} else if (giLocal.getBetsToCall(IDoffset) > 1) {
					if (preflopGroupLocal < 3) {
						// Call2
						//debug("Cold call from mIDoffsetdle position: groups 1-2");
						pt1.set(.02, .95, .03);
						return pt1;
					} else {
						pt1.set(.95, .04, .01);
						return pt1;
					}
				} else { // no raisers, only callers if any
					if (preflopGroupLocal > 4) {
						//debug("Fold group 5 or greater from early position");
						pt1.set(.95, .04, .01);
						return pt1;
					} else if (preflopGroupLocal > 2) {
						//debug("Call groups 3 and 4 from early position");
						pt1.set(.02, .95, .03);
						return pt1;
					} else {
						//debug("Raise groups 1 and 2 from early position");
						pt1.set(.02, .08, .90);
						return pt1;
					}
				}
			} else if (
				(IDoffset == (giLocal.getNumPlayers() - 2))
					|| (IDoffset == (giLocal.getNumPlayers() - 3))
					|| (IDoffset == 6 && giLocal.getNumPlayers() == 10)) {
				// MIDDLE POSITION
				//debug("Middle position (10:6,7,8; 9:6,7; 8:5,6");
				if (giLocal.getBetsToCall(IDoffset) > 2) {
					if (preflopGroupLocal == 1) {
						//Call any
						pt1.set(.02, .95, .03);
						return pt1;
					} else {
						pt1.set(.95, .04, .01);
						return pt1;
					}
				} else if (giLocal.getBetsToCall(IDoffset) > 1) {
					if (preflopGroupLocal < 3) {
						// Call2
						//debug("Cold call from middle position: groups 1-2");
						pt1.set(.02, .95, .03);
						return pt1;
					} else {
						pt1.set(.95, .04, .01);
						return pt1;
					}
				} else { // no raisers, only callers if any
					if (preflopGroupLocal > 5) {
						//debug("Fold group 6 or greater from middle position");
						pt1.set(.95, .04, .01);
						return pt1;
					} else if (preflopGroupLocal > 2) {
						//debug("Call groups 3 thru 5 from middle position");
						pt1.set(.02, .95, .03);
						return pt1;
					} else {
						//debug("Raise groups 1 and 2 from middle position");
						pt1.set(.01, .01, .98);
						return pt1;
					}
				}
			} else if (
				(IDoffset == 0)
					|| (IDoffset == (giLocal.getNumPlayers() - 1))) {
				// LATE POSITION
				//debug("Late position (button and 1st to the right)");
				if (giLocal.getBetsToCall(IDoffset) > 2) {
					if (preflopGroupLocal == 1) {
						//Call any
						pt1.set(.02, .95, .03);
						return pt1;
					} else {
						pt1.set(.95, .04, .01);
						return pt1;
					}
				} else if (giLocal.getBetsToCall(IDoffset) > 1) {
					if (preflopGroupLocal < 5) {
						// Call2
						//debug("Cold call from late position: groups 1-4");
						pt1.set(.02, .95, .03);
						return pt1;
					} else {
						pt1.set(.95, .04, .01);
						return pt1;
					}
				} else { // only callers if any
					if (preflopGroupLocal < 5) {
						//debug("Raise from late position: groups 1-4");
						pt1.set(.05, .01, .94);
						return pt1;
					} else if (preflopGroupLocal > 6) {
						pt1.set(.95, .04, .01);
						return pt1;
					} else if (
						(IDoffset == 0 && giLocal.getNumActivePlayers() > 3)
							|| (giLocal.getNumActivePlayers() > 4)) {
						//debug("Call from late position: groups 5-6");
						pt1.set(.02, .95, .03);
						return pt1;
					} else { // we are first to act
						//debug("Steal blinds from late position: groups 5-6");
						pt1.set(.20, .01, .79);
						return pt1;
					}
				}
			} else {
				// LIVE BLINDS
				//debug("Live blind");
				if (((IDoffset == 1)
					&& (giLocal.getBetsToCall(IDoffset) > 1.5))
					|| ((IDoffset == 2)
						&& (giLocal.getBetsToCall(IDoffset) > 1))) {
					if (preflopGroupLocal == 1) {
						//Call any
						pt1.set(.01, .79, .20);
						return pt1;
					} else {
						pt1.set(.95, .04, .01);
						return pt1;
					}
				} else if (
					((IDoffset == 1) && (giLocal.getBetsToCall(IDoffset) > .5))
						|| ((IDoffset == 2)
							&& (giLocal.getBetsToCall(IDoffset) > 0))) {
					if (preflopGroupLocal < 3) {
						// Call raiser
						//debug("Call raiser from blinds: groups 1-2");
						pt1.set(.01, .79, .20);
						return pt1;
					} else {
						pt1.set(.95, .04, .01);
						return pt1;
					}
				} else // only callers if any
					if (preflopGroupLocal < 5) {
						//debug("Raise from blinds: groups 1-4");
						pt1.set(.01, .02, .97);
						return pt1;
					} else if (preflopGroupLocal > 6) {
						// don't fold if there are no bets to call (i.e. Big Blind)
						if (giLocal.getAmountToCall(IDoffset) == 0) {
							pt1.set(.02, .95, .03);
							return pt1;
						} else {
							pt1.set(.95, .04, .01);
							return pt1;
						}
					} else if (
						IDoffset == 1 && giLocal.getNumActivePlayers() > 2) {
						//debug("Call from small blind: groups 5-6");
						pt1.set(.02, .95, .03);
						return pt1;
					} else if (IDoffset == 1) { // we are first to act
						//debug("Steal big blind from small blind: groups 5-6");
						pt1.set(.20, .01, .79);
						return pt1;
					} else {
						//debug("Check from big blind: groups 5-6");
						pt1.set(.02, .95, .03);
						return pt1;
					}
			}
		} else {
			// SHORTHANDED
			pt1.set(.20, .50, .30);
			return pt1;
		}
	}

	/**
	* Decide what to do for a post-flop action
	*/
	private int postFlopAction() {

		// number of players left in the hand (including us)
		int np = gi.getNumActivePlayers();

		// amount to call
		double toCall = gi.getAmountToCall(ID);

		// immediate pot odds
		double PO = toCall / (double) (gi.getPot() + toCall);

		// compute our current hand rank
		double HRN = he.handRank(c1, c2, gi.getBoard(), np - 1);

		// compute our current hand strength vs standard weight table
		double HSTstd = stdWT.handStrength(c1, c2, gi.getBoard());

		// compute our current hand strength vs each active opponent weight table
		double HST = 1;
		double oppHST = 1;
		for (int i = 0; i < gi.getNumPlayers(); i++) {
			if (gi.activePlayer(i) && i != ID) {
				oppHST = wt[i].handStrength(c1, c2, gi.getBoard());
				debug(
					"handStregth vs. " + gi.getPlayerName(i) + " is " + oppHST);
				HST *= oppHST;
			}
		}

		// compute a fast approximation of our hand potential
		double PPOT = 0.0;
		if (gi.getStage() < Holdem.RIVER) {
			PPOT = ppot1(c1, c2, gi.getBoard());
		}

		debug(
			" | HRN = "
				+ Reporter.round(HRN, 3)
				+ " HSTstd = "
				+ Reporter.round(HSTstd, 3)
				+ " HST = "
				+ Reporter.round(HST, 3)
				+ " PPot = "
				+ Reporter.round(PPOT, 3)
				+ " PotOdds = "
				+ Reporter.round(PO, 3));

		if (HRN == 1.0) {
			// dah nuts -- raise the roof!
			return Holdem.RAISE;
		}

		if (gi.getStage() == Holdem.FLOP) {
			// *** FLOP ***
			// consider checking or betting:
			if (toCall == 0) {
				//if (rand.nextDouble() < HRN * HRN) {
				if ((double) gi.getBetSize() / (gi.getPot() + gi.getBetSize())
					< HST) {
					return Holdem.RAISE;
					// bet a hand in proportion to it's strength
				}

				// SEMI BLUFF
				if (((double) gi.getBetSize()
					/ (gi.getPot() + gi.getBetSize() * 2))
					< PPOT) {
					// Semi-bluff: if it would be good enough to call a bet, then raise
					debug("Good enough to call a bet");
					if (gi.getNumActivePlayers() < 6) {
						// increases chance that everyone will fold
						debug("less than six active players");
						if (gi.getNumToAct() == 1) {
							if (rand.nextDouble() < .25) {
								debug("acting last, semibluff 25% of the time");
								return Holdem.BET; // semi-bluff		
							} else {
								debug("not acting last always semibluff");
								return Holdem.BET; // semi-bluff
							}
						}
					}
				}

				// just check
				return Holdem.CALL;
			} else {
				//	consider folding, calling or raising:			

				//if (rand.nextDouble() < Math.pow(HRN, 1 + gi.getNumRaises())) {
				if ((double) (gi.getBetSize() + gi.getAmountToCall(ID))
					/ (gi.getPot() + gi.getBetSize() + gi.getAmountToCall(ID))
					< Math.pow(HST, 1 + gi.getNumRaises())) {
					// raise in proportion to the strength of our hand
					return Holdem.RAISE;
				}
				//if (HRN * HRN * gi.getPot() > toCall || PPOT > PO) {
				if (HST * HST * gi.getPot() > toCall || PPOT > PO) {
					// if we have draw odds or a strong enough hand to call
					return Holdem.CALL;
				}
				return Holdem.FOLD;
			}
		} else if (gi.getStage() == Holdem.TURN) {
			// *** TURN ***
			//	consider checking or betting:
			if (toCall == 0) {
				//if (rand.nextDouble() < HRN * HRN) {
				if ((double) gi.getBetSize() / (gi.getPot() + gi.getBetSize())
					< HST) {
					return Holdem.BET;
					// bet a hand in proportion to it's strength
				}
				// SEMI BLUFF
				if (((double) gi.getBetSize()
					/ (gi.getPot() + gi.getBetSize() * 2))
					< PPOT) {
					// Semi-bluff: if it would be good enough to call a bet, then raise
					debug("Good enough to call a bet");
					if (gi.getNumActivePlayers() < 6) {
						// increases chance that everyone will fold
						debug("less than six active players");
						if (gi.getNumToAct() == 1) {
							if (rand.nextDouble() < .25) {
								debug("acting last, semibluff 25% of the time");
								return Holdem.BET; // semi-bluff		
							} else {
								debug("not acting last always semibluff");
								return Holdem.BET; // semi-bluff
							}
						}
					}
				}
				// just check
				return Holdem.CALL;
			} else {
				//	consider folding, calling or raising:			
				//if (rand.nextDouble() < Math.pow(HRN, 1 + gi.getNumRaises())) {
				if ((double) (gi.getBetSize() + gi.getAmountToCall(ID))
					/ (gi.getPot() + gi.getBetSize() + gi.getAmountToCall(ID))
					< Math.pow(HST, 1 + gi.getNumRaises())) {
					// raise in proportion to the strength of our hand
					return Holdem.RAISE;
				}
				//if (HRN * HRN * gi.getPot() > toCall || PPOT > PO) {
				if (HST * HST * gi.getPot() > toCall || PPOT > PO) {
					// if we have draw odds or a strong enough hand to call
					return Holdem.CALL;
				}
				return Holdem.FOLD;
			}
		} else {
			// *** RIVER ***
			//	consider checking or betting:
			if (toCall == 0) {
				//if (rand.nextDouble() < HRN * HRN) {
				if ((double) gi.getBetSize() / (gi.getPot() + gi.getBetSize())
					< HST) {
					return Holdem.BET;
					// bet a hand in proportion to it's strength
				}
				// just check
				return Holdem.CALL;
			} else {
				//	consider folding, calling or raising:			
				//if (rand.nextDouble() < Math.pow(HRN, 1 + gi.getNumRaises())) {
				if ((double) (gi.getBetSize() + gi.getAmountToCall(ID))
					/ (gi.getPot() + gi.getBetSize() + gi.getAmountToCall(ID))
					< Math.pow(HST, 1 + gi.getNumRaises())) {
					// raise in proportion to the strength of our hand
					return Holdem.RAISE;
				}
				//if (HRN * HRN * gi.getPot() > toCall || PPOT > PO) {
				if (HST * HST * gi.getPot() > toCall || PPOT > PO) {
					// if we have draw odds or a strong enough hand to call
					return Holdem.CALL;
				}
				return Holdem.FOLD;
			}
		}
	}

	/**
	 * Calculate the raw (unweighted) PPot1 and NPot1 of a hand. (Papp 1998, 5.3)
	 * Does a one-card look ahead.
	 * 
	 * @param c1 the first hole card
	 * @param c2 the second hole card
	 * @param bd the board cards
	 * @return the ppot (also sets npot not returned)
	 */
	public double ppot1(Card c1, Card c2, Hand bd) {
		double[][] HP = new double[3][3];
		double[] HPTotal = new double[3];
		int ourrank7, opprank;
		int index;
		Hand board = new Hand(bd);
		HandEvaluator HandEvaluator = new HandEvaluator();
		int ourrank5 = HandEvaluator.rankHand(c1, c2, bd);

		// remove all known cards
		Deck d = new Deck();
		d.extractCard(c1);
		d.extractCard(c2);
		d.extractHand(board);

		// pick first opponent card
		for (int i = d.getTopCardIndex(); i < Deck.NUM_CARDS; i++) {
			Card o1 = d.getCard(i);
			// pick second opponent card
			for (int j = i + 1; j < Deck.NUM_CARDS; j++) {
				Card o2 = d.getCard(j);

				opprank = HandEvaluator.rankHand(o1, o2, bd);
				if (ourrank5 > opprank)
					index = AHEAD;
				else if (ourrank5 == opprank)
					index = TIED;
				else
					index = BEHIND;
				HPTotal[index]++;

				// tally all possiblities for next board card
				for (int k = d.getTopCardIndex(); k < Deck.NUM_CARDS; k++) {
					if (i == k || j == k)
						continue;
					board.addCard(d.getCard(k));
					ourrank7 = HandEvaluator.rankHand(c1, c2, board);
					opprank = HandEvaluator.rankHand(o1, o2, board);
					if (ourrank7 > opprank)
						HP[index][AHEAD]++;
					else if (ourrank7 == opprank)
						HP[index][TIED]++;
					else
						HP[index][BEHIND]++;
					board.removeCard();
				}
			}
		} /* end of possible opponent hands */

		double ppot = 0, npot = 0;
		double den1 = (45 * (HPTotal[BEHIND] + (HPTotal[TIED] / 2.0)));
		double den2 = (45 * (HPTotal[AHEAD] + (HPTotal[TIED] / 2.0)));
		if (den1 > 0) {
			ppot =
				(HP[BEHIND][AHEAD]
					+ (HP[BEHIND][TIED] / 2.0)
					+ (HP[TIED][AHEAD] / 2.0))
					/ (double) den1;
		}
		if (den2 > 0) {
			npot =
				(HP[AHEAD][BEHIND]
					+ (HP[AHEAD][TIED] / 2.0)
					+ (HP[TIED][BEHIND] / 2.0))
					/ (double) den2;
		}
		return ppot;
	}

	// constants used in above method:
	private final static int AHEAD = 0;
	private final static int TIED = 1;
	private final static int BEHIND = 2;

}
