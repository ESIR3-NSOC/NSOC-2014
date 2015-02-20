package fr.esir.regulation;

import fr.esir.maintasks.MyActivity;

/**
 * Created by Nicolas on 06/02/2015.
 */
public class DataFromKNX {
    private double i_temp;
    private double o_temp;
    private double i_hum;
    private double o_hum;
    private double o_lum;

    private double cons;

    public DataFromKNX(double cons){
        this.i_temp = 0;
        this.o_temp =0;
        this.i_hum = 0;
        this.o_hum = 0;
        this.o_lum =0;

        this.cons = cons;
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

    public double getO_lum(){
        return o_lum;
    }

    public double getCons() {
        return cons;
    }

    public void setAll(){
        i_temp = MyActivity.lastTemp_in;
        o_temp = MyActivity.lastTemp_out;
        i_hum = MyActivity.lastHum_in;
        o_hum = MyActivity.lastHum_out;
        o_lum = MyActivity.lastLum_out;
    }
}
