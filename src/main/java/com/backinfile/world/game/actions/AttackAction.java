package com.backinfile.world.game.actions;

import java.util.HashSet;
import java.util.Set;

import com.backinfile.world.game.Card;
import com.backinfile.world.game.Player;

public class AttackAction extends AbstractAction {
	private Player opponent;
	private Card storeCard;
	private Response response = new Response();

	private static class Response {
		public Card responeseCard = null;
		public int responesePos = -1;

		public boolean isResponsed() {
			return responesePos != -1 || responeseCard != null;
		}
	}

	public AttackAction(Player player, Card storeCard) {
		super(player);
		this.opponent = player.board.getOpponent(player);
		this.storeCard = storeCard;
	}

	@Override
	public void init() {
		Set<Integer> posSet = opponent.getEmptyBattlePos();
		if (!posSet.isEmpty()) {

		} else {
			Set<Card> opponentStores = opponent.getAllStore(false, false, false, true, true, null);
			player.operChooseOne(opponentStores, false, "choose one to attack", responseCard -> {
				this.response.responeseCard = responseCard;
			});
		}
	}

	@Override
	public void update() {
		if (response.isResponsed()) {
			if (response.responesePos >= 0) {
				player.board.actionManager.addToTop(new DiscardFromBattleAction(opponent, response.responesePos));
			} else {

			}

			isDone = true;
		}
	}
}
