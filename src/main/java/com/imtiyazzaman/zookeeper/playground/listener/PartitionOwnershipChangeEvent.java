package com.imtiyazzaman.zookeeper.playground.listener;

public class PartitionOwnershipChangeEvent extends OwnershipChangeEvent {
    private final String partition;

    public PartitionOwnershipChangeEvent(String newOwner, String partition) {
        super(newOwner);
        this.partition = partition;
    }

    public String getPartition() {
        return partition;
    }
}
