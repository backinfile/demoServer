package com.backinfile.world.game;

public abstract class Card {
	public int id;
	public int sn;
	public int chapter = 1;

	public Card(Board board, int sn, int chapter) {
		this.id = board.getCardId();
		this.sn = sn;
		this.chapter = chapter;
	}

	public boolean canUse() {
		return true;
	}

	public void use() {

	}

	public boolean canDefend() {
		return false;
	}

	public void defend() {

	}
}
