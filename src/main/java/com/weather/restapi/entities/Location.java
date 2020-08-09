package com.weather.restapi.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "Location")
@JsonPropertyOrder({ "id", "zipcode" })
public class Location implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@JsonProperty(value = "id")
	private Long id;

	@JsonProperty(value = "zipcode")
	@Column(name = "zipcode", unique = true)
	private Long zipcode;

	public Location() {
	}

	public Location(Long zipcode) {
		this.zipcode = zipcode;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getZipcode() {
		return zipcode;
	}

	public void setZipcode(Long zipCode) {
		this.zipcode = zipCode;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Location that = (Location) o;
		return id.equals(that.id) &&
				zipcode.equals(that.zipcode);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, zipcode);
	}

	@Override
	public String toString() {
		return "Location{" +
				"id=" + id +
				", zipcode=" + zipcode +
				'}';
	}
}
