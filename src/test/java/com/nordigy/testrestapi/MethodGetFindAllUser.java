package com.nordigy.testrestapi;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.PostConstruct;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
// It allows to refresh context(Database) before an each method. So your tests always will be executed on the same snapshot of DB.
@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)
class MethodGetFindAllUser {

    @LocalServerPort
    private int port;

    @PostConstruct
    public void init() {
        RestAssured.port = port;
    }

    @Test
    public void shouldReturnCorrectUsersListSize() {
        given().log().all()
                .when().get("/api/users")
                .then().log().ifValidationFails()
                .statusCode(200)
                .body("page.totalElements", is(20));
    }

    @Test
    public void shouldReturnSetupPageSize() {
        ObjectNode user = given().log().all()
                .param("page","0")
                .param("size", "10")
                .param("sort", "id,sec")
                .when().get("/api/users")
                .then().log().ifValidationFails()
                .statusCode(200)
                .extract().body().as(ObjectNode.class);

        assertThat((user.get("page").get("size")).asInt()).isEqualTo(10);
    }
}