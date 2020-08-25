package carsApiTest;

import java.util.ArrayList;

import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.LogStatus;

import Utilities.FileAndEnv;
import apiConfigs.ApiPath;
import apiVerification.APIVerification;
import baseTest.BaseTest;
import io.restassured.RestAssured;
import io.restassured.response.Response;

/*
 * Verifying End Point: /v1/car-types/main-types
 */
public class CarTypesAPITests extends BaseTest {

	private static Response response;

	@BeforeTest
	public void setUp() {
		test.log(LogStatus.INFO, "Test started to validate the endpoint :" + ApiPath.apiPath.GET_CAR_MAIN_TYPES);
	}

	@Test
	public void VerifyCorrectStatusCode() {
		response = RestAssured.given().spec(BaseTest.reqSpecification).param("manufacturer", "020")
				.get(ApiPath.apiPath.GET_CAR_MAIN_TYPES);

		APIVerification.responseCodeValidation(response, 200);
		APIVerification.resonseTimeValidation(response);
	}

	@Test
	public void VerifyUnauthoriseStatusCodeValidation() {
		RestAssured.baseURI = FileAndEnv.envAndFile().get("ServerURL");

		final int status_code = RestAssured.given().param("manufacturer", "020").get(ApiPath.apiPath.GET_CAR_MAIN_TYPES)
				.getStatusCode();

		Assert.assertEquals(status_code, 401);
	}

	@Test
	public void VerifyResponsePayload() {

		ArrayList<String> arraList = new ArrayList<String>();
		arraList.add("page");
		arraList.add("pageSize");
		arraList.add("totalPageCount");
		arraList.add("wkda");
		APIVerification.responsePayloadValidation(response, arraList);

	}

	@Test
	public void VerifyNoValueOnInvalidManufacturerCode() {

		RestAssured.baseURI = FileAndEnv.envAndFile().get("ServerURL");

		Response resp_car_main_types = RestAssured.given().spec(BaseTest.reqSpecification).param("manufacturer", "20")
				.get(ApiPath.apiPath.GET_CAR_MAIN_TYPES);

		APIVerification.responseCodeValidation(resp_car_main_types, 200);
		
		APIVerification.verifyInvalidManufacturerCodeResponseGracefully(resp_car_main_types);
		
	}

	@Test
	public void VerifyMappingCarTypesWithManufacturersValidation() {
		Response car_man_response = RestAssured.given().spec(BaseTest.reqSpecification)
				.get(ApiPath.apiPath.GET_LIST_OF_CAR_MANUFACTURERS);

		APIVerification.responseCodeValidationMappingTypesAndManufacturer(car_man_response, 200);
	}
	
	@AfterTest
	public void tearDown() {
		test.log(LogStatus.INFO, "Test execution ended");
	}

}
