package com.weather.restapi.util;

public class RestApiResponse {
	private final Integer statusCode;
	private final String response;

	public RestApiResponse(Integer statusCode, String response) {
		this.statusCode = statusCode;
		this.response = response;
	}

	public Integer getStatusCode() {
		return statusCode;
	}

	public String getResponse() {
		return response;
	}

}

