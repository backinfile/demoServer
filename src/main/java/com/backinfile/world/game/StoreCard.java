package com.backinfile.world.game;

import java.util.HashSet;
import java.util.Set;

public class StoreCard extends Card {
	protected StoreType storeType;

	public StoreCard(Board board, int sn, int chapter) {
		super(board, sn, chapter);
	}

	public Set<StoreType> getStoreTypes() {
		Set<StoreType> storeTypes = new HashSet<StoreType>();
		storeTypes.add(storeType);
		return storeTypes;
	}

}
