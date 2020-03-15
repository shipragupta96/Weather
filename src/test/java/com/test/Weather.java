package com.test;

import java.text.ParseException;
import java.util.logging.Logger;

import org.testng.Assert;
import org.testng.annotations.*;

import io.restassured.RestAssured;
import io.restassured.response.Response;

import com.test.util.CommonUtil;

public class Weather {

	Response response;
	CommonUtil testPage;
	Logger logger = Logger.getLogger(Weather.class.getName());

	/**
	 * Get the hourly weather forecast of London Location
	 * 
	 */
	@BeforeSuite
	public void getWeatherMap() throws Exception {
		RestAssured.baseURI = "https://samples.openweathermap.org";
		response = RestAssured.given().queryParams("q", "London,us", "appid", "b6907d289e10d714a6e88b30761fae22").when()
				.get("/data/2.5/forecast/hourly");
		testPage = new CommonUtil(response, logger);
	}

	/**
	 * Is the response contains 4 days of data
	 * 
	 * @throws ParseException
	 * 
	 */
	@Test
	public void verifyDays() throws ParseException {
		logger.info("No. of Days verification Begins....");
		boolean daysVerify = testPage.daysTest(4);
		Assert.assertEquals(daysVerify, true);
		logger.info("No. of Days verification is successful....");
	}

	/**
	 * Is all the forecast in the hourly interval ( no hour should be missed )
	 * 
	 * @throws ParseException
	 * 
	 */
	@Test(dependsOnMethods = "verifyDays")
	public void verifyHourlyForecast() throws ParseException {
		logger.info("Hourly Forecast verification Begins....");
		boolean hourlyVerify = testPage.hourlyTest();
		Assert.assertEquals(hourlyVerify, true);
		logger.info("Hourly Forecast verification is successful....");
	}

	/**
	 * For all 4 days, the temp should not be less than temp_min and not more
	 * than temp_max
	 * 
	 */
	@Test(dependsOnMethods = "verifyHourlyForecast")
	public void verifyTemp() {
		logger.info("Temperature verification Begins....");
		boolean verifyTemp = testPage.tempTest();
		Assert.assertEquals(verifyTemp, true);
		logger.info("Temperature verification is successful....");
	}

	/**
	 * If the weather id is 500, the description should be light rain
	 * 
	 */
	@Test(dependsOnMethods = "verifyTemp")
	public void verifyLightRain() {
		logger.info("Verify Light Rain for Id 500 Begins....");
		boolean lightRain = testPage.IdAndDescriptionTest(500, "light rain");
		Assert.assertEquals(lightRain, true);
		logger.info("Verification is successful for Light Rain with Id 500....");
	}

	/**
	 * If the weather id is 800, the description should be clear sky
	 * 
	 */
	@Test(dependsOnMethods = "verifyLightRain")
	public void verifyClearSky() {
		logger.info("Verify Clear Sky for Id 800 Begins....");
		boolean clearSky = testPage.IdAndDescriptionTest(800, "clear sky");
		Assert.assertEquals(clearSky, true);
		logger.info("Verification is successful for Clear Sky with Id 800....");
	}
}
