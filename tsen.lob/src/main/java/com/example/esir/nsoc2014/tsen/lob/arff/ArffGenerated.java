package com.example.esir.nsoc2014.tsen.lob.arff;

import java.util.ArrayList;

import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;

public class ArffGenerated {

	private Instances data;
	private Instance instance;

	public ArffGenerated() {
		this.data = null;
	}

	public Instances getArff() {
		return data;
	}

	private ArrayList<Attribute> atts = new ArrayList<Attribute>() {
		private static final long serialVersionUID = 1L;
		{
			add(new Attribute("hum_ext"));
			add(new Attribute("temp_ext"));
			add(new Attribute("temp_int"));
		}
	};

	public void generateArff(String user_id) {
		data = new Instances("pref_" + user_id, atts, 0);
	}

	/**
	 * 
	 * @param att
	 * @return
	 */
	private boolean addData(double... att) {
		instance = new DenseInstance(3);
		instance.setValue(data.attribute("hum_ext"), att[0]);
		instance.setValue(data.attribute("temp_ext"), att[1]);
		instance.setValue(data.attribute("temp_int"), att[2]);

		data.add(instance);

		return true;
	}

	/**
	 * 
	 * @return
	 */
	public boolean addDataGeneric() {
		addData(85, 17, 22.5);
		addData(43, 21, 21);
		addData(62, 19, 21.5);
		addData(24, 24, 20);
		addData(50, 20, 20.5);
		addData(59, 20, 20.5);
		addData(93, 4, 24);

		return true;
	}

}
