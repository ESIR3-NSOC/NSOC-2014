package com.example.esir.nsoc2014.regulator.knx;

/**
 * Created by Nicolas on 06/02/2015.
 */
public class DataFromKNX {
    private double i_temp;
    private double o_temp;
    private double i_hum;
    private double o_hum;
    private double i_lum;
    private double o_lum;

    private double cons;

    public DataFromKNX(){
        this.i_temp = 0;
        this.o_temp =0;
        this.i_hum = 0;
        this.o_hum = 0;
        this.i_lum = 0;
        this.o_lum =0;

        this.cons = 0;
    }

    public double getI_temp(){
        return i_temp;
    }

    public double getO_temp(){
        return o_temp;
    }

    public double getI_hum(){
        return i_hum;
    }

    public double getO_hum(){
        return o_hum;
    }

    public double getI_lum(){
        return i_lum;
    }

    public double getO_lum(){
        return o_lum;
    }

    public double getCons() {
        return cons;
    }

    public void setI_temp(){

    }

    public void setO_temp(){

    }

    public void setI_hum(){

    }

    public void setO_hum(){

    }

    public void setI_lum(){

    }

    public void setO_lum(){

    }

    public void setCons(){

    }
}
