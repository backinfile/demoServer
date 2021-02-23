package com.backinfile.world.game;

import java.util.Random;

public class Board {
	private int idCounter = 1;

	private Player[] players = new Player[] { null, null };
	public Random random = new Random();

	public Player getOpponent(Player player) {
		if (player == players[0]) {
			return players[1];
		}
		return players[0];
	}

	public int getCardId() {
		return idCounter++;
	}

}
