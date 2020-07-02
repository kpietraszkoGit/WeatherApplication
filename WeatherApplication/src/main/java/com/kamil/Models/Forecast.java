package com.kamil.Models;

/**
 * Created by DELL on 2020-06-20.
 */
import java.util.Date;

public class Forecast
{
    String temperature1, symbol, description, pressure;
    Date time;

    Forecast(){}
    Forecast(String a, String b, String c, Date d, String p)
    {
        temperature1 = a;
        symbol = b;
        description = c;
        time = d;
        pressure = p;
    }

}
