package com.kamil.models;



import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Created by DELL on 2020-07-24.
 */
public class ForecastTest {

    @Test
    public void checkingWeatherData() {
        //given
        Date date = new Date();
        //when
        Forecast forecast = new Forecast("26", "01d.png", "clear sky", date, "1012");
        //then
        assertNotNull(forecast);
        assertThat(forecast, is(notNullValue()));
    }
}
