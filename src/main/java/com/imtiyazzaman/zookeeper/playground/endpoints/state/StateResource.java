package com.imtiyazzaman.zookeeper.playground.endpoints.state;

import com.imtiyazzaman.zookeeper.playground.coordinator.Coordinator;
import com.imtiyazzaman.zookeeper.playground.state.State;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import java.util.List;

@Path("/state")
public class StateResource {
    private final Coordinator coordinator;

    public StateResource() {
        this.coordinator = new Coordinator("observer");
        this.coordinator.start();
    }

    @GET
    public List<State> getState() {
        return coordinator.getClusterState();
    }
}
