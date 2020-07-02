package com.kamil.Controllers;

/**
 * Created by DELL on 2020-06-16.
 */
import com.kamil.Models.ImageHandler;
import com.kamil.Models.WeatherManager;
import com.kamil.Models.WeatherUtility;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    WeatherManager weatherManager;
    WeatherUtility weatherUtility;
    String citySet;

    @FXML
    private ImageView img, img1, img2, img3, img4, img5, img6, img7, img8, img9;
    @FXML
    private JFXButton change, set, cancel;
    @FXML
    private JFXTextField cityName, invis;
    @FXML
    private Label city, temperature, day, desc, errors, windSpeed, cloudiness, pressure, humidity, tomorrow,
    dayDayDayAfter, dayDayAfter, dayAfter, tomorrow1, dayDayDayAfter1, dayDayAfter1, dayAfter1, pressure1, desc1,
            temperature1, day1, city1;

    //Constructor to set the initial city to Pune
    public Controller() {
        this.citySet = "Cracow".toUpperCase();
    }// ustawienie miejsca startowego

    //Event Handler for each button
    @FXML
    private void handleButtonClicks(javafx.event.ActionEvent ae) {
        String initialCity = city.getText(); //stores the last searched city-name

        if(ae.getSource() == change){
            cityName.setText("");
            bottomSet(true);
            cityName.requestFocus();
        }else if (ae.getSource() == set) {
            setPressed();
        } else if (ae.getSource() == cancel) {
            cityName.setText(initialCity);
            bottomSet(false);
            invis.requestFocus();
        }
    }

    @FXML
    private void yourLocationButton(javafx.event.ActionEvent ae) {
        playButtonActionPerformed(ae);
    }

    private void playButtonActionPerformed(javafx.event.ActionEvent ae)

    {
        // TODO add your handling code here:

        String wapi1 = weatherUtility.latWeather();
        weatherUtility.getWeather(wapi1);

        String todayDay = weatherUtility.getTodayDay();
        if(todayDay.equals("Mon")){
            day1.setText("MONDAY");
        }
        else if(todayDay.equals("Tue")){
            day1.setText("TUESDAY");
        }
        else if(todayDay.equals("Wed")){
            day1.setText("WEDNESDAY");
        }
        else if(todayDay.equals("Thu")){
            day1.setText("THURSDAY");
        }
        else if(todayDay.equals("Fri")){
            day1.setText("FRIDAY");
        }
        else if(todayDay.equals("Sat")){
            day1.setText("SATURDAY");
        }
        else if(todayDay.equals("Sun")){
            day1.setText("SUNDAY");
        }

        temperature1.setText(weatherUtility.getTempToday().toString()+"°C");
        desc1.setText(weatherUtility.getDescriptionToday().toUpperCase());
        pressure1.setText(weatherUtility.getPressureToday()+" hPa");
        city1.setText(weatherUtility.getCity().toUpperCase());


        tomorrow1.setText(weatherUtility.getTomorrow());
        dayAfter1.setText(weatherUtility.getDayAfter());
        dayDayAfter1.setText(weatherUtility.getDayDayAfter());
        dayDayDayAfter1.setText((weatherUtility.getDayDayDayAfter()));

        img5.setImage(new Image("/images/" + weatherUtility.getIconA() + ".png"));
        img6.setImage(new Image("/images/" + weatherUtility.getIconB() + ".png"));
        img7.setImage(new Image("/images/" + weatherUtility.getIconC() + ".png"));
        img8.setImage(new Image("/images/" + weatherUtility.getIconD() + ".png"));
        img9.setImage(new Image("/images/" + weatherUtility.getIcon0() + ".png"));

    }


    //method to clear all the fields
    private void reset() {
        bottomSet(false);
        day.setText("");
        temperature.setText("");
        desc.setText("");
        windSpeed.setText("");
        cloudiness.setText("");
        pressure.setText("");
        humidity.setText("");
        img.setImage(null);
    }

    //method to set the new entered city
    private void setPressed(){
        //if user enters nothing into cityName field
        if(cityName.getText().equals("")){
            showToast("City Name cannot be blank");
            return;
        }else {
            try {
                errors.setText("");
                this.citySet = cityName.getText().trim();
                cityName.setText((cityName.getText().trim()).toUpperCase());
                weatherManager = new WeatherManager(citySet);
                weatherUtility = new WeatherUtility(citySet);
                city.setTextFill(Color.web("#c5e4fc"));
                showWeather();
                showForecast2();
                bottomSet(false);
                invis.requestFocus();
            }catch(Exception e){
                city.setText("Error!");
                city.setTextFill(Color.RED);
                showToast("City with the given name was not found.");
                reset();
                invis.requestFocus();
            }
        }
    }

    //method to handle nodes at botton part of the scene
    private void bottomSet(boolean statement){
        cityName.setDisable(!statement);
        set.setVisible(statement);
        change.setVisible(!statement);
        cancel.setVisible(statement);
    }

    //method to show error messages
    private void showToast(String message) {
        errors.setText(message);
        errors.setTextFill(Color.RED);
        errors.setStyle("-fx-background-color: #fff; -fx-background-radius: 50px;");

        FadeTransition fadeIn = new FadeTransition(Duration.seconds(1), errors);
        fadeIn.setToValue(1);
        fadeIn.setFromValue(0);
        fadeIn.play();

        fadeIn.setOnFinished(event -> {
            PauseTransition pause = new PauseTransition(Duration.seconds(2));
            pause.play();
            pause.setOnFinished(event2 -> {
                FadeTransition fadeOut = new FadeTransition(Duration.seconds(2), errors);
                fadeOut.setToValue(0);
                fadeOut.setFromValue(1);
                fadeOut.play();
            });
        });
    }

    //actual method to call and get the weather and populate the scene
    private void showWeather(){
        weatherManager.getWeather();
        city.setText(weatherManager.getCity().toUpperCase());
        temperature.setText(weatherManager.getTemperature().toString()+"°C");
        day.setText(weatherManager.getDay().toUpperCase());
        desc.setText(weatherManager.getDescription().toUpperCase());
        img.setImage(new Image(ImageHandler.getImage(weatherManager.getIcon())));
        windSpeed.setText(weatherManager.getWindSpeed()+" m/s");
        cloudiness.setText(weatherManager.getCloudiness()+"%");
        pressure.setText(weatherManager.getPressure()+" hPa");
        humidity.setText(weatherManager.getHumidity()+"%");
    }

    //get weather from 5 days
    private void showForecast2(){

        String wapi2 = weatherUtility.cityWeather();
        weatherUtility.getWeather(wapi2);
        tomorrow.setText(weatherUtility.getTomorrow());
        dayAfter.setText(weatherUtility.getDayAfter());
        dayDayAfter.setText(weatherUtility.getDayDayAfter());
        dayDayDayAfter.setText((weatherUtility.getDayDayDayAfter()));

        img1.setImage(new Image("/images/" + weatherUtility.getIconA() + ".png"));
        img2.setImage(new Image("/images/" + weatherUtility.getIconB() + ".png"));
        img3.setImage(new Image("/images/" + weatherUtility.getIconC() + ".png"));
        img4.setImage(new Image("/images/" + weatherUtility.getIconD() + ".png"));
    }

    public void initialize(URL location, ResourceBundle resources) {
        cityName.setText(citySet);
        cityName.setDisable(true);
        set.setVisible(false);
        cancel.setVisible(false);
        errors.setText("");
        weatherManager = new WeatherManager(citySet);
        weatherUtility = new WeatherUtility(citySet);
        invis.requestFocus();

        //try catch block to see if there is internet and disabling every field
        try{
            showWeather();
               // System.out.println("dziala weather!!");
            showForecast2();
                //System.out.println("dziala forecast 2!!");
        } catch (Exception e){
            city.setText("Error! - No Internet");
            city.setTextFill(Color.RED);
                //System.out.println("NIE NIE dziala forecast!!");
            showToast("Internet Down. Please Connect");
            reset();
            change.setDisable(true);
            cityName.setText("");
        }

        //Set the city entered into textField on pressing enter on Keyboard
        cityName.setOnKeyPressed(e -> {
            if(e.getCode() == KeyCode.ENTER){
                setPressed();
            }
        });
    }
}
