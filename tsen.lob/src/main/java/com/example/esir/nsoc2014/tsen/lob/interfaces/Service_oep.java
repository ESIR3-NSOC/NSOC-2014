package com.example.esir.nsoc2014.tsen.lob.interfaces;

import java.io.IOException;

/**
 * Created by Nicolas on 04/02/2015.
 */
public interface Service_oep {
    void startPrediction() throws IOException;

    boolean initialize();
}
