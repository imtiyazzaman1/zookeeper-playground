package com.imtiyazzaman.zookeeper.playground.process;

import com.imtiyazzaman.zookeeper.playground.coordinator.Coordinator;
import org.jboss.logging.Logger;

import java.util.ArrayList;
import java.util.List;

public class DistributedProcess {
    private static final Logger LOG = Logger.getLogger(DistributedProcess.class);

    List<String> resources = new ArrayList<>();
    Coordinator coordinator = new Coordinator();

    String id;

    public DistributedProcess(String id) {
        this.id = id;
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
}
