package com.weather.restapi.config;

import com.weather.restapi.entities.Location;
import com.weather.restapi.repositories.LocationRepository;
import com.weather.restapi.service.WeatherUpdaterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.client.RestTemplate;

import java.lang.invoke.MethodHandles;

@Configuration
@PropertySource("classpath:application.properties")
public class WeatherAppSchedulerConfiguration {

	private static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	private static final int PAGE_SIZE = 20;
	//TODO: make this timeout property configurable
	private static final int HTTP_TIMEOUT = 5000;

	@Value("${openweathermap.integration.enabled}")
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
			int elementsInPage;
			do {
				Page<Location> page = locationRepository.findAll(PageRequest.of(pageNumber, PAGE_SIZE));
				elementsInPage = page.getNumberOfElements();
				if (elementsInPage > 0) {
					weatherUpdaterService.updateWeatherDetails(page.get());
					pageNumber++;
				}
			} while (elementsInPage == PAGE_SIZE);
		} else {
			log.info("The integration with 'api.openweathermap.org' is disabled, please enable 'openweathermap.integration.enaled' property, with proper APIKey.");
		}
	}

	private ClientHttpRequestFactory getClientHttpRequestFactory() {
		HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
		requestFactory.setConnectionRequestTimeout(HTTP_TIMEOUT);
		requestFactory.setReadTimeout(HTTP_TIMEOUT);
		requestFactory.setConnectTimeout(HTTP_TIMEOUT);
		return requestFactory;
	}

}
