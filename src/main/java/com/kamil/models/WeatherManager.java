package com.kamil.models;

/**
 * Created by DELL on 2020-07-18.
 */


import org.json.JSONException;
import org.json.JSONObject;
import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class WeatherManager {
    private final String city;
    private String day;
    private Integer temperature;
    private String icon;
    private String description;
    private String windSpeed;
    private String cloudiness;
    private String pressure;
    private String humidity;
    private static final String KEY_API = "d3c9f8ba9b96d5b61d961d54d9b5d0ea";

     public WeatherManager(String city) {
       this.city = city;
    }

    private String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    public JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
        try (InputStream is = new URL(url).openStream()){
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            return new JSONObject(jsonText);
        }
    }

    public void fetchDataWeather() throws JSONException {
        int date = 0;

        JSONObject json;
        JSONObject json_specific;

        SimpleDateFormat df2 = new SimpleDateFormat("EEEE", Locale.ENGLISH);
        Calendar cal = Calendar.getInstance();

        try {
            json = readJsonFromUrl("http://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid" +
                    "=" + KEY_API + "&lang=eng&units=metric");
        } catch (IOException | JSONException e) {
            return;
        }

        json_specific = json.getJSONObject("main");
        this.temperature = json_specific.getInt("temp");
        this.pressure = json_specific.get("pressure").toString();
        this.humidity = json_specific.get("humidity").toString();
        json_specific = json.getJSONObject("wind");
        this.windSpeed = json_specific.get("speed").toString();
        json_specific = json.getJSONObject("clouds");
        this.cloudiness = json_specific.get("all").toString();

        cal.add(Calendar.DATE, date);
        this.day = df2.format(cal.getTime());

        json_specific = json.getJSONArray("weather").getJSONObject(0);
        this.description = json_specific.get("description").toString();
        this.icon = json_specific.get("icon").toString();
    }

    public String getCity() {
        return city;
    }

    public String getDay() {
        return day;
    }

    public Integer getTemperature() {
        return temperature;
    }

    public String getIcon() {
        return icon;
    }

    public String getDescription() {
        return description;
    }

    public String getWindSpeed() {
        return windSpeed;
    }

    public String getCloudiness() {
        return cloudiness;
    }

    public String getPressure() {
        return pressure;
    }

    public String getHumidity() {
        return humidity;
    }
}
