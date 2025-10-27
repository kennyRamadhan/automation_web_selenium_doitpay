package com.kenny.doitpay.automation.API;

import org.json.JSONObject;

import com.aventstack.extentreports.ExtentTest;


import io.restassured.response.Response;

public class MasterAPI {

	private final BaseTestAPI apiClient;

	public MasterAPI(ExtentTest test) {
		this.apiClient = new BaseTestAPI(test);
	}

	public Response createBooking(String firstname, String lastname, int totalprice, boolean depositpaid,
			String checkin, String checkout, String additionalneeds) {
		
		JSONObject bookingDates = new JSONObject();
		bookingDates.put("checkin", checkin);
		bookingDates.put("checkout", checkout);

		JSONObject body = new JSONObject();
		body.put("firstname", firstname);
		body.put("lastname", lastname);
		body.put("totalprice", totalprice);
		body.put("depositpaid", depositpaid);
		body.put("bookingdates", bookingDates);
		body.put("additionalneeds", additionalneeds);
		return apiClient.post("/booking", body.toString());
	}

}
