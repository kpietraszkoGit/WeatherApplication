package com.kamil.models;

/**
 * Created by DELL on 2020-07-18.
 */

import java.io.*;
import java.net.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.xml.stream.*;


public class WeatherUtility {
    private InputStream local;
    private Date date;
    private String temperature;
    private String pressure;
    private String latitude;
    private String longitude;
    private String city;
    private String tomorrow;
    private String dayAfter;
    private String dayDayAfter;
    private String dayDayDayAfter;
    private String tempToday;
    private String pressureToday;
    private String descriptionToday;
    private String todayDay;
    private String icon0;
    private String iconA;
    private String iconB;
    private String iconC;
    private String iconD;
    public String temp;
    public String sym;
    public String descript;
    public String press;
    public Date clock;
    public List<String> forecast = new ArrayList();
    public List<Forecast> fore = new ArrayList();

    public WeatherUtility(String city) {
        this.city = city;
    }

    public void fetchLocalApi() {
        try {
            local = new URL("http://ip-api.com/xml").openConnection().getInputStream();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void fetchDataFromApi() {
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
                        city = reader.getElementText();
                    }

                    if (el.equals("lat")) {
                        latitude = reader.getElementText();
                    }

                    if (el.equals("lon")) {
                        longitude = reader.getElementText();
                    }

                    if (el.equals("formatted_address")) {
                        String[] arr = reader.getElementText().split(",");
                        String state = arr[1].substring(1, 3);
                    }
                }
                reader.next();
            }
            reader.close();
        } catch (XMLStreamException e) {
            e.getMessage();
        } catch (Exception e) {
            e.printStackTrace();
        }
        fetchLocalApi();
    }

    public String coordinatesWeather() {
        fetchDataFromApi();
        return "http://api.openweathermap.org/data/2.5/forecast?lat=" + latitude + "&lon=" + longitude +
                "&units=metric&mode=xml&appid=d3c9f8ba9b96d5b61d961d54d9b5d0ea";
    }

    public String cityWeather() {
        String weatherApi = "http://api.openweathermap.org/data/2.5/forecast?q=" + city +
                "&units=metric&mode=xml&appid=d3c9f8ba9b96d5b61d961d54d9b5d0ea";
        fetchDataFromApi();
        return weatherApi;
    }

    public void fetchDataWeather(String api) {
        fore.clear();
        forecast.clear();
        try {
            InputStream weatherApi = new URL(api).openConnection().getInputStream();

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
                    if (el.equals("name")) {
                        city = reader.getElementText();
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

        if (forecast.isEmpty()) {
            forecast.add("0");
            fore.add(new Forecast(temp, sym, descript, clock, press));
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            List<Integer> array = new ArrayList();

            for (int i = 0; i < fore.size(); i++) {
                String now = sdf.format(fore.get(i).time);
                if (now.equals("09:00:00")) {
                    array.add(i);
                }
            }

            int fetchForecastDataFromArray0 = array.get(0);
            int fetchForecastDataFromArray1 = array.get(1);
            int fetchForecastDataFromArray2 = array.get(2);
            int fetchForecastDataFromArray3 = array.get(3);

            List<Double> day1 = new ArrayList();
            List<Double> day2 = new ArrayList();
            List<Double> day3 = new ArrayList();
            List<Double> day4 = new ArrayList();

            for (int i = fetchForecastDataFromArray0; i < fetchForecastDataFromArray1; i++) {
                Double temp = Double.parseDouble(fore.get(i).temperature);
                day1.add(temp);
            }

            for (int i = fetchForecastDataFromArray1; i < fetchForecastDataFromArray2; i++) {
                Double temp = Double.parseDouble(fore.get(i).temperature);
                day2.add(temp);
            }

            for (int i = fetchForecastDataFromArray2; i < fetchForecastDataFromArray3; i++) {
                Double temp = Double.parseDouble(fore.get(i).temperature);
                day3.add(temp);
            }
            for (int i = fetchForecastDataFromArray3; i < (fetchForecastDataFromArray3 + 8); i++) {
                Double temp = Double.parseDouble(fore.get(i).temperature);
                day4.add(temp);
            }

            Collections.sort(day1);
            Collections.sort(day2);
            Collections.sort(day3);
            Collections.sort(day4);

            int firstDay = day1.get(day1.size() - 1).intValue();
            int secondDay = day2.get(day1.size() - 1).intValue();
            int thirdDay = day3.get(day1.size() - 1).intValue();
            int fourthDay = day4.get(day1.size() - 1).intValue();

            String tempTom = Integer.toString(firstDay);
            String tempDay = Integer.toString(secondDay);
            String tempDayDay = Integer.toString(thirdDay);
            String tempDayDayDay = Integer.toString(fourthDay);

            this.tempToday = forecast.get(0).substring(0, 2);
            this.pressureToday = fore.get(fetchForecastDataFromArray0).pressure;
            this.todayDay = fore.get(0).time.toString().substring(0, 3);
            this.descriptionToday = fore.get(0).description;

            this.tomorrow =
                    " " + fore.get(fetchForecastDataFromArray0).time.toString().substring(0, 4) + "     " + tempTom + "ºC      " + fore.get(fetchForecastDataFromArray0).pressure + " hPa" + "     " + fore.get(fetchForecastDataFromArray0).description;
            this.dayAfter =
                    " " + fore.get(fetchForecastDataFromArray1).time.toString().substring(0, 4) + "     " + tempDay + "ºC      " + fore.get(fetchForecastDataFromArray1).pressure + " hPa" + "     " + fore.get(fetchForecastDataFromArray1).description;
            this.dayDayAfter =
                    " " + fore.get(fetchForecastDataFromArray2).time.toString().substring(0, 4) + "     " + tempDayDay + "ºC      " + fore.get(fetchForecastDataFromArray2).pressure + " hPa" + "     " + fore.get(fetchForecastDataFromArray2).description;
            this.dayDayDayAfter =
                    " " + fore.get(fetchForecastDataFromArray3).time.toString().substring(0, 4) + "     " + tempDayDayDay + "ºC      " + fore.get(fetchForecastDataFromArray3).pressure + " hPa" + "     " + fore.get(fetchForecastDataFromArray3).description;

            this.icon0 = fore.get(0).symbol;
            this.iconA = fore.get(fetchForecastDataFromArray0).symbol;
            this.iconB = fore.get(fetchForecastDataFromArray1).symbol;
            this.iconC = fore.get(fetchForecastDataFromArray2).symbol;
            this.iconD = fore.get(fetchForecastDataFromArray3).symbol;

        }
    }

    public String getCity() {
        return city;
    }

    public String getIcon0() {
        return icon0;
    }

    public String getIconA() { return iconA; }

    public String getIconB() {
        return iconB;
    }

    public String getIconC() {
        return iconC;
    }

    public String getIconD() {
        return iconD;
    }

    public String getTodayDay() {
        return todayDay;
    }

    public String getDescriptionToday() {
        return descriptionToday;
    }

    public String getTempToday() {
        return tempToday;
    }

    public String getPressureToday() {
        return pressureToday;
    }

    public String getTomorrow() {
        return tomorrow;
    }

    public String getDayAfter() {
        return dayAfter;
    }

    public String getDayDayAfter() {
        return dayDayAfter;
    }

    public String getDayDayDayAfter() {
        return dayDayDayAfter;
    }
}
