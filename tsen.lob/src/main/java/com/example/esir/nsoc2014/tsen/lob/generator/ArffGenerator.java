package com.example.esir.nsoc2014.tsen.lob.generator;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffSaver;

public class ArffGenerator {

	static Instance instance;
	static Instances data;

	public static void main(String[] args) throws Exception {
		ArrayList<Attribute> atts;

		// 1. Set up attributes
		atts = new ArrayList<Attribute>();
		// - numeric
		atts.add(new Attribute("houseSize"));
		atts.add(new Attribute("lotSize"));
		atts.add(new Attribute("bedrooms"));
		atts.add(new Attribute("granite"));
		atts.add(new Attribute("bathroom"));
		atts.add(new Attribute("sellingPrice"));

		// 2. Create instances object
		data = new Instances("house", atts, 0);

		// 3. Fill with data
		addData(3529, 9191, 6, 0, 0, 205000);
		addData(3247, 10061, 5, 1, 1, 224900);
		addData(4032, 10150, 5, 0, 1, 197900);
		addData(2397, 14156, 4, 1, 0, 189900);
		addData(2200, 9600, 4, 0, 1, 195000);
		addData(3536, 19994, 6, 1, 1, 325000);
		addData(2983, 9365, 5, 0, 1, 230000);
		addData(9596, 15005, 6, 1, 3, 651000);
		addData(5030, 11520, 4, 1, 2, 460500);

		// 4. Display output
		System.out.println(data);
		if (saveInstancesInArffFile(data, args[0]))
			System.out.println("arff file created");
	}

	public static boolean addData(double...att) {
		instance = new DenseInstance(6);
		instance.setValue(data.attribute("houseSize"), att[0]);
		instance.setValue(data.attribute("lotSize"), att[1]);
		instance.setValue(data.attribute("bedrooms"), att[2]);
		instance.setValue(data.attribute("granite"), att[3]);
		instance.setValue(data.attribute("bathroom"), att[4]);
		instance.setValue(data.attribute("sellingPrice"), att[5]);

		data.add(instance);

		return true;
	}

	public static boolean saveInstancesInArffFile(Instances dataSet, String name)
			throws IOException {
		ArffSaver saver = new ArffSaver();
		saver.setInstances(dataSet);
		saver.setFile(new File("./data/" + name + ".arff"));
		saver.writeBatch();

		return true;
	}

}
