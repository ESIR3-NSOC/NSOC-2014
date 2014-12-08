/*
 * Use a linear regression model on data from arff files
 * Test class, don't use
 * 
 * 
 */

package com.example.esir.nsoc2014.tsen.lob.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import com.example.esir.nsoc2014.tsen.lob.generator.ArffGenerator;

import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.classifiers.functions.LinearRegression;

public class Regression {

	static Instances data;
	static Instances newData;
	static LinearRegression model;
	static ArrayList<String> students = new ArrayList<String>();

	public static void main(String args[]) throws Exception {
		students.add("Jojo");
		students.add("Toto");
		students.add("Rojo");
		students.add("iono");
		students.add("oooo");
		for (String item : students) {
			String[] arg0 = { item };
			ArffGenerator.main(arg0);
			if (executeModel("./data/" + item + ".arff")) {
				if (addInputs(3550, 9401, 3, 0, 1)) {
					// classify the last instance
					Instance myHouse = newData.lastInstance(); // load the last
																// instance
					double price = model.classifyInstance(myHouse); // use the
																	// model
																	// on
																	// the last
																	// instance
					System.out.println("My house (" + myHouse + "): " + price);
					newData.lastInstance().setValue(
							newData.attribute("sellingPrice"), price); // set
																		// the
																		// value
																		// of
																		// the
																		// missing
																		// attribute
																		// that
																		// we
																		// are
																		// looking
																		// for
					System.out.println(newData);

					if (saveInstancesInArffFile(newData, item))
						System.out.println("save success !");
				}
			}
		}
	}

	public static boolean executeModel(String pathArff) throws Exception {
		// load data from arff file
		BufferedReader reader = new BufferedReader(new FileReader(pathArff));
		data = new Instances(reader);
		data.setClassIndex(data.numAttributes() - 1); // checks for the
														// attributes
		// build a model
		model = new LinearRegression();
		model.buildClassifier(data); // the last instance with a missing value
										// is
										// not used
		System.out.println(model);
		reader.close();

		return true;
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
	public static boolean addInputs(double...att) {
		newData = new Instances(data); // copy the instances of the arff file
										// (needed to be modified)
		System.out.println(newData);
		instance.setValue(newData.attribute("houseSize"), att[0]);
		instance.setValue(newData.attribute("lotSize"), att[1]);
		instance.setValue(newData.attribute("bedrooms"), att[2]);
		instance.setValue(newData.attribute("granite"), att[3]);
		instance.setValue(newData.attribute("bathroom"), att[4]);

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
	public static boolean saveInstancesInArffFile(Instances dataSet, String name)
			throws IOException {
		ArffSaver saver = new ArffSaver();
		saver.setInstances(dataSet);
		saver.setFile(new File("./data/" + name + ".arff"));
		saver.writeBatch();

		return true;
	}
}
