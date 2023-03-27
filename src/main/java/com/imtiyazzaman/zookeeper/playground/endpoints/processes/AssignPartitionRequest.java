package com.imtiyazzaman.zookeeper.playground.endpoints.processes;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AssignPartitionRequest {
    @JsonProperty
    String partition;
}
