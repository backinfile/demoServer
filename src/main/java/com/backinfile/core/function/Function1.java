package com.backinfile.core.function;

@FunctionalInterface
public interface Function1<T, R> {
    R invoke(T t);
}
