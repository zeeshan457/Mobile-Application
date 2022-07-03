package com.example.weatherradar;

public class WeatherModel {

    private String Time;
    private String Temperature;
    private String icon;
    private String WindSpeed;


    // getters and setters
    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getTemperature() {
        return Temperature;
    }

    public void setTemperature(String temperature) {
        Temperature = temperature;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getWindSpeed() {
        return WindSpeed;
    }

    public void setWindSpeed(String windSpeed) {
        WindSpeed = windSpeed;
    }

    // constructor
    public WeatherModel(String time, String temperature, String icon, String windSpeed) {
        this.Time = time;
        this.Temperature = temperature;
        this.icon = icon;
        this.WindSpeed = windSpeed;
    }


}
