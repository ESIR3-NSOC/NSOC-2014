package fr.esir.control;

import android.os.Environment;
import com.example.esir.nsoc2014.tsen.lob.objects.ArffGenerated;

import java.io.File;
import java.io.IOException;

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

    public void setHeat_time(long start, long end) {
        heat_time = end - start;
    }

    public void setInArff() {
        String externalStorage = Environment.getExternalStorageDirectory().getAbsolutePath();
        String path = externalStorage + File.separator + "DCIM" + File.separator + "arffRegul.arff";

        ArffGenerated arff = new ArffGenerated();

        File file = new File(path);
        if (!file.exists()) {
            arff.generateArffRegul();
            arff.addDataRegulGeneric();
            try {
                arff.saveInstancesInArffFile(arff.getArff(), path);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        arff.addDataCustomRegul(mknx.getI_temp(), mknx.getO_temp(), mknx.getI_hum(), mknx.getO_hum(),
                mknx.getO_lum(), mknx.getCons(), mknx.getNb_pers(), heat_time);
    }
}
