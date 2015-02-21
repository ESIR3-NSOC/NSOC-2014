package com.example.esir.nsoc2014.tsen.lob.objects;

import weka.classifiers.functions.LinearRegression;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffSaver;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

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
            add(new Attribute("lum_ext"));
            add(new Attribute("temp_int"));
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

    private ArrayList<Attribute> attsregul = new ArrayList<Attribute>() {
        private static final long serialVersionUID = 1L;

        {
            add(new Attribute("i_temp"));
            add(new Attribute("o_temp"));
            add(new Attribute("i_hum"));
            add(new Attribute("o_hum"));
            add(new Attribute("i_lum"));
            add(new Attribute("o_lum"));
            add(new Attribute("consigne"));
            add(new Attribute("nb_pers"));
            add(new Attribute("heat-time"));
        }
    };

    public void generateArffRegul() {
        FastVector fvWekaAttributes = new FastVector(8);
        for (int i = 0; i < 8; i++) {
            fvWekaAttributes.addElement(attsregul.get(i));
        }
        data = new Instances("regulation", fvWekaAttributes, 0);
    }

    public void generateArff(String user_id) {
        FastVector fvWekaAttributes = new FastVector(4);
        for (int i = 0; i < 4; i++) {
            fvWekaAttributes.addElement(atts.get(i));
        }
        data = new Instances(user_id, fvWekaAttributes, 0);
    }

    public void generateArfflum() {
        FastVector fvWekaAttributes = new FastVector(5);
        for (int i = 0; i < 5; i++) {
            fvWekaAttributes.addElement(attslum.get(i));
        }
        data = new Instances("Check_lum", fvWekaAttributes, 0);
        addDataLum();
    }

    /**
     * @param att params
     * @return true
     */
    private boolean addData4Regul(double... att) {
        instance = new Instance(7);
        instance.setValue(data.attribute("i_temp"), att[0]);
        instance.setValue(data.attribute("o_temp"), att[1]);
        instance.setValue(data.attribute("i_hum"), att[2]);
        instance.setValue(data.attribute("o_hum"), att[3]);
        instance.setValue(data.attribute("o_lum"), att[4]);
        instance.setValue(data.attribute("consigne"), att[5]);
        instance.setValue(data.attribute("nb_pers"), att[6]);
        instance.setValue(data.attribute("heat_time"), att[7]);

        data.add(instance);

        return true;
    }

    private boolean addData4lum(double... att) {
        instance = new Instance(5);
        instance.setValue(data.attribute("season"), att[0]);
        instance.setValue(data.attribute("cloud"), att[1]);
        instance.setValue(data.attribute("temp_ext"), att[2]);
        instance.setValue(data.attribute("hum_ext"), att[3]);
        instance.setValue(data.attribute("lum_ext"), att[4]);

        data.add(instance);

        return true;
    }

    private boolean addData(double... att) {
        instance = new Instance(4);
        instance.setValue(data.attribute("hum_ext"), att[0]);
        instance.setValue(data.attribute("temp_ext"), att[1]);
        instance.setValue(data.attribute("lum_ext"), att[2]);
        instance.setValue(data.attribute("temp_int"), att[3]);

        data.add(instance);

        return true;
    }

    public boolean addDataCustom(double humidity, double temp_e, double temp_i, double lum_e) {
        addData(humidity, temp_e, temp_i, lum_e);

        return true;
    }

    public boolean addDataCustomRegul(double i_temp, double o_temp, double i_hum, double o_hum, double o_lum, double consigne, double nb_pers, double millitime) {
        addData4Regul(i_temp, o_temp, i_hum, o_hum, o_lum, consigne, nb_pers, millitime);

        return true;
    }

    public boolean addDataRegulGeneric() {
        addData4Regul(18, 6, 30, 50, 40000, 23, 6, 900000);
        addData4Regul(18, 20, 20, 30, 70000, 21, 15, 300000);
        addData4Regul(18, 12, 35, 95, 10000, 22, 18, 400000);
        addData4Regul(19, 6, 30, 50, 40000, 23, 14, 780000);
        addData4Regul(19, 20, 20, 30, 70000, 21, 18, 200000);
        addData4Regul(19, 12, 35, 95, 10000, 22, 20, 300000);
        addData4Regul(20, 6, 30, 50, 40000, 23, 11, 690000);
        addData4Regul(20, 20, 20, 30, 70000, 21, 16, 160000);
        addData4Regul(20, 12, 35, 95, 10000, 22, 18, 245000);
        addData4Regul(21, 6, 30, 50, 40000, 23, 6, 360000);
        addData4Regul(21, 20, 20, 30, 70000, 21, 15, 0);
        addData4Regul(21, 12, 35, 95, 10000, 22, 16, 280000);
        addData4Regul(22, 6, 30, 50, 40000, 23, 18, 90000);
        addData4Regul(22, 20, 20, 30, 70000, 21, 6, 0);
        addData4Regul(22, 12, 35, 95, 10000, 22, 15, 0);
        addData4Regul(23, 6, 30, 50, 40000, 23, 16, 0);
        addData4Regul(23, 20, 20, 30, 70000, 21, 18, 0);
        addData4Regul(23, 12, 35, 95, 10000, 22, 6, 0);

        return true;
    }

    /**
     * @return boolean
     */
    public boolean addDataGeneric() {
        addData(85, 17, 21000, 22.5);
        addData(43, 21, 61500, 21);
        addData(62, 19, 34000, 21.5);
        addData(24, 24, 82300, 20);
        addData(50, 20, 56800, 20.5);
        addData(59, 20, 39420, 20.5);
        addData(93, 4, 6300, 24);
        addData(95, 10, 12000, 23.5);

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
        double db = model.classifyInstance(data.lastInstance());
        System.out.println("model result " + db);
        return db;
    }

    public void addInstance(double hum, double temp, double lum) {
        Instance value_futur = new Instance(4);
        value_futur.setValue(data.attribute("hum_ext"), hum);
        value_futur.setValue(data.attribute("temp_ext"), temp);
        value_futur.setValue(data.attribute("lum_ext"), lum);
        data.add(value_futur);
    }

    public void addInstance(double hum, double temp, double cloud, double season) {
        Instance value_futur = new Instance(5);
        value_futur.setValue(data.attribute("hum_ext"), hum);
        value_futur.setValue(data.attribute("temp_ext"), temp);
        value_futur.setValue(data.attribute("cloud"), cloud);
        value_futur.setValue(data.attribute("season"), season);
        data.add(value_futur);
    }

    public void addInstance(double i_temp, double o_temp, double i_hum, double o_hum, double o_lum, double consigne, double nb_pers) {
        Instance value_futur = new Instance(7);
        value_futur.setValue(data.attribute("i_temp"), i_temp);
        value_futur.setValue(data.attribute("o_temp"), o_temp);
        value_futur.setValue(data.attribute("i_hum"), i_hum);
        value_futur.setValue(data.attribute("o_hum"), o_hum);
        value_futur.setValue(data.attribute("o_lum"), o_lum);
        value_futur.setValue(data.attribute("nb_pers"), nb_pers);
        value_futur.setValue(data.attribute("consigne"), consigne);

        data.add(value_futur);
    }

    public static boolean saveInstancesInArffFile(Instances dataSet, String path)
            throws IOException {
        ArffSaver saver = new ArffSaver();
        saver.setInstances(dataSet);
        saver.setFile(new File(path));
        saver.writeBatch();

        return true;
    }

}