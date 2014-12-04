package com.example.esir.nsoc2014.tsen.lob.generator;

import java.io.File;
import java.io.IOException;

import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffSaver;

public class ArffGenerator {
	public static void main(String[] args) throws Exception {
		FastVector atts; //find an other way -> arrayList ?
		Instances data;
		Instance instance;
		
		//1. Set up attributes
		atts = new FastVector();
		// - numeric
		atts.addElement(new Attribute("houseSize"));
		atts.addElement(new Attribute("lotSize"));
		atts.addElement(new Attribute("bedrooms"));
		atts.addElement(new Attribute("granite"));
		atts.addElement(new Attribute("bathroom"));
		atts.addElement(new Attribute("sellingPrice"));
		
		//2. Create instances object
		data = new Instances("house", atts, 0);
		
		//3. Fill with data
		instance = new DenseInstance(6);
		instance.setValue(data.attribute("houseSize"), 5031);
		instance.setValue(data.attribute("lotSize"), 12300);
		instance.setValue(data.attribute("bedrooms"), 3);
		instance.setValue(data.attribute("granite"), 1);
		instance.setValue(data.attribute("bathroom"), 2);
		instance.setValue(data.attribute("sellingPrice"), 193520);
		
		data.add(instance);
		
		//4. Display output
		System.out.println(data);
		if(saveInstancesInArffFile(data))
			System.out.println("arff file created");
	}
	
	public static boolean saveInstancesInArffFile(Instances dataSet)
			throws IOException {
		ArffSaver saver = new ArffSaver();
		saver.setInstances(dataSet);
		saver.setFile(new File("./data/housingGene.arff"));
		saver.writeBatch();

		return true;
	}

}
