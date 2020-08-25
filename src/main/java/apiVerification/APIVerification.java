package apiVerification;

import org.testng.Assert;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONObject;
import com.relevantcodes.extentreports.LogStatus;

import Utilities.ExtentReportListner;
import Utilities.FileAndEnv;
import apiConfigs.ApiPath;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class APIVerification extends ExtentReportListner{

	// Validating the response code from the api response
	public static void responseCodeValidation(Response response, int code) {
		try {
			Assert.assertEquals(code, response.getStatusCode());
			test.log(LogStatus.PASS, "Successfully validated the status "+ response.getStatusCode());
		} catch (AssertionError e) {
			test.log(LogStatus.FAIL, "Detail Error Message"+e.getStackTrace());
			
			test.log(LogStatus.FAIL, 
					"Expected Status Code :"+code,
					"Instead got: "+response.getStatusCode());
		} catch (Exception e) {
			test.log(LogStatus.FAIL, e.fillInStackTrace());
		}
	}
	
//	 Validating the page count entry from the api reponse end point
	public static void responseKeyValidator(Response response, String key, int expected) {
		try {
			JsonPath jsonPathEvaluator = response.jsonPath();
			Assert.assertEquals(jsonPathEvaluator.get(key), expected);
			test.log(LogStatus.PASS, "Validating the pagecount"+jsonPathEvaluator.get(key));
		} catch (Exception e) {
			test.log(LogStatus.FAIL, e.fillInStackTrace());
		}
	}
	
	// validating the time the api endpoint takes to reponse
	public static void resonseTimeValidation(Response response) {
		
		try {
			long time = response.time();
			test.log(LogStatus.PASS, "Response time from Api "+ time);
		} catch (Exception e) {
			test.log(LogStatus.FAIL, e.fillInStackTrace());
		}
	}
	
	// Verify the payload from the api end points
	public static void responsePayloadValidation(Response response, ArrayList<String> arrList) {
		try {
			JSONObject jsonObject = new JSONObject(response.asString());
			
			List<String> keysList = new ArrayList<String>();
			
			Iterator<String> keys = jsonObject.keys();
			
			while(keys.hasNext()) {
			    String key = (String) keys.next();
//			    System.out.println(key);
			    keysList.add(key);
			}
			Collections.sort(keysList);
			
			
			Assert.assertEquals(keysList, arrList, "Listed keys are not present");
			test.log(LogStatus.PASS, "Verified Payloads "+keysList);
			
		} catch (Exception e) {
			test.log(LogStatus.FAIL, e.fillInStackTrace());
		}
	}

	// Verify the headers from the api response 
	public static void responseHeadersValidation(Response response, String headerKey, String expected) {
		
		try {
			Assert.assertEquals(response.getHeader(headerKey), expected, "Unable to find right header");
			test.log(LogStatus.PASS, "Access-Control-Allow-Credentials: "+response.getHeader(headerKey));
		} catch (Exception e) {
			test.log(LogStatus.FAIL, e.fillInStackTrace());
		}
		
	}

	// Verify the response data should not be empty as validate 
	public static void noEmptyResponseValidation(Response response) {
		
		try {
			JSONObject jsonObject = new JSONObject(response.asString());
			Assert.assertNotNull(jsonObject, "Found an empty response");
			test.log(LogStatus.PASS, "Response information is not empty");
		} catch (Exception e) {
			test.log(LogStatus.FAIL, "Response information is empty");
		}
		
	}

	// Verify manufacturers code listed on passing the manufacturers code
	public static void responseManufactureAndMainTypeValidation(Response response, String manufacturer, String expected) {
		
		try {
			
			JsonPath jsonPathEvaluator = response.jsonPath();
		
			JSONObject jsonObject = new JSONObject(jsonPathEvaluator.getMap("wkda"));
			
			Assert.assertEquals(jsonObject.get(manufacturer), expected, "Unable to find the manufacturer");
			
			test.log(LogStatus.PASS, "Manufacturer information found : Manufacturer Main type : "+manufacturer+" Manufacturer Name: "+ jsonObject.get(manufacturer));
			
		} catch (Exception e) {
			
			test.log(LogStatus.FAIL, e.fillInStackTrace());
		}
		
	}
	
	// Verify Manufacture code and Manufacturer name are not empty
	public static void responseMultiValueValidation(Response response) {
		
		try {
			JsonPath jsonPath = new JsonPath(response.asString());
			Map<String, String> applicationsMap = jsonPath.getMap("wkda");
			Set<String> applicationKeys = applicationsMap.keySet();
			
			for(String key:applicationKeys){
//			    System.out.println("Key value: " + key + ", Element value: " + applicationsMap.get(key));
			    Assert.assertNotNull(key);
			    Assert.assertNotNull(applicationsMap.get(key));
			    test.log(LogStatus.PASS, "Found Keys :" + key + ", Found Mapping Manufacturer: " + applicationsMap.get(key));
			}
		} catch (Exception e) {
			test.log(LogStatus.FAIL, e.fillInStackTrace());
		}
	}

	// Verify car models code and car model names
	public static void responseCodeValidationMappingTypesAndManufacturer(Response car_man_response, int code) {
		
		try {
			JsonPath jsonPath = new JsonPath(car_man_response.asString());
			Map<String, String> applicationsMap = jsonPath.getMap("wkda");
			Set<String> applicationKeys = applicationsMap.keySet();
			RestAssured.baseURI = FileAndEnv.envAndFile().get("ServerURL");
			
			for(String key:applicationKeys){
//			    System.out.println("Key value: " + key + ", Element value: " + applicationsMap.get(key));
			    Assert.assertNotNull(key);
			    
			    int status_code = RestAssured.given()
			    		.param("manufacturer", key)
			    		.param("wa_key", FileAndEnv.envAndFile().get("Key"))
			    		.get(ApiPath.apiPath.GET_CAR_MAIN_TYPES).getStatusCode();
			    
			    Assert.assertNotNull(applicationsMap.get(key));
			    Assert.assertEquals(status_code, code);
			    
			    test.log(LogStatus.PASS, "Model Code: "+ key + ", Model Name "+applicationsMap.get(key));
			}
		} catch (Exception e) {
			test.log(LogStatus.FAIL, e.fillInStackTrace());
		}
		
	}

	// Verify build date based on the manufacturer code
	public static void verifyManufacuringYearForDiffererntModelValidation(Response car_main_type_resp, String manufacturer_code, int code) {
		
		try {
			JsonPath jsonPath = new JsonPath(car_main_type_resp.asString());
			Map<String, String> applicationsMap = jsonPath.getMap("wkda");
			Set<String> carModelKeys = applicationsMap.keySet();
			
			RestAssured.baseURI = FileAndEnv.envAndFile().get("ServerURL");
			
			for(String key:carModelKeys){
//			    System.out.println("Key value: " + key + ", Element value: " + applicationsMap.get(key));
			    Assert.assertNotNull(key);
			    
			    int status_code = RestAssured.given()
			    		.param("manufacturer", manufacturer_code)
			    		.param("main-type", key)
			    		.param("wa_key", FileAndEnv.envAndFile().get("Key"))
			    		.get(ApiPath.apiPath.GET_CAR_BUILD_DATE).getStatusCode();
			    
			    Assert.assertNotNull(applicationsMap.get(key));
			    Assert.assertEquals(status_code, code);
			    
			    test.log(LogStatus.PASS, "Manufacturer code: "+manufacturer_code+" Main types: "+key);
			}
			
		} catch (Exception e) {
			test.log(LogStatus.FAIL, e.fillInStackTrace());
		}
		
	}

	// Verify the incorrect manufacturer code response success but with empty wkda value
	public static void verifyInvalidManufacturerCodeResponseGracefully(Response resp_car_main_types) {
		
		try {
			JsonPath jsonPath = new JsonPath(resp_car_main_types.asString());
			
			Map<String, String> applicationsMap = jsonPath.getMap("wkda");
		
			Assert.assertEquals(applicationsMap.isEmpty(), true);

			test.log(LogStatus.PASS, "Invalid manufacture code : "+resp_car_main_types.asString());
			
		} catch (Exception e) {
			test.log(LogStatus.FAIL, e.fillInStackTrace());
		}
		
	}
}
























