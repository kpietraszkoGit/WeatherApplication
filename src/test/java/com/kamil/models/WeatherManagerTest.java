package com.kamil.models;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;


/**
 * Created by DELL on 2020-07-23.
 */
public class WeatherManagerTest {

    @Test
    public void gettingCity() {
        //given
        //when
        WeatherManager weatherManager = new WeatherManager("CRACOW");
        //then
        assertEquals("Cracow".toUpperCase(), weatherManager.getCity(), "Getting city is not ok");
    }

    @Test
    public void shouldReadAll() {
        //given
        StringBuilder sb = new StringBuilder();
        //when
        sb.append('H');
        //then
        assertNotNull(sb);
        assertThat(sb, is(notNullValue()));
    }

    public JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
        try (InputStream is = new URL(url).openStream()){
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            return new JSONObject(jsonText);
        }
    }

    private String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }
    
    @Test
    public void emptyJsonObject() throws IOException, JSONException {
        //given
        //when
        String url = "http://ip-api.com/json";
        JSONObject jsonObject = readJsonFromUrl(url);
        //then
        assertNotNull(jsonObject, "Null");
    }

    @ParameterizedTest
    @CsvSource({"24, 1012, broken clouds, 03d, Thu, Cracow", "23, 1015, light rain, 02d, Mon, Roma", "30, 1010, clear" +
            " " +
            "sky," +
            " " +
            "01d, Wed, Rio"})
    void givenDataWeatherShouldNotBeEmptyAndHaveProperNames(String temperature, String pressure, String description,
                                                          String icon, String day, String city) {
        assertThat(temperature, notNullValue());
        assertThat(pressure, notNullValue());
        assertThat(description, notNullValue());
        assertThat(icon, notNullValue());
        assertThat(day, notNullValue());
        assertThat(city, notNullValue());
        assertThat(icon, containsString("d"));
    }
}
