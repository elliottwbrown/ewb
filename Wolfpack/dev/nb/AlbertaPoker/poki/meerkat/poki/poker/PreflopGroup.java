package poker.ai.player;


import poker.Card;

public class PreflopGroup {

	public static int preflopGroup(Card c1, Card c2) {

		// classify hole cards into a preflop group
		// Group 1	AA, KK, QQ, JJ, AKs
		// Group 2	TT, AQs, AJs, KQs, AK
		// Group 3	99, JTs, QJs, KJs, ATs, AQ
		// Group 4	T9s, KQ, 88, QTs, 98s, J9s, AJ, KTs
		// Group 5	77, 87s, Q9s, T8s, KJ, QJ, JT, 76s, 97S, Axs, 65s
		// Group 6	66, AT, 55, 86s, KT, QT, 54s, K9s, J8s, 75s
		// Group 7	44, J9, 64s, T9, 53s, 33, 98, 43s, 22, Kxs, T7s, Q8s
		// Group 8	87, A9, Q9, 76, 42s, 32s, 96s, 85s, J8, J7s, 65, 54, 74s, K9, T8

		// assume the worst
		int group = 9;

		int c1Rank = c1.getRank();
		int c2Rank = c2.getRank();
		int tmpRank = 0;
		
		if ( c1Rank < c2Rank ) {
			tmpRank = c2Rank;
			c2Rank = c1Rank;
			c1Rank = tmpRank;
		}

		System.out.println(" -------> Assigning hand to a preflop group");

		// do we have a pair
		if (c1Rank == c2Rank) {
			switch (c1Rank) {
				case Card.ACE :
					{
						group = 1;
						break;
					}
				case Card.KING :
					{
						group = 1;
						break;
					}
				case Card.QUEEN :
					{
						group = 1;
						break;
					}
				case Card.JACK :
					{
						group = 1;
						break;
					}
				case Card.TEN :
					{
						group = 2;
						break;
					}
				case Card.NINE :
					{
						group = 3;
						break;
					}
				case Card.EIGHT :
					{
						group = 4;
						break;
					}
				case Card.SEVEN :
					{
						group = 5;
						break;
					}
				case Card.SIX :
					{
						group = 6;
						break;
					}
				case Card.FIVE :
					{
						group = 6;
						break;
					}
				default :
					{
						group = 7;
						break;
					}
			}
		} else { //no pair
			// suited?
			if (c1.getSuit() == c2.getSuit()) {
				switch (c1Rank) {
					case Card.ACE :
						{
							switch (c2Rank) {
								case Card.KING :
									{
										group = 1;
										break;
									}
								case Card.QUEEN :
									{
										group = 2;
										break;
									}
								case Card.JACK :
									{
										group = 2;
										break;
									}
								case Card.TEN :
									{
										group = 3;
										break;
									}
								default :
									{
										group = 5;
										break;
									}
							}
							break;
						}
					case Card.KING :
						{
							switch (c2Rank) {
								case Card.QUEEN :
									{
										group = 2;
										break;
									}
								case Card.JACK :
									{
										group = 3;
										break;
									}
								case Card.TEN :
									{
										group = 4;
										break;
									}
								case Card.NINE :
									{
										group = 6;
										break;
									}
								default :
									{
										group = 7;
										break;
									}
							}
							break;
						}
					case Card.QUEEN :
						{
							switch (c2Rank) {
								case Card.JACK :
									{
										group = 3;
										break;
									}
								case Card.TEN :
									{
										group = 4;
										break;
									}
								case Card.NINE :
									{
										group = 5;
										break;
									}
								case Card.EIGHT :
									{
										group = 7;
										break;
									}
								default :
									{
										group = 9;
										break;
									}
							}
							break;
						}
					case Card.JACK :
						{
							switch (c2Rank) {
								case Card.TEN :
									{
										group = 3;
										break;
									}
								case Card.NINE :
									{
										group = 4;
										break;
									}
								case Card.EIGHT :
									{
										group = 6;
										break;
									}
								case Card.SEVEN :
									{
										group = 8;
										break;
									}
								default :
									{
										group = 9;
										break;
									}
							}
							break;
						}
					case Card.TEN :
						{
							switch (c2Rank) {
								case Card.NINE :
									{
										group = 4;
										break;
									}
								case Card.EIGHT :
									{
										group = 5;
										break;
									}
								case Card.SEVEN :
									{
										group = 7;
										break;
									}
								default :
									{
										group = 9;
										break;
									}
							}
							break;
						}
					case Card.NINE :
						{
							switch (c2Rank) {
								case Card.EIGHT :
									{
										group = 4;
										break;
									}
								case Card.SEVEN :
									{
										group = 5;
										break;
									}
								case Card.SIX :
									{
										group = 8;
										break;
									}
								default :
									{
										group = 9;
										break;
									}
							}
							break;
						}
					case Card.EIGHT :
						{
							switch (c2Rank) {
								case Card.SEVEN :
									{
										group = 5;
										break;
									}
								case Card.SIX :
									{
										group = 6;
										break;
									}
								case Card.FIVE :
									{
										group = 8;
										break;
									}
								default :
									{
										group = 9;
										break;
									}
							}
							break;
						}
					case Card.SEVEN :
						{
							switch (c2Rank) {
								case Card.SIX :
									{
										group = 5;
										break;
									}
								case Card.FIVE :
									{
										group = 6;
										break;
									}
								case Card.FOUR :
									{
										group = 8;
										break;
									}
								default :
									{
										group = 9;
										break;
									}
							}
							break;
						}
					case Card.SIX :
						{
							switch (c2Rank) {
								case Card.FIVE :
									{
										group = 5;
										break;
									}
								case Card.FOUR :
									{
										group = 7;
										break;
									}
								default :
									{
										group = 9;
										break;
									}
							}
							break;
						}
					case Card.FIVE :
						{
							switch (c2Rank) {
								case Card.FOUR :
									{
										group = 6;
										break;
									}
								case Card.THREE :
									{
										group = 7;
										break;
									}
								default :
									{
										group = 9;
										break;
									}
							}
							break;
						}
					case Card.FOUR :
						{
							switch (c2Rank) {
								case Card.THREE :
									{
										group = 7;
										break;
									}
								case Card.TWO :
									{
										group = 8;
										break;
									}
								default :
									{
										group = 9;
										break;
									}
							}
							break;
						}
					case Card.THREE :
						{
							switch (c2Rank) {
								case Card.TWO :
									{
										group = 8;
										break;
									}
								default :
									{
										group = 9;
										break;
									}
							}
							break;
						}
				}
			} else { // not suited
				switch (c1Rank) {
					case Card.ACE :
						{
							switch (c2Rank) {
								case Card.KING :
									{
										group = 2;
										break;
									}
								case Card.QUEEN :
									{
										group = 3;
										break;
									}
								case Card.JACK :
									{
										group = 4;
										break;
									}
								case Card.TEN :
									{
										group = 6;
										break;
									}
								case Card.NINE :
									{
										group = 8;
										break;
									}
								default :
									{
										group = 9;
										break;
									}
							}
							break;
						}
					case Card.KING :
						{
							switch (c2Rank) {
								case Card.QUEEN :
									{
										group = 4;
										break;
									}
								case Card.JACK :
									{
										group = 5;
										break;
									}
								case Card.TEN :
									{
										group = 6;
										break;
									}
								case Card.NINE :
									{
										group = 8;
										break;
									}
								default :
									{
										group = 9;
										break;
									}
							}
							break;
						}
					case Card.QUEEN :
						{
							switch (c2Rank) {
								case Card.JACK :
									{
										group = 5;
										break;
									}
								case Card.TEN :
									{
										group = 6;
										break;
									}
								case Card.NINE :
									{
										group = 8;
										break;
									}
								default :
									{
										group = 9;
										break;
									}
							}
							break;
						}
					case Card.JACK :
						{
							switch (c2Rank) {
								case Card.TEN :
									{
										group = 5;
										break;
									}
								case Card.NINE :
									{
										group = 7;
										break;
									}
								case Card.EIGHT :
									{
										group = 8;
										break;
									}
								default :
									{
										group = 9;
										break;
									}
							}
							break;
						}
					case Card.TEN :
						{
							switch (c2Rank) {
								case Card.NINE :
									{
										group = 7;
										break;
									}
								case Card.EIGHT :
									{
										group = 8;
										break;
									}
								default :
									{
										group = 9;
										break;
									}
							}
							break;
						}
					case Card.NINE :
						{
							switch (c2Rank) {
								case Card.EIGHT :
									{
										group = 7;
										break;
									}
								default :
									{
										group = 9;
										break;
									}
							}
							break;
						}
					case Card.EIGHT :
						{
							switch (c2Rank) {
								case Card.SEVEN :
									{
										group = 8;
										break;
									}
								default :
									{
										group = 9;
										break;
									}
							}
							break;
						}
					case Card.SEVEN :
						{
							switch (c2Rank) {
								case Card.SIX :
									{
										group = 8;
										break;
									}
								default :
									{
										group = 9;
										break;
									}
							}
							break;
						}
					case Card.SIX :
						{
							switch (c2Rank) {
								case Card.FIVE :
									{
										group = 8;
										break;
									}
								default :
									{
										group = 9;
										break;
									}
							}
							break;
						}
					case Card.FIVE :
						{
							switch (c2Rank) {
								case Card.FOUR :
									{
										group = 8;
										break;
									}
								default :
									{
										group = 9;
										break;
									}
							}
							break;
						}
				}
			}
		}

		//System.out.println(" -------> Preflop group is " + Integer.toString(group));

		return group;

	}

}
