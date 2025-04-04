package de.qaware.cloud;

import java.net.URI;

import org.eclipse.microprofile.rest.client.RestClientBuilder;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class NvdConnector {

    private static final String PASSWORD = "DON'T DO THIS!";

    private CveResource cveResource;

    public NvdConnector() {
        cveResource = RestClientBuilder.newBuilder()
            .baseUri(URI.create("https://services.nvd.nist.gov"))
            .build(CveResource.class);
    }

    public String queryCve(String cveId) {
        return cveResource.getByCveId(cveId);
    }

}
