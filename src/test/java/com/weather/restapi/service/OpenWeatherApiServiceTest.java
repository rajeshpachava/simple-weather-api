package com.weather.restapi.service;

import com.weather.restapi.entities.Location;
import com.weather.restapi.entities.Weather;
import org.junit.Test;
import org.springframework.web.client.RestTemplate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class OpenWeatherApiServiceTest {

	@Test
	public void testInvokeWeatherApi() {
		OpenWeatherApiService service = new OpenWeatherApiService();
		Weather weather = service.invokeWeatherApi(new RestTemplate(),
				"api.openweathermap.org/data/2.5/weather",
				"1c9770dfaf3b327dd03510a4c07b7f2d",
				new Location(99501L));
		System.out.println(weather);
		assertNotNull(weather);
		assertNotNull(weather.getLocation());
		assertEquals(new Long(99501), weather.getLocation().getZipcode());
		assertNotNull(weather.getTemperature());
	}

}