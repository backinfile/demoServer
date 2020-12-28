package com.backinfile.core.function;

@FunctionalInterface
public interface Action1<T> {
	void invoke(T t);
}
