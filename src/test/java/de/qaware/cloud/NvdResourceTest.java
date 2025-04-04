package de.qaware.cloud;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

@QuarkusTest
class NvdResourceTest {
    @Test
    void testGetCve() {
        given()
          .when().get("/api/cves/CVE-2021-44228")
          .then()
             .statusCode(200)
             .body(is(notNullValue()));
    }

}