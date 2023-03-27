package com.imtiyazzaman.zookeeper.playground.listener;

import com.imtiyazzaman.zookeeper.playground.model.Resource;

public class ResourceOwnershipChangeEvent extends OwnershipChangeEvent {
    private final Resource resource;

    public ResourceOwnershipChangeEvent(String newOwner, Resource resource) {
        super(newOwner);
        this.resource = resource;
    }

    public Resource getResource() {
        return resource;
    }
}
