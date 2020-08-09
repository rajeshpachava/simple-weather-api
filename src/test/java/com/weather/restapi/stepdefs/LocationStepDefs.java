package com.weather.restapi.stepdefs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.weather.restapi.WeatherApplication;
import com.weather.restapi.entities.Location;
import com.weather.restapi.util.RestApiClient;
import com.weather.restapi.util.RestApiResponse;
import cucumber.api.DataTable;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.json.JSONObject;
import org.junit.Assume;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;

import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class LocationStepDefs {

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

	@Given("^the application has started successfully$")
	public void theApplicationHasStartedSuccessfully() {
		if (!isWebAppStarted) {
			log.info("Starting NBI WebApp");
			SpringApplication.run(WeatherApplication.class);
			log.info("Started NBI WebApp");
			isWebAppStarted = true;
		}
	}

	@When("^the following data is added to the Location table$")
	public void theFollowingDataIsAddedToTheLocationTable(DataTable dataTable) throws Exception {
		String nbiUrl = "http://localhost:" + serverPort + "/location/add";
		List<Map<String, String>> records = dataTable.asMaps(String.class, String.class);
		for (Map<String, String> record : records) {
			StringBuilder inputParameters = new StringBuilder();
			String zipcode = record.get("zipcode");
//			String date = record.get("date");
//			String temperature = record.get("temperature");

			inputParameters.append(nbiUrl).append("?").append("zipcode=").append(zipcode);
			RestApiResponse response = RestApiClient.invoke(inputParameters.toString(), "PUT");
			statusCode = response.getStatusCode();
			assertEquals(new Integer(200), statusCode);
		}
	}

	@Then("^the total number of records in Location table is (\\d+)$")
	public void theTotalNumberOfRecordsInLocationTableIs(int count) throws Exception {
		String nbiUrl = "http://localhost:" + serverPort + "/location/fetchAll";
		RestApiResponse response = RestApiClient.invoke(nbiUrl, "GET");
		statusCode = response.getStatusCode();
		assertEquals(new Integer(200), statusCode);
		jsonOutputString = response.getResponse();
		JSONObject jsonObject = new JSONObject(jsonOutputString);
		String content = jsonObject.getString("content");

		ObjectMapper mapper = new ObjectMapper();
		List<Location> locations = mapper.readValue(content, mapper.getTypeFactory().constructCollectionType(List.class, Location.class));
		assertEquals(count, locations.size());

	}

	@When("^I request location by zipcode \"([^\"]*)\"$")
	public void iRequestLocationByZipcode(String zipcode) throws Throwable {
		String nbiUrl = "http://localhost:" + serverPort + "/location/fetchByZipcode?zipcode=" + zipcode;
		RestApiResponse response = RestApiClient.invoke(nbiUrl, "GET");
		statusCode = response.getStatusCode();
//		assertEquals(new Integer(200), statusCode);
		jsonOutputString = response.getResponse();
	}

	@Then("^I expect the response as \"(.*)\"$")
	public void iExpectTheResponseAs(String expectedResponse) throws Throwable {
		assertEquals(expectedResponse, jsonOutputString);
	}

	@When("^I provide page number \"([^\"]*)\", size \"([^\"]*)\" and sort \"([^\"]*)\"$")
	public void iProvidePageNumberSizeAndSort(String page, String size, String sort) throws Throwable {
		String nbiUrl = "http://localhost:" + serverPort + "/location/fetchAll?" + "page=" + page + "&size=" + size + "&sort=" + sort;
		RestApiResponse response = RestApiClient.invoke(nbiUrl, "GET");
		statusCode = response.getStatusCode();
		assertEquals(new Integer(200), statusCode);
		jsonOutputString = response.getResponse();
	}

	@Then("^I expect in the response the content as \"(.*)\"$")
	public void iExpectInTheResponseTheContentAs(String expectedContent) throws Throwable {
		ObjectMapper objectMapper = new ObjectMapper();
		List<Location> expectedLocations = objectMapper.readValue(expectedContent, objectMapper.getTypeFactory().constructCollectionType(List.class, Location.class));

		JSONObject jsonObject = new JSONObject(jsonOutputString);
		String actualContent = jsonObject.getString("content");
		List<Location> actualLocations = objectMapper.readValue(actualContent, objectMapper.getTypeFactory().constructCollectionType(List.class, Location.class));

		assertEquals(expectedLocations.size(), actualLocations.size());
		for (int index = 0; index < expectedLocations.size(); index++) {
			Location expected = expectedLocations.get(index);
			Location actual = actualLocations.get(index);
			assertEquals(expected, actual);
		}
	}

	@Then("^I expect in the response the status code as \"(.*)\"$")
	public void iExpectInTheResponseTheStatusCodeAs(Integer expectedStatusCode) throws Throwable {
		assertEquals(expectedStatusCode, statusCode);
	}
}

