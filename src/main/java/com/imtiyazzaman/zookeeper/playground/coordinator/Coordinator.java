package com.imtiyazzaman.zookeeper.playground.coordinator;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

import static com.imtiyazzaman.zookeeper.playground.config.Constants.ZK_ADDRESS;

public class Coordinator {

    private final CuratorFramework client;

    public Coordinator() {
        client = CuratorFrameworkFactory.newClient(ZK_ADDRESS, new ExponentialBackoffRetry(1000, 2));
    }

    public void start() {
        client.start();
        try {
            client.createContainers("/zk-playground/partitions");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void stop() {
    }
}
