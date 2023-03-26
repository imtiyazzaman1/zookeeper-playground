package com.imtiyazzaman.zookeeper.playground.coordinator;

import com.imtiyazzaman.zookeeper.playground.model.Resource;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

import java.nio.charset.StandardCharsets;

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
