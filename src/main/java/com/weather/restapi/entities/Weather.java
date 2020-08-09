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

	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "locationId", referencedColumnName = "id", nullable = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JsonIgnore
	private Location location;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	@Column(name = "date", nullable = false)
	@Temporal(TemporalType.DATE)
//	@Column(name = "date", nullable = false, columnDefinition = "DATE")
	private Date date;

	@Column(name = "temperature", nullable = false)
	private Float temperature;


	public Weather() {
	}

	public Weather(Location location, Date date, Float temperature) {
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

	public Float getTemperature() {
		return temperature;
	}

	public void setTemperature(Float temperature) {
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

	@Override
	public String toString() {
		return "Weather{" +
				"id=" + id +
				", location=" + location +
				", date=" + date +
				", temperature=" + temperature +
				'}';
	}
}
