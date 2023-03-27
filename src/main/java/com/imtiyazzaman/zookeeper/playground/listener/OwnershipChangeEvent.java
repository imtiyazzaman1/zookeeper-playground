package com.imtiyazzaman.zookeeper.playground.listener;

public abstract class OwnershipChangeEvent {
    private final String newOwner;

    public OwnershipChangeEvent(String newOwner) {
        this.newOwner = newOwner;
    }

    public String getNewOwner() {
        return newOwner;
    }
}
