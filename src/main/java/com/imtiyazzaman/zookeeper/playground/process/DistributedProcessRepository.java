package com.imtiyazzaman.zookeeper.playground.process;

import javax.inject.Singleton;
import java.util.*;

@Singleton
public class DistributedProcessRepository {

    private final Map<String, DistributedProcess> processMap = new HashMap<>();

    public DistributedProcess get(String id) {
        return processMap.get(id);
    }

    public void put(String id, DistributedProcess distributedProcess) {
        processMap.put(id, distributedProcess);
    }

    public Set<Map.Entry<String, DistributedProcess>> entrySet() {
        return processMap.entrySet();
    }

    public boolean contains(String key) {
        return processMap.containsKey(key);
    }

    public List<DistributedProcess> getAll() {
        return new ArrayList<>(processMap.values());
    }
}
