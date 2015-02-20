package com.example.esir.nsoc2014.tsen.lob.interfaces;

import com.example.esir.nsoc2014.tsen.lob.objects.DatesInterval;

import java.sql.ResultSet;
import java.sql.Time;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public interface Prevision {
    //void weatherSearch() throws IOException;

    void predictNext(ResultSet result) throws Exception;

    HashMap<Date, List<DatesInterval>> getHashmap();

    List<DatesInterval> getList();
    //public List<DatesInterval> predict() throws Exception;
}
