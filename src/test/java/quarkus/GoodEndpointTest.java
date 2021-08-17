package quarkus;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static quarkus.AuthUtils.givenAdmin;
import static quarkus.AuthUtils.givenAnonymous;
import static quarkus.AuthUtils.givenNonAdmin;

@QuarkusTest
public class GoodEndpointTest {

    static {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    @Test
    public void adminShouldGetOK() {
        givenAdmin()
                .body("{\"key\":21}")
                .contentType(ContentType.JSON)
                .when().post("/hello/good-endpoint")
                .then().statusCode(200);
    }

    @Test
    public void nonAdminShouldGet403() {
        givenNonAdmin()
                .body("{\"key\":21}")
                .contentType(ContentType.JSON)
                .when().post("/hello/good-endpoint")
                .then().statusCode(403);
    }

    @Test
    public void anonymousShouldGet401() {
        givenAnonymous()
                .body("{\"key\":21}")
                .contentType(ContentType.JSON)
                .when().post("/hello/good-endpoint")
                .then().statusCode(401);
    }
}