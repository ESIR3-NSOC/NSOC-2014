package com.example.esir.nsoc2014.tsen.lob.objects;

import java.io.Serializable;
import java.sql.Time;

public class DatesInterval implements Comparable<DatesInterval>, Serializable{
    private Time start;
    private Time end;
    private double consigne;
    private int nbPerson;
    private WeatherForecast prev;
    private String lesson;
    private String id;
    private double temp;
    private double lum;
    private double humidity;

    public DatesInterval(Time start, Time end, double consigne, int nbPerson,
                         double temp, double lum, double humidity, String lesson) {
        this.start = start;
        this.end = end;
        this.consigne = consigne;
        this.nbPerson = nbPerson;
        this.humidity = humidity;
        this.temp = temp;
        this.lum = lum;
        this.lesson = lesson;
    }

    public DatesInterval(String id,Time start, Time end, double consigne,
                         double temp, double lum, double humidity, String lesson) {
        this.start = start;
        this.end = end;
        this.consigne = consigne;
        this.lesson = lesson;
        this.humidity = humidity;
        this.temp = temp;
        this.lum = lum;
        this.id = id;
    }

    public double getTemp(){
        return temp;
    }

    public double getlum(){
        return lum;
    }

    public double gethumidity(){
        return humidity;
    }

    public String getId(){
        return id;
    }

    public String getLesson(){
        return lesson;
    }

    public int getNbPerson() {
        return nbPerson;
    }

    public WeatherForecast getPrevision() {
        return prev;
    }

    public double getConsigne() {
        return consigne;
    }

    public Time getStartDate() {
        return start;
    }

    public Time getStartEnd() {
        return end;
    }

    public int compareTo(DatesInterval date2) {
        if (consigne == date2.getConsigne())
            return 0;
        else if (consigne < date2.getConsigne())
            return -1;
        else
            return 1;
    }
}
