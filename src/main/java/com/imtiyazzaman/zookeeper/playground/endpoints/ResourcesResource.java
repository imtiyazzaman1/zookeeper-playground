package com.imtiyazzaman.zookeeper.playground.endpoints;

import com.imtiyazzaman.zookeeper.playground.model.Resource;
import com.imtiyazzaman.zookeeper.playground.process.DistributedProcessRepository;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("/resources")
public class ResourcesResource {
    private final DistributedProcessRepository processRepository;

    @Inject
    public ResourcesResource(DistributedProcessRepository processRepository) {
        this.processRepository = processRepository;
    }

    @POST
    public Response addResource(AddResourceRequest addResourceRequest) {
        if (! processRepository.contains(addResourceRequest.node)) {
            return Response.status(404).build();
        }

        processRepository.get(addResourceRequest.node)
                .add(new Resource(addResourceRequest.resourceName, addResourceRequest.partition));

        return Response.ok().build();
    }
}
