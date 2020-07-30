package com.kamil.models;

/**
 * Created by DELL on 2020-07-18.
 */

import java.util.Date;

public class Forecast {
    public String temperature;
    public String symbol;
    public String description;
    public String pressure;
    public Date time;

    Forecast(String temp, String sym, String descript, Date clock, String press) {
        temperature = temp;
        symbol = sym;
        description = descript;
        time = clock;
        pressure = press;
    }
}
