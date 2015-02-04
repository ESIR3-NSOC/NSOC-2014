package com.example.esir.nsoc2014.tsen.lob.interfaces;

import java.sql.ResultSet;

public interface OnSearchCompleted {
    void onSearchCompleted(boolean o);
    void onSearchCompleted(ResultSet o);
    void onSearchCompleted(String weath);
}
