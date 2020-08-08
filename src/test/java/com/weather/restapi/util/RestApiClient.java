package com.weather.restapi.util;

import org.apache.tomcat.util.codec.binary.Base64;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

public class RestApiClient {

	public static RestApiResponse invoke(String urlString, String requestMethod) throws Exception {

		System.out.println("\nRequested NBI URL : " + urlString);

		String jsonOutputString = null;
		URL url = new URL(urlString);
		HttpURLConnection urlConnection = null;
		RestApiResponse restApiResponse = null;

		try {
			urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.setDoOutput(true);
			urlConnection.setRequestMethod(requestMethod);

			String auth = "demo:demo";
			byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(StandardCharsets.UTF_8));
			String authHeaderValue = "Basic " + new String(encodedAuth);
			urlConnection.setRequestProperty("Authorization", authHeaderValue);

			/*https://www.w3.org/TR/html401/interact/forms.html#h-17.13.4.1*/
			urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");

			try (BufferedReader br = new BufferedReader(new InputStreamReader((urlConnection.getInputStream())))) {
				jsonOutputString = br.lines().collect(Collectors.joining("\n"));
				System.out.println("\nOutput from server : \n" + jsonOutputString);
			}
		} catch (Exception e) {
			System.out.println("Exception occurred while invoking NBI with URL '" + urlString + "' : " + e.getMessage());
			if (urlConnection != null) {
				InputStream errorStream = urlConnection.getErrorStream();
				if (errorStream != null) {
					try (BufferedReader br = new BufferedReader(new InputStreamReader(errorStream))) {
						jsonOutputString = br.lines().collect(Collectors.joining("\n"));
						System.out.println("\nOutput from server : \n" + jsonOutputString);
					}
				}
			}
		} finally {
			if (urlConnection != null) {
				restApiResponse = new RestApiResponse(urlConnection.getResponseCode(), jsonOutputString);
				urlConnection.disconnect();
			}
		}
		return restApiResponse;
	}
}
