package com.imtiyazzaman.zookeeper.playground.endpoints.processes;

import com.imtiyazzaman.zookeeper.playground.model.Resource;
import com.imtiyazzaman.zookeeper.playground.process.DistributedProcess;
import com.imtiyazzaman.zookeeper.playground.process.DistributedProcessRepository;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
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

    @POST
    @Path("/{processId}/assign-partition")
    public Response assignPartition(@PathParam("processId") String processId,
                                    AssignPartitionRequest assignPartitionRequest) {
        if (! processRepository.contains(processId)) {
            return Response.status(404).build();
        }

        DistributedProcess process = processRepository.get(processId);

        process.assignPartition(assignPartitionRequest.partition);

        return Response.ok().build();
    }

    @POST
    @Path("/{processId}/assign-resource")
    public Response assignResource(@PathParam("processId") String processId, Resource resource) {
        if (! processRepository.contains(processId)) {
            return Response.status(404).build();
        }

        DistributedProcess process = processRepository.get(processId);

        process.add(resource);

        return Response.ok().build();
    }

}
