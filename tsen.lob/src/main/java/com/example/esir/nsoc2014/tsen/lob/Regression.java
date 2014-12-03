package com.example.esir.nsoc2014.tsen.lob;

import java.io.BufferedReader;
import java.io.FileReader;

import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import weka.classifiers.functions.LinearRegression;

public class Regression {

	static Instances data;

	public static void main(String args[]) throws Exception {
		// load data from arff file
		data = new Instances(new BufferedReader(new FileReader("housing.arff")));
		data.setClassIndex(data.numAttributes() - 1);
		// build a model
		LinearRegression model = new LinearRegression();
		model.buildClassifier(data); // the last instance with missing class is
										// not used
		System.out.println(model);

		if (addInputs(3550, 9401, 3, 0, 1)) {
			// classify the last instance
			Instance myHouse = data.lastInstance();
			double price = model.classifyInstance(myHouse);
			System.out.println("My house (" + myHouse + "): " + price);
		}
	}

	static Instance instance = new DenseInstance(6);

	public static boolean addInputs(double att1, double att2, double att3,
			double att4, double att5) {
		Instances newData = new Instances(data);
		System.out.println(newData);
		instance.setValue(newData.attribute("houseSize"), att1);
		instance.setValue(newData.attribute("lotSize"), att2);
		instance.setValue(newData.attribute("bedrooms"), att3);
		instance.setValue(newData.attribute("granite"), att4);
		instance.setValue(newData.attribute("bathroom"), att5);

		newData.add(instance);
		System.out.println(newData);
		return true;
	}

	public boolean addInputs(double att1, double att2, double att3,
			double att4, double att5, double att6) {
		return true;
	}
}
