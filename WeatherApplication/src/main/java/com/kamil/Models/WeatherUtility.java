package com.kamil.Models;

/**
 * Created by DELL on 2020-06-20.
 */
import java.io.*;
import java.net.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.xml.stream.*;


public class WeatherUtility
{
    InputStream local, weatherApi;
    Date date;
    String temperature1 = "";
    String pressure = "";
    String country = "";
    String state = "";
    String latitude = "";
    String longitude = "";
    String city = "";
    String zipApi, symbol, description, time;
    String tomorrow, dayAfter, dayDayAfter, dayDayDayAfter, today;
    String tempTom, tempDay, tempDayDay, tempDayDayDay, tempToday, pressureToday, descriptionToday, todayDay;
    ArrayList<String> forecast = new ArrayList();
    ArrayList<Forecast> fore = new ArrayList();
    private String icon0, iconA, iconB, iconC, iconD;

    //class constructor
    WeatherUtility()
    {
        localApi();
    }

    public WeatherUtility(String city) {
        this.city = city;
    }

    public void localApi()
    {
        try
        {
            local = new URL("http://ip-api.com/xml").openConnection().getInputStream();

        }
        catch (IOException ex)
        {}
    }

    //if int == 1, will get location using zip
    //else will get location using ip addr
    public void getLocation(int i)
    {
        if (i == 1)
        {
            try
            {
                local = new URL(zipApi).openConnection().getInputStream();
            }
            catch (MalformedURLException ex)
            {}
            catch (IOException ex)
            {}
        }
        // create the XMLInputFactory object
        XMLInputFactory inputFactory =
                XMLInputFactory.newInstance();
        try
        {
            //create xml stream reader
            XMLStreamReader reader =
                    inputFactory.createXMLStreamReader(local);

            // Read XML here
            reader.next();

            while(reader.hasNext())
            {
                //System.out.println(reader.getLocalName());
                int eventType = reader.getEventType();
                if(eventType == XMLStreamReader.START_ELEMENT)
                {
                    String el = reader.getLocalName();

                    if(el.equals("city"))
                    {
                        city = reader.getElementText();
                    }

                    if(el.equals("region"))
                    {
                        state = reader.getElementText();
                    }

                    if(el.equals("country"))
                    {
                        country = reader.getElementText();
                    }

                    if(el.equals("lat"))
                    {
                        latitude = reader.getElementText();
                    }

                    if(el.equals("lon"))
                    {
                        longitude = reader.getElementText();
                        //System.out.println("here");
                    }

                    if(el.equals("formatted_address"))
                    {
                        String[] arr = reader.getElementText().split(",");
                        state = arr[1].substring(1,3);
                    }
                }

                reader.next();
            }
            reader.close();
        }
        catch (XMLStreamException e)
        {}
        catch (Exception e)
        {}
        localApi();
    }

    public String latWeather()
    {
        getLocation(0);
        //latitude =  "51.5073";longitude = "-0.1277";//london
        String wapi = "http://api.openweathermap.org/data/2.5/forecast?lat=" + latitude + "&lon=" + longitude +
              "&units=metric&mode=xml&appid=d3c9f8ba9b96d5b61d961d54d9b5d0ea";
        return wapi;
    }

    public String cityWeather()
    {
        String wapi = "http://api.openweathermap.org/data/2.5/forecast?q=" + city +
                "&units=metric&mode=xml&appid=d3c9f8ba9b96d5b61d961d54d9b5d0ea";
        getLocation(1);
        return wapi;
    }

