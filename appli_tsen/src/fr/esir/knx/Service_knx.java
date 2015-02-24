package fr.esir.knx;

import tuwien.auto.calimero.exception.KNXException;

/**
 * Created by Nicolas on 18/02/2015.
 */
public interface Service_knx {
    boolean initialize();

    void sendDisplayData(String add, String data);

    KnxManager getKnxManager();

    void setVanne(int Percent, String address) throws KNXException;
}
