package com.example.esir.nsoc2014.tsen.lob.interfaces;

import com.example.esir.nsoc2014.tsen.lob.objects.DatesInterval;
import com.example.esir.nsoc2014.tsen.lob.objects.WeatherForecast;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.Time;
import java.util.HashMap;
import java.util.List;

public interface Prevision {
    //void weatherSearch() throws IOException;

    WeatherForecast getWeatherForecast();

    void predictNext(ResultSet result) throws Exception;

    HashMap<Time, List<DatesInterval>> getHashmap();

    List<DatesInterval> getList();
    //public List<DatesInterval> predict() throws Exception;
}
