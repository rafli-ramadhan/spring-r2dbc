package com.example.spring_app.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Logger {

	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSXXX");

	public enum LogLevel {
		DEBUG, INFO, WARN;
	}

	public enum ExceptionCategory {
		TECHNICAL, BUSINESS;
	}

	private static Logger instance = null;

	public static Logger getInstance() {
		if (instance == null) {
			instance = new Logger();
		}

		return instance;
	}

	public void logTrace(String methodName,
			long processTime, String requestPayload, String responsePayload, HttpStatus httpStatusCode) {

		JSONObject jsonObject = new JSONObject();
		jsonObject.put("logTimestamp", sdf.format(new Date()));
		jsonObject.put("methodName", methodName);
		jsonObject.put("processTime", String.valueOf(processTime));
		jsonObject.put("requestPayload", requestPayload);
		jsonObject.put("responsePayload", responsePayload);
		jsonObject.put("httpStatusCode", String.valueOf(httpStatusCode));

		log.info("{}", jsonObject);
	}

	public void logException(String methodName,
			long processTime, String requestPayload, String responsePayload, HttpStatus httpStatusCode, Throwable e) {

		JSONObject jsonObject = new JSONObject();
		jsonObject.put("exceptionDetail", e != null ? ExceptionUtils.getStackTrace(e) : null);
		jsonObject.put("exceptionMessage", e.getMessage());
		jsonObject.put("timeStamp", sdf.format(new Date()));
		jsonObject.put("methodName", methodName);
		jsonObject.put("requestPayload", requestPayload);
		jsonObject.put("responsePayload", responsePayload);
		jsonObject.put("processTime", String.valueOf(processTime));
		jsonObject.put("httpStatusCode", httpStatusCode);

		log.error("{}", jsonObject);
	}

}
