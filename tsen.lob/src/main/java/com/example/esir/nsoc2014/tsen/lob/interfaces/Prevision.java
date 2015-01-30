package com.example.esir.nsoc2014.tsen.lob.interfaces;

import java.util.List;

import com.example.esir.nsoc2014.tsen.lob.objects.DatesInterval;

public interface Prevision {
	public List<DatesInterval> predict() throws Exception;
}
