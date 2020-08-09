package com.weather.restapi.service;

import com.weather.restapi.entities.Location;

import java.util.stream.Stream;

public interface WeatherUpdaterService {
	void updateWeatherDetails(Stream<Location> locationStream);
}
