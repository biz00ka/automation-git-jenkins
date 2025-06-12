package org.sid.tests;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class ApiTests {



    @Test
    public void showFirst()
    {
        System.out.println("first test launch.");
        RestAssured.baseURI = "https://reqres.in/api";
        given()
                .when()
                .get("/users?page=2")
                .then()
                .statusCode(200)
                .body("data[0].first_name", equalTo("Michael"));

    }

    @Test
    public void showSecond()
    {
        System.out.println("Second test launch.");
        RestAssured.baseURI = "https://reqres.in/api";
        Response response=given()
                .when()
                .get("/users?page=2")
                .andReturn();

        System.out.println(response.prettyPrint());
    }


}
