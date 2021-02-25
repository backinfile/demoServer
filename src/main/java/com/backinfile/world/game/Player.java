package com.backinfile.world.game;

import java.util.HashSet;
import java.util.Set;

import com.backinfile.core.function.Action1;

public class Player {
	public int chapter;
	public Board board;
	public CardGroup handPile;
	public CardGroup drawPile;
	public CardGroup discardPile;
	public CardGroup reviewPile;
	public CardGroup markPile;

	public CardGroup battleStoreSealPile;
	public CardGroup battleStoreUnreadyPile;
	public CardGroup battleStoreReadyPile;
	public CardGroup battleHarassPile;
	public CardGroup battleRidePile;

	public boolean isPlanCardHide = true; // 计划牌是隐藏状态

	public Player(Board board, int chapter) {
		this.board = board;
		this.chapter = chapter;
	}

	public void init() {
		initCardGroup();
		initCardSet();
	}

	private void initCardGroup() {
		handPile = new CardGroup(this);
		discardPile = new CardGroup(this);
		drawPile = new CardGroup(this);
		reviewPile = new CardGroup(this);
		markPile = new CardGroup(this);

		battleStoreSealPile = new CardGroup(this, 5);
		battleStoreUnreadyPile = new CardGroup(this, 5);
		battleStoreReadyPile = new CardGroup(this, 5);
		battleHarassPile = new CardGroup(this, 5);
		battleRidePile = new CardGroup(this, 5);
	}

	private void initCardSet() {
		// 添加牌库
	}

	public Set<Card> getAllStore() {
		return getAllStore(true, true, true, true, true, null);
	}

	/**
	 * 获取所有储备牌
	 * 
	 * @param ready         必须是可使用状态
	 * @param inHand        是否检测手牌中的
	 * @param inMark        是否检测标记中的
	 * @param inBattleFront 是否检测储备位中的
	 * @param inPlan        是否检测标计划区中的
	 * @return
	 */
	public Set<Card> getAllStore(boolean ready, boolean inHand, boolean inMark, boolean inBattleFront, boolean inPlan,
			Set<StoreType> storeTypes) {
		Set<Card> cards = new HashSet<>();
		if (inBattleFront) {
			for (int i = 0; i < ConstGame.BattleFrontNum - 1; i++) {
				Card card = battleStoreReadyPile.get(i);
				if (card != null && card.isInStoreTypes(storeTypes)) {
					cards.add(card);
				}
			}
			for (int i = 0; i < ConstGame.BattleFrontNum - 1; i++) {
				Card card = battleStoreUnreadyPile.get(i);
				if (!ready) {
					if (card != null && card.isInStoreTypes(storeTypes)) {
						cards.add(card);
					}
				}
			}
			if (inPlan) {
				{
					Card card = battleStoreReadyPile.get(ConstGame.PlanCardPos);
					if (card != null && card.isInStoreTypes(storeTypes)) {
						cards.add(card);
					}
				}
				if (!ready) {
					Card card = battleStoreUnreadyPile.get(ConstGame.PlanCardPos);
					if (card != null && card.isInStoreTypes(storeTypes)) {
						cards.add(card);
					}
				}
			}
		}
		return cards;
	}

	public Card getPlanCard() {
		Card card = null;
		if (card == null) {
			card = battleStoreReadyPile.get(ConstGame.PlanCardPos);
		}
		if (card == null) {
			card = battleStoreUnreadyPile.get(ConstGame.PlanCardPos);
		}
		if (card == null) {
			card = battleStoreSealPile.get(ConstGame.PlanCardPos);
		}
		return card;
	}

	public boolean isDead() {
		return !battleStoreSealPile.any(card -> card == null);
	}

	public void operChooseOne(Set<Card> cards, boolean canIgnore, String tip, Action1<Card> callBack) {
		// TODO
	}

	public void operChooseBattlePos(Set<Integer> poses, boolean canIgnore, String tip, Action1<Card> callBack) {
		// TODO
	}

	public Set<Integer> getEmptyBattlePos() {
		Set<Integer> posSet = new HashSet<>();
		for (int i = 0; i < ConstGame.BattleFrontNum; i++) {
			if (battleStoreReadyPile.get(i) != null) {
				continue;
			}
			if (battleStoreUnreadyPile.get(i) != null) {
				continue;
			}
			if (battleStoreSealPile.get(i) != null) {
				continue;
			}
			posSet.add(i);
		}
		return posSet;
	}
}
