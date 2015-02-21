package fr.esir.knx;

/**
 * Created by Nicolas on 18/02/2015.
 */
public interface Service_knx {
    boolean initialize();

    void sendDisplayData(String add, String data);
}
