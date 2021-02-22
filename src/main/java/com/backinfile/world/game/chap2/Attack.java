package com.backinfile.world.game.chap2;

import com.backinfile.world.game.ActionCard;
import com.backinfile.world.game.Board;

public class Attack extends ActionCard {

	public static final int SN = 21001; // 章节+类型+序号
	public static final int CHAPTER = 2;

	public Attack(Board board) {
		super(board, SN, CHAPTER);
	}

}
