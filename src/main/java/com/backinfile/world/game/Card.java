package com.backinfile.world.game;

import java.util.Set;

public abstract class Card {
	public int id;
	public int sn;
	public int chapter = 1;
	public Board board;

	public Card(Board board, int sn, int chapter) {
		this.board = board;
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

	public boolean inSelfTurn() {
		return board.getCurActionPlayer() == board.getCurTurnPlayer();
	}

	public boolean isInStoreTypes(Set<StoreType> storeTypes) {
		if (this instanceof StoreCard) {
			if (storeTypes == null) {
				return true;
			}
			for (StoreType type : ((StoreCard) this).getStoreTypes()) {
				if (storeTypes.contains(type)) {
					return true;
				}
			}
		}
		return false;
	}
}
