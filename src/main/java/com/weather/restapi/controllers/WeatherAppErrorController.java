package com.weather.restapi.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletRequest;
import java.lang.invoke.MethodHandles;
import java.util.Map;

@RestController
public class WeatherAppErrorController implements ErrorController {

	private static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
	private final static String ERROR_PATH = "/error";

	private final ErrorAttributes errorAttributes = new DefaultErrorAttributes();

	@RequestMapping(value = ERROR_PATH)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> error(HttpServletRequest request, WebRequest webRequest) {
		Map<String, Object> body = getErrorAttributes(webRequest, getTraceParameter(request));
		HttpStatus status = getStatus(request);
		log.debug("treating /error request for no html format...body=" + body + " status=" + status);
		return new ResponseEntity<>(body, status);
	}

	private boolean getTraceParameter(HttpServletRequest request) {
		String parameter = request.getParameter("trace");
		return parameter != null && !"false".equals(parameter.toLowerCase());
	}

	private Map<String, Object> getErrorAttributes(WebRequest request, boolean includeStackTrace) {

		ErrorAttributeOptions errorAttributeOptions = ErrorAttributeOptions.of(ErrorAttributeOptions.Include.BINDING_ERRORS);
		if (includeStackTrace) {
			errorAttributeOptions = errorAttributeOptions.including(ErrorAttributeOptions.Include.STACK_TRACE);
		}
		return this.errorAttributes.getErrorAttributes(request, errorAttributeOptions);
	}

	private HttpStatus getStatus(HttpServletRequest request) {
		Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
		if (statusCode != null) {
			return HttpStatus.valueOf(statusCode);
		}
		return HttpStatus.INTERNAL_SERVER_ERROR;
	}

	@Override
	public String getErrorPath() {
		return ERROR_PATH;
	}
}