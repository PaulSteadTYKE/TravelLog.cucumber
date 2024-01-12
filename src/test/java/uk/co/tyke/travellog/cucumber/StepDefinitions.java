package uk.co.tyke.travellog.cucumber;

import io.cucumber.java.en.*;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.JSONObject;
import uk.co.tyke.travellog.journey.model.Trip;
import uk.co.tyke.travellog.journey.model.TripPoint;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public class StepDefinitions {

    private static final String BASE_URL = "http://localhost:8080/journey";

    private static Response response;

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
        requestBody.put("name", "Route to nowhere");
        requestBody.put("notes", "It's a long long way");

        TripPoint start = new TripPoint(BigDecimal.valueOf(53.9628), BigDecimal.valueOf(2.0163)
        , 100, LocalDateTime.now());
        TripPoint finish = new TripPoint(BigDecimal.valueOf(51.1472), BigDecimal.valueOf(2.0475)
                , 105, LocalDateTime.now());

        Trip trip = new Trip(Arrays.asList(start, finish));

        requestBody.put("trips", Arrays.asList(trip));

        response = request.body(requestBody.toString())
                .post("/");
    }
    @Then("I will see a HTTP No Content response")
    public void i_will_see_a_HTTP_No_Content_response() {
        // Check the result of the save
//        throw new io.cucumber.java.PendingException();
    }

}
