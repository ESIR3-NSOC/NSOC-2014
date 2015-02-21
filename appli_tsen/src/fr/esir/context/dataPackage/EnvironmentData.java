package fr.esir.context.dataPackage;

/**
 * Created by mathi_000 on 21/02/2015.
 */
public class EnvironmentData {

    private double indoorTemp;
    private double outdoorTemp;

    private double outdoorLum;
    private double outdoorHum;

    private long ts;

    public EnvironmentData(long t ,double intTemp,double outTemp, double outL, double outH){

        ts = t;
        outdoorLum = outL;
        indoorTemp = intTemp;
        outdoorHum = outH;
        outdoorTemp = outTemp;
    }

    public EnvironmentData(long t){
        ts = t;
    }

    public void setOutdoorLum(double indoorLum) {
        this.outdoorLum = indoorLum;
    }

    public void setIndoorTemp(double indoorTemp) {
        this.indoorTemp = indoorTemp;
    }

    public void setOutdoorHum(double outdoorHum) {
        this.outdoorHum = outdoorHum;
    }

    public void setOutdoorTemp(double outdoorTemp) {
        this.outdoorTemp = outdoorTemp;
    }

    public double getIndoorLum() {
        return outdoorLum;
    }

    public double getIndoorTemp() {
        return indoorTemp;
    }

    public double getOutdoorHum() {
        return outdoorHum;
    }

    public double getOutdoorTemp() {
        return outdoorTemp;
    }

    public long getTs() {
        return ts;
    }

    public void setEnvironmentData(double intTemp,double outTemp, double outL, double outH){
        outdoorLum = outL;
        indoorTemp = intTemp;
        outdoorHum = outH;
        outdoorTemp = outTemp;
    }


}
