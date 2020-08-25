package baseTest;

import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Listeners;
import Utilities.ExtentReportListner;
import Utilities.FileAndEnv;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

/*
 * Creating base test that creates the base URL with additional mandatory key parameters 
 * @params {wa_key}
 * From the environment variable it pulls the right properties and append to the query params
 * e.g. -Denv=qa -Dtestng.dtd.http=true
 */
@Listeners(ExtentReportListner.class)
public class BaseTest extends ExtentReportListner {

	public static RequestSpecification reqSpecification;

	@BeforeSuite
	public void baseTest() {
		reqSpecification = (RequestSpecification) new RequestSpecBuilder()
				.setBaseUri(FileAndEnv.envAndFile().get("ServerURL"))
				.addParam("wa_key", FileAndEnv.envAndFile().get("Key")).setContentType(ContentType.JSON)
				.setAccept(ContentType.JSON).log(LogDetail.ALL).build();
	}
}
