package com.imtiyazzaman.zookeeper.playground.coordinator;

import com.imtiyazzaman.zookeeper.playground.listener.PartitionOwnershipChangeListener;
import com.imtiyazzaman.zookeeper.playground.listener.ResourceOwnershipChangeEvent;
import com.imtiyazzaman.zookeeper.playground.listener.ResourceOwnershipChangeListener;
import com.imtiyazzaman.zookeeper.playground.model.Resource;
import com.imtiyazzaman.zookeeper.playground.state.ClusterState;
import com.imtiyazzaman.zookeeper.playground.state.State;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.CuratorWatcher;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.AddWatchMode;
import org.apache.zookeeper.Watcher;
import org.jboss.logging.Logger;

import java.awt.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.imtiyazzaman.zookeeper.playground.config.Constants.ZK_ADDRESS;

public class Coordinator {

    private static final Logger LOG = Logger.getLogger(Coordinator.class);
    public static final String BASE_PATH = "/zk-playground/partitions";
    private final CuratorFramework client;
    private final String processId;
    private final byte[] processIdBytes;

    private PartitionOwnershipChangeListener partitionOwnershipChangeListener;
    private ResourceOwnershipChangeListener resourceOwnershipChangeListener;

    public Coordinator(String id) {
        this.processId = id;
        this.processIdBytes = id.getBytes(StandardCharsets.UTF_8);
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

            List<String> resources = client.getChildren().forPath(partitionPath);
            client.createContainers(resourcePath);

            if (resources.contains(resource.resourceName())) {
                String partitionOwner = new String(client.getData().forPath(partitionPath));

                if (! processId.equals(partitionOwner)) {
                    LOG.warn(processId + " is taking control over partition: " + resource.partition());
                }
            }

            client.setData().forPath(resourcePath, processIdBytes);

            CuratorWatcher watcher = event -> {
                if (event.getType().equals(Watcher.Event.EventType.NodeDataChanged)) {
                    byte[] bytes = client.getData()
                            .forPath(resourcePath);

                    String owner = new String(bytes);

                    if (!owner.equals(processId)) {
                        ResourceOwnershipChangeEvent resourceOwnershipChangeEvent =
                                new ResourceOwnershipChangeEvent(owner, resource);

                        resourceOwnershipChangeListener.onChange(resourceOwnershipChangeEvent);
                    }
                }
            };

            client.watchers()
                    .add()
                    .withMode(AddWatchMode.PERSISTENT)
                    .usingWatcher(watcher)
                    .forPath(resourcePath);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<Resource> assignPartiton(String partition) {
        try {
            String partitionPath = BASE_PATH + "/" + partition;
            LOG.info("Assigning partition: " + partition + " to node: " + processId);
            client.createContainers(partitionPath);
            client.setData().forPath(partitionPath, processIdBytes);

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

    public void registerPartitionOwnershipChangeListener(PartitionOwnershipChangeListener listener) {
        partitionOwnershipChangeListener = listener;
    }

    public void registerResourceOwnershipChangeListener(ResourceOwnershipChangeListener listener) {
        resourceOwnershipChangeListener = listener;
    }
}
