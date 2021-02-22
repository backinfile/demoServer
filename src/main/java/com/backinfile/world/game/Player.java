package com.backinfile.world.game;

import java.util.ArrayList;
import java.util.List;

public class Player {
	public int chapter;
	private Board board;
	public CardGroup handPile;
	public CardGroup drawPile;
	public CardGroup disCardPile;

	public Player(Board board, int chapter) {
		this.board = board;
		this.chapter = chapter;
	}

	/**
	 * 是否是储备
	 * 
	 * @param card
	 * @param forceReady 是否是可使用状态
	 * @return
	 */
	public boolean isStore(Card card, boolean ready) {
		return false;
	}

	/**
	 * 获取所有储备牌
	 * 
	 * @param ready         可使用状态
	 * @param inHand        是否检测手牌中的
	 * @param inMark        是否检测标记中的
	 * @param inBattleFront 是否检测储备位中的
	 * @param inPlan        是否检测标计划区中的
	 * @return
	 */
	public List<Card> getAllStore(boolean ready, boolean inHand, boolean inMark, boolean inBattleFront,
			boolean inPlan) {
		List<Card> cards = new ArrayList<>();
		return cards;
	}
}
