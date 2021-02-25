package com.backinfile.world.game.chap2;

import com.backinfile.world.game.ActionCard;
import com.backinfile.world.game.Board;
import com.backinfile.world.game.Player;
import com.backinfile.world.game.actions.AttackCardAttackAction;

public class Attack extends ActionCard {

	public static final int SN = 21001; // 章节+类型+序号
	public static final int CHAPTER = 2;

	public Attack(Board board) {
		super(board, SN, CHAPTER);
	}

	@Override
	public boolean canUse() {
		Player player = board.getCurActionPlayer();
		int store = player.getAllStore().size();
		return store > 0;
	}

	@Override
	public void use() {
		board.actionManager.addToBottom(new AttackCardAttackAction(board.getCurActionPlayer()));
	}

}
