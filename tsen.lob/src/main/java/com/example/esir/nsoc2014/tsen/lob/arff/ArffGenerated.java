package com.example.esir.nsoc2014.tsen.lob.arff;

import java.util.ArrayList;

import weka.classifiers.functions.LinearRegression;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;

public class ArffGenerated {

	private Instances data;
	private Instance instance;
	private LinearRegression model;

	public ArffGenerated() {
		this.data = null;
		this.model = null;
	}

	public Instances getArff() {
		return data;
	}

	public LinearRegression getModel() {
		return model;
	}

	private ArrayList<Attribute> atts = new ArrayList<Attribute>() {
		private static final long serialVersionUID = 1L;
		{
			add(new Attribute("hum_ext"));
			add(new Attribute("temp_ext"));
			add(new Attribute("temp_int"));
			add(new Attribute("lum_ext"));
		}
	};

	private ArrayList<Attribute> attslum = new ArrayList<Attribute>() {
		private static final long serialVersionUID = 1L;
		{
			add(new Attribute("season"));
			add(new Attribute("cloud"));
			add(new Attribute("temp_ext"));
			add(new Attribute("hum_ext"));
			add(new Attribute("lum_ext"));
		}
	};

	public void generateArff(String user_id) {
		data = new Instances("pref_" + user_id, atts, 0);
	}

	public void generateArfflum() {
		data = new Instances("Check_lum", attslum, 0);
		addDataLum();
	}

	/**
	 * 
	 * @param att
	 * @return
	 */
	private boolean addData4lum(double... att) {
		instance = new DenseInstance(5);
		instance.setValue(data.attribute("season"), att[0]);
		instance.setValue(data.attribute("cloud"), att[1]);
		instance.setValue(data.attribute("temp_ext"), att[2]);
		instance.setValue(data.attribute("hum_ext"), att[3]);
		instance.setValue(data.attribute("lum_ext"), att[4]);

		data.add(instance);

		return true;
	}

	private boolean addData(double... att) {
		instance = new DenseInstance(4);
		instance.setValue(data.attribute("hum_ext"), att[0]);
		instance.setValue(data.attribute("temp_ext"), att[1]);
		instance.setValue(data.attribute("temp_int"), att[2]);
		instance.setValue(data.attribute("lum_ext"), att[3]);

		data.add(instance);

		return true;
	}

	/**
	 * 
	 * @return
	 */
	public boolean addDataGeneric() {
		addData(85, 17, 22.5, 21000);
		addData(43, 21, 21, 61500);
		addData(62, 19, 21.5, 34000);
		addData(24, 24, 20, 82300);
		addData(50, 20, 20.5, 56800);
		addData(59, 20, 20.5, 39420);
		addData(93, 4, 24, 6300);

		return true;
	}

	private boolean addDataLum() {
		addData4lum(1, 20, 25, 7, 100000);
		addData4lum(1, 35, 25, 25, 89000);
		addData4lum(1, 60, 25, 60, 70000);
		addData4lum(1, 80, 25, 80, 25000);
		addData4lum(2, 20, 7, 15, 90000);
		addData4lum(2, 35, 7, 40, 76000);
		addData4lum(2, 60, 7, 70, 40000);
		addData4lum(2, 80, 7, 90, 10000);
		addData4lum(3, 20, 13, 10, 92500);
		addData4lum(3, 35, 13, 35, 76000);
		addData4lum(3, 60, 13, 63, 40000);
		addData4lum(3, 80, 13, 84, 10000);
		addData4lum(4, 20, 19, 10, 96800);
		addData4lum(4, 35, 19, 30, 83500);
		addData4lum(4, 60, 19, 68, 57000);
		addData4lum(4, 80, 19, 87, 18000);

		return true;
	}

	public double executeModel() throws Exception {

		data.setClassIndex(data.numAttributes() - 1); // checks for the //
														// attributes
		// build a model
		model = new LinearRegression();
		model.buildClassifier(data); // the last instance with a missing value
										// is not used
		// System.out.println("model : "+model);

		return model.classifyInstance(data.lastInstance());
	}

	public void addInstance(double hum, double temp, double lum) {
		Instance value_futur = new DenseInstance(4);
		value_futur.setValue(data.attribute("hum_ext"), hum);
		value_futur.setValue(data.attribute("temp_ext"), temp);
		value_futur.setValue(data.attribute("lum_ext"), lum);
		data.add(value_futur);
	}

	public void addInstance(double hum, double temp, double cloud, double season) {
		Instance value_futur = new DenseInstance(5);
		System.out.println(hum + " " + temp + " " + cloud + " " + season);
		value_futur.setValue(data.attribute("hum_ext"), hum);
		value_futur.setValue(data.attribute("temp_ext"), temp);
		value_futur.setValue(data.attribute("cloud"), cloud);
		value_futur.setValue(data.attribute("season"), season);
		data.add(value_futur);
	}

}
