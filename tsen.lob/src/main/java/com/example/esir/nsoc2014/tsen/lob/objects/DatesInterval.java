package com.example.esir.nsoc2014.tsen.lob.objects;

import java.sql.Time;

public class DatesInterval implements Comparable<DatesInterval> {
    private Time start;
    private Time end;
    private double consigne;
    private int nbPerson;
    private WeatherForecast prev;
    private String lesson;
    private String id;

    public DatesInterval(Time start, Time end, double consigne, int nbPerson,
                         WeatherForecast prev, String lesson) {
        this.start = start;
        this.end = end;
        this.consigne = consigne;
        this.nbPerson = nbPerson;
        this.prev = prev;
        this.lesson = lesson;
        this.id = id;
    }

    public DatesInterval(String id,Time start, Time end, double consigne,
                         WeatherForecast prev, String lesson) {
        this.start = start;
        this.end = end;
        this.consigne = consigne;
        this.prev = prev;
        this.lesson = lesson;
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

    public String toString() {
        return "Start at " + start + " End at " + end + " Temp must be "
                + consigne + " " + nbPerson + " people will be in the classroom.";
    }

}
