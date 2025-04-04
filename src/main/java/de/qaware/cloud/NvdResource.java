package de.qaware.cloud;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/api")
public class NvdResource {

    @Inject
    NvdConnector nvdConnector;

    @GET
    @Path("/cves/{cveId}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getCve(@PathParam("cveId") String cveId) {
        return nvdConnector.queryCve(cveId);
    }
}
