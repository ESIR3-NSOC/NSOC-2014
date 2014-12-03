package com.example.esir.nsoc2014.tsen.lob;



import java.io.BufferedReader;
import java.io.FileReader;
import weka.core.Instance;
import weka.core.Instances;
import weka.classifiers.functions.LinearRegression;

public class Regression {

	public static void main(String args[]) throws Exception{
		//load data from arff file
		Instances data = new Instances(new BufferedReader(new
				FileReader("housing.arff")));
		data.setClassIndex(data.numAttributes() - 1);
		//build a model
		LinearRegression model = new LinearRegression();
		model.buildClassifier(data); //the last instance with missing class is not used
		System.out.println(model);

		//classify the last instance
		Instance myHouse = data.lastInstance();
		double price = model.classifyInstance(myHouse);
		System.out.println("My house ("+myHouse+"): "+price);
	}
}
