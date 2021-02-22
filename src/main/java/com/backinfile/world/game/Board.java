package com.backinfile.world.game;

public class Board {
	private int idCounter = 1;

	private Player[] players = new Player[] { null, null };

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
