package fr.esir.context.dataPackage;


public class EnvironmentData {

    private double indoorTemp;
    private double outdoorTemp;

    private double outdoorLum;
    private double indoorLum;

    private double outdoorHum;
    private double indoorHum;

    private double airQuality;

    private double valve;
    private long ts;

    public EnvironmentData(long t ,double intTemp,double outTemp, double outL,double inL,double inH, double outH, double c02, double v){

        ts = t;
        outdoorLum = outL;
        indoorLum = inL;

        indoorTemp = intTemp;
        outdoorTemp = outTemp;

        outdoorHum = outH;
        indoorHum = outH;

        airQuality = c02;

        valve = v;


    }

    public void setAirQuality(double airQuality) {
        this.airQuality = airQuality;
    }

    public void setIndoorHum(double indoorHum) {
        this.indoorHum = indoorHum;
    }

    public void setIndoorLum(double indoorLum) {
        this.indoorLum = indoorLum;
    }

    public void setValve(double valve) {
        this.valve = valve;
    }

    public void setTs(long ts) {
        this.ts = ts;
    }

    public double getAirQuality() {
        return airQuality;
    }

    public double getIndoorHum() {
        return indoorHum;
    }

    public double getOutdoorLum() {
        return outdoorLum;
    }

    public double getValve() {
        return valve;
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
