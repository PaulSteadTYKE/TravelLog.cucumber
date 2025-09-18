package uk.co.tyke.travellog.cucumber.user;

import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StepDefinitions {

    private static final String USER_URL = "http://localhost:8090/";

    private static RequestSpecification request;
    private static Response response;


    private String authorizationHeader;
    private String testUserEmail;

    private final Logger logger = LoggerFactory.getLogger(StepDefinitions.class);

    @Before
    public void setUp() {
        // Have pre-existing cucumber master user
        // Log in and create a new user
        // Log out with master user

        // Create the test user on a separate thread, otherwise the master.it Principal
        // is passed into controller methods by Micronaut
        CreateTestUserThread createTestUserThread = new CreateTestUserThread();
        Thread thread = new Thread(createTestUserThread);
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        testUserEmail = createTestUserThread.getTestUserEmail();

        RestAssured.baseURI = USER_URL;
        request = RestAssured.given();
        request.header("Content-Type", "application/json");
    }

    @Given("A user is logged in")
    public void a_user_is_logged_in() {
        logger.debug("Logging in user {} with password P@ssw0rd", testUserEmail);
        JSONObject testUserLoginBody = new JSONObject();
        testUserLoginBody.put("username", testUserEmail);
        testUserLoginBody.put("password", "P@ssw0rd");
        response = request.body(testUserLoginBody.toString())
                .post("login");
        authorizationHeader = getAuthorizationHeader(response);
        logger.debug("authorizationHeader {}", authorizationHeader);
   }

    @When("I change the password")
    public void i_change_the_password() {
        JSONObject requestBody = new JSONObject();
        requestBody.put("op", "P@ssw0rd");
        requestBody.put("np", "P@ssw1rd");
        request.header("Authorization", authorizationHeader);

        logger.debug(requestBody.toString());

        response = request.body(requestBody.toString())
                .put("user/password");
    }

    @Then("I will see a HTTP OK response")
    public void i_will_see_a_HTTP_OK_response() {
        Assertions.assertEquals(200, response.getStatusCode(), "Expected HTTP OK (200)");
    }

    public void i_will_see_a_HTTP_CREATED_response() {
        Assertions.assertEquals(201, response.getStatusCode(), "Expected HTTP CREATED (204)");
    }

    private String getAuthorizationHeader (Response response) {
        JsonPath json = response.jsonPath();
        String accessToken = json.get("access_token");
        return "BEARER " + accessToken;
    }

}
