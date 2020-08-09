package com.weather.restapi.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.weather.restapi.entities.Location;
import com.weather.restapi.repositories.LocationRepository;
import com.weather.restapi.service.WeatherUpdaterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.client.RestTemplate;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;

@Configuration
@PropertySource("classpath:application.properties")
public class WeatherAppConfiguration {

	private static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	@Value("${openweathermap.integration.enaled}")
	private boolean integrationEnabled;

	@Autowired
	WeatherUpdaterService weatherUpdaterService;

	@Autowired
	LocationRepository locationRepository;

	@Bean(name = "restTemplate")
	public RestTemplate getRestTemplate() {
		RestTemplate restTemplate = new RestTemplate();
		ClientHttpRequestFactory requestFactory = getClientHttpRequestFactory();
		restTemplate.setRequestFactory(requestFactory);

		return restTemplate;
	}

	/*The weather details for all the valid locations are updated for every hour*/
	@Scheduled(initialDelay = 60_000, fixedRate = 36_00_000)
	public void triggerWeatherDetailsUpdate() {
		if (integrationEnabled) {
			int pageNumber = 0;
			Page<Location> locations = locationRepository.findAll(PageRequest.of(pageNumber, 20));
			while (locations.getNumberOfElements() > 0) {
				//TODO: Create threadpool executor service and submit tasks to execute in parallel
				weatherUpdaterService.updateWeatherDetails(locations.get());
				locations = locationRepository.findAll(PageRequest.of(pageNumber + 1, 20));
			}
		} else {
			log.info("The integration with 'api.openweathermap.org' is disabled, please enable 'openweathermap.integration.enaled' property, with proper APIKey.");
		}
	}

	/*Retrieving a Resource with json content*/
	private List<HttpMessageConverter<?>> getMessageConverters(ObjectMapper mapper) {
		List<HttpMessageConverter<?>> converters = new ArrayList<>();
		MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
		converter.setObjectMapper(mapper);
		converters.add(converter);
		return converters;
	}

	private ClientHttpRequestFactory getClientHttpRequestFactory() {
		//TODO: make this timeout property configurable
		int timeout = 5000;
		HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
		requestFactory.setConnectionRequestTimeout(timeout);
		requestFactory.setReadTimeout(timeout);
		requestFactory.setConnectTimeout(timeout);
		return requestFactory;
	}

}
