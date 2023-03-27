package com.imtiyazzaman.zookeeper.playground.coordinator;

import com.imtiyazzaman.zookeeper.playground.model.Resource;
import com.imtiyazzaman.zookeeper.playground.state.State;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.CuratorWatcher;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.AddWatchMode;
import org.jboss.logging.Logger;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

import static com.imtiyazzaman.zookeeper.playground.config.Constants.ZK_ADDRESS;

public class Coordinator {

    private static final Logger LOG = Logger.getLogger(Coordinator.class);
    public static final String BASE_PATH = "/zk-playground/partitions";
    private final CuratorFramework client;
    private final String processId;

    public Coordinator(String id) {
        this.processId = id;
        client = CuratorFrameworkFactory.newClient(ZK_ADDRESS, new ExponentialBackoffRetry(1000, 2));

    }

    public void start() {
        client.start();
        try {
            client.createContainers(BASE_PATH);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void stop() {
    }

    public ClusterState getClusterState() {
        List<String> partitions = null;
        try {
            partitions = client.getChildren().forPath(BASE_PATH);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        Map<String, String> partitionMap = new HashMap<>();
        Map<String, Resource> resourceMap = new HashMap<>();

        ClusterState clusterState = new ClusterState(partitionMap, resourceMap);

        partitions.stream().forEach(partition -> {
            try {
                String partitionPath = BASE_PATH + "/" + partition;
                String assignedNode = new String(client.getData().forPath(partitionPath));

                partitionMap.put(partition, assignedNode);

                client.getChildren()
                        .forPath(partitionPath)
                        .forEach(resource -> {
                            byte[] bytes = new byte[0];
                            try {
                                bytes = client.getData().forPath(partitionPath + "/" + resource);
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }

                            resourceMap.put(new String(bytes), new Resource(resource, partition));
                        });


            } catch (Exception e) {

            }
        });

        return clusterState;
    }

    public void addNewResource(Resource resource) {
        try {
            String partitionPath = BASE_PATH + "/" + resource.partition();
            String resourcePath = partitionPath + "/" + resource.resourceName();

            client.createContainers(resourcePath);
            byte[] idBytes = processId.getBytes(StandardCharsets.UTF_8);

            String partitionOwner = new String(client.getData()
                    .forPath(partitionPath));

            if (! processId.equals(partitionOwner)) {
                LOG.warn(processId + " is taking control over partition: " + resource.partition());
                client.setData()
                        .forPath(partitionPath, idBytes);
            }

            client.setData()
                    .forPath(resourcePath, idBytes);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<Resource> assignPartiton(String partition) {
        try {
            String partitionPath = BASE_PATH + "/" + partition;
            LOG.info("Assigning partition: " + partition + " to node: " + processId);
            client.setData()
                    .forPath(partitionPath);

            LOG.info("Fetching resources for partition: " + partition);
            return client.getChildren()
                    .forPath(partitionPath)
                    .stream()
                    .map(resource -> new Resource(resource, partition))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
