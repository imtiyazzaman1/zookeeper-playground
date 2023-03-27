package com.imtiyazzaman.zookeeper.playground.state;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.imtiyazzaman.zookeeper.playground.model.Resource;

import java.util.Map;

public class ClusterState {
    @JsonProperty
    Map<String, String> partitions;
    @JsonProperty
    Map<String, Resource> resources;

    public ClusterState(Map<String, String> partitions, Map<String, Resource> resources) {
        this.partitions = partitions;
        this.resources = resources;
    }

    public Map<String, String> getPartitions() {
        return partitions;
    }

    public void setPartitions(Map<String, String> partitions) {
        this.partitions = partitions;
    }

    public Map<String, Resource> getResources() {
        return resources;
    }

    public void setResources(Map<String, Resource> resources) {
        this.resources = resources;
    }
}
