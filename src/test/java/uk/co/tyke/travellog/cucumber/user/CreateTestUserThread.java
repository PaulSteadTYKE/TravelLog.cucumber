package uk.co.tyke.travellog.cucumber.user;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.JSONObject;

public class CreateTestUserThread implements Runnable {

    public String getTestUserEmail() {
        return testUserEmail;
    }

    public volatile String testUserEmail;

    private static final String USER_URL = "http://localhost:8090/";

    @Override
    public void run() {

        RestAssured.baseURI = USER_URL;
        Response response;
        RequestSpecification  request = RestAssured.given();
        request.header("Content-Type", "application/json");

        // Master IT user logs in
        JSONObject masterUserLoginBody = new JSONObject();
        masterUserLoginBody.put("username", "master.it@tyke.co.uk");
        masterUserLoginBody.put("password", "P@ssw1rd");
        response = request.body(masterUserLoginBody.toString())
                .post("login");
        String authorizationHeader = getAuthorizationHeader(response);

        request.header("Authorization", authorizationHeader);

        response = request.get("user/itFirstName");

        JSONObject responseData = new JSONObject(response.asString());
        String previousFirstName = responseData.getJSONObject("data").getString("firstName");

        String suffix = previousFirstName.substring(4);
        int newSuffix = Integer.parseInt(suffix);
        String testUserFirstName = "test" + ++newSuffix;
        testUserEmail = testUserFirstName + ".it@tyke.co.uk";

        // Create a new user
        JSONObject createUserBody = new JSONObject();

        createUserBody.put("fi", testUserFirstName);
        createUserBody.put("la", "it");
        createUserBody.put("co", "UK");
        createUserBody.put("em", testUserEmail);
        createUserBody.put("pw", "P@ssw0rd");
        request.body(createUserBody.toString())
                .post("user");

    }

    private String getAuthorizationHeader (Response response) {
        JsonPath json = response.jsonPath();
        String accessToken = json.get("access_token");
        return "BEARER " + accessToken;
    }
}
