package com.weather.restapi.stepdefs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.weather.restapi.entities.Location;
import com.weather.restapi.entities.Weather;
import com.weather.restapi.util.RestApiClient;
import com.weather.restapi.util.RestApiResponse;
import cucumber.api.DataTable;
import cucumber.api.PendingException;
import cucumber.api.java.Before;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.json.JSONObject;
import org.junit.Assume;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class WeatherStepDefs {

	private final static Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	private static boolean isWebAppStarted = false;

	@Value("${server.port:8080}")
	private int serverPort = 8080;

	private Integer statusCode;
	private String jsonOutputString;
	private boolean valueEncodingEnabled = true;

	@Before
	public void ensureOsIsNotWindows() {
		Assume.assumeFalse(System.getProperty("os.name").toLowerCase().startsWith("windows"));
	}

	/*@Given("^the application has started successfully$")
	public void theApplicationHasStartedSuccessfully() {
		if (!isWebAppStarted) {
			log.info("Starting NBI WebApp");
			SpringApplication.run(WeatherApplication.class);
			log.info("Started NBI WebApp");
			isWebAppStarted = true;
		}
	}*/

	@When("^the following data is added to the Weather table$")
	public void theFollowingDataIsAddedToTheWeatherTable(DataTable dataTable) throws Exception {
		String nbiUrl = "http://localhost:" + serverPort + "/weather/add";
		List<Map<String, String>> records = dataTable.asMaps(String.class, String.class);
		for (Map<String, String> record : records) {
			StringBuilder inputParameters = new StringBuilder();
			String zipcode = record.get("zipcode");
			String date = record.get("date");
			String temperature = record.get("temperature");

			inputParameters.append(nbiUrl).append("?").append("zipcode=").append(zipcode).append("&date=")
					.append(date).append("&temperature=").append(temperature);
			RestApiResponse response = RestApiClient.invoke(inputParameters.toString(), "PUT");
			statusCode = response.getStatusCode();
			assertEquals(new Integer(200), statusCode);
		}
	}

	@Then("^the total number of records in the Location table is (\\d+)$")
	public void theTotalNumberOfRecordsInLocationTableIs(int count) throws Exception {
		String nbiUrl = "http://localhost:" + serverPort + "/location/fetchAll";
		RestApiResponse response = RestApiClient.invoke(nbiUrl, "GET");
		statusCode = response.getStatusCode();
		assertEquals(new Integer(200), statusCode);
		jsonOutputString = response.getResponse();
		JSONObject jsonObject = new JSONObject(jsonOutputString);
		String content = jsonObject.getString("content");

		ObjectMapper mapper = new ObjectMapper();
		List<Location> locations = mapper.readValue(content,
				mapper.getTypeFactory().constructCollectionType(List.class, Location.class));
		assertEquals(count, locations.size());

	}

	@Then("^the total number of records in Weather table is (\\d+)$")
	public void theTotalNumberOfRecordsInWeatherTableIs(int count) throws Exception {
		String nbiUrl = "http://localhost:" + serverPort + "/weather/fetchAll";
		RestApiResponse response = RestApiClient.invoke(nbiUrl, "GET");
		statusCode = response.getStatusCode();
		assertEquals(new Integer(200), statusCode);
		jsonOutputString = response.getResponse();
		JSONObject jsonObject = new JSONObject(jsonOutputString);
		String content = jsonObject.getString("content");

		ObjectMapper mapper = new ObjectMapper();
		List<Weather> weatherInfos = mapper.readValue(content,
				mapper.getTypeFactory().constructCollectionType(List.class, Weather.class));
		assertEquals(count, weatherInfos.size());

	}

	@When("^I request weather details by zipcode \"(.*)\"$")
	public void iRequestWeatherByZipcode(String zipcode) throws Throwable {
		String nbiUrl = "http://localhost:" + serverPort + "/weather/fetchAllByZipcode?zipcode=" + zipcode;
		RestApiResponse response = RestApiClient.invoke(nbiUrl, "GET");
		statusCode = response.getStatusCode();
//		assertEquals(new Integer(200), statusCode);
		jsonOutputString = response.getResponse();
	}

	@Then("^I expect weather details in the content as \"(.*)\"$")
	public void iExpectWeatherDetailsInTheContentAs(String expectedContent) throws Throwable {
		ObjectMapper objectMapper = new ObjectMapper();
		List<Weather> expectedWeatherDetails = objectMapper.readValue(expectedContent,
				objectMapper.getTypeFactory().constructCollectionType(List.class, Weather.class));

		JSONObject jsonObject = new JSONObject(jsonOutputString);
		String actualContent = jsonObject.getString("content");
		List<Weather> actualWeatherDetails = objectMapper.readValue(actualContent,
				objectMapper.getTypeFactory().constructCollectionType(List.class, Weather.class));

		assertEquals(expectedWeatherDetails.size(), actualWeatherDetails.size());
		for (int index = 0; index < expectedWeatherDetails.size(); index++) {
			Weather expected = expectedWeatherDetails.get(index);
			Weather actual = actualWeatherDetails.get(index);
			assertEquals(expected, actual);
		}

	}

	@When("^I request weather details using zipcode \"([^\"]*)\" and date \"([^\"]*)\"$")
	public void iRequestWeatherDetailsUsingZipcodeAndDate(String zipcode, String date) throws Throwable {
		String nbiUrl = "http://localhost:" + serverPort + "/weather/fetchByZipcodeAndDate?zipcode=" +
				zipcode + "&date=" + date;
		RestApiResponse response = RestApiClient.invoke(nbiUrl, "GET");
		statusCode = response.getStatusCode();
//		assertEquals(new Integer(200), statusCode);
		jsonOutputString = response.getResponse();
	}

	@Then("^I expect weather details in the response as \"(.*)\"$")
	public void iExpectWeatherDetailsInTheResponseAs(String expectedResponse) throws Throwable {
		assertEquals(expectedResponse, jsonOutputString);
	}

	@When("^I request weather details using zipcode \"([^\"]*)\", startDate \"([^\"]*)\" and endDate \"([^\"]*)\"$")
	public void iRequestWeatherDetailsUsingZipcodeStartDateAndEndDate(String zipcode, String startDate, String endDate) throws Throwable {
		String nbiUrl = "http://localhost:" + serverPort + "/weather/fetchAllByZipcodeBetweenDates?zipcode=" +
				zipcode + "&startDate=" + startDate + "&endDate=" + endDate;
		RestApiResponse response = RestApiClient.invoke(nbiUrl, "GET");
		statusCode = response.getStatusCode();
//		assertEquals(new Integer(200), statusCode);
		jsonOutputString = response.getResponse();
	}

	@When("^I request weather details using zipcode \"([^\"]*)\", startDate \"([^\"]*)\", endDate \"([^\"]*)\", page \"([^\"]*)\", size \"([^\"]*)\" and sort \"([^\"]*)\"$")
	public void iRequestWeatherDetailsUsingZipcodeStartDateEndDatePageSizeAndSort(String zipcode, String startDate, String endDate, String page, String size, String sort) throws Throwable {
		String nbiUrl = "http://localhost:" + serverPort + "/weather/fetchAllByZipcodeBetweenDates?zipcode=" + zipcode
				+ "&startDate=" + startDate + "&endDate=" + endDate + "&page=" + page + "&size=" + size + "&sort=" + sort;
		RestApiResponse response = RestApiClient.invoke(nbiUrl, "GET");
		statusCode = response.getStatusCode();
//		assertEquals(new Integer(200), statusCode);
		jsonOutputString = response.getResponse();
	}

	@Then("^I expect in the response the status code is \"(.*)\"$")
	public void iExpectInTheResponseTheStatusCodeAs(Integer expectedStatusCode) throws Throwable {
		assertEquals(expectedStatusCode, statusCode);
	}

	@When("^I request weather details with zipcode \"([^\"]*)\", page \"([^\"]*)\", size \"([^\"]*)\" and sort \"([^\"]*)\"$")
	public void iRequestWeatherDetailsWithZipcodePageSizeAndSort(String zipcode, String page, String size, String sort) throws Throwable {
		String nbiUrl = "http://localhost:" + serverPort + "/weather/fetchAllByZipcode?zipcode=" + zipcode
				+ "&page=" + page + "&size=" + size + "&sort=" + sort;
		RestApiResponse response = RestApiClient.invoke(nbiUrl, "GET");
		statusCode = response.getStatusCode();
//		assertEquals(new Integer(200), statusCode);
		jsonOutputString = response.getResponse();
	}
}

