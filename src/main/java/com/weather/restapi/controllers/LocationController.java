package com.weather.restapi.controllers;

import com.weather.restapi.entities.Location;
import com.weather.restapi.repositories.LocationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.lang.invoke.MethodHandles;
import java.util.Optional;

@RestController
@RequestMapping("/location")
public class LocationController {

	private static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	@Autowired
	LocationRepository locationRepository;

	@GetMapping(value = "/fetchAll")
	public Page<Location> fetchAll(Pageable pageable) {
		return locationRepository.findAll(pageable);
	}

	@GetMapping(value = "/fetchByZipcode")
	public Optional<Location> fetchByZipcode(@RequestParam Long zipcode) {
		return locationRepository.findByLocationZipCode(zipcode);
	}

	@PutMapping(value = "/add")
	public void add(@RequestParam(value = "zipcode") Long zipcode) {
		//TODO: need to check if the added location is valid, need to see in openweathermap.api if there are any
		// specific endpoints to validate the location by zipcode.
		Optional<Location> location = locationRepository.findByLocationZipCode(zipcode);
		if (location.isPresent()) {
			log.info("Zipcode already exist in the database, hence skipping insertion.");
		} else {
			locationRepository.saveAndFlush(new Location(zipcode));
		}
	}
}