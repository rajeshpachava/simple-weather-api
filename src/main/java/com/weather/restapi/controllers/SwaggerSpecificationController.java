package com.weather.restapi.controllers;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;

@RestController
public class SwaggerSpecificationController {

	public static final String SWAGGER_JSON = "swagger.json";

	@RequestMapping(value = "/" + SWAGGER_JSON, method = RequestMethod.GET)
	public void getSwaggerSpec(HttpServletResponse response) throws Exception {
		InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream(SWAGGER_JSON);
		IOUtils.copy(resourceAsStream, response.getOutputStream());
		response.setHeader("Content-Disposition", "attachment; filename=" + SWAGGER_JSON);
		response.flushBuffer();
	}

}
