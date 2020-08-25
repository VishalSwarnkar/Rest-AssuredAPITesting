package carsApiTest;

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
 * Verifying End Points: /v1/car-types/built-dates
 */
public class CarBuildDatesTests extends BaseTest {

	private static Response response;

	@BeforeTest
	public void setUp() {
		test.log(LogStatus.INFO, "Test started to validate the endpoint :" + ApiPath.apiPath.GET_CAR_BUILD_DATE);

	}

	@Test
	public void VerifyCorrectStatusCode() {
		response = RestAssured.given().spec(BaseTest.reqSpecification).param("manufacturer", "020")
				.param("main-type", "Grande Punto").get(ApiPath.apiPath.GET_CAR_BUILD_DATE);

		APIVerification.responseCodeValidation(response, 200);
		APIVerification.resonseTimeValidation(response);
	}

	@Test
	public void VerifyUnauthoriseStatusCodeValidation() {
		RestAssured.baseURI = FileAndEnv.envAndFile().get("ServerURL");

		int status_code = RestAssured.given().param("manufacturer", "020").param("main-type", "Grande Punto")
				.get(ApiPath.apiPath.GET_CAR_BUILD_DATE).getStatusCode();

		Assert.assertEquals(status_code, 401);
	}
	
	@Test
	public void VerifyCarBuildingDatesMappedWithManufacturers() {
		Response car_main_type_resp = RestAssured.given().spec(BaseTest.reqSpecification).param("manufacturer", "020")
		.get(ApiPath.apiPath.GET_CAR_MAIN_TYPES);
		
		APIVerification.verifyManufacuringYearForDiffererntModelValidation(car_main_type_resp, "020", 200);
	}
	
	@AfterTest
	public void tearDown() {
		test.log(LogStatus.INFO, "Test execution ended");
	}
	
}
