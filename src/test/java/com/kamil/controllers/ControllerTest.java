package com.kamil.controllers;

import com.kamil.models.WeatherManager;
import com.kamil.models.WeatherUtility;
import org.json.JSONException;
import org.junit.jupiter.api.Test;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by DELL on 2020-07-22.
 */
public class ControllerTest {

    @Test
    public void gettingCityName() {
        //given
        //when
        Controller newController = new Controller();
        //then
        assertEquals("Cracow".toUpperCase(), newController.citySet, "Getting city is not ok");
    }

    @Test
    public void controlGettingTodayDay() {
        //given
        WeatherUtility weatherUtility = new WeatherUtility("Cracow");
        //when
        String todayDay = weatherUtility.getTodayDay();
        //then
        assertThat(weatherUtility.getTodayDay(), equalTo(todayDay));
    }

    @Test
    public void controlFields() {
        //given
        WeatherUtility weatherUtility = new WeatherUtility("Cracow");
        //when
        weatherUtility.getDayAfter();
        weatherUtility.getDayDayDayAfter();
        //then
        assertNull(weatherUtility.getDayAfter(), "Not null");
        assertNull(weatherUtility.getDayDayDayAfter(), "Not null");
    }

    @Test
    public void controlFieldsWeatherManager() throws JSONException {
        //given
        WeatherManager weatherManager = new WeatherManager("Cracow");
        //when
        weatherManager.fetchDataWeather();
        String city = weatherManager.getCity();
        Integer temperature = weatherManager.getTemperature();
        String description = weatherManager.getDescription();
        //then
        assertNotNull(city, "Null");
        assertNotNull(temperature, "Null");
        assertNotNull(description, "Null");
    }
}
