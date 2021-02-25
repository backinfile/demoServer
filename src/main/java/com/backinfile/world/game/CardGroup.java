package com.backinfile.world.game;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.backinfile.core.function.Action1;
import com.backinfile.core.function.Function1;

public class CardGroup {

	private Player player;
	private LinkedList<Card> cards = new LinkedList<Card>();

	public CardGroup(Player player) {
		this.player = player;
	}

	public CardGroup(Player player, int num) {
		this(player);
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

	public Card getTop() {
		if (cards.isEmpty()) {
			return null;
		}
		return cards.get(0);
	}

	public boolean remove(Card card) {
		return cards.remove(card);
	}

	public Card remove(int pos) {
		Card card = cards.get(pos);
		cards.set(pos, null);
		return card;
	}

	public int indexOf(Card card) {
		for (int i = 0; i < cards.size(); i++) {
			if (card == cards.get(i)) {
				return i;
			}
		}
		return -1;
	}

	public List<Card> take(int n) {
		List<Card> takes = new ArrayList<>();
		for (int i = 0; i < n; i++) {
			if (!cards.isEmpty()) {
				takes.add(cards.pollFirst());
			} else {
				break;
			}
		}
		return takes;
	}

	public List<Card> takeRandom(int n) {
		List<Card> takes = new ArrayList<>();
		for (int i = 0; i < n; i++) {
			if (!cards.isEmpty()) {
				takes.add(cards.remove(player.board.random.nextInt(cards.size())));
			} else {
				break;
			}
		}
		return takes;
	}

	public void forEach(Action1<Card> action) {
		for (int i = 0; i < cards.size(); i++) {
			action.invoke(cards.get(i));
		}
	}

	public boolean any(Function1<Card, Boolean> function) {
		for (int i = 0; i < cards.size(); i++) {
			if (function.invoke(cards.get(i))) {
				return true;
			}
		}
		return false;
	}

	public boolean all(Function1<Card, Boolean> function) {
		for (int i = 0; i < cards.size(); i++) {
			if (!function.invoke(cards.get(i))) {
				return false;
			}
		}
		return true;
	}

	public void shuffle() {
		if (cards.isEmpty()) {
			return;
		}
		for (int i = 0; i < cards.size(); i++) {
			int rnd = player.board.random.nextInt(cards.size());
			Card tmp = cards.get(i);
			cards.set(i, cards.get(rnd));
			cards.set(rnd, tmp);
		}
	}
}
