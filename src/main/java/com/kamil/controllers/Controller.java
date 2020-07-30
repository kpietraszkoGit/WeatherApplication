package com.kamil.controllers;

/**
 * Created by DELL on 2020-07-18.
 */

import com.kamil.models.ImageHandler;
import com.kamil.models.WeatherManager;
import com.kamil.models.WeatherUtility;
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
import org.json.JSONException;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    private WeatherManager weatherManager;
    private WeatherUtility weatherUtility;
    public String citySet;

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

    public Controller() { this.citySet = "Cracow".toUpperCase(); }

    @FXML
    private void handleButtonClicks(javafx.event.ActionEvent ae) {
        String initialCity = city.getText();

        if (ae.getSource() == change) {
            cityName.setText("");
            bottomSet(true);
            cityName.requestFocus();
        } else if (ae.getSource() == set) {
            setPressed();
        } else if (ae.getSource() == cancel) {
            cityName.setText(initialCity);
            bottomSet(false);
            invis.requestFocus();
        }
    }

    @FXML
    public void yourLocationButton()
    {
        String dataFromApi = weatherUtility.coordinatesWeather();
        weatherUtility.fetchDataWeather(dataFromApi);

        String todayDay = weatherUtility.getTodayDay();
        switch (todayDay) {
            case "Mon":
                day1.setText("MONDAY");
                break;
            case "Tue":
                day1.setText("TUESDAY");
                break;
            case "Wed":
                day1.setText("WEDNESDAY");
                break;
            case "Thu":
                day1.setText("THURSDAY");
                break;
            case "Fri":
                day1.setText("FRIDAY");
                break;
            case "Sat":
                day1.setText("SATURDAY");
                break;
            case "Sun":
                day1.setText("SUNDAY");
                break;
        }

        temperature1.setText(weatherUtility.getTempToday() + "°C");
        desc1.setText(weatherUtility.getDescriptionToday().toUpperCase());
        pressure1.setText(weatherUtility.getPressureToday() + " hPa");
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

    public void reset() {
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

    private void setPressed() {
        if (cityName.getText().equals("")) {
            showToast("City Name cannot be blank");
        } else {
            try {
                errors.setText("");
                this.citySet = cityName.getText().trim();
                cityName.setText((cityName.getText().trim()).toUpperCase());
                weatherManager = new WeatherManager(citySet);
                weatherUtility = new WeatherUtility(citySet);
                city.setTextFill(Color.web("#c5e4fc"));
                showWeather();
                showForecast();
                bottomSet(false);
                invis.requestFocus();
            } catch (Exception e) {
                city.setText("Error!");
                city.setTextFill(Color.RED);
                showToast("City with the given name was not found.");
                reset();
                invis.requestFocus();
            }
        }
    }

    private void bottomSet(boolean statement) {
        cityName.setDisable(!statement);
        set.setVisible(statement);
        change.setVisible(!statement);
        cancel.setVisible(statement);
    }

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

    public void showWeather() throws JSONException {
        weatherManager.fetchDataWeather();
        city.setText(weatherManager.getCity().toUpperCase());
        temperature.setText(weatherManager.getTemperature().toString() + "°C");
        day.setText(weatherManager.getDay().toUpperCase());
        desc.setText(weatherManager.getDescription().toUpperCase());
        img.setImage(new Image(ImageHandler.getImage(weatherManager.getIcon())));
        windSpeed.setText(weatherManager.getWindSpeed() + " m/s");
        cloudiness.setText(weatherManager.getCloudiness() + "%");
        pressure.setText(weatherManager.getPressure() + " hPa");
        humidity.setText(weatherManager.getHumidity() + "%");
    }

    private void showForecast() {

        String weatherApi = weatherUtility.cityWeather();
        weatherUtility.fetchDataWeather(weatherApi);
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

        try {
            showWeather();
            showForecast();
        } catch (Exception e) {
            city.setText("Error! - No Internet");
            city.setTextFill(Color.RED);
            showToast("Internet Down. Please Connect");
            reset();
            change.setDisable(true);
            cityName.setText("");
        }

        cityName.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                setPressed();
            }
        });
    }
}
