package com.backinfile.world.game.actions;

import com.backinfile.world.game.Player;

public abstract class AbstractAction {
	// 标记Action结束
	protected boolean isDone = false;
	protected Player player;

	public AbstractAction(Player player) {

	}

	public void init() {

	}

	public void update() {
		isDone = true;
	}

	public boolean isDone() {
		return isDone;
	}

	public Player getPlayer() {
		return player;
	}
}
