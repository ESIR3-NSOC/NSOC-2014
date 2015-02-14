package com.example.esir.nsoc2014.regulator.objects;

import com.example.esir.nsoc2014.regulator.knx.DataFromKNX;
import com.example.esir.nsoc2014.tsen.lob.objects.ArffGenerated;

import java.util.Date;

/**
 * Created by Nicolas on 06/02/2015.
 */
public class DataLearning {
    private DataFromKNX mknx;
    private long heat_time;

    public DataLearning(DataFromKNX mknx) {
        this.mknx = mknx;
        this.heat_time = 0;
    }

    public DataFromKNX getMknx() {
        return mknx;
    }

    public long getHeat_time() {
        return heat_time;
    }

    public void setHeat_time(Date start, Date end) {
        heat_time = end.getTime() - start.getTime();
    }

    public void setInArff(ArffGenerated arff) {
        arff.addDataCustomRegul(mknx.getI_temp(), mknx.getO_temp(), mknx.getI_hum(), mknx.getO_hum(), mknx.getO_lum(), mknx.getCons(), heat_time);
    }
}
