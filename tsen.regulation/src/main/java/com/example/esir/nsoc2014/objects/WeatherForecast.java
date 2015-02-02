package com.example.esir.nsoc2014.objects;

public class WeatherForecast {
    private double humidity;
    private double temp;
    private double lum;


    public WeatherForecast() {
        this.humidity = 0;
        this.temp = 0;
        this.lum = 0;
    }

    public double getLum() {
        return lum;
    }

    /**
     * get the humidity value
     *
     * @return
     */
    public double getHumidity() {
        return humidity;

    }

    /**
     * get the temperature value
     *
     * @return
     */
    public double getTemp() {
        return temp;
    }
}
