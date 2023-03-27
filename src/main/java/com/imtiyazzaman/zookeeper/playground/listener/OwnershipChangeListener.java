package com.imtiyazzaman.zookeeper.playground.listener;

@FunctionalInterface
public interface OwnershipChangeListener<T> {
    void onChange(T event);
}
