package com.backinfile.world.game.actions;

import com.backinfile.world.game.Player;

public class InTurnAction extends AbstractAction {
	public InTurnAction(Player player) {
		super(player);
	}

	@Override
	public void init() {
		// 发送消息
	}

	@Override
	public void update() {
		// 轮询是否接收到消息
	}

}
