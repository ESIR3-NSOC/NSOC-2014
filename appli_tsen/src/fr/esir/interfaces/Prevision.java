package fr.esir.interfaces;

import fr.esir.objects.DatesInterval;

import java.util.List;

public interface Prevision {
	public List<DatesInterval> predict() throws Exception;
}
