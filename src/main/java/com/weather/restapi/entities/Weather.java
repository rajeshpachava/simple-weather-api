package com.weather.restapi.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "Weather")
@JsonPropertyOrder({"id", "locationId", "date", "temperature"})
public class Weather implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@JoinColumn(name = "id", nullable = false)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "locationId", referencedColumnName = "id", nullable = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JsonIgnore
	private Location location;

	@JoinColumn(name = "date", nullable = false)
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date date;

	@JoinColumn(name = "temperature", nullable = false)
	private Double temperature;


	public Weather() {
	}

	public Weather(Location location, Date date, Double temperature) {
		this.location = location;
		this.date = date;
		this.temperature = temperature;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Double getTemperature() {
		return temperature;
	}

	public void setTemperature(Double temperature) {
		this.temperature = temperature;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Weather weather = (Weather) o;
		return id.equals(weather.id) &&
				date.equals(weather.date) &&
				temperature.equals(weather.temperature);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, location, date, temperature);
	}
}
