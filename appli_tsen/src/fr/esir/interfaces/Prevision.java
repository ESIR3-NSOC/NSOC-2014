package fr.esir.interfaces;

import fr.esir.objects.DatesInterval;

import java.io.IOException;
import java.util.List;

public interface Prevision {
    public void weatherSearch() throws IOException;
	//public List<DatesInterval> predict() throws Exception;
}
