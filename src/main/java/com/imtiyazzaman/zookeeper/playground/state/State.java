package com.imtiyazzaman.zookeeper.playground.state;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class State {
    @JsonProperty
    String partition;
    @JsonProperty
    String assignedTo;
    @JsonProperty
    List<String> resources;

    public State(String partition, String assignedTo, List<String> resources) {
        this.partition = partition;
        this.assignedTo = assignedTo;
        this.resources = resources;
    }
}
