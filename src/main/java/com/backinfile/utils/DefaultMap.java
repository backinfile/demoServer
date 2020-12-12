package com.backinfile.utils;

import java.util.HashMap;
import java.util.function.Supplier;

/**
 * 使用get时，如果不存在则自动创建
 *
 * @param <K>
 * @param <V>
 */
@SuppressWarnings("serial")
public class DefaultMap<K, V> extends HashMap<K, V> {
	private final Supplier<V> defaultProvider;

	public DefaultMap(Supplier<V> defaultProvider) {
		this.defaultProvider = defaultProvider;
	}

	@SuppressWarnings("unchecked")
	@Override
	public V get(Object key) {
		V value = super.get(key);
		if (value == null) {
			value = defaultProvider.get();
			super.put((K) key, value);
		}
		return value;
	}
}
