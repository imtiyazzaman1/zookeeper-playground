package com.imtiyazzaman.zookeeper.playground.endpoints.processes;

import com.imtiyazzaman.zookeeper.playground.process.DistributedProcess;
import com.imtiyazzaman.zookeeper.playground.process.DistributedProcessRepository;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import java.util.List;

@Path("/processes")
public class ProcessesResource {
    private final DistributedProcessRepository processRepository;

    @Inject
    public ProcessesResource(DistributedProcessRepository processRepository) {
        this.processRepository = processRepository;
    }

    @GET
    public List<DistributedProcess> getAllProcesses() {
        return processRepository.getAll();
    }
}
