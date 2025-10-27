package com.kenny.doitpay.automation.Listeners;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.kenny.doitpay.automation.Helper.JsonHelper;

import io.restassured.http.Headers;
import io.restassured.response.Response;


/**
 * ApiLogHelper adalah kelas utilitas untuk mencatat detail request dan response API
 * ke dalam ExtentReports secara terstruktur.
 * <p>
 * class ini memanfaatkan {@link ExtentTest} untuk membuat node log terpisah untuk
 * request dan response, termasuk headers, endpoint, method, dan body (dengan format JSON yang
 * rapi).
 * </p>
 * @author Kenny Ramadhan
 * @version 1.0
 */
public class ApiLogHelper {


    private final ExtentTest test;

    public ApiLogHelper(ExtentTest test) {
        this.test = test;
    }
    public void logRequest(String method, String endpoint, Headers headers, String requestBody) {
    	ExtentTest requestNode = test.createNode(
    		    MarkupHelper.createLabel("Request Info", ExtentColor.BLACK).getMarkup()
    		);


        if (headers != null && !headers.asList().isEmpty()) {
            requestNode.info("Headers: " + headers.toString());
        }

        if (requestBody != null && !requestBody.isEmpty()) {

        	requestNode.info("Method: " + method);
        	requestNode.info("Endpoint: " + endpoint);
            String pretty = JsonHelper.prettyPrint(requestBody);
            requestNode.info(MarkupHelper.createLabel("Request Body:", ExtentColor.BLUE));
            requestNode.info(MarkupHelper.createCodeBlock(pretty));
            
        }
    }

    public void logResponse(Response response) {
    	 ExtentTest responseNode =  test.createNode(
     		    MarkupHelper.createLabel("Response Info", ExtentColor.BLACK).getMarkup()
    	    		);

         responseNode.info("Status Code: " + response.getStatusCode());

        Headers headers = response.getHeaders();
        if (headers != null && !headers.asList().isEmpty()) {
        	responseNode.info("Headers: " + headers.toString());
        }

        String body = response.getBody().asString();
        if (body != null && !body.isEmpty()) {
            String pretty = JsonHelper.prettyPrint(body);
            responseNode.info(MarkupHelper.createLabel("Response Body:", ExtentColor.GREEN));
            responseNode.info(MarkupHelper.createCodeBlock(pretty));
        }
    }
   
}