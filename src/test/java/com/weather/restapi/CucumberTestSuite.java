package com.weather.restapi;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(features = {
		"classpath:location.feature",
		"classpath:weather.feature"},
		glue = {"com.weather.restapi.stepdefs"},
		plugin = {"pretty", "json:target/cucumber.json"},
		strict = true)
public class CucumberTestSuite {
}
