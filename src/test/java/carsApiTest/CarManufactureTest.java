package carsApiTest;

/*
 * Verifying End Points: /v1/car-types/manufacturer
 * This test suite is to verify the car manufacture api endpoints and its response
 * @extends BaseTest test case
 */

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

public class CarManufactureTest extends BaseTest {

	private static Response response;

	@BeforeTest
	public void setUp() {
		test.log(LogStatus.INFO,
				"Test started to validate the endpoint :" + ApiPath.apiPath.GET_LIST_OF_CAR_MANUFACTURERS);

		response = RestAssured.given().spec(BaseTest.reqSpecification)
				.get(ApiPath.apiPath.GET_LIST_OF_CAR_MANUFACTURERS);
	}

	@Test
	public void VerifyCorrectHTTPStatusCode() {

//	  Response response = RestAssured.given()
//	  RestAssured.given()
//	  .spec(BaseTest.reqSpecification)
//	  .get(ApiPath.apiPath.GET_LIST_OF_CAR_MANUFACTURERS).then().log().all();

//	  ResponseBody body = response.getBody();
//	  test.log(LogStatus.INFO, ""+body.asString());

		APIVerification.responseCodeValidation(response, 200);
		APIVerification.resonseTimeValidation(response);
		APIVerification.responseKeyValidator(response, "totalPageCount", 1);

	}

	@Test
	public void VerifyUnauthoriseStatusCodeValidation() {
		RestAssured.baseURI = FileAndEnv.envAndFile().get("ServerURL");
		Response response = RestAssured.get(ApiPath.apiPath.GET_LIST_OF_CAR_MANUFACTURERS);

		Assert.assertEquals(response.statusCode(), 401);
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
	public void VerifyResponseHeaders() {
		APIVerification.responseHeadersValidation(response, "Access-Control-Allow-Credentials", "true");
	}

	@Test
	public void VerifyNoEmptyResponse() {
		APIVerification.noEmptyResponseValidation(response);
	}

	@Test
	public void VerifyResponseWithManufactureCodeAndMainType() {
		APIVerification.responseManufactureAndMainTypeValidation(response, "830", "Suzuki");
		APIVerification.responseManufactureAndMainTypeValidation(response, "195", "Daewoo");
		APIVerification.responseManufactureAndMainTypeValidation(response, "020", "Abarth");
		APIVerification.responseManufactureAndMainTypeValidation(response, "960", "Zastava");

		APIVerification.responseMultiValueValidation(response);
	}

	@AfterTest
	public void tearDown() {
		test.log(LogStatus.INFO, "Test execution ended");
	}
}
