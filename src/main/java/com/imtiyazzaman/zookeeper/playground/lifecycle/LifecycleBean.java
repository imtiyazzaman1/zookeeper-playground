package com.imtiyazzaman.zookeeper.playground.lifecycle;

import com.imtiyazzaman.zookeeper.playground.coordinator.Coordinator;
import com.imtiyazzaman.zookeeper.playground.process.DistributedProcess;
import com.imtiyazzaman.zookeeper.playground.process.DistributedProcessRepository;
import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

@ApplicationScoped
public class LifecycleBean {
    private static final Logger LOG = Logger.getLogger(LifecycleBean.class.getCanonicalName());


    private final DistributedProcessRepository processRepository;

    @Inject
    public LifecycleBean(DistributedProcessRepository processRepository) {
        this.processRepository = processRepository;
    }

    void onStart(@Observes StartupEvent ev) throws Exception {
        LOG.info("Starting application...");
        DistributedProcess p1 = new DistributedProcess("node-1");
        DistributedProcess p2 = new DistributedProcess("node-2");

        processRepository.put(p1.getId(), p1);
        processRepository.put(p2.getId(), p1);

        p1.start();
        p2.start();
    }

    void onStop(@Observes ShutdownEvent ev) {
        LOG.info("Shutting down application...");


        LOG.info("Cluster dropped... shutting down");
    }
}
