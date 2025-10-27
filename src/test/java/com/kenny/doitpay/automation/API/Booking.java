package com.kenny.doitpay.automation.API;

import org.testng.annotations.Test;
import org.testng.AssertJUnit;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.aventstack.extentreports.ExtentTest;
import com.github.javafaker.Faker;
import com.kenny.doitpay.automation.Listeners.ExtentNode;

import io.restassured.response.Response;

public class Booking {

	private final Faker faker = new Faker();

	String firstname = faker.name().firstName();
	String lastname = faker.name().lastName();
	int totalPrice = faker.number().numberBetween(100, 1000);
	boolean depositPaid = faker.bool().bool();
	String checkin = "2025-01-01"; // Bisa static atau custom date
	String checkout = "2025-01-10";
	String additionalNeeds = faker.food().dish();

	@Test
	public void createBooking() {
		ExtentTest test = ExtentNode.getNode();
		MasterAPI authApi = new MasterAPI(test);
		Response response = authApi.createBooking(firstname, lastname, totalPrice, depositPaid, checkout, checkout,
				additionalNeeds);
		AssertJUnit.assertEquals(response.getStatusCode(), 200);

	}

}
