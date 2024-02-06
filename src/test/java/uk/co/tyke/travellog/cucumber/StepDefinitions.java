package uk.co.tyke.travellog.cucumber;

import io.cucumber.java.en.*;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.co.tyke.travellog.journey.model.Location;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class StepDefinitions {

    private static final String BASE_URL = "http://localhost:8080/journey";

    private static Response response;

    private Logger logger = LoggerFactory.getLogger(StepDefinitions.class);

    @Given("A journey has been completed")
    public void a_journey_has_been_completed() {
        // Nothing to do here?
        // The journey will be marked as completed in the UI
    }
    @When("I save the journey")
    public void i_save_the_journey() {
        // Call the save journey endpoint
        RestAssured.baseURI = BASE_URL;
        RequestSpecification request = RestAssured.given();
        request.header("Content-Type", "application/json");

        JSONObject requestBody = new JSONObject();
        requestBody.put("na", "Route to nowhere");
        requestBody.put("no", "It's a long long way");

        Location start = new Location(BigDecimal.valueOf(53.9628), BigDecimal.valueOf(2.0163)
        , 100, LocalDateTime.now());
        Location finish = new Location(BigDecimal.valueOf(51.1472), BigDecimal.valueOf(2.0475)
                , 105, LocalDateTime.now());

        JSONObject startPoint = new JSONObject();
        startPoint = new JSONObject();
        startPoint.put("la", BigDecimal.valueOf(53.9628));
        startPoint.put("lg", BigDecimal.valueOf(2.0163));
        startPoint.put("al", 100);
        startPoint.put("ti", LocalDateTime.now());

        JSONObject endPoint = new JSONObject();
        endPoint = new JSONObject();
        endPoint.put("la", BigDecimal.valueOf(51.1472));
        endPoint.put("lg", BigDecimal.valueOf(2.0475));
        endPoint.put("al", 105);
        endPoint.put("ti", LocalDateTime.now());

        JSONArray locations = new JSONArray();
        locations.put(startPoint);
        locations.put(endPoint);

        JSONObject Leg = new JSONObject();
        Leg.put("los", locations);


        JSONArray legs = new JSONArray();
        legs.put(Leg);

        requestBody.put("les", legs);

        logger.debug(requestBody.toString());

        response = request.body(requestBody.toString())
                .post("");
    }
    @Then("I will see a HTTP No Content response")
    public void i_will_see_a_HTTP_No_Content_response() {
        // Check the result of the save
        Assertions.assertEquals(201, response.getStatusCode(), "Expected HTTP Create (201)");
    }

}
