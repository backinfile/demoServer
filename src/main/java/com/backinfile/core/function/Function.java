package com.backinfile.core.function;

@FunctionalInterface
public interface Function<R> {
    R invoke();
}
