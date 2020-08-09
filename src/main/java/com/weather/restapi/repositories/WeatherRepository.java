package com.weather.restapi.repositories;

import com.weather.restapi.entities.Weather;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.Optional;

public interface WeatherRepository extends JpaRepository<Weather, Long> {

	@Query("SELECT  weather FROM  Weather weather INNER JOIN Location location ON weather.location.id = location.id " +
			"WHERE location.zipcode = :zipcode")
	Page<Weather> findAllByZipcode(@Param(value = "zipcode") Long zipCode,
								   Pageable pageable);

	@Query("SELECT  weather FROM  Weather weather INNER JOIN Location location ON weather.location.id = location.id " +
			"WHERE location.zipcode = :zipcode and weather.date = :date")
	Optional<Weather> fetchByZipcodeAndDate(@Param(value = "zipcode") Long zipcode,
											@Param("date") Date date);

	@Query("SELECT  weather FROM  Weather weather INNER JOIN Location location ON weather.location.id = location.id " +
			"WHERE location.zipcode = :zipcode and weather.date BETWEEN :startDate AND :endDate")
	Page<Weather> fetchAllByZipcodeBetweenDates(@Param(value = "zipcode") Long zipCode,
									  @Param("startDate") Date startDate,
									  @Param("endDate") Date endDate,
									  Pageable pageable);
}
