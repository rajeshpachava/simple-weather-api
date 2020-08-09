package com.weather.restapi.controllers;

import com.weather.restapi.entities.Location;
import com.weather.restapi.entities.Weather;
import com.weather.restapi.repositories.LocationRepository;
import com.weather.restapi.repositories.WeatherRepository;
import com.weather.restapi.util.DateTimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Optional;


@RestController
@RequestMapping("/weather")
public class WeatherController {

	@Autowired
	LocationRepository locationRepository;

	@Autowired
	WeatherRepository weatherRepository;

	@PutMapping(value = "/add")
	public void add(@RequestParam Long zipcode,
					@RequestParam(value = "date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date date,
					@RequestParam Float temperature) {
		Optional<Location> locationInfoFromDb = locationRepository.findByLocationZipCode(zipcode);
		Location location = locationInfoFromDb.orElseGet(() -> locationRepository.save(new Location(zipcode)));

		Optional<Weather> weather = weatherRepository.fetchByZipcodeAndDate(location.getZipcode(), DateTimeUtil.getDateWithStartOfTheDay(date));
		Weather updated;
		if (weather.isPresent()) {
			updated = weather.get();
			updated.setTemperature(temperature);
		} else {
			updated = new Weather(location, date, temperature);
		}
		weatherRepository.saveAndFlush(updated);
	}

	@GetMapping(value = "/fetchAll")
	public Page<Weather> fetchAll(Pageable pageable) {
		return weatherRepository.findAll(pageable);
	}

	@GetMapping(value = "/fetchAllByZipcode")
	public Page<Weather> fetchAllByZipcode(@RequestParam Long zipcode, Pageable pageable) {
		return weatherRepository.findAllByZipcode(zipcode, pageable);
	}

	@GetMapping(value = "/fetchByZipcodeAndDate")
	public Optional<Weather> fetchByZipcodeAndDate(@RequestParam Long zipcode,
												   @RequestParam(value = "date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date date) {
		return weatherRepository.fetchByZipcodeAndDate(zipcode, date);
	}

	@GetMapping(value = "/fetchAllByZipcodeBetweenDates")
	public ResponseEntity<?> fetchAllByZipcodeBetweenDates(@RequestParam Long zipcode,
														   @RequestParam(value = "startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,
														   @RequestParam(value = "endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate,
														   Pageable pageable) {
		if (startDate.after(endDate)) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid input request, startDate cannot be greater than endDate");
		}
		return new ResponseEntity<>(weatherRepository.fetchAllByZipcodeBetweenDates(zipcode, startDate, endDate, pageable), HttpStatus.OK);
	}

}