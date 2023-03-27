package com.imtiyazzaman.zookeeper.playground.endpoints.resources;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AddResourceRequest {
    @JsonProperty
    String partition;
    @JsonProperty
    String resourceName;
    @JsonProperty
    String node;
}
