package com.example.esir.nsoc2014.tsen.lob;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.classifiers.functions.LinearRegression;

public class Regression {

	static Instances data;
	static Instances newData;

	public static void main(String args[]) throws Exception {
		// load data from arff file
		BufferedReader reader = new BufferedReader(new FileReader(
				"./data/housing.arff"));
		data = new Instances(reader);
		data.setClassIndex(data.numAttributes() - 1); // checks for the
														// attributes except the
														// last
		// build a model
		LinearRegression model = new LinearRegression();
		model.buildClassifier(data); // the last instance with a missing value is
										// not used
		System.out.println(model);
		reader.close();

		if (addInputs(3550, 9401, 3, 0, 1)) {
			// classify the last instance
			Instance myHouse = newData.lastInstance(); // load the last instance
			double price = model.classifyInstance(myHouse); // use the model on
															// the last instance
			System.out.println("My house (" + myHouse + "): " + price);
			newData.lastInstance().setValue(newData.attribute("sellingPrice"),
					price); // set the value of the missing attribute that we
							// are looking for
			System.out.println(newData);

			if (saveInstancesInArffFile(newData))
				System.out.println("save success !");
		}
	}

	static Instance instance = new DenseInstance(6);

	/**
	 * add a new instance with 5 known attributes and the sixth is "?" (the one
	 * we are looking for)
	 * 
	 * @param att1
	 *            : first attribute
	 * @param att2
	 *            : second attribute
	 * @param att3
	 *            : third attribute
	 * @param att4
	 *            : fourth attribute
	 * @param att5
	 *            : fifth attribute
	 * @return true if success else false
	 */
	public static boolean addInputs(double att1, double att2, double att3,
			double att4, double att5) {
		newData = new Instances(data); // copy the instances of the arff file
										// (needed to be modified)
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

	/**
	 * save the new instance in the arff file
	 * 
	 * @param dataSet
	 *            : the instances we want to save
	 * @return true
	 * @throws IOException
	 */
	public static boolean saveInstancesInArffFile(Instances dataSet)
			throws IOException {
		ArffSaver saver = new ArffSaver();
		saver.setInstances(dataSet);
		saver.setFile(new File("./data/housing.arff"));
		saver.writeBatch();

		return true;
	}
}
