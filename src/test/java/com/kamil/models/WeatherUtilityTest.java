package com.kamil.models;

import org.junit.jupiter.api.Test;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.concurrent.atomic.AtomicReference;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
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
}
