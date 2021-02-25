package com.backinfile.world.game;

import java.util.LinkedList;

import com.backinfile.world.game.actions.AbstractAction;

public class ActionManager {
	private final LinkedList<AbstractAction> actionQueue = new LinkedList<>();
	private AbstractAction curAction = null;
	private boolean running = false;

	public AbstractAction getCurAction() {
		return curAction;
	}

	public void addToTop(AbstractAction action) {
		actionQueue.addFirst(action);
	}

	public boolean isRunning() {
		return running;
	}

	public void addToBottom(AbstractAction action) {
		actionQueue.addLast(action);
	}

	public void update() {
		running = true;
		if (curAction == null) {
			if (actionQueue.isEmpty()) {
				running = false;
			}
			curAction = actionQueue.pollFirst();
			curAction.init();
		}
		curAction.update();
		if (curAction.isDone()) {
			curAction = null;
		}
	}
}
