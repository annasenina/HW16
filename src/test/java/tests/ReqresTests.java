package tests;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static utils.FileUtils.readStringFromFile;

public class ReqresTests {

    @BeforeEach
    void beforeEach() {
        //RestAssured.filters(new AllureRestAssured());
        RestAssured.baseURI = "https://reqres.in/api";
    }

    @Test
    void successSingleUserTest(){
        Response response = given()
                .when()
                .get("/users/2")
                .then()
                .statusCode(200)
                .log().body()
                .extract()
                .response();

        assertEquals(response.path("data.email"), "janet.weaver@reqres.in", response.asString());
        assertEquals(response.path("data.first_name"), "Janet", response.asString());
        assertEquals(response.path("data.last_name"), "Weaver", response.asString());
    }

    @Test
    void unsuccessSingleUserTest(){
        given()
                .when()
                .get("/users/23")
                .then()
                .statusCode(404)
                .log().body();

    }

    @Test
    void successCreateUserTest(){
        String data = readStringFromFile("src/test/resources/createUser.json");

        Response response = given()
                .contentType(ContentType.JSON)
                .body(data)
                .when()
                .post("/users")
                .then()
                .statusCode(201)
                .log().body()
                .extract()
                .response();

        assertEquals(response.path("name"), "anna", response.asString());
        assertEquals(response.path("job"), "qa", response.asString());
    }

    @Test
    void successUpdateUserTest(){
        String data = readStringFromFile("src/test/resources/updateUser.json");

        Response response = given()
                .contentType(ContentType.JSON)
                .body(data)
                .when()
                .put("/users/2")
                .then()
                .statusCode(200)
                .log().body()
                .extract()
                .response();

        assertEquals(response.path("name"), "anna", response.asString());
        assertEquals(response.path("job"), "qa", response.asString());
    }

    @Test
    void successRegisterUserTest(){
        String data = readStringFromFile("src/test/resources/registerUser.json");

        given()
                .contentType(ContentType.JSON)
                .body(data)
                .when()
                .post("/register")
                .then()
                .statusCode(200)
                .log().body()
                .body("token", is(notNullValue()));


    }
}
