package com.example.esir.nsoc2014.regulator.learning;

import com.example.esir.nsoc2014.regulator.knx.DataFromKNX;

import java.sql.Time;

/**
 * Created by Nicolas on 06/02/2015.
 */
public class DataLearning {
    private DataFromKNX mknx;
    private Time heat_time;

    public DataLearning(DataFromKNX mknx){
        this.mknx = mknx;
        this.heat_time = null;
    }

    public DataFromKNX getMknx(){
        return mknx;
    }

    public Time getHeat_time(){
        return  heat_time;
    }

    public void setHeat_time(Time start, Time end){
        heat_time = end - start;
    }
}
