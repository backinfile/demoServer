package com.backinfile.world.game.actions;

import java.util.ArrayList;
import java.util.List;

import com.backinfile.world.game.Card;
import com.backinfile.world.game.Player;

public class DiscardFromBattleAction extends AbstractAction {
	public int pos;

	public DiscardFromBattleAction(Player player, int pos) {
		super(player);
		this.pos = pos;
	}
	public DiscardFromBattleAction(Player player, Card card) {
		super(player);
		
	}

	@Override
	public void update() {
		List<Card> cards = new ArrayList<Card>();
		cards.add(player.battleStoreReadyPile.remove(pos));
		cards.add(player.battleStoreUnreadyPile.remove(pos));
		cards.add(player.battleStoreSealPile.remove(pos));
		cards.add(player.battleRidePile.remove(pos));
		for (Card card : cards) {
			if (card == null) {
				continue;
			}
			player.discardPile.add(card);
		}
	}
}
