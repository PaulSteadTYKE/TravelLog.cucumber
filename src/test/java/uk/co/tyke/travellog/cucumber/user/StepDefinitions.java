package uk.co.tyke.travellog.cucumber.user;

import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
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

    private static final String USER_URL = "http://localhost:8090/";

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
        JSONObject masterUserLoginBody = new JSONObject();
        masterUserLoginBody.put("username", "master.it@tyke.co.uk");
        masterUserLoginBody.put("password", "P@ssw0rd");
        response = request.body(masterUserLoginBody.toString())
                .post("login");

        i_will_see_a_HTTP_OK_response();

        // Get access token and create header
        JsonPath json = response.jsonPath();
        String accessToken = json.get("access_token");

        String authorizationHeader = "BEARER " + accessToken;
        request.header("Authorization", authorizationHeader);

        // Create a new user
        JSONObject createUserBody = new JSONObject();

        createUserBody.put("fi", "test001");
        createUserBody.put("la", "it");
        createUserBody.put("co", "UK");
        createUserBody.put("em", "test001.it@tyke.co.uk");
        createUserBody.put("pw", "P@ssw0rd");
        response = request.body(createUserBody.toString())
                .post("user");

        i_will_see_a_HTTP_CREATED_response();
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
                .post("user");
    }
    @Then("I will see a HTTP OK response")
    public void i_will_see_a_HTTP_OK_response() {
        Assertions.assertEquals(200, response.getStatusCode(), "Expected HTTP OK (200)");
    }

    public void i_will_see_a_HTTP_CREATED_response() {
        Assertions.assertEquals(201, response.getStatusCode(), "Expected HTTP CREATED (204)");
    }

}
