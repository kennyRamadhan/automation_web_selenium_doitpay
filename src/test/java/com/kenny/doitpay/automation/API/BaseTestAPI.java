package com.kenny.doitpay.automation.API;

import static io.restassured.RestAssured.given;

import com.aventstack.extentreports.ExtentTest;
import com.kenny.doitpay.automation.Listeners.ApiLogHelper;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class BaseTestAPI {
	private final RequestSpecification spec;
    private final ApiLogHelper apiLogger;
    private static final String BASE_URL = "https://restful-booker.herokuapp.com";
    private String authToken;

    
    
    
    public BaseTestAPI(ExtentTest test) {
    	 this.apiLogger = new ApiLogHelper(test);

         // Login otomatis
         String requestBody = "{ \"username\": \"admin\", \"password\": \"password123\" }";
         Response authResponse = given()
                 .baseUri(BASE_URL)
                 .contentType(ContentType.JSON)
                 .body(requestBody)
                 .when()
                 .post("/auth")
                 .then()
                 .extract()
                 .response();

         this.authToken = authResponse.jsonPath().getString("token");

         // Build spec dengan token
         this.spec = new RequestSpecBuilder()
                 .setBaseUri(BASE_URL)
                 .setContentType(ContentType.JSON)
                 .addCookie("token", authToken) 
                 .build();
    }
    
    public Response post(String endpoint, String body) {
        apiLogger.logRequest("POST", endpoint ,null, body);

        Response response = given()
        		.baseUri(BASE_URL)
                .spec(spec)
                .body(body)
                .when()
                .post(endpoint)
                .then()
                .extract()
                .response();

        apiLogger.logResponse(response);
        return response;
    }
    
    public Response get(String endpoint) {
        apiLogger.logRequest("GET", endpoint, null, null);

        Response response = given()
                .spec(spec)
                .when()
                .get(endpoint)
                .then()
                .extract()
                .response();

        apiLogger.logResponse(response);
        return response;
    }

}
