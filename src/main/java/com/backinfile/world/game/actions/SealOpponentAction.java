package com.backinfile.world.game.actions;

import com.backinfile.world.game.Card;
import com.backinfile.world.game.Player;

public class SealOpponentAction extends AbstractAction {
	public int pos;
	public Player opponent;
	public Card seal;

	public SealOpponentAction(Player player, Card seal, int pos) {
		super(player);
		this.pos = pos;
		this.opponent = player.board.getOpponent(player);
		this.seal = seal;
	}

	@Override
	public void init() {
	}

	@Override
	public void update() {
		player.reviewPile.remove(seal);
		opponent.battleStoreSealPile.set(pos, seal);
		isDone = true;
	}
}
