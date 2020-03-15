package com.test.util;

import java.text.ParseException;
import java.util.*;
import java.util.logging.Logger;

import org.json.JSONArray;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class CommonUtil {

	Gson gson = new GsonBuilder().create();
	Response response;
	Logger logger;

	public CommonUtil(Response response, Logger logger) {
		this.response = response;
		this.logger = logger;
	}

	/**
	 * To Verify the Weather Description corresponding to it's Id
	 * 
	 */
	public boolean IdAndDescriptionTest(Integer idValue, String descValue) {
		String weathers = "list.weather.findAll { it[0].id == " + idValue + " && it[0].description != '" + descValue
				+ "'}.size()";
		// Used '!=' condition for description to fetch incorrect records
		int count = JsonPath.with(response.body().asString()).get(weathers);
		return count == 0; // count should be zero if all the records are
							// correct
	}

	/**
	 * Temp not less than temp_min && not greater than temp_max
	 * 
	 */
	public boolean tempTest() {
		String temp = "list.findAll {it.main.temp < it.main.temp_min || it.temp > it.main.temp_min}.size()";
		// This condition is used to fetch incorrect records
		int count = JsonPath.with(response.body().asString()).get(temp);
		return count == 0; // count should be zero if all the records are
							// correct
	}

	/**
	 * Hourly Forecast
	 * 
	 */
	public boolean hourlyTest() throws ParseException {
		List<Map<String, Object>> list = JsonPath.with(response.body().asString()).getList("list");
		int record = 1;
		String previous = null, current;
		for (Map<String, Object> listDetail : list) {
			if (record == 1) {
				previous = (String) listDetail.get("dt_txt");
			} else {
				current = (String) listDetail.get("dt_txt");
				int hoursDiff = DateUtil.getHoursBetweenTwoDates(previous, current, "yyyy-MM-dd HH:mm:ss");
				if (hoursDiff > 1) {
					logger.info("Forecast is not provided hourly");
					return false;
				}
				previous = current;
			}
			record++;
		}
		return true;
	}

	/**
	 * Calculates number of days
	 * 
	 * @throws ParseException
	 * 
	 */
	public boolean daysTest(Integer expectedDays) throws ParseException {
		JsonPath body = JsonPath.with(response.body().asString());
		String startDate = body.getString("list[0].dt_txt");
		String endDate =  body.getString("list[-1].dt_txt");
		int actualDays = DateUtil.getDaysBetweenTwoDates(startDate, endDate, "yyyy-MM-dd HH:mm:ss");
		logger.info("Actual Days: " + actualDays);
		return expectedDays == actualDays;
	}
}
