package fr.esir.interfaces;

import java.sql.ResultSet;

public interface OnSearchCompleted {
    void onSearchCompleted(boolean o);
    void onSearchbisCompleted(ResultSet o);
}
