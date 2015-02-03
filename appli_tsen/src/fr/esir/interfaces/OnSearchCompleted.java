package fr.esir.interfaces;

import java.sql.ResultSet;

/**
 * Created by Nicolas on 03/02/2015.
 */
public interface OnSearchCompleted {
    void onSearchCompleted(boolean o);
    void onSearchbisCompleted(ResultSet o);
}