    public void getWeather(String s) {
        //clear array that holds Forecast objects
        fore.clear();
        //clear the array that holds temperatures
        forecast.clear();
        //api to use to lookup the weather
        String wapi = s;
        try {
            weatherApi = new URL(wapi).openConnection().getInputStream();

            // create the XMLInputFactory object
            XMLInputFactory inputFactory =
                    XMLInputFactory.newInstance();

            //create xml stream reader
            XMLStreamReader reader =
                    inputFactory.createXMLStreamReader(weatherApi);

            // Read XML here
            reader.next();
            while (reader.hasNext()) {
                //for each line, check the tag type and get relavat info from it
                //System.out.println(reader.getLocalName());
                int eventType = reader.getEventType();
                if (eventType == XMLStreamReader.START_ELEMENT) {
                    String el = reader.getLocalName();

                    if (el.equals("temperature")) {
                        temperature1 = reader.getAttributeValue(1);
                        //add temp to array
                        forecast.add(temperature1);
                    }
                    if (el.equals("name")) {
                        city = reader.getElementText();
                    }

                    if (el.equals("time")) {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                        time = reader.getAttributeValue(1); //yyyy-MM-dd'T'HH:mm:ss //time = reader.getAttributeValue
                        // (1); //yyyy-MM-dd'T'HH:mm:ss
                        //System.out.println("czas sie wyświetla: " + time);
                        try {
                            date = sdf.parse(time);
                            //System.out.println("data sie wyświetla: " + date);
                        } catch (ParseException ex) {
                        }
                    }

                    if (el.equals("pressure")) {
                        pressure = reader.getAttributeValue(1);
                        //add temp to array
                        forecast.add(pressure);
                    }

                    if (el.equals("symbol")) {
                        symbol = reader.getAttributeValue(2);
                        description = reader.getAttributeValue(1);
                        //System.out.println(el);
                        //add the info to array
                        Forecast temp = new Forecast(temperature1, symbol, description, date, pressure);
                        fore.add(temp);
                    }

                }
                reader.next();
            }
        } catch (IOException e) {
        } catch (XMLStreamException ex) {
        }

        if (forecast.isEmpty()) {
            forecast.add("0");
            fore.add(new Forecast());
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            //array to store any intinces where time is new day
            ArrayList<Integer> array = new ArrayList();

            for (int i = 0; i < fore.size(); i++) {
                String now = sdf.format(fore.get(i).time);
                if (now.equals("09:00:00")) {
                    array.add(i);
                }
            }

            //getting the info for future forecast from array of forecast
            int a = array.get(0);
            int b = array.get(1);
            int c = array.get(2);
            int d = array.get(3);

            //arrays to store the temps of the different days
            ArrayList<Double> day1 = new ArrayList();
            ArrayList<Double> day2 = new ArrayList();
            ArrayList<Double> day3 = new ArrayList();
            ArrayList<Double> day4 = new ArrayList();

            //loop through fore array and add temps to each array
            for (int i = a; i < b; i++) {
                Double temp = Double.parseDouble(fore.get(i).temperature1);
                day1.add(temp);
            }

            for (int i = b; i < c; i++) {
                Double temp = Double.parseDouble(fore.get(i).temperature1);
                day2.add(temp);
            }

            for (int i = c; i < d; i++) {
                Double temp = Double.parseDouble(fore.get(i).temperature1);
                day3.add(temp);
            }
            for (int i = d; i < (d + 8); i++) {
                Double temp = Double.parseDouble(fore.get(i).temperature1);
                day4.add(temp);
            }

            //sort the new arrays
            Collections.sort(day1);
            Collections.sort(day2);
            Collections.sort(day3);
            Collections.sort(day4);

            //get the highest value of each array and cast to integer
            Integer one = day1.get(day1.size() - 1).intValue();
            Integer two = day2.get(day1.size() - 1).intValue();
            Integer three = day3.get(day1.size() - 1).intValue();
            Integer four = day4.get(day1.size() - 1).intValue();

            //prepare for display
            tempTom = one.toString();
            tempDay = two.toString();
            tempDayDay = three.toString();
            tempDayDayDay = four.toString();

            this.tempToday = forecast.get(0).substring(0, 2);
            this.pressureToday = fore.get(a).pressure;
            this.todayDay = fore.get(0).time.toString().substring(0, 3);
            this.descriptionToday = fore.get(0).description;

            this.tomorrow =
                    " " + fore.get(a).time.toString().substring(0, 4) + "     " + tempTom + "ºC      " + fore.get(a).pressure + " hPa" + "     " + fore.get(a).description;// + " " + fore.get(a).symbol;
            this.dayAfter =
                    " " + fore.get(b).time.toString().substring(0, 4) + "     " + tempDay + "ºC      " + fore.get(b).pressure + " hPa" + "     " + fore.get(b).description;// + " " + fore.get(b).symbol;
            this.dayDayAfter =
                    " " + fore.get(c).time.toString().substring(0, 4) + "     " + tempDayDay + "ºC      " + fore.get(c).pressure + " hPa" + "     " + fore.get(c).description;// + " " + fore.get(c).symbol;
            this.dayDayDayAfter =
                    " " + fore.get(d).time.toString().substring(0, 4) + "     " + tempDayDayDay + "ºC      " + fore.get(d).pressure + " hPa" + "     " + fore.get(d).description;// + " " + fore.get(d).symbol;

            this.icon0 = fore.get(0).symbol;
            this.iconA = fore.get(a).symbol;
            this.iconB = fore.get(b).symbol;
            this.iconC = fore.get(c).symbol;
            this.iconD = fore.get(d).symbol;

        }

//        System.out.println(today);
//        System.out.println(fore.get(0).time.toString().substring(0, 4) + " " + fore.get(0).description);
//        System.out.println(forecast.get(0).substring(0, 2) + " C " + forecast.get(1) + " hPa " + fore.get(0).symbol);
//        System.out.println(wapi);
//        System.out.println(fore.get(0).time);
//        System.out.println(tomorrow);
//        System.out.println(dayAfter);
//        System.out.println(dayDayAfter);
//        System.out.println(dayDayDayAfter);
//        System.out.println(city);

    }

    public String getCity()
    {
        return city;
    }
    //returns the string that is as url to get icon from
    public String getIcon0()
    {
        return icon0;
    }
    public String getIconA()
    {
        //String s = "http://openweathermap.org/img/w/" + fore.get(0).symbol +".png";
        //String s = "01n.png";

        //String a = iconA;
        //return a;
        return iconA;
    }
    public String getIconB()
    {
        return iconB;
    }
    public String getIconC()
    {
        return iconC;
    }
    public String getIconD()
    {
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

