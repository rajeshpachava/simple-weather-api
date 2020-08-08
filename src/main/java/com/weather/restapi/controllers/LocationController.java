package com.weather.restapi.controllers;

import com.weather.restapi.entities.Location;
import com.weather.restapi.repositories.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/location")
public class LocationController {

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
		locationRepository.saveAndFlush(new Location(zipcode));
	}
}