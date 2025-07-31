package uk.co.tyke.travellog.cucumber.user;

import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//import uk.co.tyke.travellog.journey.model.Location;
//
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class StepDefinitions {

    private static final String USER_URL = "http://localhost:8090/user";

    private static RequestSpecification request;
    private static Response response;

    private Logger logger = LoggerFactory.getLogger(StepDefinitions.class);

    @Before
    public void setUp() {
        // Have pre-existing cucumber master user
        // Log in and create a new user
        // Log out with master user
        // Log in with new user


        RestAssured.baseURI = USER_URL;
        request = RestAssured.given();
        request.header("Content-Type", "application/json");

        // Master IT user logs in
        JSONObject requestBody = new JSONObject();
        requestBody.put("username", "master.it@tyke.co.uk");
        requestBody.put("password", "Passw0rd");

        logger.debug(requestBody.toString());

        response = request.body(requestBody.toString())
                .post("http://localhost:8090/login");

        i_will_see_a_HTTP_OK_response();



    }

    @Given("A user is logged in")
    public void a_user_is_logged_in() {
//        RestAssured.baseURI = USER_URL;
//        RequestSpecification request = RestAssured.given();
//        request.header("Content-Type", "application/json");
        JSONObject requestBody = new JSONObject();
        requestBody.put("op", "Skipton123");
        requestBody.put("np", "Skipton124");
    }

    @When("I change the password")
    public void i_change_the_password() {
        // Call the change password endpoint
//        RestAssured.baseURI = USER_URL;
//        RequestSpecification request = RestAssured.given();
//        request.header("Content-Type", "application/json");

        JSONObject requestBody = new JSONObject();
        requestBody.put("op", "Skipton123");
        requestBody.put("np", "Skipton124");

        logger.debug(requestBody.toString());

        response = request.body(requestBody.toString())
                .post("");
    }
    @Then("I will see a HTTP OK response")
    public void i_will_see_a_HTTP_OK_response() {
        Assertions.assertEquals(200, response.getStatusCode(), "Expected HTTP OK (200)");
    }

}
