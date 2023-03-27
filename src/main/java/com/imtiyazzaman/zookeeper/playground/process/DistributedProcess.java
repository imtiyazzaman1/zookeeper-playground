package com.imtiyazzaman.zookeeper.playground.process;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.imtiyazzaman.zookeeper.playground.coordinator.Coordinator;
import com.imtiyazzaman.zookeeper.playground.listener.PartitionOwnershipChangeListener;
import com.imtiyazzaman.zookeeper.playground.listener.ResourceOwnershipChangeListener;
import com.imtiyazzaman.zookeeper.playground.model.Resource;
import org.jboss.logging.Logger;

import java.util.*;

public class DistributedProcess {
    private static final Logger LOG = Logger.getLogger(DistributedProcess.class);

    @JsonProperty
    Set<Resource> resources = new HashSet<>();
    @JsonProperty
    Set<String> partitions = new HashSet<>();
    Coordinator coordinator;

    @JsonProperty
    String id;

    public DistributedProcess(String id) {
        this.id = id;
        this.coordinator = new Coordinator(id);
    }

    public void start() {
        LOG.info("Starting process: " + id);
        coordinator.start();
        coordinator.registerResourceOwnershipChangeListener(onResourceChange());
        coordinator.registerPartitionOwnershipChangeListener(onPartitionChange());
    }

    public void stop() {

    }

    public String getId() {
        return id;
    }

    public void addAllResources(Collection<Resource> resources) {
        resources.forEach(this::add);
    }
    public void add(Resource resource) {
        LOG.info("Processing new resource: " + resource)
        ;
        coordinator.addNewResource(resource);
        resources.add(resource);
    }


    public void assignPartition(String partition) {
        List<Resource> resourcesFromNewPartition = coordinator.assignPartiton(partition);

        partitions.add(partition);
        addAllResources(resourcesFromNewPartition);
    }

    private ResourceOwnershipChangeListener onResourceChange() {
        return event -> {
          if (! event.getNewOwner().equals(id)) {
              Resource resource = event.getResource();
              LOG.info("Detected ownership change for resource: " + resource);
              LOG.info("Removing resource from node: " + id);

              resources.remove(resource);
          }
        };
    }

    private PartitionOwnershipChangeListener onPartitionChange() {
        return event -> {
            if (! event.getNewOwner().equals(id)) {
                String partition = event.getPartition();

                LOG.info("Detected ownership change for partition: " + partition);

                partitions.remove(partition);

                resources.stream()
                        .filter(resource -> resource.partition().equals(partition))
                        .forEach(resource -> {
                            LOG.info("Removing resource from node: " + id + ": " + resource);
                            resources.remove(resource);
                        });
            }
        };
    }
}
