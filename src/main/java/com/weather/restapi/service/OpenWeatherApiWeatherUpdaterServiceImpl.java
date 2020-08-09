package com.weather.restapi.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.weather.restapi.entities.Location;
import com.weather.restapi.entities.Weather;
import com.weather.restapi.repositories.LocationRepository;
import com.weather.restapi.repositories.WeatherRepository;
import com.weather.restapi.util.DateTimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.lang.invoke.MethodHandles;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

@Service
@PropertySource("classpath:application.properties")
public class OpenWeatherApiWeatherUpdaterServiceImpl implements WeatherUpdaterService {

	private static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	@Value("${openweathermap.api.url}")
	private String url;

	@Value("${openweathermap.apikey}")
	private String apiKey;

	@Autowired
	RestTemplate restTemplate;

	@Autowired
	LocationRepository locationRepository;

	@Autowired
	WeatherRepository weatherRepository;

	private UriComponentsBuilder uriComponentsBuilder;

	public void updateWeatherDetails(Stream<Location> locationStream) {
		locationStream.map(l -> invokeWeatherApi(restTemplate, url, apiKey, l))
				.filter(Objects::nonNull)
				.forEach(w -> persistWeatherDetails(w));
	}

	private void persistWeatherDetails(Weather weather) {
		Optional<Weather> optionalWeather = weatherRepository.fetchByZipcodeAndDate(weather.getLocation().getZipcode(), weather.getDate());
		Weather weatherToPersist;
		if (optionalWeather.isPresent()) {
			weatherToPersist = optionalWeather.get();
			weatherToPersist.setTemperature(weather.getTemperature());
		} else {
			weatherToPersist = weather;
		}
		weatherRepository.saveAndFlush(weatherToPersist);
	}

	Weather invokeWeatherApi(RestTemplate restTemplate, String url, String apiKey, Location location) {

		try {
			UriComponents uriComponents = getUriComponentBuilder(url).buildAndExpand(location.getZipcode(), apiKey);
			String uri = uriComponents.toUriString();
			ResponseEntity<String> resp = restTemplate.exchange(uri, HttpMethod.GET, null, String.class);

			HttpStatus statusCode = resp.getStatusCode();
			if (HttpStatus.NOT_FOUND.equals(statusCode)) {
				log.warn("Invalid location {} to fetch the current temperature", location);
			}
			if (HttpStatus.OK.equals(statusCode)) {
				ObjectMapper mapper = new ObjectMapper();
				Map<String, Object> keyValuePairs = mapper.readValue(resp.getBody(), new TypeReference<Map<String, Object>>() {
				});
				Object mainContent = keyValuePairs.getOrDefault("main", null);
				if (mainContent instanceof Map) {
					Object temp = ((Map) mainContent).getOrDefault("temp", null);
					if (temp != null && !"".equals(temp)) {
						float kelvin = Float.parseFloat(temp.toString());
						float celsius = kelvin - 273.15F;
						return new Weather(location, DateTimeUtil.getDateWithStartOfTheDay(new Date()), celsius);
					}
				}
			}
		} catch (JsonProcessingException e) {
			log.error("Error while fetching the temperature details for location {}, skipping and continuing fetching for other locations.", location);
		}
		return null;
	}

	private UriComponentsBuilder getUriComponentBuilder(String url) {
		if (uriComponentsBuilder == null) {
			uriComponentsBuilder = UriComponentsBuilder
					.newInstance()
					.scheme("http")
					.host(url)
					.path("")
					.query("zip={zip}&appid={appid}");
		}
		return uriComponentsBuilder;
	}
}
