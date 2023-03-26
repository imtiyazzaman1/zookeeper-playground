package com.imtiyazzaman.zookeeper.playground.process;

import com.imtiyazzaman.zookeeper.playground.coordinator.Coordinator;
import com.imtiyazzaman.zookeeper.playground.model.Resource;
import org.jboss.logging.Logger;

import java.util.ArrayList;
import java.util.List;

public class DistributedProcess {
    private static final Logger LOG = Logger.getLogger(DistributedProcess.class);

    List<String> resources = new ArrayList<>();
    Coordinator coordinator;

    String id;

    public DistributedProcess(String id) {
        this.id = id;
        this.coordinator = new Coordinator(id);
    }

    public void start() {
        LOG.info("Starting process: " + id);
        coordinator.start();
    }

    public void stop() {

    }

    public String getId() {
        return id;
    }

    public void add(Resource resource) {
        coordinator.addNewResource(resource);
    }
}
