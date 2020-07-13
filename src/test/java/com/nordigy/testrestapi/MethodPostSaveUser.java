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
class MethodPostSaveUser {

    @LocalServerPort
    private int port;

    @PostConstruct
    public void init() {
        RestAssured.port = port;
    }

    @Test
    public void shouldReturnSaveField() {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("dayOfBirth", "1222-01-02");
        objectNode.put("email", "wiujafdsddshf@gmail.com");
        objectNode.put("firstName", "Asdasafasasaad");
        objectNode.put("lastName","Vssadassdwq");

        ObjectNode user = given().log().all()
                .body(objectNode)
                .contentType(ContentType.JSON)
                .when().post("/api/users")
                .then().log().ifValidationFails()
                .statusCode(201)
                .extract().body().as(ObjectNode.class);

        assertThat((user.get("dayOfBirth")).asText()).isEqualTo("1222-01-02");
        assertThat((user.get("email")).asText()).isEqualTo("wiujafdsddshf@gmail.com");
        assertThat((user.get("firstName")).asText()).isEqualTo("Asdasafasasaad");
        assertThat((user.get("lastName")).asText()).isEqualTo("Vssadassdwq");
    }
}