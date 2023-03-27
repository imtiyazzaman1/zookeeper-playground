package com.imtiyazzaman.zookeeper.playground.state;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.imtiyazzaman.zookeeper.playground.model.Resource;

import java.util.List;
import java.util.Map;

public class ClusterState {
    @JsonProperty
    Map<String, String> partitions;
    @JsonProperty
    Map<String, List<Resource>> resources;

    public ClusterState(Map<String, String> partitions, Map<String, List<Resource>> resources) {
        this.partitions = partitions;
        this.resources = resources;
    }
}
