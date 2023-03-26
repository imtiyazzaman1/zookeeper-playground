package com.imtiyazzaman.zookeeper.playground.coordinator;

import com.imtiyazzaman.zookeeper.playground.model.Resource;
import com.imtiyazzaman.zookeeper.playground.state.State;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.data.Stat;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

import static com.imtiyazzaman.zookeeper.playground.config.Constants.ZK_ADDRESS;

public class Coordinator {

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

    public List<State> getClusterState() {
        List<String> partitions = null;
        try {
            partitions = client.getChildren().forPath(BASE_PATH);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return partitions.stream().map(partition -> {
                    try {
                        String partitionPath = BASE_PATH + "/" + partition;
                        String assignedNode = new String(client.getData().forPath(partitionPath));
                        List<String> resources = client.getChildren().forPath(partitionPath);

                        return new State(partition, assignedNode, resources);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toList());
    }

    public void addNewResource(Resource resource) {
        try {
            String partitionPath = BASE_PATH + "/" + resource.partition();
            client.createContainers(partitionPath + "/" + resource.resourceName());
            client.setData()
                    .forPath(partitionPath, processId.getBytes(StandardCharsets.UTF_8));

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
