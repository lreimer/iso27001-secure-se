package de.qaware.cloud;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;

@Path("/rest/json/cves/2.0")
public interface CveResource {
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    String getByCveId(@QueryParam("cveId") String cveId);
}
