package com.weather.restapi.repositories;

import com.weather.restapi.entities.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {

	@Query("SELECT location FROM Location location WHERE location.zipcode = :zipcode")
	Optional<Location> findByLocationZipCode(Long zipcode);

}

