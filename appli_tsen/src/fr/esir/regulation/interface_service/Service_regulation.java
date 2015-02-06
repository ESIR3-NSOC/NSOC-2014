package fr.esir.regulation.interface_service;

import android.content.SharedPreferences;

/**
 * Created by Nicolas on 06/02/2015.
 */
public interface Service_regulation {
    boolean initialize();

    SharedPreferences getSharedpreferences();
}
