package com.example.esir.nsoc2014.tsen.lob.arff;

import java.util.ArrayList;

import weka.core.Attribute;
import weka.core.Instances;

public class ArffGenerated {
	private Instances arff;
	
	Instances data;

	public ArffGenerated() {
		this.arff = null;
	}

	public Instances getArff() {
		return arff;
	}

	private ArrayList<Attribute> atts = new ArrayList<Attribute>() {
		private static final long serialVersionUID = 1L;
		{
			add(new Attribute("hum_ext"));
			add(new Attribute("temp_ext"));
			add(new Attribute("temp_int"));
		}
	};
	
	public void generateArff(ArrayList<?> list, String user_id) {
		data = new Instances("pref_"+user_id, atts, 0);
	}

}
