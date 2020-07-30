package com.kamil.models;

import org.junit.jupiter.api.Test;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by DELL on 2020-07-29.
 */
public class WeatherUtilityTest {

    @Test
    public void controllingGettingCity() {
        //given
        //when
        WeatherUtility weatherUtility = new WeatherUtility("CRACOW");
        //then
        assertEquals("Cracow".toUpperCase(), weatherUtility.getCity(), "Getting city is not ok");
    }

    @Test
    public void controllingURLLocal() throws IOException {
        //given
        //when
        InputStream local = new URL("http://ip-api.com/xml").openConnection().getInputStream();
        //then
        assertThat(local, notNullValue());
    }

    @Test
    public void controllingDataFromApi() throws IOException {
        //given
        AtomicReference<String> city = new AtomicReference<>("");
        AtomicReference<String> latitude = new AtomicReference<>("");
        AtomicReference<String> longitude = new AtomicReference<>("");
        InputStream local = new URL("http://ip-api.com/xml").openConnection().getInputStream();
        //when
        XMLInputFactory inputFactory =
                XMLInputFactory.newInstance();
            try {
            XMLStreamReader reader =
                    inputFactory.createXMLStreamReader(local);
            reader.next();

            while (reader.hasNext()) {
                int eventType = reader.getEventType();
                if (eventType == XMLStreamReader.START_ELEMENT) {
                    String el = reader.getLocalName();

                    if (el.equals("city")) {
                        city.set(reader.getElementText());
                    }

                    if (el.equals("lat")) {
                        latitude.set(reader.getElementText());
                    }

                    if (el.equals("lon")) {
                        longitude.set(reader.getElementText());
                    }

                    if (el.equals("formatted_address")) {
                        String[] arr = reader.getElementText().split(",");
                        String state = arr[1].substring(1, 3);
                    }
                }
                reader.next();
            }
            reader.close();
        } catch (
        XMLStreamException e) {
            e.getMessage();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //then
        assertThat(local, notNullValue());
        assertThat(city, notNullValue());
        assertThat(latitude, notNullValue());
        assertThat(longitude, notNullValue());
    }

    @Test
    public void controllingXMLFile() {
        //given
        //when
        String xmlWeather = "http://api.openweathermap.org/data/2.5/forecast?lat=50.06143&lon=19.93658&units=metric&mode=xml&appid=d3c9f8ba9b96d5b61d961d54d9b5d0ea";
        //then
        assertNotNull(xmlWeather, "Null xml file");
    }

    @Test
    public void controllingDataWeather() {
        //given
        List<String> forecast = new ArrayList();
        List<Forecast> fore = new ArrayList();
        String temperature = "";
        Date date = null;
        String pressure = "";
        WeatherUtility weatherUtility = new WeatherUtility("Cracow");
        //when
        try {
            InputStream weatherApi = new URL("http://api.openweathermap.org/data/2.5/forecast?lat=50.06143&lon=19" +
                    ".93658&units=metric&mode=xml&appid=d3c9f8ba9b96d5b61d961d54d9b5d0ea").openConnection().getInputStream();

            XMLInputFactory inputFactory =
                    XMLInputFactory.newInstance();

            XMLStreamReader reader =
                    inputFactory.createXMLStreamReader(weatherApi);

            reader.next();
            while (reader.hasNext()) {
                int eventType = reader.getEventType();
                if (eventType == XMLStreamReader.START_ELEMENT) {
                    String el = reader.getLocalName();

                    if (el.equals("temperature")) {
                        temperature = reader.getAttributeValue(1);
                        forecast.add(temperature);
                    }

                    if (el.equals("time")) {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                        String time = reader.getAttributeValue(1);
                        try {
                            date = sdf.parse(time);
                        } catch (ParseException ex) {
                            ex.printStackTrace();
                        }
                    }

                    if (el.equals("pressure")) {
                        pressure = reader.getAttributeValue(1);
                        forecast.add(pressure);
                    }

                    if (el.equals("symbol")) {
                        String symbol = reader.getAttributeValue(2);
                        String description = reader.getAttributeValue(1);
                        Forecast temp = new Forecast(temperature, symbol, description, date, pressure);
                        fore.add(temp);
                    }
                }
                reader.next();
            }
        } catch (IOException | XMLStreamException ex) {
            ex.printStackTrace();
        }
        //then
        assertNull(weatherUtility.temp, "Not null");
        assertNull(weatherUtility.getTomorrow(), "Not null");
        assertThat(temperature, notNullValue());
        assertThat(date, notNullValue());
        assertThat(pressure, notNullValue());
        assertThat(fore, is(not(empty())));
        assertThat(forecast, is(not(empty())));
    }
}
