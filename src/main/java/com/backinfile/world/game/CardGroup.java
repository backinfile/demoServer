package com.backinfile.world.game;

import java.util.LinkedList;
import java.util.List;

public class CardGroup {

	private LinkedList<Card> cards = new LinkedList<Card>();

	public CardGroup() {
	}

	public CardGroup(int num) {
		for (int i = 0; i < num; i++) {
			cards.add(null);
		}
	}

	public Card get(int i) {
		return cards.get(i);
	}

	public void set(int i, Card card) {
		cards.set(i, card);
	}

	public void add(Card card) {
		cards.add(card);
	}

	public int size() {
		return cards.size();
	}
}
